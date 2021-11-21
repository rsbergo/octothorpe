package gameserver;

import java.io.IOException;
import java.net.Socket;

import game.Command;
import game.Game;
import game.Request;
import game.Response;
import game.ResponseStatus;
import logger.Logger;
import observer.Event;
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
        gameThread = new Thread(() -> { playGame(); });
        gameThread.setName("Player" + this.socket.getId() + "-Game");
        notificationsThread = new Thread(() -> { processNotifications(); });
        notificationsThread.setName("Player" + this.socket.getId() + "-Notifications");
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
            Logger.log(LogLevel.Error, "Error terminating notifications thread");
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
                // TODO: Game receives Requests and returns Responses
                // TODO: Add "disconnect" to Response
                // TODO: Create "Request Handlers" for the game
                Logger.log(LogLevel.Debug, request.toString());
                if (request.getCommand() == Command.Quit)
                    setConnected(false);
                else if (request.getCommand() == Command.Login)
                    game.processLogin(request);
                else
                    socket.send(new Response(ResponseStatus.Success, "Echo: " + request));
            }
            setConnected(false);
        }
        catch (IOException e)
        {
            Logger.log(LogLevel.Error, "Error receiving or sending message from socket");
            e.printStackTrace();
            setConnected(false);
        }
    }
    
    // Processes notifications received from the game.
    private void processNotifications()
    {
        Logger.log(LogLevel.Debug, "Starting notifications thread");
        Observer observer = new Observer()
        {
            @Override
            public void processEvent(Event req)
            {
                try
                {
                    Logger.log(LogLevel.Debug, "Event received! - " + req.getSubject());
                    socket.send(new Response(ResponseStatus.Update, "Event received! - " + req.getSubject()));
                }
                catch (IOException e)
                {
                    Logger.log(LogLevel.Error, "Error sending message to socket");
                    e.printStackTrace();
                    setConnected(false);
                }
            }
        };
        game.subscribe("login", observer);
        while (isConnected())
            ; // keep running while connected
        game.unsubscribe(observer);
    }
}
