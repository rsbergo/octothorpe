package client.gameclient;

import java.io.IOException;

import client.connector.Connector;
import client.connector.Response;
import client.connector.ResponseCode;
import client.event.MapDataEvent;
import client.event.ResponseEvent;
import client.event.SynchronousResponseEvent;
import client.game.Map;
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
    private Map map = null; // TODO: this seems to be out of place. Review.
    // TODO: add "Response handler"

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
                generateEvent(response);
        }
        catch (IOException e)
        {
            Logger.log(LogLevel.Error, "Error receiving message from game server", e);
        }
    }
    
    // Generates a new event
    private void generateEvent(Response response)
    {
        if (response.getResponseCode().getCode() >= 200)
            generateSynchronousResponseEvent(response);
        else if (response.getResponseCode() == ResponseCode.MapData)
            generateMapDataEvent(response);
        generateResponseEvent(response); // TODO: review. Generate only response events. Game client generates the other events.
    }

    // Generates a new SynchronousResponseEvent and notifies subscribers.
    private void generateSynchronousResponseEvent(Response response)
    {
        notify(new SynchronousResponseEvent(response));
    }

    // Generates a new MapDataEvent and notifies subscribers.
    private void generateMapDataEvent(Response response)
    {
        try
        {
            String[] tokens = response.getMessage().split(", ");
            if (tokens.length == 2) // starting new map
                map = new Map(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[0]));
            else
            {
                if (map != null)
                    map.addRow(response.getMessage());
                else
                    Logger.log(LogLevel.Error, "Error retrieving map data");
            }
            if (map.getMap().size() == map.getRowCount())
                notify(new MapDataEvent(map));
        }
        catch (NumberFormatException e)
        {
            map = null;
            Logger.log(LogLevel.Error, "Error retrieving map size from response");
            e.printStackTrace();
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
