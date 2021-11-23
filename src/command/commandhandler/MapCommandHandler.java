package command.commandhandler;

import command.Action;
import command.Command;
import command.Result;
import command.ResultCode;
import game.GameMap;

/**
 * Processes commands whose action is Action.Map.
 * Receives an instance of the game map when installed.
 * Action.Map does not expect any arguments (command args should be empty).
 * Initiates synchronous map_data events containing map data.
 * 
 * TODO: Also receive game's event manager, so it can trigger map_data events
 */
public class MapCommandHandler implements CommandHandler
{
    private final Action EXPECTED_ACTION = Action.Map; // expected command action
    private final int EXPECTED_ARGS_COUNT = 0;         // expected number of args for the Map action
    
    private GameMap map = null; // reference to the map the game is running
    
    /**
     * Constructor.
     * Receives a reference to the map the game is using.
     * 
     * @param map the game map
     */
    public MapCommandHandler(GameMap map)
    {
        this.map = map;
    }
    
    @Override
    public void processCommand(Command command, Result result)
    {
        if (isValidCommand(command, EXPECTED_ACTION, EXPECTED_ARGS_COUNT, result))
        {
            result.setResultCode(ResultCode.Success);
            result.setMessage(getSuccessMessage());
            // TODO: create map_data event
            // TODO: trigger map_data synchronous event
        }
    }
    
    // Builds the message to be sent in a successful result.
    private String getSuccessMessage()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("OK. Game world is ");
        sb.append(map.getNumberOfRows());
        sb.append("x");
        sb.append(map.getNumberOfColumns());
        sb.append(" spaces");
        return sb.toString();
    }
    
    // Create a map_data event.
    // The map_data event contains the map size and the character representation of the map
    private void getMapDataEvent()
    {
        // TODO: should it trigger one event or a series of events?
    }
    
    // TODO: Delete
    /**
     * Sends the map data to the player.
     * 
     * @param player the player who requested the map data
     */
    /*
     * public void sendMapData(String player)
     * {
     * Result result = new Result();
     * result.setPlayer(player);
     * result.setResultCode(ResultCode.MapData);
     * for (int row = 0; row < map.getNumberOfRows(); row++)
     * {
     * result.setResultCode(ResultCode.MapData);
     * result.setMessage(map.getMapRow(row));
     * // TODO: send result synchronously
     * }
     * }
     */
    
    /**
     * Sends the map size to the player.
     * 
     * @param player the player who requested the map data
     */
    /*
     * public void sendMapSize(String player)
     * {
     * Result result = new Result();
     * result.setPlayer(player);
     * result.setResultCode(ResultCode.MapData);
     * result.setMessage(map.getNumberOfRows() + ", " + map.getNumberOfColumns());
     * // TODO: send result synchronously
     * }
     */
}
