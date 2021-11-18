package gameserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
            PlayerSocket playerSocket = acceptConnection(socket);
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
            System.out.println("Stopping the server...");
            socket.close();
            System.out.println("Goodbye");
        }
        catch (IOException e)
        {
            System.out.println("Something went wrong while stopping the server");
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
            System.out.println("Listening on port " + port);
        }
        catch (IOException e)
        {
            running = false;
            System.out.println("Something went wrong while setting up the server socket on port " + port);
            e.printStackTrace();
        }
        return server;
    }
    
    // Accepts a new connection from a client.
    // Creates a ClientHandler associated with the new connection.
    private PlayerSocket acceptConnection(ServerSocket server)
    {
        PlayerSocket clientHandler = null;
        try
        {
            System.out.println("Waiting for a new connection...");
            Socket client = server.accept();
            clientHandler = new PlayerSocket(client);
            System.out.println("New connection established. Client: " + clientHandler.getId() + ", " + client);
        }
        catch (IOException e)
        {
            System.out.println("Something went wrong while accepting connection from client...");
            e.printStackTrace();
        }
        return clientHandler;
    }
}
