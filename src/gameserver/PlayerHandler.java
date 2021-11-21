package gameserver;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import game.consts.Consts;
import gameserver.eventhandlers.PlayerConnectedEventHandler;
import gameserver.eventhandlers.PlayerUpdatedEventHandler;
import game.Command;
import game.Game;
import game.Request;
import game.Response;
import logger.Logger;
import observer.Event;
import observer.EventHandler;
import observer.Observer;
import logger.LogLevel;

/**
 * Coordinates the communication between the Player and the Game.
 * Creates two threads:
 * - gameThread: responsible for receiving messages from the player and forward them to the game, and receive responses
 * from the game and pass them back to the player.
 * - notificationsThread: responsible for sending notifications from the game to the player asynchronously.
 */
public class PlayerHandler implements Runnable
{
    private PlayerSocket socket = null;        // the socket associated with the player
    private boolean connected = false;         // indicates whether the player is connected to the game
    private Thread gameThread = null;          // thread handling regular interactions between player and game
    private Thread notificationsThread = null; // thread that handling notifications received from the game
    private Game game = null;                  // the instance of the game the player is playing
    private String player = null;              // player identifier
    
    /**
     * Constructor.
     * 
     * @param socket the socket connected to the player's client
     * @param game   the instance of the game the player will be playing
     */
    public PlayerHandler(Socket socket, Game game)
    {
        this.socket = new PlayerSocket(socket);
        this.game = game;
        gameThread = new Thread(() -> { playGame(); }, "Player" + this.socket.getId() + "-Game");
        notificationsThread = new Thread(() -> { processNotifications(); }, "Player" + this.socket.getId() + "-Notifications");
    }
    
    /**
     * Returns the ID of this PlayerHandler.
     * 
     * @return this PlayerHandler's ID
     */
    public int getId()
    {
        return socket.getId();
    }
    
    // Sets the player's connection state with the game
    private synchronized void setConnected(boolean connected)
    {
        this.connected = connected;
    }
    
    // Checks whether the player is connected with the game
    private synchronized boolean isConnected()
    {
        return connected;
    }

    // Sets the player's connection state with the game
    private synchronized void setPlayerName(String player)
    {
        this.player = player;
    }
    
    // Checks whether the player is connected with the game
    private synchronized String getPlayerName()
    {
        return player;
    }
    
    @Override
    public void run()
    {
        setConnected(true);
        Thread.currentThread().setName("Player" + socket.getId());
        gameThread.start();
        notificationsThread.start();
        while (isConnected())
            ; // keep running while connected
        terminate();
    }
    
    /**
     * Terminates the PlayerHandler and the threads associated with it.
     * Closes the underlying socket.
     */
    public void terminate()
    {
        setConnected(false);
        try
        {
            gameThread.join();
            Logger.log(LogLevel.Debug, "Game thread terminated");
            notificationsThread.join();
            Logger.log(LogLevel.Debug, "Notifications thread terminated");
            socket.close();
        }
        catch (InterruptedException e)
        {
            Logger.log(LogLevel.Error, "Error terminating thread");
            e.printStackTrace();
        }
    }
    
    // Processes interactions between player and game.
    private void playGame()
    {
        Logger.log(LogLevel.Debug, "Starting game thread for player " + socket.getId());
        try
        {
            Request request = null;
            while (isConnected() && (request = socket.receive()) != null)
            {
                // TODO: Add "disconnect" to Response
                // TODO: Create "Request Handlers" for the game
                Logger.log(LogLevel.Info, "Request received: \"" + request.toString() + "\"");
                if (request.getCommand() == Command.Quit)
                    setConnected(false);
                request.setPlayer(player);
                Response response = game.processRequest(request);
                if (getPlayerName() == null)
                    setPlayerName(response.getPlayer());
                Logger.log(LogLevel.Info, "Sending response: \"" + response.toString() + "\"");
                socket.send(response);
            }
            setConnected(false);
        }
        catch (IOException e)
        {
            Logger.log(LogLevel.Error, "Socket error receiving or sending message");
            e.printStackTrace();
            setConnected(false);
        }
    }
    
    // Processes notifications received from the game.
    private void processNotifications()
    {
        Logger.log(LogLevel.Debug, "Starting notifications thread");
        List<EventObserver> eventObservers = new ArrayList<EventObserver>();

        EventObserver playerConnected = new EventObserver(new PlayerConnectedEventHandler());
        game.subscribe(Consts.EVENT_PLAYER_CONNECTED, playerConnected);
        eventObservers.add(playerConnected);

        EventObserver playerUpdated = new EventObserver(new PlayerUpdatedEventHandler());
        game.subscribe(Consts.EVENT_PLAYER_UPDATED, playerUpdated);
        eventObservers.add(playerUpdated);

        while (isConnected())
        {
            if (getPlayerName() == null) //TODO: there must be a better way to do it. E.g. initialize this thread only after login. Missing my own connected notification.
            {
                for (EventObserver observer : eventObservers)
                    observer.clearEventQueue();
            }
            else
            {
                for (EventObserver observer : eventObservers)
                    observer.processEvents();
            }
        }
        for (EventObserver observer : eventObservers)
            game.unsubscribe(observer);
    }
    
    /**
     * Provides specific EventHandlers.
     * Publishes responses to events to the player socket.
     */
    private class EventObserver extends Observer
    {
        private EventHandler handler = null; // the event handler for this event observer
        
        /**
         * Constructor.
         * 
         * @param handler the event handler associated with this EventObserver
         */
        public EventObserver(EventHandler handler)
        {
            super();
            this.handler = handler;
        }
        
        @Override
        protected void processEvent(Event event)
        {
            try
            {
                Logger.log(LogLevel.Debug, "Event received: \"" + event + "\"");
                Response response = handler.processEvent(event);
                Logger.log(LogLevel.Info, "Sending notification: \"" + response.toString() + "\"");
                socket.send(response);
            }
            catch (IOException e)
            {
                Logger.log(LogLevel.Error, "Socket error sending message");
                e.printStackTrace();
                setConnected(false);
            }
        }
    }
}
