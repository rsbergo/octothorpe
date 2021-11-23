package game;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import game.command.Action;
import game.command.Command;
import game.command.Result;
import game.command.ResultCode;
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
 * TODO: add item events
 * TODO: persist information about players
 * TODO: parameterize map used by game
 */
public class OctothorpeGame
{
    private final String MAP_FILE = "res/game.map";                       // default game map
    
    private game.GameMap map = null;                                          // the map used in the game
    private Map<String, Player> players = new HashMap<String, Player>();  // list of players in the game
    private CommandHandlerManager handlers = new CommandHandlerManager(); // command handler manager
    // private Map<Action, CommandHandler> commandHandlers = new HashMap<Action, CommandHandler>(); // list of command
    // handlers for the gale
    
    /**
     * Constructor.
     * Initializes the game map.
     * Installs command handlers.
     */
    public OctothorpeGame()
    {
        map = new game.GameMap(new File(MAP_FILE));
        handlers.installCommandHandler(Action.Map, new MapCommandHandler(map));
        handlers.installCommandHandler(Action.Message, new MessageCommandHandler());
        handlers.installCommandHandler(Action.Move, new MoveCommandHandler(players, map));
        handlers.installCommandHandler(Action.Players, new PlayersCommandHandler(players));
        handlers.installCommandHandler(Action.Quit, new QuitCommandHandler(players));
    }
    
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
                // TODO: validate if the command's player is in the game before returning the result
                /*
                 * if (players.get(command.getPlayer()) == null)
                 * {
                 * result.setResultCode(ResultCode.BadRequest);
                 * result.setMessage("Error. The player is not in the game.");
                 * return false;
                 * }
                 */
                if (handlers.getCommandHandler(command.getAction()) == null)
                {
                    result.setResultCode(ResultCode.ServerError);
                    result.setMessage("Error. Cannot execute command.");
                }
                else
                    handlers.getCommandHandler(command.getAction()).processCommand(command, result);
                System.out.println(result);
            }
        }
    }
}
