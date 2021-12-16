package client.event;

import client.game.Player;

/**
 * Event raised when a player updated message is received indicated that a player has disconnected.
 * Player updated message indicating that a player disconnected is assumed to carry player coordinates (-1, -1).
 */
public class PlayerDisconnectedEvent extends Event
{
    private Player player = null; // the player that was updated

    /**
     * Constructor.
     * Creates a new event with the subject PlayerDisconnected.
     */
    public PlayerDisconnectedEvent()
    {
        super();
        setSubject(Subject.PlayerDisconnected);
    }

    /**
     * Constructor.
     * Creates a new event containing the player information specified.
     * 
     * @param player the player that has disconnected
     */
    public PlayerDisconnectedEvent(Player player)
    {
        this();
        this.player = player;
    }

    @Override
    public String toString()
    {
        return "Event " + getSubject() + ": " + player;
    }

    // Setters and Getters
    public void setPlayer(Player player) { this.player = player; }
    public Player getPlayer() { return player; }    
}
