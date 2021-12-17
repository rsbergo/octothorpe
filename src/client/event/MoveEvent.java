package client.event;

import client.game.MoveDirection;

/**
 * Event raised when the player moves on the map.
 */
public class MoveEvent extends Event
{
    private MoveDirection direction = MoveDirection.Unknown; // direction the player is moving

    /**
     * Constructor.
     * Creates a new event with the subject Move.
     */
    public MoveEvent()
    {
        super();
        setSubject(Subject.Move);
    }

    /**
     * Constructor.
     * Creates a new event containing the direction specified.
     * 
     * @param direction the list of players
     */
    public MoveEvent(MoveDirection direction)
    {
        this();
        this.direction = direction;
    }

    @Override
    public String toString()
    {
        return "Event " + getSubject() + ": " + direction;
    }

    // Setters and Getters
    public void setDirection(MoveDirection direction) { this.direction = direction; }
    public MoveDirection getDirection() { return direction; }
}
