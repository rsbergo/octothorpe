package game;

public class Response
{
    private ResponseStatus status = ResponseStatus.Unknown; // this Response's status
    private String data = "";                               // this Response's data

    /**
     * Constructor.
     * Buids a response based on the status and data specified.
     * 
     * @param status The response status
     * @param data The response data
     */
    public Response(ResponseStatus status, String data)
    {
        this.status = status;
        this.data = data;
    }

    // Setters and Getters
    public ResponseStatus getResponseStatus() { return status; }
    public String getData() { return data; }

    @Override
    public String toString()
    {
        return status.getCode() + ":" + data;
    }
}
