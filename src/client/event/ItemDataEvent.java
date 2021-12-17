package client.event;

import client.game.Item;

/**
 * Event raised when a message containing item data is received.
 * Item data include the item ID and the item position formatted as "102:<id>, <x>, <y>, <text>"
 */
public class ItemDataEvent extends Event
{
    private Item item = null; // the item whose information was received

    /**
     * Constructor.
     * Creates a new ItemDataEvent with the subject ItemData.
     */
    public ItemDataEvent()
    {
        super();
        setSubject(Subject.ItemData);
    }

    /**
     * Constructor.
     * Creates a new event based on the item information specified.
     * 
     * @param item the item whose data is being advertised
     */
    public ItemDataEvent(Item item)
    {
        this();
        this.item = item;
    }

    @Override
    public String toString()
    {
        return "Event " + getSubject() + ": " + item;
    }

    // Setters and Getters
    public Item getItem() { return item; }
}
