package game;

import java.util.ArrayList;
import java.util.List;

public class Request
{
    private Command command = Command.Unknown;   // the command to be executed
    private List<String> data = new ArrayList<String>(); // the arguments for the command to be executed
    
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
            command = Command.fromString(tokens[0]);
            for (int i = 1; i < tokens.length; i++)
                data.add(tokens[i]);
        }
    }
    
    /**
     * Constructor.
     * Builds a request with the specified command and data.
     * 
     * @param command The command of this Request
     * @param data    The arguments for this Request's command
     */
    public Request(Command command, String... data)
    {
        this.command = command;
        for (String s : data)
            this.data.add(s);
    }
    
    // Setters and Getters
    public Command getCommand() { return command; }
    public List<String> getData() { return data; }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(command + " ");
        for (String s : data)
            sb.append(s + " ");
        sb.deleteCharAt(sb.length() - 1); // remove extra " " added for last element of data
        return sb.toString();
    }
}
