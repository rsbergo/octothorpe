package gameserver;

import java.io.IOException;
import java.net.Socket;

import command.Action;
import command.Command;
import command.Result;
import command.ResultCode;
import event.Subject;
import eventhandler.EventHandlerManager;
import eventhandler.ItemCollectedEventHandler;
import eventhandler.ItemDataEventHandler;
import eventhandler.MapDataEventHandler;
import eventhandler.PlayerConnectedEventHandler;
import eventhandler.PlayerDisconnectedEventHandler;
import eventhandler.PlayerUpdateEventHandler;
import eventhandler.SendMessageEventHandler;
import game.Game;
import game.Player;
import logger.Logger;
import logger.LogLevel;

/**
 * Coordinates the communication between the Player and the Game.
 */
public class PlayerHandler implements Runnable
{
    private PlayerSocket socket = null; // the socket associated with the player
    private Game game = null;           // the instance of the game the player is playing
    private boolean connected = false;  // indicates whether the player is connected to the game
    private Player player = null;       // the player associated with this PlayerHandler
    private EventHandlerManager handlers = null; // 
    
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
        handlers = createEventHandlerManager();
        player = new Player(null);
        player.setEventHandlerManager(handlers);
    }
    
    // Setters and Getters
    public PlayerSocket getSocket() { return socket; }
    public synchronized void setConnected(boolean connected) { this.connected = connected; }
    public synchronized boolean isConnected() { return connected; }

    @Override
    public void run()
    {
        Thread.currentThread().setName("Player" + socket.getId());
        Logger.log(LogLevel.Debug, "Starting player handler thread " + socket.getId());
        setConnected(true);
        try
        {
            Request request = null;
            while (isConnected() && (request = socket.receive()) != null)
            {
                Logger.log(LogLevel.Info, "Request received: \"" + request.toString() + "\"");
                Command command = requestToCommand(request);
                Result result = game.processCommand(command);
                player.setName(result.getPlayer());
                Response response = resultToResponse(result);
                Logger.log(LogLevel.Info, "Sending response: \"" + response.toString() + "\"");
                socket.send(response);
                if (command.getAction() == Action.Quit && response.getResponseCode() == ResultCode.Success)
                    setConnected(false);
            }
            terminate();
        }
        catch (IOException e)
        {
            Logger.log(LogLevel.Error, "Socket error receiving or sending message", e);
            setConnected(false);;
        }
    }

    /**
     * Terminates the PlayerHandler and the threads associated with it.
     * Closes the underlying socket.
     */
    public void terminate()
    {
        setConnected(false);
        socket.close();
    }

    // Prepares a game command from a request
    private Command requestToCommand(Request request)
    {
        Command command = new Command();
        command.setPlayer(player);
        command.setAction(request.getAction());
        command.setArgs(request.getArgs());
        return command;
    }
    
    // Prepares a response based on a game result
    private Response resultToResponse(Result result)
    {
        Response response = new Response();
        response.setResponseCode(result.getResultCode());
        response.setMessage(result.getMessage());
        return response;
    }

    private EventHandlerManager createEventHandlerManager()
    {
        EventHandlerManager handlers = new EventHandlerManager();
        handlers.installEventHandler(Subject.MapData, new MapDataEventHandler(this));
        handlers.installEventHandler(Subject.ItemCollected, new ItemCollectedEventHandler(this));
        handlers.installEventHandler(Subject.ItemData, new ItemDataEventHandler(this));
        handlers.installEventHandler(Subject.PlayerConnected, new PlayerConnectedEventHandler(this));
        handlers.installEventHandler(Subject.PlayerDisconnected, new PlayerDisconnectedEventHandler(this));
        handlers.installEventHandler(Subject.PlayerUpdate, new PlayerUpdateEventHandler(this));
        handlers.installEventHandler(Subject.SendMessage, new SendMessageEventHandler(this));
        return handlers;
    }
}
