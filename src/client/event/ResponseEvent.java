package client.event;

import client.connector.Request;
import client.connector.Response;

/**
 * Event generated when the game client receives a response from the game server.
 */
public class ResponseEvent extends Event
{
    Request request = null;   // the request that generated the response
    Response response = null; // the response received from the game server

    /**
     * Constructor.
     * Creates a new ResponseEvent with the subject Response.
     */
    public ResponseEvent()
    {
        super();
        setSubject(Subject.Response);
    }

    /**
     * Constructor.
     * Creates a new event with the response received.
     * 
     * @param response a response received from the game server
     */
    public ResponseEvent(Response response)
    {
        this(null, response);
    }

    /**
     * Constructor.
     * Creates a new event response received and the request that generated the response.
     * 
     * @param request  the original request
     * @param response a response received from the game server
     */
    public ResponseEvent(Request request, Response response)
    {
        this();
        this.request = request;
        this.response = response;
    }

    // Setters and Getters
    public void setRequest(Request request) { this.request = request; }
    public Request getRequest() { return request; }
    public void setResponse(Response response) { this.response = response; }
    public Response getResponse() { return response; }

    @Override
    public String toString()
    {
        return "Event " + getSubject() + ": \"" + response + "\"";
    }    
}
