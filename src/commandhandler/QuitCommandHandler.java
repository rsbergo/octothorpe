package commandhandler;

import java.util.Map;

import command.Action;
import command.Command;
import command.Result;
import command.ResultCode;
import game.Player;
import logger.LogLevel;
import logger.Logger;

/**
 * Processes commands whose action is Action.Quit.
 * Receives an instance of the list of players in the game.
 * Action.Quit does not expect any arguments (command args should be empty).
 * Removes the player from the game
 * Initiates asynchronous player_disconnected.
 * 
 * TODO: Also receive game's event manager, so it can trigger player_disconnected events
 */
public class QuitCommandHandler implements CommandHandler
{
    private final Action EXPECTED_ACTION = Action.Quit; // expected command action
    private final int EXPECTED_ARGS_COUNT = 0;          // expected number of args for the Move action
    
    private Map<String, Player> players = null; // reference to the list of players in the game
    
    /**
     * Constructor.
     * Receives a reference to the list of players in the game.
     * 
     * @param players the list of players in the game
     */
    public QuitCommandHandler(Map<String, Player> players)
    {
        this.players = players;
    }
    
    @Override
    public void processCommand(Command command, Result result)
    {
        if (isValidCommand(command, EXPECTED_ACTION, EXPECTED_ARGS_COUNT, result))
        {
            Logger.log(LogLevel.Debug, "Start processing command: \"" + command + "\"");
            players.remove(command.getPlayer());
            result.setResultCode(ResultCode.Success);
            result.setMessage("So long, and thanks for all the fish!");
            // TODO: create player_disconnected event
            // TODO: trigger asynchronous player_disconnected event
            Logger.log(LogLevel.Debug, "Processing command finished. Result: \"" + result + "\"");
        }
    }
}
