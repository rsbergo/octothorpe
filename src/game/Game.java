package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// TODO: The server may need to be changed. Should the server run a game for each client? Or should the game open a thread for each client?
// login
// map
// move
// notifications
// treasures

public class Game
{
    private List<Player> players = new ArrayList<Player>(); // list of players in the game

    /**
     * Runs the Octothorpe game
     */
    public void run()
    {
        try (Scanner scanner  = new Scanner(System.in))
        {
            Request req = new Request( scanner.nextLine());
            Response resp = processRequest(req);
            System.out.println(resp);
        }
    }

    // Processes a request received
    private Response processRequest(Request request)
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
        Player p = new Player(request.getData().get((0)));
        if (players.contains(p))
            return new Response(ResponseStatus.BadRequest, "Player [" + p.getName() + "] already logged in");
        players.add(p);
        return new Response(ResponseStatus.Success, "Welcome to Octothorpe # The Game, " + p.getName());
    }
}
