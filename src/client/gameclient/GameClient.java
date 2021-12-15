package client.gameclient;

import java.io.IOException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import client.connector.Connector;
import client.connector.Request;
import client.connector.Response;
import client.event.Event;
import client.event.RequestEvent;
import client.event.ResponseEvent;
import client.event.Subject;
import client.observer.Observable;
import client.observer.Observer;
import logger.LogLevel;
import logger.Logger;

/**
 * Coordinates the interactions between the player and the game server.
 * Observes RequestEvents. When a RequestEvent is received, sends the request in the event to the game server.
 * Observes ResponseEvents. When a response event is received, acts as a proxy and notifies its observers of the new 
 * respose event.
 * Expects one single synchronous response for each request sent. These responses have response code greater than or 
 * equal to 200. After sending a request to the game server, blocks until a synchronous response is received, not 
 * sending aditional requests.
 */
public class GameClient extends Observable implements Observer, Runnable
{
    // connection to the game server
    private Connector conn = null; // the connection with the game server
    private String host = null;    // the game server host
    private int port = 0;          // the port in which the game server is listening for new connections

    // state
    private boolean running = false;  // indicates whether the game client is running
    
    // internal threads
    private Thread listener = null;   // thread that listens to messages from game server
    
    // events
    private NotificationManager notifier = null; // event generator

    // synchrononous messages
    private BlockingDeque<RequestEvent> requestQueue = new LinkedBlockingDeque<RequestEvent>();
    private BlockingDeque<ResponseEvent> responseQueue = new LinkedBlockingDeque<ResponseEvent>();
    
    /**
     * Constructor.
     * Receives the parameters to connect with the game server
     * 
     * @param host the host where the game server is running
     * @param port the port where the game server is listening for connections
     */
    public GameClient(String host, int port)
    {
        this.host = host;
        this.port = port;
        registerSubject(Subject.Response);
    }

    // Setters and Getters
    public NotificationManager getNotificationManager() { return notifier; }

    /**
     * Starts running the game client.
     */
    public void run()
    {
        try
        {
            initialize(host, port);
            listener.start();
            running = true;
            while (running)
            {
                Request request = receiveRequest();
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
            conn.close();
            listener.join();
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
        else if (event.getSubject() == Subject.Response)
        {
            ResponseEvent responseEvent = (ResponseEvent) event;
            if (responseEvent.getResponse().getResponseCode().getCode() >= 200) // synchronous response
                responseQueue.add(responseEvent);
            notify(event); // forward responses
        }
    }

    // Initializes the resources for the game server
    private void initialize(String host, int port) throws IOException
    {
        Logger.log(LogLevel.Info, "Initializing game client...");
        conn = new Connector();
        conn.connectTo(host, port);
        initializerNotifier(conn);
        listener = new Thread(notifier);
        Logger.log(LogLevel.Info, "Game client initialized");
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
        notifier.registerSubject(Subject.Response);

        notifier.subscribe(Subject.Request, this);
        notifier.subscribe(Subject.Response, this);
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
            ResponseEvent event = responseQueue.takeFirst();
            response = event.getResponse();

        }
        catch (InterruptedException e)
        {
            Logger.log(LogLevel.Error, "Waiting for a response was interrupted", e);
        }
        return response;
    }

    // Prints the response.
    private void handleEvent(Request request, Response response)
    {
        ResponseEvent event = new  ResponseEvent();
        event.setRequest(request);
        event.setResponse(response);
        notify(event);
    }
}
