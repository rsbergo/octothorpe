package game;

import eventhandler.EventHandlerManager;

/**
 * Represents a player within the game.
 * A player is identified by its name. Two players cannot have the same name.
 * A player holds a position within the game map. The position is given by a pair of coordinates (x, y). A player moves
 * by updating its position.
 * The player total score is the result of the players actions within the game.
 */
public class Player
{
    String name = null;                       // the player's name
    Position pos = new Position();            // the player's position
    int score = 0;                            // the player's score
    EventHandlerManager eventHandlers = null; // reference to this player's handler's event handler manager
    
    /**
     * Constructor.
     * Defines the player's name.
     * The player's position is set to (0, 0).
     * The player's score is set to 0.
     * 
     * @param name the player's name
     */
    public Player(String name)
    {
        this(name, new Position(), 0);
    }
    
    /**
     * Constructor.
     * Initializes the player's attributes.
     * 
     * @param name  the player's name
     * @param pos   the player's position
     * @param score the player's score
     */
    public Player(String name, Position pos, int score)
    {
        this.name = name;
        this.pos = pos;
        this.score = score;
    }
    
    /**
     * Constructor.
     * Initializes the player's attributes.
     * 
     * @param name  the player's name
     * @param x     the player's position's x coordinate
     * @param y     the player's position's y coordinate
     * @param score the player's score
     */
    public Player(String name, int x, int y, int score)
    {
        this(name, new Position(x, y), score);
    }
    
    // Setters and Getters
    public void setName(String name) { this.name = name; }
    public String getName() { return name; }
    public Position getPosition() { return pos; }
    public int getScore() { return score; }
    public void setEventHandlerManager(EventHandlerManager eventHandlers) { this.eventHandlers = eventHandlers; }
    public EventHandlerManager getEventHandlerManager() { return eventHandlers; }

    /**
     * Updates the player's positions.
     * 
     * @param pos the player's new position
     */
    public void updatePosition(Position pos)
    {
        this.pos = pos;
    }
    
    /**
     * Updates the player's positions.
     * 
     * @param x the player's new x coordinate
     * @param y the player's new y coordinate
     */
    public void updatePosition(int x, int y)
    {
        updatePosition(new Position(x, y));
    }

    /**
     * Updates the player's score by the specified points.
     * 
     * @param points the number of points to be added to the player's score
     */
    public void updateScore(int points)
    {
        score += points;
    }

    @Override
    public String toString()
    {
        return name + ", " + pos.getX() + ", " + pos.getY() + ", " + score;
    }
}
