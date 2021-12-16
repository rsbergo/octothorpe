package client.gui;

import javax.swing.JPanel;

import client.observer.Observable;

/**
 * The GUI panel that coordinates interactions with the player.
 */
public class GamePanel extends Observable
{
    private JPanel panel = new JPanel();

    // Setters and Getters
    public JPanel getPanel() { return panel; }
}
