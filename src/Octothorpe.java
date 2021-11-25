import java.io.File;

import gameserver.GameServer;
import logger.Logger;
import logger.LogLevel;

public class Octothorpe
{
    private static final String DEFAULT_MAP = "res/default.map";

    public static void main(String[] args)
    {
        if (args.length < 1 || args.length > 2)
        {
            System.err.println("Usage: java Octothorpe <port number> [<map file>]");
            System.exit(1);
        }
        
        Logger.setLogLevel(LogLevel.Debug);
        int port = getPort(args[0]);
        File mapFile = getMapFile(args);
        new GameServer(port).runGameServer(mapFile);
    }
    
    // Gets the port number from command line arguments
    private static int getPort(String portString)
    {
        try
        {
            int port = Integer.parseInt(portString);
            if (port < 0 || port > 65535)
            {
                System.err.println("Port value out of range: " + port);
                System.err.println("Port number should be a number in the range [0,65535]");
                System.exit(1);
            }
            return port;
        }
        catch (NumberFormatException e)
        {
            System.err.println("Invalid value for port: \"" + portString + "\"");
            System.err.println("Port number should be a number in the range [0,65535]");
            System.exit(1);
        }
        return 0;
    }

    // Gets the file name for the map file from command line arguments
    private static File getMapFile(String[] args)
    {
        File mapFile = new File(DEFAULT_MAP);
        if (args.length == 2)
            mapFile = new File(args[1]);
        if (!mapFile.exists())
        {
            System.err.println("Could not find map fils \"" + mapFile.getAbsolutePath() + "\"");
            System.exit(1);
        }
        return mapFile;
    }
}
