package client.event;

import client.connector.Response;
import client.connector.ResponseCode;

/**
 * Event raised when an item taken message is received.
 * Item taken message is assumed to be in the form "103:<player>, <item id>, <points>, <text>"
 */
public class ItemTakenEvent extends Event
{
    private static final int FIELD_COUNT = 3; // expected number of fields in the message
 
    private String playerName = null; // player name
    private int itemId = 0;           // item ID // TODO: assumes that item ID is number. Could it be string?
    private int itemValue = 0;        // value of item

    /**
     * Default constructor.
     * Creates a new ItemTakenEvent with the subject ItemTaken.
     */
    public ItemTakenEvent()
    {
        super();
        setSubject(Subject.ItemTaken);
    }

    /**
     * Constructor.
     * Creates a new event based on the information contained in the message.
     * 
     * @param response a response received from the game server
     */
    public ItemTakenEvent(Response response)
    {
        this();
        if (response.getResponseCode() == ResponseCode.ItemTaken)
            getItemTakenInformation(response.getMessage());
    }

    @Override
    public String toString()
    {
        return "Event " + getSubject() + ": " + playerName + " took item " + itemId + " (" + itemValue + " points)";
    }

    // Setters and Getters
    public String getPlayerName() { return playerName; }
    public int getItemId() { return itemId; };
    public int getItemValue() { return itemValue; } 

    // Retrieves player information from the response message
    private void getItemTakenInformation(String message)
    {
        String[] tokens = message.split(", ");
        if (tokens.length < FIELD_COUNT)
            System.err.println("Error getting item information from response");

        try
        {
            playerName = tokens[0];
            itemId = Integer.parseInt(tokens[1]); // TODO: assumes that the item ID is a number. Could it be a string?
            itemValue = Integer.parseInt(tokens[2]);
        }
        catch (NumberFormatException e)
        {
            System.err.println("Error getting item taken information from response");
            e.printStackTrace();
        }
    }
}
