package game;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import command.Action;
import command.Command;
import command.Result;
import command.ResultCode;
import commandhandler.CommandHandlerManager;
import commandhandler.LoginCommandHandler;
import commandhandler.MapCommandHandler;
import commandhandler.MessageCommandHandler;
import commandhandler.MoveCommandHandler;
import commandhandler.PlayersCommandHandler;
import commandhandler.QuitCommandHandler;
import event.Subject;
import eventmanager.EventManager;

/**
 * An instance of the Octothorpe Game.
 * A player can be added to the game and issue commands to move around the map.
 * 
 * TODO: persist information about players
 * TODO: parameterize map used by game
 */
public class OctothorpeGame
{
    private final String MAP_FILE = "res/game.map"; // default game map
    
    private game.GameMap map = null;                                      // the map used in the game
    private Map<String, Player> players = new HashMap<String, Player>();  // list of players in the game
    private CommandHandlerManager handlers = new CommandHandlerManager(); // command handler manager
    private EventManager eventManager = new EventManager();               // list of event managers
    
    /**
     * Constructor.
     * Initializes the game map.
     * Installs command handlers.
     */
    public OctothorpeGame()
    {
        map = new game.GameMap(new File(MAP_FILE));
        installCommandHandlers();
        registerEvents();
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

    // Installs command handlers for the game.
    private void installCommandHandlers()
    {
        handlers.installCommandHandler(Action.Login, new LoginCommandHandler(players, map, eventManager));
        handlers.installCommandHandler(Action.Map, new MapCommandHandler(map, eventManager));
        handlers.installCommandHandler(Action.Message, new MessageCommandHandler(eventManager));
        handlers.installCommandHandler(Action.Move, new MoveCommandHandler(players, map, eventManager));
        handlers.installCommandHandler(Action.Players, new PlayersCommandHandler(players, eventManager));
        handlers.installCommandHandler(Action.Quit, new QuitCommandHandler(players, eventManager));
    }

    // Register events generated by the game.
    private void registerEvents()
    {
        eventManager.registerSubject(Subject.ItemCollected);
        eventManager.registerSubject(Subject.ItemData);
        eventManager.registerSubject(Subject.MapData);
        eventManager.registerSubject(Subject.PlayerConnected);
        eventManager.registerSubject(Subject.PlayerDisconnected);
        eventManager.registerSubject(Subject.PlayerUpdate);
        eventManager.registerSubject(Subject.SendMessage);
    }
}
