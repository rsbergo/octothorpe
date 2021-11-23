package game.command.commandhandler;

import java.util.Map;

import game.GameMap;
import game.Player;
import game.command.Action;
import game.command.Command;
import game.command.Result;
import game.command.ResultCode;

/**
 * Processes commands whose action is Action.Login.
 * Receives an instance of the list of players in the game and an instance of the game map when installed.
 * Action.Login expects a single argument: the player's name.
 * The player is added to the game if not already logged in and if there are no players with the same name in the game.
 * Initiates synchronous map_data event for the player.
 * Initiates synchronous item_data events for the player.
 * Initiates an asynchronous player_connected event containing the new player information.
 * 
 * TODO: Also receive game's event manager, so it can trigger map_data, item_data, and player_connected events
 */
public class LoginCommandHandler implements CommandHandler
{
    private final Action EXPECTED_ACTION = Action.Login; // expected command action
    private final int EXPECTED_ARGS_COUNT = 1;           // expected number of args for the Move action
    
    private Map<String, Player> players = null; // reference to the list of players in the game
    private GameMap map = null;                 // reference to the map the game is running
    
    /**
     * Constructor.
     * Receives a reference to the list of players in the game and a reference to the map the game is using.
     * 
     * @param players the list of players in the game
     * @param map     the game map
     */
    public LoginCommandHandler(Map<String, Player> players, GameMap map)
    {
        this.players = players;
        this.map = map;
    }
    
    @Override
    public void processCommand(Command command, Result result)
    {
        if (isValidCommand(command, EXPECTED_ACTION, EXPECTED_ARGS_COUNT, result))
        {
            String name = command.getArgs().get(0);
            if (command.getPlayer() != null)         // player has already logged in
                getPlayerLoggedInResult(result);
            else if (players.get(name) != null)      // player's name already in use in the game
                getPlayerInGameResult(result, name);
            else                                     // command's player is null and name is not in use
            {
                Player player = new Player(name);
                player.updatePosition(map.getSpawnPoint());
                players.put(player.getName(), player);
                getSuccessResult(result, player.getName());
            }
            // TODO: create map_data event
            // TODO: trigger synchronous map_data event
            // TODO: create item_data events
            // TODO: trigger synchronous item_data events
            // TODO: create player_connected event
            // TODO: trigger asynchronous player_connected event
        }
    }
    
    @Override
    public boolean isValidCommand(Command command, Action expectedAction, int expectedArgsCount, Result result)
    {
        return hasValidAction(command.getAction(), result)
                && isExpectedAction(expectedAction, command.getAction(), result)
                && hasExpectedNumberofArguments(command.getArgs(), expectedArgsCount, result);
    }
    
    // Updates result with the outcome of a login attempt when the player is already logged in.
    private void getPlayerLoggedInResult(Result result)
    {
        result.setResultCode(ResultCode.BadRequest);
        result.setMessage("You are already logged in!");
    }
    
    // Updates result with the outcome of a login attempt when the player's name is already in use.
    private void getPlayerInGameResult(Result result, String name)
    {
        result.setResultCode(ResultCode.BadRequest);
        result.setMessage("Could not connect you to <" + name + ">");
    }
    
    // Updates result with the outcome of a succesful login attempt.
    private void getSuccessResult(Result result, String name)
    {
        result.setResultCode(ResultCode.Success);
        result.setMessage("Welcome to Octothorpe # The Game, <" + name + ">");
    }
}
