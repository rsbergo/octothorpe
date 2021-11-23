package command;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a command received by the game.
 * A command must have the player that is issuing the command and an action to be executed, and may have a list of
 * arguments for this action. The list of arguments for the action may be empty.
 */
public class Command
{
    private String player = null;                        // the player issuing the command
    private Action action = Action.Unknown;              // the command action
    private List<String> args = new ArrayList<String>(); // the list of arguments for the action
    
    /**
     * Default constructor.
     */
    public Command()
    {}

    /**
     * Constructor.
     * Sets the command action and its list of arguments.
     * 
     * @param player the player issuing the command
     * @param action the action to be executed
     * @param args   the list of arguments for the action to be executed
     */
    public Command(String player, Action action, List<String> args)
    {
        this.player = player;
        this.action = action;
        this.args = args;
    }
    
    /**
     * Constructor.
     * Creates a Command based on the command string.
     * The command string is a list of tokens separated by a whitespace (' ').
     * The command string must have at least two tokens: the player and the action.
     * The first token in the command string must be the player issuing the command. 
     * The second token in the command string must be the action to be executed.
     * The following tokens constitute the arguments for the action.
     * 
     * @param commandString
     */
    public Command(String commandString)
    {
        String[] tokens = commandString.split(" ");
        if (tokens.length >= 2)
        {
            player = tokens[0].equalsIgnoreCase("null") ? null : tokens[0]; // TODO: remove, this is for testing only
            action = Action.fromString(tokens[1]);
            for (int i = 2; i < tokens.length; i++)
                args.add(tokens[i]);
        }
    }

    // Setters and Getters
    public void setPlayer(String player) { this.player = player; }
    public String getPlayer() { return player; }
    public void setAction(Action action) { this.action = action; }
    public Action getAction() { return action; }
    public void setArgs(List<String> args) { this.args = args; }
    public List<String> getArgs() { return args; }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(player);
        sb.append(" " + action);
        for (String arg : args)
            sb.append(" " + arg);
        return sb.toString();
    }
}
