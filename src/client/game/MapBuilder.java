package client.game;

import logger.Logger;
import logger.LogLevel;

public class MapBuilder
{
    private static Map map = null;          // the map being built
    
    /**
     * Adds map data to the map.
     * 
     * @param mapData the data to add to map
     */
    public static void build(String mapData)
    {
        if (isMapDimensions(mapData))
            map = initializeNewMap(mapData);
        else
        {
            if (!isFinished())
                map.addRow(mapData);
        }
    }
    
    /**
     * Retrieves the map that was built.
     * If the map isn't completed yet, returns null.
     * 
     * @return the map built if the map has been completed; null otherwise
     */ 
    public static Map getMap()
    {
        if (isFinished())
            return map;
        return null;
    }

    // Checks if mapData contains map dimensions.
    // Map dimensions is expected to be in the form "<rows>, <cols>".
    // ',' is not expected to appear anywhere in the map data.
    // TODO: needs to invert to <cols>, <rows> in the server.
    private static boolean isMapDimensions(String mapData)
    {
        String[] tokens = mapData.split(", ");
        return tokens.length == 2;
    }
    
    // Initializes a new map with dimensions provided in map data.
    private static Map initializeNewMap(String mapData)
    {
        Map map = null;
        try
        {
            String[] tokens = mapData.split(", ");
            int cols = Integer.parseInt(tokens[0]);
            int rows = Integer.parseInt(tokens[1]);
            map = new Map(rows, cols);
        }
        catch (NumberFormatException e)
        {
            Logger.log(LogLevel.Error, "Error retrieving map data information", e);
        }
        return map;
    }
    
    // Checks if the map has been finished by checking if the number of rows in the map matches the number of rows
    // expected.
    private static boolean isFinished()
    {
        if (map == null)
            return false;
        return map.getMap().size() == map.getRowCount();
    }
}
