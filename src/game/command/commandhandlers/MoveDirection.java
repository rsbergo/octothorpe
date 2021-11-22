package game.command.commandhandlers;

public enum MoveDirection 
{
    North,
    South,
    East,
    West,
    Unknown;
    
    /**
     * Retrieves the MoveDirection based on a string.
     * 
     * @param direction the string representing the direction
     * @return the MoveDirection represented by the specified string
     */
    public static MoveDirection fromString(String direction)
    {
        if (direction.equalsIgnoreCase("north"))
            return North;
        if (direction.equalsIgnoreCase("south"))
            return South;
        if (direction.equalsIgnoreCase("east"))
            return East;
        if (direction.equalsIgnoreCase("west"))
            return West;
        return Unknown;
    }
}
