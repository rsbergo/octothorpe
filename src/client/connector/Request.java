package client.connector;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a request sent to the game server.
 * A request specifies the command to be exeuted and any arguments for that command.
 */
public class Request
{
    private Command command = Command.Unknown;           // the command to be executed
    private List<String> args = new ArrayList<String>(); // the arguments for the command to be executed
    
    /**
     * Default constructor.
     */
    public Request()
    {}
    
    /**
     * Constructor.
     * Creates a request with the command and args specified.
     * 
     * @param command the command to be executed
     * @param args    the list of arguments to be executed
     */
    public Request(Command command, List<String> args)
    {
        this.command = command;
        this.args = args;
    }
    
    /**
     * Constructor.
     * Creates a request based on the string specified.
     * Assumes that the string is a space-separated list of tokens, in which the first token is the command to be
     * executed and the following tokens are the arguments for the command.
     * 
     * @param request the resquest string
     */
    public Request(String request)
    {
        String[] tokens = request.split(" ");
        command = Command.fromString(tokens[0]);
        for (int i = 1; i < tokens.length; i++)
            args.add(tokens[i]);
    }

    // Setters and Getters
    public void setCommand(Command command) { this.command = command; }
    public Command getCommand() { return command; }
    public void setArgs(List<String> args) { this.args = args; }
    public List<String> getArgs() { return args; }

    /**
     * Extracts the argument list from the string specified.
     * Assumes the string is a whitespace (' ') separated list.
     * 
     * @param args the list of arguments for the command, each argument separated by a single whitespace (' ')
     */
    public void setArgs(String argList)
    {
        args.clear();
        String[] values = argList.split(" ");
        for (String value : values)
            args.add(value);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(command);
        for (String arg : args)
            sb.append(" " + arg);
        return sb.toString();
    }
}
