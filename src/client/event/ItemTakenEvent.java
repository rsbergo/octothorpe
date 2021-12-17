package client.event;

/**
 * Event raised when an item taken message is received.
 * Item taken message is assumed to be in the form "103:<player>, <item id>, <points>, <text>"
 */
public class ItemTakenEvent extends Event
{ 
    private String playerName = null; // player name
    private String itemId = null;     // item ID
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
     * Creates a new event based on the information specified.
     * 
     * @param playerName the player who took the item
     * @param itemId     the item that was taken
     * @param itemValue  the value of the item that was taken
     */
    public ItemTakenEvent(String playerName, String itemId, int itemValue)
    {
        this();
        this.playerName = playerName;
        this.itemId = itemId;
        this.itemValue = itemValue;
    }

    @Override
    public String toString()
    {
        return "Event " + getSubject() + ": " + playerName + " took item " + itemId + " (" + itemValue + " points)";
    }

    // Setters and Getters
    public void setPlayerName(String playerName) { this.playerName = playerName; }
    public String getPlayerName() { return playerName; }
    public void setItemId(String itemId) { this.itemId = itemId; }
    public String getItemId() { return itemId; };
    public void setItemValue(int itemValue) { this.itemValue = itemValue; }
    public int getItemValue() { return itemValue; } 
}
