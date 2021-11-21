package game;

import java.util.ArrayList;
import java.util.List;

import game.consts.Consts;
import game.events.PlayerConnectedEvent;
import observer.Observable;

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
        if (request.getCommand() == Command.Login)
            return processLogin(request);
        else    
            return new Response(ResponseStatus.BadRequest, "Unknown command");
    }

    private Response processLogin(Request request)
    {
        if (request.getData().size() != 1)
            return new Response(ResponseStatus.BadRequest, "Request badly formatted");
        Player player = new Player(request.getData().get((0)));
        if (players.contains(player))
            return new Response(ResponseStatus.BadRequest, "You are already logged in!");
        players.add(player);
        notifyAll(new PlayerConnectedEvent(player));
        return new Response(ResponseStatus.Success, "Welcome to Octothorpe # The Game, " + player.getName());
    }
}
