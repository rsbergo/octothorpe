package client.gameclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import javax.swing.JFrame;

import client.connector.Command;
import client.connector.Connector;
import client.connector.Request;
import client.connector.ResponseCode;
import client.event.Event;
import client.event.Subject;
import client.event.SynchronousResponseEvent;
import client.gui.ClientWindow;
import client.observer.Observer;

/**
 * Coordinates the interactions between the player and the game server.
 * Receives instructions from the player and sends them as Requests to the game server.
 * Receives Responses from the game server and consumes them.
 * Responses are received in a different thread. Only synchronous responses (ResponseCode >= 200) are sent to player.
 * Asynchronous responses are consumed by the listener thread.
 */
public class GameClient implements Observer
{
    private Connector conn = null;    // the connection with the game server
    private BufferedReader in = null; // the input stream for player commands
    private boolean running = false;  // indicates whether the game client is running
    private Thread listener = null;   // thread that listens to messages from game server

    // synchronizes synchronous responses between the listener thread and the main thread
    private BlockingDeque<Event> eventQueue = new LinkedBlockingDeque<Event>();
    
    public void run(String host, int port, BufferedReader in)
    {
        try
        {
            initialize(host, port, in);
            listener.start();
            running = true;
            
            java.awt.EventQueue.invokeLater(() -> 
            {
                JFrame mainWindow = new ClientWindow();
                mainWindow.setVisible(true);
            });
            
            String command = null;
            System.out.print("> ");
            while (running && (command = in.readLine()) != null)
            {
                Request request = new Request(command);
                conn.send(request);
                Event responseEvent = receiveEvent();
                handleEvent(request, responseEvent);
                System.out.print("> ");
            }
            terminate();
        }
        catch (IOException e)
        {
            System.err.println("Error initializing the game client"); // TODO: replace with logger
            e.printStackTrace();
        }
    }
    
    /**
     * Stops the game client and closes all resources.
     */
    public void terminate()
    {
        try
        {
            running = false;
            in.close();
            conn.close();
            listener.join();
        }
        catch (IOException e)
        {
            System.err.println("Something went wrong while terminating the game client..."); // TODO: replace with logger
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            System.err.println("Error while joining listener thread"); // TODO: replace with logger
            e.printStackTrace();
        }
    }

    @Override
    public void processEvent(Event event)
    {
        eventQueue.add(event);
    }

    // Initializes the resources for the game server
    private void initialize(String host, int port, BufferedReader in) throws IOException
    {
        System.out.println("Initializing game server..."); // TODO: replace with logger
        conn = new Connector();
        conn.connectTo(host, port);
        this.in = in;
        listener = new Thread(initializerNotificationManager(conn));
        System.out.println("Game server initialized"); // TODO: replace with logger
    }

    // Initializes the notifications manager for the game client.
    private NotificationManager initializerNotificationManager(Connector conn)
    {
        NotificationManager notificationManager = new NotificationManager(conn);

        notificationManager.registerSubject(Subject.SynchronousResponse);
        notificationManager.registerSubject(Subject.PlayerUpdated);
        notificationManager.registerSubject(Subject.ItemData);
        notificationManager.registerSubject(Subject.ItemTaken);
        notificationManager.registerSubject(Subject.MapData);

        notificationManager.subscribe(Subject.SynchronousResponse, this);

        return notificationManager;
    }

    // Retrieves the first element from event queue.
    // Blocks if event queue is empty, until an event becomes available.
    private Event receiveEvent()
    {
        Event event = null;
        try
        {
            event = eventQueue.takeFirst();
        }
        catch (InterruptedException e)
        {
            System.err.println("Waiting for a response was interrupted"); // TODO: replace with logger
            e.printStackTrace();
        }
        return event;
    }

    // Prints the response.
    // If the command was Quit and the response is successful, stop the game client.
    private void handleEvent(Request request, Event event)
    {
        if (event.getSubject() == Subject.SynchronousResponse)
        {
            SynchronousResponseEvent responseEvent = (SynchronousResponseEvent) event;
            System.out.println("< " + responseEvent.getResponse().getMessage());
            if (request.getCommand() == Command.Quit && responseEvent.getResponse().getResponseCode() == ResponseCode.Success)
                running = false;
        }
    }
}
