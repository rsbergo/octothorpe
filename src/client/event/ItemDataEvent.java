package client.event;

import client.connector.Response;
import client.connector.ResponseCode;
import client.game.Item;

/**
 * Event raised when a message containing item data is received.
 * Item data include the item ID and the item position formatted as "102:<id>, <x>, <y>, <text>"
 */
public class ItemDataEvent extends Event
{
    private static final int FIELD_COUNT = 3; // expected number of fields in the message

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
     * Creates a new event based on the item information contained in the message.
     * 
     * @param response a response received from the game server
     */
    public ItemDataEvent(Response response)
    {
        this();
        if (response.getResponseCode() == ResponseCode.ItemNotification)
            item = getItemInformation(response.getMessage());
    }

    @Override
    public String toString()
    {
        return "Event " + getSubject() + ": " + item;
    }

    // Setters and Getters
    public Item getItem() { return item; }

    // Retrieves player information from the response message
    private Item getItemInformation(String message)
    {
        Item item = null;

        String[] tokens = message.split(", ");
        if (tokens.length < FIELD_COUNT)
        {
            System.err.println("Error getting item information from response");
            return item;
        }

        try
        {
            // TODO: assumes that the item ID is a number. Could it be a string?
            int id = Integer.parseInt(tokens[0]);
            int x = Integer.parseInt(tokens[1]);
            int y = Integer.parseInt(tokens[2]);
            item = new Item(id, x, y);
        }
        catch (NumberFormatException e)
        {
            System.err.println("Error getting item information from response");
            e.printStackTrace();
        }
        return item;
    }
}
