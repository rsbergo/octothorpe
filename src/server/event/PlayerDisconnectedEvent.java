package server.event;

import server.game.Player;

/**
 * Event generated when a new player quits the game.
 */
public class PlayerDisconnectedEvent extends Event
{
    private Player player; // the player that just disconnected
    
    /**
     * Default constructor.
     * Sets the event subject to Subject.PlayerDisconnected.
     */
    public PlayerDisconnectedEvent()
    {
        super(Subject.PlayerDisconnected);
    }
    
    /**
     * Default constructor.
     * Sets the event subject to Subject.PlayerDisconnected.
     * Sets the player that just disconnected.
     * 
     * @param player the player that just disconnected
     */
    public PlayerDisconnectedEvent(Player player)
    {
        this();
        this.player = player;   
    }
    
    // Setters and Getters
    public void setPlayer(Player player) { this.player = player; }
    public Player getPlayer() { return player; }

    @Override
    public String toString()
    {
        return "Event " + subject + ": " + player;
    }
}
