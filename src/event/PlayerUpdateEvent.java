package event;

import game.Player;

/**
 * Event generated when a new player moves.
 */
public class PlayerUpdateEvent extends Event
{
    private Player player; // the player that was updated
    
    /**
     * Default constructor.
     * Sets the event subject to Subject.PlayerUpdate.
     */
    public PlayerUpdateEvent()
    {
        super(Subject.PlayerUpdate);
    }
    
    /**
     * Default constructor.
     * Sets the event subject to Subject.PlayerUpdate.
     * Sets the player that was updated.
     * 
     * @param player the player that was updated
     */
    public PlayerUpdateEvent(Player player)
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
