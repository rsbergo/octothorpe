package server.event;

import server.game.Item;

/**
 * Event generated to provide item information.
 */
public class ItemDataEvent extends Event
{
    private Item item; // the item whose information is being provided
    
    /**
     * Default constructor.
     * Sets the event subject to Subject.ItemData.
     */
    public ItemDataEvent()
    {
        super(Subject.ItemData);
    }
    
    /**
     * Default constructor.
     * Sets the event subject to Subject.ItemData.
     * Sets the item whose information is being provided.
     * 
     * @param item the item whose information is being provided
     */
    public ItemDataEvent(Item item)
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
