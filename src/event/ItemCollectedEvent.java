package event;

import game.Item;
import game.Player;

/**
 * Event generated when a player collects an item.
 */
public class ItemCollectedEvent extends Event
{
    private Player player; //player who collected the item
    private Item item; // the item that was collected
    
    /**
     * Default constructor.
     * Sets the event subject to Subject.ItemCollected.
     */
    public ItemCollectedEvent()
    {
        super(Subject.ItemCollected);
    }
    
    /**
     * Default constructor.
     * Sets the event subject to Subject.ItemCollected.
     * Sets the item that was collected.
     * 
     * @param player the player who collected the item
     * @param item the item that was collected
     */
    public ItemCollectedEvent(Player player, Item item)
    {
        this();
        this.item = item;
        this.player = player;
    }
    
    // Setters and Getters
    public void setItem(Item item) { this.item = item; }
    public Item getItem() { return item; }
    public void setPlayer(Player player) { this.player = player; }
    public Player getPlayer() { return player;}

    @Override
    public String toString()
    {
        return "Event " + subject + ": " + player + " - " + item;
    }
}
