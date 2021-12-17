package client.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores some state of the game being run.
 */
public class Game
{
    private Map<String, Player> players = new HashMap<String, Player>(); // players currently connected to the game
    private Map<Integer, Item> items = new HashMap<Integer, Item>();     // items on the map
    private client.game.Map map = null;                                  // the game map
    private Player player = null;                                        // the player currently logged in

    /**
     * Default constructor
     */
    public Game()
    {}

    /**
     * Constructor.
     * Initializes the game state with the player currently logged in the client.
     * 
     * @param name the name of the player logged in
     */
    public Game(String name)
    {
        setCurrentPlayer(name);
    }

    // Setters and Getters
    public Player getCurrentPlayer() { return player; }
    public void setMap(client.game.Map map) { this.map = map; }
    public client.game.Map getMap() { return map; }

    /**
     * Sets the player currently logged in.
     */
    public void setCurrentPlayer(String name)
    {
        if (players.containsKey(name))
            player = players.get(name);
        else
        {
            player = new Player(name);
            players.put(name, player);
        }
    }

    /**
     * Updates the information for a player on the game, including the current player.
     * If the player is already in the list of players, its information is updated.
     * If the player isn't in the list of players, it is added.
     * 
     * @param player the player whose information is to be updated
     */
    public void updatePlayer(Player player)
    {
        if (this.player != null && this.player.equals(player))
            this.player = player;
        players.put(player.getName(), player);
    }

    /**
     * Removes a player from the game.
     * 
     * @param player the player to be removed
     */
    public void removePlayer(Player player)
    {
        players.remove(player.getName());
    }

    /**
     * Returns a list of players connected to the game.
     * 
     * @return the list of players connected to the game
     */
    public List<Player> getPlayers()
    {
        List<Player> list = new ArrayList<Player>();
        for (String name : players.keySet())
            list.add(players.get(name));
        return list;
    }

    /**
     * Adds an item to the list of map items.
     * 
     * @param item the item to be added
     */
    public void addItem(Item item)
    {
        items.put(item.getId(), item);
    }

    /**
     * Retrieves an item from the list of map items
     * 
     * @param id ID of the item to be retrieved
     * @return the map item identified by the ID specified
     */
    public Item getItem(int id)
    {
        return items.get(id);
    }

    /**
     * Retrieves a string representation of the game map, with all its elements.
     * 
     * @return a string representation of the game map
     */
    public String getMapString()
    {
        String mapString = null;
        if (map != null)
        {
            StringBuilder sb = new StringBuilder();
            char[][] mapArray = getCompleteMap();
            for (int i = 0; i < mapArray.length; i++)
            {
                String row = new String(mapArray[i]);
                sb.append(row + "\r\n");
            }
            sb.delete(sb.length() - 2, sb.length()); // remove extra "\r\n" added after last row
            mapString = sb.toString();
        }
        return mapString;
    }

    // Returns an array representation of the map, including the position of items and players.
    private char[][] getCompleteMap()
    {
        char[][] mapArray = null;
        if (map != null)
        {
            mapArray = map.getMapArray();
            populateItems(mapArray);
            populatePlayers(mapArray);
        }
        return mapArray;
    }

    // Populate the map array with items at their position.
    private void populateItems(char[][] mapArray)
    {
        for (int id : items.keySet())
        {
            Item item = items.get(id);
            Position pos = item.getPosition();
            mapArray[pos.getY()][pos.getX()] = '*'; //'⋆'; // TODO: find a better character
        }
    }

    // Populate the map array with players at their current position.
    // A player is represented by the first char in its name.
    private void populatePlayers(char[][] mapArray)
    {
        for (String name : players.keySet())
        {
            Player player = players.get(name);
            Position pos = player.getPosition();
            mapArray[pos.getY()][pos.getX()] = player.getName().charAt(0);
        }
    }
}
