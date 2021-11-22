package old.game_old;

import java.util.ArrayList;
import java.util.List;

import game.command.Action;
import old.game_old.consts.Consts;
import old.game_old.events.PlayerConnectedEvent;
import old.game_old.events.PlayerUpdatedEvent;
import old.observer.Observable;

// login
// map
// move
// notifications
// treasures

/**
 * An instance of the game being played.
 * Receives requests and generates responses to these requests.
 * Also generates asynchronous events.
 */
public class Game extends Observable
{
    private List<Player> players = new ArrayList<Player>(); // list of players in the game
    boolean running = false;                                // indicates whether this game has started

    public Game()
    {
        register(Consts.EVENT_PLAYER_CONNECTED);
        register(Consts.EVENT_PLAYER_UPDATED);
    }

    /**
     * Runs the Octothorpe game
     */
    public void start()
    {
        running = true;
    }

    // Processes a request received
    public Response processRequest(Request request)
    {
        if (request.getCommand() == Action.Map)
            return processLogin(request);
        if (request.getCommand() == Action.Move)
            return processMove(request);
        else    
            return new Response(ResponseStatus.BadRequest, "Unknown command");
    }

    // Processes a login request
    // TODO: Organize this into different functions
    private Response processLogin(Request request)
    {
        Response response = new Response();
        if (request.getData().size() != 1)
        {
            response.setResponseStatus(ResponseStatus.BadRequest);
            response.setData("Error: missing player name - \"login <name>\"");
            return response;
        }
        
        if (request.getPlayer() != null)
        {
            response.setPlayer(request.getPlayer());
            response.setResponseStatus(ResponseStatus.BadRequest);
            response.setData("You are already logged in!");
            return response;
        }
        
        Player player = new Player(request.getData().get((0)));
        if (players.contains(player))
        {
            response.setResponseStatus(ResponseStatus.BadRequest);
            response.setData("Error: could not connect you to \"" + player.getName() + "\"");
            return response;
        }
        players.add(player);
        notifyAll(new PlayerConnectedEvent(player));
        response.setPlayer(player.getName());
        response.setResponseStatus(ResponseStatus.Success);
        response.setData("Welcome to Octothorpe # The Game, " + player.getName());
        return response;
    }

    private Response processMove(Request request)
    {
        Response response = new Response(ResponseStatus.Success, request.toString());
        Player player = null;
        for (Player p : players)
        {
            if (p.getName().equals(request.getPlayer()))
                player = p;
        }
        if (player != null)
        {
            player.setPositionX(1);
            notifyAll(new PlayerUpdatedEvent(player));
        }
        return response;
    }
}
