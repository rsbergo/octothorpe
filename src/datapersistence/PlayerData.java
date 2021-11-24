package datapersistence;

import game.Player;

/**
 * Represents the data from a player as stored in the player data file.
 * Player data contains the player's name, the x and y coordinates for the player's position, and the player's score.
 */
public class PlayerData
{
    private final int FIELD_COUNT = 4;

    public String name = null; // the player's name
    public int x = 0;          // the x coordinate of the player's position
    public int y = 0;          // the y coordinate of the player's position
    public int score = 0;      // the player's score

    /**
     * Constructor.
     * Retrieves a player data from the string specified.
     * The string is expected to be in the form of "name,x,y,score".
     * 
     * @param data the string containing player data
     */
    public PlayerData(String data)
    {
        String[] tokens = data.split(",");
        if (tokens.length == FIELD_COUNT)
        {
            name = tokens[0];
            x = Integer.parseInt(tokens[1]);
            y = Integer.parseInt(tokens[2]);
            score = Integer.parseInt(tokens[3]);
        }
    }

    /**
     * Constructor.
     * Retrieves a player data from a Player object.
     * 
     * @param player the player whose data should be retrieved
     */
    public PlayerData(Player player)
    {
        name = player.getName();
        x = player.getPosition().getX();
        y = player.getPosition().getY();
        score = player.getScore();
    }

    @Override
    public String toString()
    {
        return name + "," + x + "," + y + "," + score;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof PlayerData)
        {
            PlayerData data = (PlayerData) obj;
            return this.name.equalsIgnoreCase(data.name);
        }
        return false;
    }
}
