package commandhandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import command.Action;
import command.Command;
import command.Result;
import command.ResultCode;
import event.PlayerDisconnectedEvent;
import eventmanager.EventListener;
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
            savePlayersData();
            Player player = players.remove(command.getPlayer().getName());
            result.setResultCode(ResultCode.Success);
            result.setMessage("So long, and thanks for all the fish!");
            for (EventListener listener : player.getEventHandlerManager().getEventHandlerList())
                eventManager.unsubscribe(listener);
            eventManager.notify(new PlayerDisconnectedEvent(player));
            Logger.log(LogLevel.Debug, "Processing command finished. Result: \"" + result + "\"");
        }
    }

    // Store players data into a file.
    // Player data is stored as a comma-separated list of fields:
    // name,score,x,y
    // TODO: replace with static class for updating player data.
    // TODO: synchronize file
    private void savePlayersData()
    {
        String file = "res/players.data";
        StringBuilder sb = new StringBuilder();
        List<Player> list = readFileData();
        try (PrintWriter writer = new PrintWriter(file)) // PrintWriter truncates file, need to read before creating it
        {
            for (Player player : list)
            {
                sb.append(player.getName() + ",");
                sb.append(player.getScore() + ",");
                sb.append(player.getPosition().getX() + ",");
                sb.append(player.getPosition().getY() + "\r\n");
            }
            writer.write(sb.toString());
            writer.flush();
        }
        catch (FileNotFoundException e)
        {
            Logger.log(LogLevel.Debug, "Could not find players data file", e);
        }
    }

    private List<Player> readFileData()
    {
        String file = "res/players.data";
        List<Player> list = new ArrayList<Player>();
        try (Scanner sc = new Scanner(new File(file)))
        {
            String line = "";
            while (sc.hasNextLine() && !(line = sc.nextLine()).isEmpty())
            {
                String[] tokens = line.split(",");
                list.add(new Player(tokens[0], Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[1])));
            }
        }
        catch (FileNotFoundException e)
        {
            Logger.log(LogLevel.Debug, "Could not find players data file", e);
            // do nothing, use spawn point from map
        }
        for (String name : players.keySet())
        {
            Player player = players.get(name);
            list.remove(player);
            list.add(player);
        }
        return list;
    }
}
