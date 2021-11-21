package game.events;

import game.Player;
import game.consts.Consts;
import observer.Event;

public class PlayerUpdatedEvent extends Event
{
    private Player player; // the player that was updated
    
    /**
     * Constructor.
     * 
     * @param player the player that was updated
     */
    public PlayerUpdatedEvent(Player player)
    {
        super(Consts.EVENT_PLAYER_UPDATED);
        this.player = player;
    }

    // Setters and Getters
    public Player getPlayer() { return player; }

    @Override
    public String toString()
    {
        return super.toString() + ", Player: " + player;
    }
}
