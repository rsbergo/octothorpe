package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import logger.LogLevel;
import logger.Logger;

/**
 * Represents a map within the game.
 * A map is represented essentially by a 2-dimmensional array of characters: each row represents a tick for the y
 * coordinate and each column represents a tick for the x coordinate.
 * The character '#' represents an obstacle on the map. A player cannot move into a position represented by '#'.
 * Players spawn at the spawn point at their login. The character 'S' represents a spawn point for players.
 * Maps can have a list of items. The character 'i' represents an item on the map.
 * 
 * TODO: add support for variable line lenght
 * TODO: determine item value on map file
 * TODO: randomize item position
 * TODO: randomize item value
 * TODO: randomize spawn point
 * TODO: support moving from one edge to another
 */
public class Map
{
    private final char OBSTACLE = '#';
    private final char SPAWN = 'S';
    private final char ITEM = 'i';

    private int rows = 0;                             // the number of rows in the map
    private int cols = 0;                             // the number of columns in the map
    private char[][] map;                             // the map representation
    private Position spawnPoint = new Position();     // spawn point of players
    private List<Item> items = new ArrayList<Item>(); // list of items contained in the map
    
    /**
     * Constructor.
     * Loads a map from the specified file.
     * 
     * @param map the file containing the map layout
     */
    public Map(File mapFile)
    {
        loadMapFromFile(mapFile);
    }

    // Setters and Getters
    public Position getSpawnPoint() { return spawnPoint; }
    public List<Item> getItems() { return items; }
    public int getNumberOfRows() { return rows; }
    public int getNumberOfColumns() { return cols; }

    /**
     * Checks whether the specified position is valid for a player move.
     * 
     * @param pos the position being checked
     * @return true if the position is not represented on the map by an obstacle character; false otherwise
     */
    public boolean isValidPosition(Position pos)
    {
        return map[pos.getY()][pos.getX()] != OBSTACLE;
    }

    /**
     * Checks whether the specified coordinates are valid for a player move.
     * 
     * @param x the x coordinate being checked
     * @param y the y coordinate being checked
     * @return true if the coordinates are not represented on the map by an obstacle character; false otherwise
     */
    public boolean isValidPosition(int x, int y)
    {
        return isValidPosition(new Position(x, y));
    }

    /**
     * Retrieves the specified row of the map
     * 
     * @param row the row to be retrieved
     * @return A String containing the characters in the specified map row; null if the specified row is out of bounds
     */
    public String getMapRow(int row)
    {
        if (row < 0 || row > rows)
            return null;
        String mapRow = new String(map[row]);
        return mapRow.replaceAll("[^#]", " "); // do not show spawn point or items
    }

    /**
     * Retrieves an item at the specified position.
     * Return null if there are no items at the position.
     * 
     * @param pos the position the item should be retrieved from
     * @return the item at the specified position; null if there are no items at the specified position
     */
    public Item getItemAtPosition(Position pos)
    {
        for (Item item : items)
        {
            if (item.getPosition().equals(pos))
                return item;
        }
        return null;
    }

    /**
     * Retrieves an item at the specified coordinates.
     * Return null if there are no items at the coordinates.
     * 
     * @param x the position's x coordinate
     * @param y the position's y coordinate
     * @return the item at the specified coordinates; null if there are no items at the specified position
     */
    public Item getItemAtPosition(int x, int y)
    {
        return getItemAtPosition(new Position(x, y));
    }
    
    // Loads a map layout from the specified file.
    // Assumes all lines in the file have the same lenght
    private void loadMapFromFile(File mapFile)
    {
        Logger.log(LogLevel.Info, "Loading map from file: \"" + mapFile.getAbsolutePath() + "\"");
        List<String> lines = readFileLines(mapFile);
        rows = lines.size();
        cols = lines.get(0).length();
        map = new char[rows][cols];
        for (int i = 0; i < rows; i++)
        {
            String line = lines.get(i);
            for (int j = 0; j < cols; j++)
            {
                map[i][j] = line.charAt(j);
                if (map[i][j] == SPAWN)
                    spawnPoint = new Position(j, i);
                if (map[i][j] == ITEM)
                    items.add(new Item(j, i, i + j));
            }
        }
    }
    
    // Reads the file lines.
    private List<String> readFileLines(File mapFile)
    {
        List<String> lines = new ArrayList<String>();
        try (Scanner sc = new Scanner(mapFile))
        {
            while (sc.hasNextLine())
                lines.add(sc.nextLine());
        }
        catch (FileNotFoundException e)
        {
            String message = "Could not open file containing map: \"" + mapFile.getAbsolutePath() + "\"";
            Logger.log(LogLevel.Error, message, e);
        }
        return lines;
    }
}
