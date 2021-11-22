package game;

/**
 * Represents an item on the map.
 * Each item has an ID, a position on the map, and a value.
 */
public class Item
{
    private static int itemCount = 0;      // global item id
    private int itemId = 0;                // item ID
    private Position pos = new Position(); // item position on the game map
    private int value = 0;                 // item value
    
    /**
     * Default constructor.
     * Sets the item's position to (0, 0).
     * Sets the item's value to 0.
     */
    public Item()
    {
        this(new Position(), 0);
    }
    
    /**
     * Constructor.
     * Sets the item's position to the specified position.
     * Sets the item's value to the specified value.
     * 
     * @param pos   the item's position on the map
     * @param value the item's value
     */
    public Item(Position pos, int value)
    {
        itemId = ++itemCount;
        this.pos = pos;
        this.value = value;
    }

    /**
     * Constructor.
     * Sets the item's position to (x, y).
     * Sets the item's value to the specified value.
     * 
     * @param x the item's position's x coordinate
     * @param y the item's position's y coordinate
     * @param value the item's value
     */
    public Item(int x, int y, int value)
    {
        this(new Position(x, y), value);
    }

    // Setters and Getters
    public int getId() { return itemId; }
    public Position getPosition() { return pos; }
    public int getValue() { return value; }

    @Override
    public String toString()
    {
        return "Item " + itemId + ": " + pos + ", " + value + "points";
    }
}
