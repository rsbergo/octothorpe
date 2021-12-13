package client.gameclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import client.connector.Command;
import client.connector.Connector;
import client.connector.Request;
import client.connector.Response;
import client.connector.ResponseCode;
import client.event.Event;
import client.event.RequestEvent;
import client.event.Subject;
import client.event.SynchronousResponseEvent;
import client.gui.ClientGUI;
import client.observer.Observer;
import logger.Logger;
import logger.LogLevel;

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
    private BlockingDeque<RequestEvent> requestQueue = new LinkedBlockingDeque<RequestEvent>();
    private BlockingDeque<SynchronousResponseEvent> responseQueue = new LinkedBlockingDeque<SynchronousResponseEvent>();
    private NotificationManager notifier = null; // event generator
    
    public void run(String host, int port, BufferedReader in)
    {
        try
        {
            initialize(host, port, in);
            listener.start();
            
            ClientGUI gui = new ClientGUI();
            gui.start(notifier);
            
            running = true;
            while (running)
            {
                Request request = receiveRequest();
                System.out.println("> " + request); // print to stdout
                conn.send(request);
                Response response = receiveResponse();
                handleEvent(request, response);
            }
            terminate();
        }
        catch (IOException e)
        {
            Logger.log(LogLevel.Error, "Error initializing the game client", e);
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
            Logger.log(LogLevel.Error, "Something went wrong while terminating the game client...", e);
        }
        catch (InterruptedException e)
        {
            Logger.log(LogLevel.Error, "Error while joining listener thread", e);
        }
    }

    @Override
    public void processEvent(Event event)
    {
        if (event.getSubject() == Subject.Request)
            requestQueue.add((RequestEvent) event);
        else if (event.getSubject() == Subject.SynchronousResponse)
            responseQueue.add((SynchronousResponseEvent) event);
    }

    // Initializes the resources for the game server
    private void initialize(String host, int port, BufferedReader in) throws IOException
    {
        Logger.log(LogLevel.Info, "Initializing game server...");
        conn = new Connector();
        conn.connectTo(host, port);
        this.in = in;
        initializerNotifier(conn);
        listener = new Thread(notifier);
        Logger.log(LogLevel.Info, "Game server initialized");
    }

    // Initializes the notifications manager for the game client.
    private void initializerNotifier(Connector conn)
    {
        notifier = new NotificationManager(conn);

        notifier.registerSubject(Subject.SynchronousResponse);
        notifier.registerSubject(Subject.PlayerUpdated);
        notifier.registerSubject(Subject.ItemData);
        notifier.registerSubject(Subject.ItemTaken);
        notifier.registerSubject(Subject.MapData);
        notifier.registerSubject(Subject.Request);

        notifier.subscribe(Subject.SynchronousResponse, this);
        notifier.subscribe(Subject.Request, this);
    }

    // Retrieves the first element from request queue.
    // Blocks if request queue is empty, until a request becomes available.
    private Request receiveRequest()
    {
        Request request = null;
        try
        {
            RequestEvent event = requestQueue.takeFirst();
            request = event.getRequest();
        }
        catch (InterruptedException e)
        {
            Logger.log(LogLevel.Error, "Waiting for a request was interrupted", e);
        }
        return request;
    }
    
    // Retrieves the first element from response queue.
    // Blocks if response queue is empty, until a response becomes available.
    private Response receiveResponse()
    {
        Response response = null;
        try
        {
            SynchronousResponseEvent event = responseQueue.takeFirst();
            response = event.getResponse();

        }
        catch (InterruptedException e)
        {
            Logger.log(LogLevel.Error, "Waiting for a response was interrupted", e);
        }
        return response;
    }

    // Prints the response.
    // If the command was Quit and the response is successful, stop the game client.
    private void handleEvent(Request request, Response response)
    {
        System.out.println("< " + response.getMessage()); // print to stdout
        if (request.getCommand() == Command.Quit && response.getResponseCode() == ResponseCode.Success)
            running = false;
    }
}
