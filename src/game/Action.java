package game;

/**
 * Represents an action a player can execute on the game.
 */
public enum Action
{
    Map, 
    Message, 
    Move, 
    Players, 
    Quit, 
    Unknown;
    
    /**
     * Retrieves the Action based on a string.
     * 
     * @param action the string representing the action
     * @return the Action represented by the specified string
     */
    public static Action fromString(String action)
    {
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
}
