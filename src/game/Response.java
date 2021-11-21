package game;

/**
 * A response to a request.
 * A response has information (player name) about the player to which it is supposed to be delivered. If the player
 * name is null, the response is delivered to all players.
 */
public class Response
{
    private String player = null;                           // player to which the Response is addressed
    private ResponseStatus status = ResponseStatus.Unknown; // this Response's status
    private String data = "";                               // this Response's data
    
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
     * @param status The response status
     * @param data   The response data
     */
    public Response(ResponseStatus status, String data)
    {
        this.status = status;
        this.data = data;
    }
    
    /**
     * Constructor.
     * Buids a response based on the status and data specified.
     * 
     * @param player the player to which the response is addressed; null if broadcast
     * @param status the response status
     * @param data   the response data
     */
    public Response(String player, ResponseStatus status, String data)
    {
        this.player = player;
        this.status = status;
        this.data = data;
    }
    
    // Setters and Getters
    public void setResponseStatus(ResponseStatus status) { this.status = status; }
    public ResponseStatus getResponseStatus() { return status; }
    public void setData(String data) { this.data = data; }
    public String getData() { return data; }
    public void setPlayer(String player) { this.player = player; }
    public String getPlayer() { return player; }
    
    @Override
    public String toString()
    {
        return status.getCode() + ":" + data;
    }
}
