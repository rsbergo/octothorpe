package client.gameclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import javax.swing.JFrame;

import client.connector.Connector;
import client.connector.Request;
import client.connector.Response;
import client.gui.ClientWindow;

/**
 * Coordinates the interactions between the player and the game server.
 * Receives instructions from the player and sends them as Requests to the game server.
 * Receives Responses from the game server and consumes them.
 * Responses are received in a different thread. Only synchronous responses (ResponseCode >= 200) are sent to player.
 * Asynchronous responses are consumed by the listener thread.
 */
public class GameClient
{
    // TODO: make it an observer. Listener thread becomes an object that generates "synchronous events" (Observable). This processEvent puts the response in the response queue.

    private Connector conn = null;    // the connection with the game server
    private BufferedReader in = null; // the input stream for player commands
    private boolean running = false;  // indicates whether the game client is running
    private Thread listener = null;   // thread that listens to messages from game server

    // synchronizes synchronous responses between the listener thread and the main thread
    private BlockingDeque<Response> responseQueue = new LinkedBlockingDeque<Response>();
    
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

            while (running)
            {
                String command = null;
                System.out.print("> ");
                while ((command = in.readLine()) != null)
                {
                    conn.send(new Request(command));
                    Response response = receiveResponse();
                    System.out.println("< " + response);
                    System.out.print("> ");
                }
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

    // Initializes the resources for the game server
    private void initialize(String host, int port, BufferedReader in) throws IOException
    {
        System.out.println("Initializing game server..."); // TODO: replace with logger
        conn = new Connector();
        conn.connectTo(host, port);
        this.in = in;
        listener = new Thread(() -> { startListening(); } );
        System.out.println("Game server initialized"); // TODO: replace with logger
    }

    // Starts listening to messages from the game server
    private void startListening()
    {
        try
        {
            Response response = null;
            while ((response = conn.receive()) != null)
            {
                if (response.getResponseCode().getCode() >= 200)
                    responseQueue.add(response);
            }
        }
        catch (IOException e)
        {
            System.err.println("Error receiving message from game server"); // TODO: replace with logger
            e.printStackTrace();
        }
    }

    // Retrieves the first element from response queue.
    // Blocks if response queue is empty, until a response becomes available.
    private Response receiveResponse()
    {
        Response response = null;
        try
        {
            response = responseQueue.takeFirst();
        }
        catch (InterruptedException e)
        {
            System.err.println("Waiting for a response was interrupted"); // TODO: replace with logger
            e.printStackTrace();
        }
        return response;
    }
}
