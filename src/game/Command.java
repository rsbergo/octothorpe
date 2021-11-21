package game;

public enum Command
{
    Unknown,
    Login,
    Move,
    Quit;

    /**
     * Gets the Command based on the specified string.
     * 
     * @param command the string representing the command
     * @return the Command represented by the string specified
     */
    public static Command fromString(String command)
    {
        if (command.equalsIgnoreCase("login"))
            return Login;
        if (command.equalsIgnoreCase("move"))
            return Move;
        else if (command.equalsIgnoreCase("quit"))
            return Quit;
        else
            return Unknown;
    }
}
