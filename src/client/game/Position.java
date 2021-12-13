package client.game;

/**
 * Represents a position on a game map.
 * A position is given by a pair of coordinates (x, y).
 */
public class Position
{
    private int x = 0; // the x coordinate for the position
    private int y = 0; // the y coordinate for the position

    /**
     * Default constructor.
     * Creates a new position with coordinates (0, 0).
     */
    public Position()
    {
        this(0, 0);
    }

    /**
     * Constructor.
     * Creates a new position with the corrdinates (x, y) specified.
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Position(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    // Setters and Getters
    public void setX(int x) { this.x = x; }
    public int getX() { return x; }
    public void setY(int y) { this.y = y; }
    public int getY() { return y; }

    /**
     * Updates the position coordinates with the coordinates specified.
     * 
     * @param x the new x coordinate
     * @param y the new y coordinate
     */
    public void updatePosition(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString()
    {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Position)
        {
            Position pos = (Position) obj;
            return (this.x == pos.x) && (this.y == pos.y);
        }
        return false;
    }
}
