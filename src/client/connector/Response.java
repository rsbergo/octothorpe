package client.connector;

/**
 * Represents a response received from the game server.
 * A response contains a response code that indicates the type of response, and a message.
 */
public class Response
{
    private ResponseCode code = ResponseCode.Unknown; // the response code
    private String message = "";                      // the response's message

    /**
     * Default constructor
     */
    public Response()
    {}

    /**
     * Constructor.
     * Creates a new Response containing the code and message specified.
     * 
     * @param code    the response's code
     * @param message the response's message
     */
    public Response(ResponseCode code, String message)
    {
        this.code = code;
        this.message = message;
    }

    /**
     * Constructor.
     * Creates a new Response based on the string specified.
     * Assumes that the string specified is formatted as <code>:<message>
     * 
     * @param response the response string
     */
    public Response(String response)
    {
        String[] tokens = response.split(":");
        code = ResponseCode.fromString(tokens[0]);
        message = tokens[1];
    }

    // Setters and Getters
    public void setResponseCode(ResponseCode code) { this.code = code; }
    public ResponseCode getResponseCode() { return code; }
    public void setMessage(String message) { this.message = message; }
    public String getMessage() { return message; }

    @Override
    public String toString()
    {
        return code.getCode() + ":" + message;
    }
}
