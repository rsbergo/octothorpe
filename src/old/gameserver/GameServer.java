package old.gameserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import logger.LogLevel;
import logger.Logger;
import old.game_old.Game;

/**
 * A GameServer hosts a game and coordinates the coonections of players to the game being hosted.
 * It creates a socket and listens for new player connections.
 * When a new player connects, it starts a new thread for the player so the player can interact with the game running.
 */
public class GameServer
{
    private int port;                     // port to which the GameServer is bound
    private ServerSocket socket;          // socket that will be listening for new player connections
    private boolean running;              // indicates the current state of the GameServer
    private Game game;                    // the game hosted
    private List<PlayerHandler> handlers; // list of player handlers connected to this game server
    
    /**
     * Constructor.
     * Defines the port the GameServer will be listening for new connections.
     * 
     * @param port the port this server will be bound to and listening for new connections
     */
    public GameServer(int port)
    {
        this.port = port;
        handlers = new ArrayList<PlayerHandler>();
    }
    
    /**
     * Starts the GameServer.
     * Instantiates the game being hosted and sets the GameServer status to true.
     * Initializers the server socket and starts listening for player connections.
     * When a new player connection is established, creates a thread for this player and starts it.
     */
    public void runGameServer()
    {
        game = new Game();
        running = true;
        socket = createServerSocket(port);
        while (running)
        {
            PlayerHandler handler = acceptConnection(socket);
            handlers.add(handler);
            startPlayerThread(handler);
        }
        stopGameServer();
    }
    
    /**
     * Stops the GameServer.
     * Waits for the player threads to finish and closes the player sockets
     * Closes the server socket.
     */
    public void stopGameServer()
    {
        running = false;
        try
        {
            for (PlayerHandler handler : handlers)
                handler.terminate();
            Logger.log(LogLevel.Info, "Stopping the server...");
            socket.close();
            Logger.log(LogLevel.Info, "Goodbye");
        }
        catch (IOException e)
        {
            Logger.log(LogLevel.Error, "Something went wrong while stopping the server");
            e.printStackTrace();
        }
    }
    
    // Creates a server socket that is listening for new connections on the specified port.
    private ServerSocket createServerSocket(int port)
    {
        ServerSocket server = null;
        try
        {
            server = new ServerSocket(port);
            Logger.log(LogLevel.Info, "Listening on port " + port);
        }
        catch (IOException e)
        {
            running = false;
            Logger.log(LogLevel.Error, "Something went wrong while setting up the server socket on port " + port);
            e.printStackTrace();
        }
        return server;
    }
    
    // Accepts a new connection from a player.
    // Creates a PlayerHandler associated with the new connection.
    private PlayerHandler acceptConnection(ServerSocket server)
    {
        PlayerHandler handler = null;
        try
        {
            Logger.log(LogLevel.Debug, "Waiting for a new connection...");
            Socket playerSocket = server.accept();
            handler = new PlayerHandler(playerSocket, game);
            Logger.log(LogLevel.Info, "New connection: Player" + handler.getId() + ", " + playerSocket);
        }
        catch (IOException e)
        {
            Logger.log(LogLevel.Error, "Something went wrong while accepting connection from player...");
            e.printStackTrace();
        }
        return handler;
    }

    // Starts a new thread for the PlayerHandler specified.
    private void startPlayerThread(PlayerHandler handler)
    {
        Thread thread = new Thread(handler);
        thread.setName("Player" + handler.getId());
        thread.start();
    }    
}
