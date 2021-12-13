package server.commandhandler;

import logger.LogLevel;
import logger.Logger;
import server.command.Action;
import server.command.Command;
import server.command.Result;
import server.command.ResultCode;
import server.event.MapDataEvent;
import server.event.Subject;
import server.eventmanager.EventManager;
import server.game.GameMap;

/**
 * Processes commands whose action is Action.Map.
 * Receives an instance of the game map when installed.
 * Receives an instance of the game's event manager in order to generate events.
 * Action.Map does not expect any arguments (command args should be empty).
 * Initiates synchronous map_data events containing map data.
 */
public class MapCommandHandler implements CommandHandler
{
    private final Action EXPECTED_ACTION = Action.Map; // expected command action
    private final int EXPECTED_ARGS_COUNT = 0;         // expected number of args for the Map action
    
    private GameMap map = null;               // reference to the map the game is running
    private EventManager eventManager = null; // reference to the game's event manager
    
    /**
     * Constructor.
     * Receives a reference to the map the game is using.
     * 
     * @param map          the game map
     * @param eventManager the game's event manager
     */
    public MapCommandHandler(GameMap map, EventManager eventManager)
    {
        this.map = map;
        this.eventManager = eventManager;
    }
    
    @Override
    public void processCommand(Command command, Result result)
    {
        if (isValidCommand(command, EXPECTED_ACTION, EXPECTED_ARGS_COUNT, result))
        {
            Logger.log(LogLevel.Debug, "Start processing command: \"" + command + "\"");
            result.setResultCode(ResultCode.Success);
            result.setMessage(getSuccessMessage());
            eventManager.notify(command.getPlayer().getEventHandlerManager().getEventHandler(Subject.MapData), new MapDataEvent(map));
            Logger.log(LogLevel.Debug, "Processing command finished. Result: \"" + result + "\"");
        }
    }
    
    // Builds the message to be sent in a successful result.
    private String getSuccessMessage()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("OK. Game world is ");
        sb.append(map.getRowsCount());
        sb.append("x");
        sb.append(map.getColsCount());
        sb.append(" spaces");
        return sb.toString();
    }
}
