package client.gui;

import javax.swing.JFrame;

import client.gameclient.NotificationManager;

/**
 * Manages the client GUI.
 * Generates RequestEvents from the GUI.
 */
public class ClientGUI
{
    /**
     * Initializes the GUI for the game client.
     * 
     * @param notifier the notifications manager used by the game client
     */
    public void start(NotificationManager notifier)
    {
        java.awt.EventQueue.invokeLater(() -> 
        {
            JFrame mainWindow = new ClientWindow(notifier);
            mainWindow.setVisible(true);
        });
    }
}
