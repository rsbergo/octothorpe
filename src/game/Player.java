package game;

public class Player
{
    private String name;    // player's login name
    private int x = 1;      // player's x position  // TODO: replace with a position object
    private int y = 1;      // player's y position
    private int points = 0; // player points
    
    /**
     * Constructor.
     * 
     * @param name This player's login name
     */
    public Player(String name)
    {
        this.name = name;
    }

    // Setters and Getters
    public String getName() { return name; }
    public int getPositionX() { return x; }
    public void setPositionX(int x) { this.x = x; }
    public int getPositionY() { return y; }
    public int getPoints() { return points; }

    @Override
    public boolean equals(Object player)
    {
        if (player instanceof Player)
        {
            Player otherPlayer = (Player) player;
            return this.name.equalsIgnoreCase(otherPlayer.name);
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "Player: " + name;
    }
}
