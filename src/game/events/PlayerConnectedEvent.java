package game.events;

import game.Player;
import game.consts.Consts;
import observer.Event;

public class PlayerConnectedEvent extends Event
{
    private Player player; // the player that has just connected
    
    /**
     * Constructor.
     * 
     * @param player the player that has just connected
     */
    public PlayerConnectedEvent(Player player)
    {
        super(Consts.EVENT_PLAYER_CONNECTED);
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
