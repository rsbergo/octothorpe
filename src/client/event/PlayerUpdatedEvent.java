package client.event;

import client.connector.Response;
import client.connector.ResponseCode;
import client.game.Player;

/**
 * Event raised when a player updated message is received.
 * Player updated message is assumed to be in the form of "101:<name>, <x>, <y>, <score>, <text>"
 */
public class PlayerUpdatedEvent extends Event
{
    private static final int FIELD_COUNT = 4; // expected number of fields in the message

    private Player player = null; // the player that was updated

    /**
     * Constructor.
     * Creates a new PlayerUpdatedEvent with the subject PlayerUpdated.
     */
    public PlayerUpdatedEvent()
    {
        super();
        setSubject(Subject.PlayerUpdated);
    }

    /**
     * Constructor.
     * Creates a new event based on the player information contained in the message.
     * 
     * @param response a response received from the game server
     */
    public PlayerUpdatedEvent(Response response)
    {
        this();
        if (response.getResponseCode() == ResponseCode.PlayerUpdate)
            player = getPlayerInformation(response.getMessage());
    }

    @Override
    public String toString()
    {
        return "Event " + getSubject() + ": " + player;
    }

    // Setters and Getters
    public Player getPlayer() { return player; }

    // Retrieves player information from the response message
    private Player getPlayerInformation(String message)
    {
        Player player = null;

        String[] tokens = message.split(", ");
        if (tokens.length < FIELD_COUNT)
        {
            System.err.println("Error getting player information from response");
            return player;
        }

        try
        {
            String name = tokens[0];
            int x = Integer.parseInt(tokens[1]);
            int y = Integer.parseInt(tokens[2]);
            int score = Integer.parseInt(tokens[3]);
            player = new Player(name, x, y, score);
        }
        catch (NumberFormatException e)
        {
            System.err.println("Error getting player information from response");
            e.printStackTrace();
        }
        return player;
    }
}
