package game;

public enum Command
{
    Unknown,
    Login,
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
        else if (command.equalsIgnoreCase("quit"))
            return Quit;
        else
            return Unknown;
    }
}
