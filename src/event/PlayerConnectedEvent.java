package event;

import game.Player;

/**
 * Event generated when a new player logs into the game.
 */
public class PlayerConnectedEvent extends Event
{
    private Player player; // the player that just connected
    
    /**
     * Default constructor.
     * Sets the event subject to Subject.PlayerConnected.
     */
    public PlayerConnectedEvent()
    {
        super(Subject.PlayerConnected);
    }
    
    /**
     * Default constructor.
     * Sets the event subject to Subject.PlayerConnected.
     * Sets the player that just connected.
     * 
     * @param player the player that just connected
     */
    public PlayerConnectedEvent(Player player)
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
