package client.event;

import client.game.Player;

/**
 * Event raised when a player updated message is received.
 * Player updated message is assumed to be in the form of "101:<name>, <x>, <y>, <score>, <text>"
 */
public class PlayerUpdatedEvent extends Event
{
    private Player player = null; // the player that was updated

    /**
     * Constructor.
     * Creates a new event with the subject PlayerUpdated.
     */
    public PlayerUpdatedEvent()
    {
        super();
        setSubject(Subject.PlayerUpdated);
    }

    /**
     * Constructor.
     * Creates a new event containing the player information specified.
     * 
     * @param player the player that was updated
     */
    public PlayerUpdatedEvent(Player player)
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
