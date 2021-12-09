package server.gameserver;

import server.command.ResultCode;

/**
 * A response to a request.
 * A response has information (player name) about the player to which it is supposed to be delivered. If the player
 * name is null, the response is delivered to all players.
 */
public class Response
{
    private ResultCode code = ResultCode.Unknown; // this Response's status
    private String message = "";                  // this Response's message
    
    /**
     * Default constructor.
     */
    public Response()
    {
    }
    
    /**
     * Constructor.
     * Buids a response based on the status and data specified.
     * 
     * @param code    The response code
     * @param message The response data
     */
    public Response(ResultCode code, String message)
    {
        this.code = code;
        this.message = message;
    }
    
    // Setters and Getters
    public void setResponseCode(ResultCode code) { this.code = code; }
    public ResultCode getResponseCode() { return code; }
    public void setMessage(String message) { this.message = message; }
    public String getData() { return message; }
    
    @Override
    public String toString()
    {
        return code.getCode() + ":" + message;
    }
}
