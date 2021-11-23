package commandhandler;

import java.util.Map;

import command.Action;
import command.Command;
import command.Result;
import command.ResultCode;
import event.PlayerDisconnectedEvent;
import eventmanager.EventManager;
import game.Player;
import logger.LogLevel;
import logger.Logger;

/**
 * Processes commands whose action is Action.Quit.
 * Receives an instance of the list of players in the game.
 * Receives an instance of the game's event manager in order to generate events.
 * Action.Quit does not expect any arguments (command args should be empty).
 * Removes the player from the game
 * Initiates asynchronous player_disconnected.
 */
public class QuitCommandHandler implements CommandHandler
{
    private final Action EXPECTED_ACTION = Action.Quit; // expected command action
    private final int EXPECTED_ARGS_COUNT = 0;          // expected number of args for the Move action
    
    private Map<String, Player> players = null; // reference to the list of players in the game
    private EventManager eventManager = null; // reference to the game's event manager
    
    /**
     * Constructor.
     * Receives a reference to the list of players in the game.
     * 
     * @param players      the list of players in the game
     * @param eventManager the game's event manager
     */
    public QuitCommandHandler(Map<String, Player> players, EventManager eventManager)
    {
        this.players = players;
        this.eventManager = eventManager;
    }
    
    @Override
    public void processCommand(Command command, Result result)
    {
        if (isValidCommand(command, EXPECTED_ACTION, EXPECTED_ARGS_COUNT, result))
        {
            Logger.log(LogLevel.Debug, "Start processing command: \"" + command + "\"");
            Player player = players.remove(command.getPlayer());
            result.setResultCode(ResultCode.Success);
            result.setMessage("So long, and thanks for all the fish!");
            eventManager.notify(new PlayerDisconnectedEvent(player));
            Logger.log(LogLevel.Debug, "Processing command finished. Result: \"" + result + "\"");
        }
    }
}
