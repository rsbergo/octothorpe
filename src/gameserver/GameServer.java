package gameserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import logger.Logger;
import logger.LogLevel;

/**
 * A game server. It creates a socket and listens for new player connections. When a new player connects, it starts a
 * new thread for the player so the player can interact with the game.
 */
public class GameServer
{
    private int port;            // port to which the GameServer is bound
    private ServerSocket socket; // socket that will be listening for new player connections
    private boolean running;     // indicates the current state of the GameServer
    
    /**
     * Constructor.
     * Defines the port the GameServer will be listening for new connections.
     * 
     * @param port the port this server will be bound to and listening for new connections
     */
    public GameServer(int port)
    {
        this.port = port;
    }
    
    /**
     * Starts the GameServer.
     */
    public void runGameServer()
    {
        running = true;
        socket = createServerSocket(port);
        while (running)
        {
            PlayerHandler player = acceptConnection(socket);
            player.run();
            // ClientThread thread = new ClientThread(client, protocol);
            // thread.start();
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
    
    // Accepts a new connection from a client.
    // Creates a ClientHandler associated with the new connection.
    private PlayerHandler acceptConnection(ServerSocket server)
    {
        PlayerHandler playerHandler = null;
        try
        {
            Logger.log(LogLevel.Debug, "Waiting for a new connection...");
            Socket playerSocket = server.accept();
            playerHandler = new PlayerHandler(playerSocket);
            Logger.log(LogLevel.Info, "New connection established. Player: " + playerHandler.getId() + ", " + playerSocket);
        }
        catch (IOException e)
        {
            Logger.log(LogLevel.Error, "Something went wrong while accepting connection from client...");
            e.printStackTrace();
        }
        return playerHandler;
    }
}
