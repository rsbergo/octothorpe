package client.game;

/**
 * Possible movement directions in the game.
 */
public enum MoveDirection
{
    Unknown("unknown"),
    East("east"),
    North("north"),
    South("south"),
    West("west");

    private String direction; // string representation of the direction
    
    // Constructor
    private MoveDirection(String direction)
    {
        this.direction = direction;
    }
    
    // Setters and Getters
    public String toString() { return direction; }
}
