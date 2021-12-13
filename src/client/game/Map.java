package client.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the map being run by the game.
 * The map is represented by a 2-D array of characters.
 */
public class Map
{
    private List<String> map = new ArrayList<String>(); // the map representation
    private int rows = 0;                               // the number of rows in the map
    private int cols = 0;                               // the number of columns in the map

    /**
     * Constructor.
     * Initializes the map representation with the dimensions specified.
     * 
     * @param rows the number of rows in the map
     * @param cols the number of columns in each row
     */
    public Map(int rows, int cols)
    {
        this.rows = rows;
        this.cols = cols;
    }

    // Setters and Getters
    public int getRowCount() { return rows; }
    public int getColumnCount() { return cols; }
    public List<String> getMap() { return map; }

    /**
     * Adds a row to the map representation.
     * 
     * @param row   the row representation
     * @throws IllegalArgumentException if the row length isn't consistent for the map
     */
    public void addRow(String row) throws IllegalArgumentException
    {
        if (row.length() != cols)
            throw new IllegalArgumentException("Invalid row for current map");

        map.add(row);
    }

    /**
     * Retrieves the map representation as a 2-D array of characters.
     * 
     * @return the representation of this map as a 2-D array of characters
     */
    public char[][] getMapArray()
    {
        char[][] mapArray = new char[rows][cols];
        for (int i = 0; i < rows; i++)
            mapArray[i] = map.get(i).toCharArray();
        return mapArray;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (String row : map)
            sb.append(row + "\r\n");
        return sb.toString();
    }
}
