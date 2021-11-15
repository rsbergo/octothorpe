package game;

public enum ResponseStatus
{
    Unknown(0),
    Success(200),
    BadRequest(400);

    private int code; // response code

    // Constructor
    private ResponseStatus(int code)
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
    public ResponseStatus fromCode(int code)
    {
        if (code == 200)
            return Success;
        else if (code == 400)
            return BadRequest;
        else
            return Unknown;
    }
}
