package old.gameserver;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import logger.LogLevel;
import logger.Logger;
import old.game_old.Request;
import old.game_old.Response;

/**
 * Represents a socket for a player connected to the GameServer.
 * The PlayerSocket provides access to the socket connected to the client by receiving Requests and sending Responses
 * through this socket.
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
     * Receives a Request from the underlying socket.
     * Blocks until input is available.
     * 
     * @return A Request received through the socket
     * @throws IOException if an I/O error occurs while reading from the underlying socket
     */
    public Request receive() throws IOException
    {
        Logger.log(LogLevel.Debug, "Waiting for message from socket...");
        if (reader != null)
        {
            Request request = null;
            String message = null;
            if ((message = reader.readLine()) != null)
                request = new Request(message);
            return request;
        }
        throw new IOException("Socket reader is not set up correctly for this socket");
    }
    
    /**
     * Sends the specified Response through the underlying socket.
     * 
     * @param response the response to be sent
     */
    public synchronized void send(Response response) throws IOException
    {
        Logger.log(LogLevel.Debug, "Sending message...");
        if (writer != null)
        {
            writer.print(response + "\r\n");
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
            Logger.log(LogLevel.Debug, "Setting up socket reader");
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
            Logger.log(LogLevel.Debug, "Setting up socket writer");
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
