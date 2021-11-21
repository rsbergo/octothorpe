package game;

public class Player
{
    private String name; // player's login name
    
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
