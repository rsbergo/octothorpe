package gameserver;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import logger.Logger;
import logger.LogLevel;

/**
 * Represents a socket for a player connected to the GameServer.
 * The PlayerSocket provides access to the socket connected to the client by sending and receiving messages through this
 * socket.
 * Writing terminates the message by appending '\r\n'.
 */
public class PlayerSocket implements Closeable
{
    private static int id = 0;            // identifier of the PlayerSocket
    private Socket socket;                // the socket connected to the player's client
    private BufferedReader reader = null; // reads data from the socket
    private PrintWriter writer = null;    // writes data into the socket
    
    /**
     * Constructor.
     * Associates a PlayerSocket with the socket specified.
     * Initializes this PlayerSocket's reader and writer.
     * 
     * @param socket the socket associated to this PlayerSocket
     */
    public PlayerSocket(Socket socket)
    {
        this.socket = socket;
        reader = getSocketReader(socket);
        writer = getSocketWriter(socket);
        id++;
    }
    
    // Setters and Getters
    public int getId() { return id; }
    
    /**
     * Reads a String line from the underlying socket.
     * Blocks until input is available.
     * 
     * @return A String containing a line from the underlying socket
     * @throws IOException if an I/O error occurs while reading from the underlying socket
     */
    public String receive() throws IOException
    {
        Logger.log(LogLevel.Info, "Waiting for message from socket...");
        if (reader != null)
            return reader.readLine();
        throw new IOException("Socket reader is not set up correctly for this socket");
    }
    
    /**
     * Writes the specified message into the underlying socket.
     * The message is sent right away; i.e. the message is not buffered.
     * Line termination "\r\n" is appended to the message before sending.
     * 
     * @param message the message to be sent through the underlying socket
     */
    public synchronized void send(String message) throws IOException
    {
        Logger.log(LogLevel.Debug, "Sending message...");
        if (writer != null)
        {
            writer.print(message + "\r\n");
            writer.flush();
            return;
        }
        throw new IOException("Socket writer is not set up correctly for this socket");
    }
    
    @Override
    public void close()
    {
        try
        {
            Logger.log(LogLevel.Debug, "Client " + id + " - Closing connection");
            if (reader != null)
                reader.close();
            if (writer != null)
                writer.close();
            socket.close();
        }
        catch (IOException e)
        {
            Logger.log(LogLevel.Error, "Error closing socket: " + socket);
            e.printStackTrace();
        }
    }
    
    // Creates a socket reader for the socket specified.
    // Returns null if an error occurs while setting up the socket reader.
    private BufferedReader getSocketReader(Socket socket)
    {
        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (IOException e)
        {
            Logger.log(LogLevel.Error, "Error setting up socket reader");
            e.printStackTrace();
        }
        return reader;
    }
    
    // Creates a socket writer for the socket specified.
    // Returns null if an error occurs while setting up the socket writer.
    private PrintWriter getSocketWriter(Socket socket)
    {
        PrintWriter writer = null;
        try
        {
            writer = new PrintWriter(socket.getOutputStream());
        }
        catch (IOException e)
        {
            Logger.log(LogLevel.Error, "Error setting up socket writer");
            e.printStackTrace();
        }
        return writer;
    }
}
