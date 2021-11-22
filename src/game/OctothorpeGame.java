package game;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import game.command.Action;
import game.command.Command;
import game.command.Result;
import game.command.ResultCode;
import game.command.commandhandler.CommandHandler;
import game.command.commandhandler.CommandHandlerManager;
import game.command.commandhandler.MapCommandHandler;
import game.command.commandhandler.MessageCommandHandler;
import game.command.commandhandler.MoveCommandHandler;
import game.command.commandhandler.PlayersCommandHandler;
import game.command.commandhandler.QuitCommandHandler;

/**
 * An instance of the Octothorpe Game.
 * A player can be added to the game and issue commands to move around the map.
 * 
 * TODO: persist information about players
 * TODO: parameterize map used by game
 */
public class OctothorpeGame
{
    private final String MAP_FILE = "res/game.map";                       // default game map

    private game.Map map = null;                                          // the map used in the game
    private Map<String, Player> players = new HashMap<String, Player>();  // list of players in the game
    private CommandHandlerManager handlers = new CommandHandlerManager(); // command handler manager
    //private Map<Action, CommandHandler> commandHandlers = new HashMap<Action, CommandHandler>(); // list of command handlers for the gale

    /**
     * Constructor.
     * Initializes the game map.
     */
    public OctothorpeGame()
    {
        map = new game.Map(new File(MAP_FILE));
        handlers.installCommandHandler(Action.Map, new MapCommandHandler(this));
        handlers.installCommandHandler(Action.Message, new MessageCommandHandler(this));
        handlers.installCommandHandler(Action.Move, new MoveCommandHandler(this));
        handlers.installCommandHandler(Action.Players, new PlayersCommandHandler(this));
        handlers.installCommandHandler(Action.Quit, new QuitCommandHandler(this));
    }

    // Setters and Getters
    public game.Map getMap() { return map; }

    /**
     * Adds a player to the game.
     * 
     * @param name the player's name
     */
    public void addPlayer(String name)
    {
        Player player = new Player(name);
        player.updatePosition(map.getSpawnPoint());
        players.put(player.getName(), player);
    }

    /**
     * Removes a player from the game.
     * 
     * @param name the player to be removed
     */
    public void removePlayer(String name)
    {
        players.remove(name);
        // TODO: ensure player data is persisted
    }

    /**
     * Retrieves the number of players currently in the game.
     * 
     * @return the number of players in the game
     */
    public int getPlayerCount()
    {
        return players.size();
    }

    /**
     * Retrieves the player identified by name.
     * 
     * @param name the name of the player t be retrieved
     * @return the player identified by the specified name, or null if there is no player identified by the specified
     *         name in the game
     */
    public Player getPlayer(String name)
    {
        return players.get(name);
    }

    /**
     * Sends the map data to the player.
     * 
     * @param player the player who requested the map data
     */
    public void sendMapData(String player)
    {
        Result result = new Result();
        result.setPlayer(player);
        result.setResultCode(ResultCode.MapData);
        for (int row = 0; row < map.getNumberOfRows(); row++)
        {
            result.setResultCode(ResultCode.MapData);
            result.setMessage(map.getMapRow(row));
            // TODO: send result synchronously
        }
    }

    /**
     * Sends the map size to the player.
     * 
     * @param player the player who requested the map data
     */
    public void sendMapSize(String player)
    {
        Result result = new Result();
        result.setPlayer(player);
        result.setResultCode(ResultCode.MapData);
        result.setMessage(map.getNumberOfRows() + ", " + map.getNumberOfColumns());
        // TODO: send result synchronously
    }

    /**
     * Sends message to players.
     * 
     * @param player the player who originated the message
     */
    public void sendMessage(String player, String message)
    {
        Result result = new Result();
        result.setPlayer(player);
        result.setResultCode(ResultCode.Message);
        result.setMessage(message);
        // TODO: send message asynchronously
    }

    // run the game
    public void run()
    {
        try (Scanner sc = new Scanner(System.in))
        {
            Command command = null;
            while ((command = new Command(sc.nextLine())) != null)
            {
                Result result = new Result();
                result.setPlayer(command.getPlayer());
                if (isValidCommand(command, result))
                {
                    if (handlers.getCommandHandler(command.getAction()) == null)
                    {
                        result.setResultCode(ResultCode.ServerError);
                        result.setMessage("Error. Cannot execute command.");
                    }
                    else
                        handlers.getCommandHandler(command.getAction()).processCommand(command, result);
                }
                System.out.println(result);
            }
        }
    }

    // Checks whether the command contains all the required attributes.
    private boolean isValidCommand(Command command, Result result)
    {
        if (command.getPlayer() == null)
        {
            result.setResultCode(ResultCode.BadRequest);
            result.setMessage("Error. The player who issued the command must be identified.");
            return false;
        }

        if (players.get(command.getPlayer()) == null)
        {
            result.setResultCode(ResultCode.BadRequest);
            result.setMessage("Error. The player is not in the game.");
            return false;
        }

        if (command.getAction() == Action.Unknown)
        {
            result.setResultCode(ResultCode.BadRequest);
            result.setMessage("Error. Unknown command.");
            return false;
        }

        return true;
    }
}
