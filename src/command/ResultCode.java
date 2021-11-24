package command;

/**
 * Represents the type of outcome of an action executed by a player on the game.
 */
public enum ResultCode
{
    Unknown(0),
    Message(100),
    PlayerUpdate(101),
    ItemNotification(102),
    ItemTaken(103),
    MapData(104),
    Success(200),
    BadRequest(400),
    ServerError(500);
    
    private int code; // response numeric code
    
    // Constructor
    private ResultCode(int code)
    {
        this.code = code;
    }
    
    // Setters and Getters
    public int getCode() { return code; }
    
    /**
     * Gets the ResponseCode based on the specified code.
     * 
     * @param code the response status code
     * @return the ResponseCode represented by the code specified
     */
    public ResultCode fromCode(int code)
    {
        if (code == 100)
            return Message;
        if (code == 101)
            return PlayerUpdate;
        if (code == 102)
            return ItemNotification;
        if (code == 103)
            return ItemTaken;
        if (code == 104)
            return MapData;
        if (code == 200)
            return Success;
        if (code == 400)
            return BadRequest;
        if (code == 500)
            return ServerError;
        return Unknown;
    }
}
