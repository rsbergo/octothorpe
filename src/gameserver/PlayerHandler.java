package gameserver;

import java.io.IOException;
import java.net.Socket;

import logger.Logger;
import logger.LogLevel;

/**
 * Represents the thread in which a player's interatcions is running.
 * It runs an additional thread for the player to receive notifications.
 */
public class PlayerHandler extends Thread
{
    private PlayerSocket playerSocket = null;  // the PlayerSocket associated with this PlayerHandler
    private Thread notificationsThread = null; // thread that will send notifications to the Player
    private boolean running = false;           // indicates the current state of the GameServer
    
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
        setUpNotificationsThread();
    }
    
    /**
     * Returns the ID of this PlayerHandler.
     * 
     * @return this PlayerHandler's ID
     */
    public int getSocketId()
    {
        return playerSocket.getId();
    }
    
    @Override
    public void run()
    {
        running = true;
        Thread.currentThread().setName("PlayerSocket" + playerSocket.getId());
        notificationsThread.start();
        try
        {
            String message = null;
            while (running && (message = playerSocket.receive()) != null)
            {
                // TODO: Add private member Game. Receive the instance of Game in constructor.
                // TODO: Create request based on input (line)
                // TODO: Call Game.ProcessRequest on input
                // TODO: Send response
                // TODO: stop if "quit"
                Logger.log(LogLevel.Debug, message);
                if (message.equals("quit"))
                    terminate();
                else
                    playerSocket.send("Echo: " + message);
            }
        }
        catch (IOException e)
        {
            Logger.log(LogLevel.Error, "Error receiving message from socket");
            running = false;
            e.printStackTrace();
        }
    }
    
    // Sets up the notifications thread
    private void setUpNotificationsThread()
    {
        notificationsThread = new Thread(() ->
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
        notificationsThread.setName("PlayerSocket" + playerSocket.getId() + "-Notifications");
    }

    // Terminates this PlayerHandler
    private void terminate()
    {
        try
        {
            running = false;
            notificationsThread.join();
            Logger.log(LogLevel.Debug, "Notifications thread terminated");
            Logger.log(LogLevel.Debug, "PlayerHandler thread terminated");
            playerSocket.close();
        }
        catch (InterruptedException e)
        {
            Logger.log(LogLevel.Error, "Error terminating notifications thread");
            e.printStackTrace();
        }
    }
}
