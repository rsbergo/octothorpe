package client.connector;

/**
 * Represents a command that can be executed on the game.
 */
public enum Command
{
    Login,   // login as a player
    Map,     // request map information
    Message, // send a message
    Move,    // move towards a direction
    Players, // request player information
    Quit,    // exit
    Unknown;
    
    /**
     * Retrieves the Command based on a string.
     * 
     * @param action the string representing the command
     * @return the Command represented by the specified string
     */
    public static Command fromString(String action)
    {
        if (action.equalsIgnoreCase("login"))
            return Login;
        if (action.equalsIgnoreCase("map"))
            return Map;
        if (action.equalsIgnoreCase("message"))
            return Message;
        if (action.equalsIgnoreCase("move"))
            return Move;
        if (action.equalsIgnoreCase("players"))
            return Players;
        if (action.equalsIgnoreCase("quit"))
            return Quit;
        return Unknown;
    }

    /**
     * Retrieves a string representation for the command
     * 
     * @param command the command
     * @return the Action represented by the specified string
     */
    public static String toString(Command command)
    {
        if (command == Login)
            return "login";
        if (command == Map)
            return "map";
        if (command == Message)
            return "message";
        if (command == Move)
            return "move";
        if (command == Players)
            return "players";
        if (command == Quit)
            return "quit";
        return "unknown";
    }
}
