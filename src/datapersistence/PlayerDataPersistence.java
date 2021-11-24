package datapersistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import game.Player;
import game.Position;
import logger.Logger;
import logger.LogLevel;

/**
 * Provides synchronized read and write operations on a file storing player data.
 */
public class PlayerDataPersistence
{
    private static final File FILE = new File("res/players.data");
    
    /**
     * Updates the files containing player data with the data from the list of players provided.
     * 
     * @param players list of players whose information should be updated
     */
    public static synchronized void storePlayersData(List<Player> players)
    {
        List<PlayerData> dataList = readPlayerData();
        for (Player player : players)
        {
            PlayerData newData = new PlayerData(player);
            if (dataList.contains(newData))
                dataList.remove(newData);
            dataList.add(newData);
        }
        writePlayerData(dataList);
    }
    
    /**
     * Updates player information with data from the file storing player data.
     * Modifies the specified player instance to contain data that has been stored.
     * Leaves the specified player instance unmodified if data has not been stored for the player it represents yet.
     * 
     * @param player the player whose information should be retrieved from storage
     */
    public static synchronized void getStoredPlayerData(Player player)
    {
        List<PlayerData> dataList = readPlayerData();
        for (PlayerData data : dataList)
        {
            if (data.name.equalsIgnoreCase(player.getName()))
            {
                player.updatePosition(new Position(data.x, data.y));
                player.resetScore(data.score);
            }
        }
    }
    
    // Reads data currently stored in the file storing player data.
    // Player data is stored as a list of <name,x,y,score> entries, one entry per line.
    private static synchronized List<PlayerData> readPlayerData()
    {
        List<PlayerData> dataList = new LinkedList<PlayerData>();
        
        try (Scanner sc = new Scanner(FILE))
        {
            String line = "";
            while (sc.hasNextLine() && !(line = sc.nextLine()).isEmpty())
                dataList.add(new PlayerData(line));
        }
        catch (FileNotFoundException e)
        {
            Logger.log(LogLevel.Debug, "Could not find players data file", e);
        }
        return dataList;
    }
    
    // Writes player data into the file storing player data.
    // Wipes out the entire data from file, replacing it with the new data.
    private static synchronized void writePlayerData(List<PlayerData> dataList)
    {
        try (PrintWriter writer = new PrintWriter(FILE)) // PrintWriter truncates file
        {
            StringBuilder sb = new StringBuilder();
            for (PlayerData data : dataList)
                sb.append(data + "\r\n");
            writer.write(sb.toString());
            writer.flush();
        }
        catch (FileNotFoundException e)
        {
            Logger.log(LogLevel.Debug, "Could not find players data file", e);
        }
    }
}
