package client.game;

/**
 * Represents an item in the game.
 */
public class Item
{
    private String id = "";              // the item ID
    private Position pos = new Position(); // the item position

    /**
     * Default constructor.
     */
    public Item()
    {
        this("", new Position());
    }

    /**
     * Constructor.
     * Creates a new item with the id and position specified.
     * 
     * @param id  the item's ID
     * @param pos the item's position
     */
    public Item(String id, Position pos)
    {
        this.id = id;
        this.pos = pos;
    }

    /**
     * Constructor.
     * Create a new item with the ID and (x, y) coordinates specified.
     * 
     * @param id the item's ID
     * @param x  the item's x coordinate
     * @param y  the item's y coordinate
     */
    public Item(String id, int x, int y)
    {
        this(id, new Position(x, y));
    }

    // Setters and Getters
    public void setId(String id) { this.id = id; }
    public String getId() { return id; }
    public void setPosition(Position pos) { this.pos = pos; }
    public void setPosition(int x, int y) { pos.updatePosition(x, y); }
    public Position getPosition() { return pos; }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Item)
        {
            Item item = (Item) obj;
            return this.id == item.id && this.pos.equals(item.pos);
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "Item " + id + " " + pos;
    }
}
