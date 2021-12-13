package client.game;

/**
 * Represents a player currently on the game.
 * A player is identified by its name on the game.
 * Player information also includes position and points.
 */
public class Player
{
    private String name = null;            // the player's name
    private Position pos = new Position(); // the player's position
    private int score = 0;                 // the player's score

    /**
     * Default constructor.
     */
    public Player()
    {
        this(null, new Position(), 0);
    }

    /**
     * Constructor.
     * Creates a new player with the specified name, with position set to (0, 0) and score set to 0.
     * 
     * @param name the player's name
     */
    public Player(String name)
    {
        this(name, new Position(), 0);
    }

    /**
     * Constructor.
     * Creates a new player with the name, position and score specified.
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
     * Creates a new player with the name, position coordinates, and score specified.
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
    public void setPosition(Position pos) { this.pos = pos; }
    public void setPosition(int x, int y) { pos.updatePosition(x, y); }
    public Position getPosition() { return pos; }
    public void setScore(int score) { this.score = score; }
    public int getScore() { return score; }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Player)
        {
            Player player = (Player) obj;
            return this.name.equalsIgnoreCase(player.name);
        }
        return false;
    }

    @Override
    public String toString()
    {
        return name + ", " + pos + ", " + score + " points";
    }
}
