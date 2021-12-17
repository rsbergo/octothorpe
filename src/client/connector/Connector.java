package client.connector;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import logger.LogLevel;
import logger.Logger;

/**
 * Represents a connection with the game server.
 * Establishes a connection with the game server through a socket and manages the communication through that socket.
 */
public class Connector implements Closeable
{
    private Socket socket = null;         // the socket connected to the game server.
    private BufferedReader reader = null; // reads data from the socket.
    private PrintWriter writer = null;    // writes data into the socket.
    private boolean connected = false;    // indicates whether this connector is connected to the game server
    
    /**
     * Establishes a new connection to the specified port number on the named host.
     * 
     * @param host the host name
     * @param port the port number
     * @throws IOException if an error occurs while establishing the connection
     */
    public void connectTo(String host, int port) throws IOException
    {
        try
        {
            Logger.log(LogLevel.Info, "Connecting to \"" + host + ":" + port + "\"...");
            socket = new Socket(host, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
            connected = true;
            Logger.log(LogLevel.Info, "Connection established!");
        }
        catch (IOException e)
        {
            disconnect();
            Logger.log(LogLevel.Error, "Something went wrong while setting up the connection.", e);
            throw e;
        }
    }

    /**
     * Checks whether the Connector is connected to the game server.
     * 
     * @return true if the Connector is connected to a game server, false otherwise
     */
    public boolean isConnected()
    {
        return connected;
    }
    
    /**
     * Disconnects the Connector from the game server.
     */
    public void disconnect()
    {
        if (isConnected())
        {
            connected = false;
            close();
        }
    }

    /**
     * Sends the specified request through the connection.
     * A line termination sequence, "\r\n", is appended to the request being sent.
     * 
     * @param request the request to be sent
     * @throws IOException if it's not possible to send the request because the connection isn't set up correctly
     */
    public void send(Request request) throws IOException
    {
        if (isConnected() && writer != null)
        {
            Logger.log(LogLevel.Debug, "Sending request: \"" + request + "\"");
            writer.print(request + "\r\n");
            writer.flush();
            return;
        }
        throw new IOException("Connection is not set up correctly");
    }

    /**
     * Receives a response from the game server.
     * Blocks until input is available in the connection.
     * 
     * @return a Response received from the game server
     * @throws IOException if its not possible to receive a response because the connectin isn't set up correctly or 
     *                     if there was an error reading from the connection's socket
     */
    public Response receive() throws IOException
    {
        if (isConnected() && reader != null)
        {
            Logger.log(LogLevel.Debug, "Waiting for message from socket");
            Response response = null;
            String message = null;
            if ((message = reader.readLine()) != null)
            {
                response = new Response(message);
                Logger.log(LogLevel.Info, "Response received: \"" + response + "\"");
            }
            return response;
        }
        throw new IOException("Connection is not set up correctly.");
    }

    @Override
    public void close()
    {
        try
        {
            connected = false;
            Logger.log(LogLevel.Info, "Closing connection...");
            if (reader != null)
                reader.close();
            if (writer != null)
                writer.close();
            if (socket != null)
                socket.close();
        }
        catch (IOException e)
        {
            Logger.log(LogLevel.Error, "Error closing socket resources", e);
        }
    }
}
