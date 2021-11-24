import gameserver.GameServer;
import logger.Logger;
import logger.LogLevel;

public class Octothorpe
{
    public static void main(String[] args)
    {
        if (args.length != 1)
        {
            System.err.println("Usage: java Octothorpe <port number>");
            System.exit(1);
        }
        
        Logger.setLogLevel(LogLevel.Debug);
        int port = getPort(args[0]);
        new GameServer(port).runGameServer();
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
}
