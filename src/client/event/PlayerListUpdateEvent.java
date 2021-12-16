package client.event;

import java.util.List;

import client.game.Player;

/**
 * Event raised when the list of players in the game is updated.
 */
public class PlayerListUpdateEvent extends Event
{
    private List<Player> players = null; // the list of players in the game

    /**
     * Constructor.
     * Creates a new event with the subject PlayerListUpdated.
     */
    public PlayerListUpdateEvent()
    {
        super();
        setSubject(Subject.PlayerListUpdated);
    }

    /**
     * Constructor.
     * Creates a new event containing the list of players.
     * 
     * @param players the list of players
     */
    public PlayerListUpdateEvent(List<Player> players)
    {
        this();
        this.players = players;
    }

    @Override
    public String toString()
    {
        return "Event " + getSubject() + ": " + players;
    }

    // Setters and Getters
    public void setPlayers(List<Player> players) { this.players = players; }
    public List<Player> getPlayers() { return players; }
}
