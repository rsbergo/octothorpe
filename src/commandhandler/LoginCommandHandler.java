package commandhandler;

import java.util.Map;

import command.Action;
import command.Command;
import command.Result;
import command.ResultCode;
import datapersistence.PlayerDataPersistence;
import event.ItemDataEvent;
import event.MapDataEvent;
import event.PlayerConnectedEvent;
import event.Subject;
import eventmanager.EventManager;
import game.GameMap;
import game.Item;
import game.Player;
import logger.LogLevel;
import logger.Logger;

/**
 * Processes commands whose action is Action.Login.
 * Receives an instance of the list of players in the game and an instance of the game map when installed.
 * Receives an instance of the game's event manager in order to generate events.
 * Action.Login expects a single argument: the player's name.
 * The player is added to the game if not already logged in and if there are no players with the same name in the game.
 * Initiates synchronous map_data event for the player.
 * Initiates synchronous item_data events for the player.
 * Initiates an asynchronous player_connected event containing the new player information.
 */
public class LoginCommandHandler implements CommandHandler
{
    private final Action EXPECTED_ACTION = Action.Login; // expected command action
    private final int EXPECTED_ARGS_COUNT = 1;           // expected number of args for the Move action
    
    private Map<String, Player> players = null; // reference to the list of players in the game
    private GameMap map = null;                 // reference to the map the game is running
    private EventManager eventManager = null;   // reference to the game's event manager
    
    /**
     * Constructor.
     * Receives a reference to the list of players in the game and a reference to the map the game is using.
     * 
     * @param players      the list of players in the game
     * @param map          the game map
     * @param eventManager the game's event manager
     */
    public LoginCommandHandler(Map<String, Player> players, GameMap map, EventManager eventManager)
    {
        this.players = players;
        this.map = map;
        this.eventManager = eventManager;
    }
    
    @Override
    public void processCommand(Command command, Result result)
    {
        if (isValidCommand(command, EXPECTED_ACTION, EXPECTED_ARGS_COUNT, result))
        {
            Logger.log(LogLevel.Debug, "Start processing command: \"" + command + "\"");
            String name = command.getArgs().get(0);
            if (command.getPlayer().getName() != null) // player has already logged in
                getPlayerLoggedInResult(result);
            else if (players.get(name) != null)        // player's name already in use in the game
                getPlayerInGameResult(result, name);
            else                                       // command's player is null and name is not in use
            {
                Player player = command.getPlayer();
                player.setName(name);
                player.updatePosition(map.getSpawnPoint());        // initial position
                players.put(player.getName(), player);
                PlayerDataPersistence.getStoredPlayerData(player); // update position and score
                if (!map.isValidPosition(player.getPosition()))    // if map changed, check if it's still valid position
                    player.updatePosition(map.getSpawnPoint());
                getSuccessResult(result, player.getName());
                installPlayerListeners(player);
                generateEvents(player);
            }
            Logger.log(LogLevel.Debug, "Processing command finished. Result: \"" + result + "\"");
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
        result.setPlayer(name);
        result.setMessage("Welcome to Octothorpe # The Game, <" + name + ">");
    }

    // Installs player's listeners to the games event manager.
    private void installPlayerListeners(Player player)
    {
        eventManager.subscribe(Subject.MapData, player.getEventHandlerManager().getEventHandler(Subject.MapData));
        eventManager.subscribe(Subject.ItemCollected, player.getEventHandlerManager().getEventHandler(Subject.ItemCollected));
        eventManager.subscribe(Subject.ItemData, player.getEventHandlerManager().getEventHandler(Subject.ItemData));
        eventManager.subscribe(Subject.PlayerConnected, player.getEventHandlerManager().getEventHandler(Subject.PlayerConnected));
        eventManager.subscribe(Subject.PlayerDisconnected, player.getEventHandlerManager().getEventHandler(Subject.PlayerDisconnected));
        eventManager.subscribe(Subject.PlayerUpdate, player.getEventHandlerManager().getEventHandler(Subject.PlayerUpdate));
        eventManager.subscribe(Subject.SendMessage, player.getEventHandlerManager().getEventHandler(Subject.SendMessage));
    }

    // Generates events triggered by a successful login
    private void generateEvents(Player player)
    {
        eventManager.notify(player.getEventHandlerManager().getEventHandler(Subject.MapData), new MapDataEvent(map));
        for (Item item : map.getItems())
            eventManager.notify(player.getEventHandlerManager().getEventHandler(Subject.ItemData), new ItemDataEvent(item));
        eventManager.notify(new PlayerConnectedEvent(player));
    }
}
