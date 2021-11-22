package old.gameserver;

/**
 * Represents a player connected to the game.
 * A player is identified by its name. The player's name is defined when the player logs in the game. A player can be
 * connected to the game server, but not logged in yet.
 * A player can move around the game map by updating its position.
 * The player's total points comes from treasures.
 * A player's handler orchestrates the communication between the player's client and the game.
 */
public class Player
{
    private String name = null;        // the player's name
    private boolean connected = false; // indicates whether the player is connected to the game
    private boolean loggedIn = false;  // indicates whether the player has logged into the game
    
}
