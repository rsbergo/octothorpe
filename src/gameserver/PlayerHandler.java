package gameserver;

import java.io.IOException;
import java.net.Socket;

import command.Command;
import command.Result;
import game.Game;
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
    private String player = null;       // the player associated with this PlayerHandler
    
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
    }
    
    @Override
    public void run()
    {
        Thread.currentThread().setName("Player" + socket.getId());
        Logger.log(LogLevel.Debug, "Starting player handler thread " + socket.getId());
        connected = true;
        try
        {
            Request request = null;
            while (connected && (request = socket.receive()) != null)
            {
                Logger.log(LogLevel.Info, "Request received: \"" + request.toString() + "\"");
                Command command = requestToCommand(request);
                Result result = game.processCommand(command);
                player = result.getPlayer();
                Response response = resultToResponse(result);
                Logger.log(LogLevel.Info, "Sending response: \"" + response.toString() + "\"");
                socket.send(response);
            }
            connected = false;
        }
        catch (IOException e)
        {
            Logger.log(LogLevel.Error, "Socket error receiving or sending message", e);
            connected = false;
        }
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
}
