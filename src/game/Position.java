package game;

/**
 * Represents a position within the game map.
 * A position is given by a set of coordinates (x, y).
 */
public class Position
{
    private int x = 0; // the x coordinate of the position
    private int y = 0; // the y coordinate of the position

    /**
     * Default constructor.
     * Initializes the position to be (0, 0).
     */
    public Position()
    {}

    /**
     * Constructor.
     * Initializes the position to the coordinates specified.
     * 
     * @param x the position's x coordinate
     * @param y the position's y coordinate
     */
    public Position(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a copy of the position specified.
     * 
     * @param pos the reference position
     */
    public Position(Position pos)
    {
        this.x = pos.x;
        this.y = pos.y;
    }

    // Setters and Getters
    public void setX(int x) { this.x = x; }
    public int getX() { return x; }
    public void setY(int y) { this.y = y; }
    public int getY() { return y; }

    /**
     * Moves the x coordinate by the specified number of ticks, which can be negative.
     * 
     * @param ticks the number of ticks to move x
     */
    public void moveX(int ticks)
    {
        x += ticks;
    }

    /**
     * Moves the y coordinate by the specified number of ticks, which can be negative.
     * 
     * @param ticks the number of ticks to move y
     */
    public void moveY(int ticks)
    {
        y += ticks;
    }

    /**
     * Updates this position's coordinates to the values specified.
     * 
     * @param x the new x coordinate for this position
     * @param y the new y coordinate for this position
     */
    public void update(int x, int y)
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
            return this.x == pos.x
                   && this.y == pos.y;
        }
        return false;
    }
}
