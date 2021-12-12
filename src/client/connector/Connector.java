package client.connector;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
        // TODO: throw exception if already connected
        try
        {
            System.out.println("Connecting to \"" + host + ":" + port + "\"..."); // TODO: replace with logger
            socket = new Socket(host, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
            connected = true;
            System.out.println("Connection established!"); // TODO: replace with logger
        }
        catch (IOException e)
        {
            disconnect();
            System.err.println("Something went wrong while setting up the connection."); // TODO: replace with logger
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
//            System.out.println("Sending request: \"" + request + "\""); // TODO: replace with logger
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
        // TODO: run in its own thread, so it doesn't block send?
        // TODO: generate events
        if (isConnected() && reader != null)
        {
//            System.out.println("Waiting for message from socket"); // TODO: replace with logger
            Response response = null;
            String message = null;
            if ((message = reader.readLine()) != null)
            {
                response = new Response(message);
//                System.out.println("Response received: \"" + response + "\"");
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
            System.out.println("Closing connection..."); // TODO: replace with logger
            if (reader != null)
                reader.close();
            if (writer != null)
                writer.close();
            if (socket != null)
                socket.close();
        }
        catch (IOException e)
        {
            System.err.println("Error closing socket resources"); // TODO: replace with logger
            e.printStackTrace();
        }
    }
}
