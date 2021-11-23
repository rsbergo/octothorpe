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
 * Processes commands whose action is Action.Players.
 * Receives an instance of the list of players in the game.
 * Action.Players does not expect any arguments (command args should be empty).
 * Initiates asynchronous player_update events containing the updated player informationfor all players.
 * 
 * TODO: Also receive game's event manager, so it can trigger player_update events
 */
public class PlayersCommandHandler implements CommandHandler
{
    private final Action EXPECTED_ACTION = Action.Players; // expected command action
    private final int EXPECTED_ARGS_COUNT = 0;             // expected number of args for the Move action
    
    private Map<String, Player> players = null; // reference to the list of players in the game
    
    /**
     * Constructor.
     * Receives a reference to the list of players in the game.
     * 
     * @param players the list of players in the game
     */
    public PlayersCommandHandler(Map<String, Player> players)
    {
        this.players = players;
    }
    
    @Override
    public void processCommand(Command command, Result result)
    {
        if (isValidCommand(command, EXPECTED_ACTION, EXPECTED_ARGS_COUNT, result))
        {
            Logger.log(LogLevel.Debug, "Start processing command: \"" + command + "\"");
            result.setResultCode(ResultCode.Success);
            result.setMessage(getSuccessMessage());
            // TODO: Create a player_update event for each player in the game
            // TODO: Trigger an asynchronous player_update event for each player in the game
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
