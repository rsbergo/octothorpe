package server;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
    private BufferedReader reader = null; // reader for the socket
    private PrintWriter writer = null;    // writer for the socket
    
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
    
    /**
     * Reads a string line from this ClientHandler's socket.
     * Blocks until input is available.
     * 
     * @return A String line from this ClientHandler's socket.
     * @throws IOException if an I/O error occurs while reading the socket
     */
    public String receiveString() throws IOException
    {
        return reader.readLine();
    }

    /**
     * Writes a String into this ClientHandler's socket.
     * 
     * @param data the String to be sent through this ClientHandler's socket
     */
    public void sendString(String data)
    {
        writer.print(data);
        writer.flush();
    }

    @Override
    public void close()
    {
        try
        {
            System.out.println("Client " + clientId + " - Closing connection");
            if (reader != null)
                reader.close();
            if (writer != null)
                writer.close();
            socket.close();
        }
        catch (IOException e)
        {
            System.out.println("Error closing socket: " + socket);
            e.printStackTrace();
        }
    }
    
    // Returns a BufferedReader for the socket.
    // If the BufferedReader hasn't been initialized yet, initialize it.
    // Throws IOException If an error occurs while initializing the BufferedReader
    private BufferedReader getSocketReader() throws IOException
    {
        if (reader == null)
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        return reader;
    }

    // Returns a PrintWriter for the socket.
    // If the PrintWriter hasn't been initialized yet, initialize it.
    // Throws IOException If an error occurs while initializing the PrintWriter
    private PrintWriter getSocketWriter() throws IOException
    {
        if (writer == null)
            writer = new PrintWriter(socket.getOutputStream(), true);
        return writer;
    }
}
