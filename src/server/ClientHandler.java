package server;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

/**
 * Represents a client connected to the server.
 * The ClientHandler provides access to the socket connected to the client, by sending and receiving messages through
 * this socket.
 */
public class ClientHandler implements Closeable
{
    private final int BUFFER_SIZE = 8192; // buffer size for reading messages; arbitrary number, similar to 
                                          // BufferedReader
    private static int clientId = 0;
    
    private Socket socket; // the socket connected to the client

    /**
     * Constructor. Initializes this ClientHandler to be associated with the specified socket.
     * 
     * @param socket the socket associated with this ClientHandler
     */
    public ClientHandler(Socket socket)
    {
        this.socket = socket;
        clientId++;
    }
    
    // Getters and Setters
    public int getClientId() { return clientId; }

    /**
     * Reads the contents from this ClientHandler's socket.
     * Blocks until input is available.
     * An empty byte array is returned if no bytes are available.
     * 
     * @return a byte array containing the bytes read from the socket
     * @throws IOException if an I/O error occurs while reading the socket
     */
    public byte[] receive() throws IOException
    {
        byte[] data = new byte[BUFFER_SIZE];
        int bytesRead = 0;
        if ((bytesRead = socket.getInputStream().read(data)) > 0)
            return Arrays.copyOfRange(data, 0, bytesRead);
        return new byte[0];
    }
    
    /**
     * Writes data into this ClientHandler's socket.
     * 
     * @param data the data to be sent through the socket
     * @throws IOException if an I/O error occurs while writing into the socket
     */
    public void send(byte[] data) throws IOException
    {
        socket.getOutputStream().write(data);
        socket.getOutputStream().flush();
    }
    
    @Override
    public void close()
    {
        try
        {
            System.out.println("Client " + clientId + " - Closing connection");
            socket.close();
        }
        catch (IOException e)
        {
            System.out.println("Error closing socket: " + socket);
            e.printStackTrace();
        }
    }
    
}
