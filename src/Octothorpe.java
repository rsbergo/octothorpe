import gameserver.GameServer;
import logger.Logger;
import logger.LogLevel;;

public class Octothorpe 
{
    // TODO: Parameterize port number

    public static void main(String[] args)
    {
        Logger.setLogLevel(LogLevel.Debug);
        new GameServer(7777).runGameServer();
    }    
}
