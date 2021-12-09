package server.gameserver;

import java.util.ArrayList;
import java.util.List;

import server.command.Action;

public class Request
{
    private Action action = Action.Unknown;              // the command to be executed
    private List<String> args = new ArrayList<String>(); // the arguments for the command to be executed
    
    /**
     * Constructor.
     * Builds a request based on the String specified.
     * The request String is expected to contain the command as its first element and the arguments for the command in
     * a space-separated list of arguments
     * 
     * @param request The request string
     */
    public Request(String request)
    {
        String[] tokens = request.split(" ");
        if (tokens.length > 0)
        {
            action = Action.fromString(tokens[0]);
            for (int i = 1; i < tokens.length; i++)
                args.add(tokens[i]);
        }
    }
    
    /**
     * Constructor.
     * Builds a request with the specified command and data.
     * 
     * @param command The command of this Request
     * @param data    The arguments for this Request's command
     */
    public Request(Action command, String... data)
    {
        this.action = command;
        for (String s : data)
            this.args.add(s);
    }
    
    // Setters and Getters
    public Action getAction() { return action; }
    public List<String> getArgs() { return args; }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(action);
        for (String s : args)
            sb.append(" " + s);
        return sb.toString();
    }
}
