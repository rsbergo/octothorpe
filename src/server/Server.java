package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A server acts as a dispatcher, accepting new connections and passing them along to a protocol.
 * The new connections are associated with a ClientHandler. The protocol uses this ClientHandler to interact with the
 * client connected to the sever.
 */
public class Server
{
    private int port;        // the port to which this server will be bound
    boolean running = false; // flag indicating whether this server is running
    ServerSocket server;     // socket that will be listening to connections
    
    /**
     * Constructor.
     * Defines the port this server will be listening for new connections.
     * 
     * @param port    the port this server will be bound to and listening for new connections
     * @param rootDir the root directory for documents served by this server
     */
    public Server(int port)
    {
        this.port = port;
    }
    
    /**
     * Runs the Server using the protocol specified.
     * 
     * @param protocol the protocol the server should run
     */
    public void run(Protocol protocol)
    {
        running = true;
        server = createServerSocket(port);
        while (running)
        {
            ClientHandler client = acceptConnection(server);
            ClientThread thread = new ClientThread(client, protocol);
            thread.start();
        }
        stop();
    }
    
    /**
     * Stops the server.
     * Waits for the client threads to finish and closes the client sockets
     * Closes the server socket.
     */
    public void stop()
    {
        running = false;
        try
        {
            System.out.println("Stopping the server...");
            server.close();
            System.out.println("Goodbye");
        }
        catch (IOException e)
        {
            System.out.println("Something went wrong while stopping the server");
            e.printStackTrace();
        }
    }
    
    // Creates a server socket that is listening for new connections in the specified port.
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
    private ClientHandler acceptConnection(ServerSocket server)
    {
        ClientHandler clientHandler = null;
        try
        {
            System.out.println("Waiting for a new connection...");
            Socket client = server.accept();
            clientHandler = new ClientHandler(client);
            System.out.println("New connection established. Client: " + clientHandler.getClientId() + ", " + client);
        }
        catch (IOException e)
        {
            System.out.println("Something went wrong while accepting connection from client...");
            e.printStackTrace();
        }
        return clientHandler;
    }
}
