package gameserver;

import java.io.IOException;
import java.net.Socket;

import logger.Logger;
import logger.LogLevel;

/**
 * Represents the thread in which a player's interatcions is running.
 * It runs an additional thread for the player to receive notifications.
 */
public class PlayerHandler
{
    // TODO: Check whether it is necessary to synchronize playerSocket
    private PlayerSocket playerSocket = null; // the PlayerSocket associated with this PlayerHandler
    private Thread interactions = null;       // thread that will handle interactions between the Player and the Game
    private Thread notifications = null;      // thread that will send notifications to the Player
    private boolean running = false;          // indicates the current state of the GameServer
    
    /**
     * Constructor.
     * 
     * @param socket the socket connected to the player's client
     * @throws IOException if an I/O error occurs while setting up this PlayerHandler
     */
    public PlayerHandler(Socket socket) throws IOException
    {
        super();
        playerSocket = new PlayerSocket(socket);
        startInteractionsThread();
        startNotificationsThread();
        // TODO: close PlayerSocket
    }
    
    /**
     * Returns the ID of this PlayerHandler.
     * 
     * @return this PlayerHandler's ID
     */
    public int getId()
    {
        return playerSocket.getId();
    }
    
    /**
     * Starts running this PlayerHandler's threads
     */
    public void run()
    {
        running = true;
        interactions.start();
        notifications.start();
        while (running);
        try
        {
            interactions.join();
            Logger.log(LogLevel.Debug, "Interactions thread finished");
            notifications.join();
            Logger.log(LogLevel.Debug, "Notifications thread finished");
            playerSocket.close();
        }
        catch (InterruptedException e)
        {
            Logger.log(LogLevel.Error, "Error joining threads");
            e.printStackTrace();
        }
    }

    // Starts the thread that handles player interactions.
    private void startInteractionsThread()
    {
        interactions = new Thread(() ->
        {
            Logger.log(LogLevel.Debug, "Starting interactions thread");
            
            // TODO: Add private member Game. Receive the instance of Game in constructor.
            // TODO: Create request based on input (line)
            // TODO: Call Game.ProcessRequest on input
            // TODO: Send response
            // TODO: stop if "quit"
            String line = null;
            try
            {
                while ((line = playerSocket.receive()) != null)
                {
                    Logger.log(LogLevel.Debug, line);
                    playerSocket.send("Echo: " + line);
                }
            }
            catch (IOException e)
            {
                Logger.log(LogLevel.Error, "Error reading from socket");
                running = false;
                e.printStackTrace();
            }
        });
        interactions.setName("PlayerSocket" + playerSocket.getId());
    }
    
    // Starts the notifications thread
    private void startNotificationsThread()
    {
        notifications = new Thread(() ->
        {
            Logger.log(LogLevel.Debug, "Starting notifications thread");

            try
            {
                while (running)
                {
                    Thread.sleep(5000);
                    Logger.log(LogLevel.Debug, "Notifications thread is running");
                    playerSocket.send("New notification: ping!");
                }
            }
            catch (InterruptedException e)
            {
                Logger.log(LogLevel.Error, "Error writing into socket");
                running = false;
                e.printStackTrace();
            }
        });
        notifications.setName("PlayerSocket" + playerSocket.getId() + "-Notifications");
    }
}
