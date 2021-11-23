package commandhandler;

import java.util.Map;

import command.Action;
import command.Command;
import command.Result;
import command.ResultCode;
import event.PlayerUpdateEvent;
import eventmanager.EventManager;
import game.Player;
import logger.LogLevel;
import logger.Logger;

/**
 * Processes commands whose action is Action.Players.
 * Receives an instance of the list of players in the game.
 * Receives an instance of the game's event manager in order to generate events.
 * Action.Players does not expect any arguments (command args should be empty).
 * Initiates asynchronous player_update events containing the updated player informationfor all players.
 */
public class PlayersCommandHandler implements CommandHandler
{
    private final Action EXPECTED_ACTION = Action.Players; // expected command action
    private final int EXPECTED_ARGS_COUNT = 0;             // expected number of args for the Move action
    
    private Map<String, Player> players = null; // reference to the list of players in the game
    private EventManager eventManager = null; // reference to the game's event manager
    
    /**
     * Constructor.
     * Receives a reference to the list of players in the game.
     * 
     * @param players      the list of players in the game
     * @param eventManager the game's event manager
     */
    public PlayersCommandHandler(Map<String, Player> players, EventManager eventManager)
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
            result.setResultCode(ResultCode.Success);
            result.setMessage(getSuccessMessage());
            for (String player : players.keySet())
                eventManager.notify(new PlayerUpdateEvent(players.get(player)));
            Logger.log(LogLevel.Debug, "Processing command finished. Result: \"" + result + "\"");
        }
    }
    
    // Builds the message to be sent in a successful result.
    private String getSuccessMessage()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("OK. ");
        sb.append(players.size());
        sb.append(players.size() == 1 ? " player" : " players");
        sb.append(" in the game.");
        return sb.toString();
    }
}
