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
     * @throws IOException if an I/O error occurs while initializing this PlayerSocket's reader and writer
     */
    public PlayerSocket(Socket socket) throws IOException
    {
        this.socket = socket;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream());
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
        return reader.readLine();
    }

    /**
     * Writes the specified message into the underlying socket.
     * The message is sent right away; i.e. the message is not buffered.
     * 
     * @param message the message to be sent through the underlying socket
     */
    public void send(String message)
    {
        writer.print(message);
        writer.flush();
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
}
