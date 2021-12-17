package client.gameclient;

import java.io.IOException;

import client.connector.Connector;
import client.connector.Response;
import client.event.ResponseEvent;
import client.observer.Observable;
import logger.LogLevel;
import logger.Logger;

/**
 * The notification manager is responsible for listening to messages sent by the game server and distribute them
 * accordingly.
 * The notification manager generates events for each message received from the game server.
 * A synchronous response event captures all the synchronous responses (response code >- 200). Asynchronous response
 * events (response code >= 100 and <= 200) are generated accordingly.
 */
public class NotificationManager extends Observable implements Runnable
{
    private Connector conn; // the connection to the game server

    /**
     * Constructor.
     * Binds this notification manager to the connector specified.
     * 
     * @param conn the connection from which responses from the game server are received
     */
    public NotificationManager(Connector conn)
    {
        super("NotificationManager");
        this.conn = conn;
    }
    
    @Override
    public void run()
    {
        try
        {
            Response response = null;
            while ((response = conn.receive()) != null)
                generateResponseEvent(response);
        }
        catch (IOException e)
        {
            Logger.log(LogLevel.Error, "Error receiving message from game server", e);
        }
    }
    
    // Generates a new ResponseEvent whenever a response is received.
    private void generateResponseEvent(Response response)
    {
        ResponseEvent event = new ResponseEvent();
        event.setResponse(response);
        notify(event);
    }
}
