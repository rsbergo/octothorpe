import gameserver.GameServer;
import logger.Logger;
import logger.LogLevel;

public class Octothorpe
{
    public static void main(String[] args)
    {
        Logger.setLogLevel(LogLevel.Debug);
        new GameServer(7777).runGameServer();
    }
}
