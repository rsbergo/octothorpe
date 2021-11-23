package event;

import game.Item;

/**
 * Event generated when a player collects an item.
 */
public class ItemCollectedEvent extends Event
{
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
     * @param item the item that was collected
     */
    public ItemCollectedEvent(Item item)
    {
        this();
        this.item = item;   
    }
    
    // Setters and Getters
    public void setItem(Item item) { this.item = item; }
    public Item getItem() { return item; }

    @Override
    public String toString()
    {
        return "Event " + subject + ": " + item;
    }
}
