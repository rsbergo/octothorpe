package client.event;

import client.connector.Request;

public class RequestEvent extends Event
{
    Request request = null; // the request received

    /**
     * Constructor.
     * Creates a new RequestEvent with the subject Request.
     */
    public RequestEvent()
    {
        super();
        setSubject(Subject.Request);
    }

    /**
     * Constructor.
     * Creates a new event based on the command received.
     * 
     * @param request a request received from the game client GUI
     */
    public RequestEvent(Request request)
    {
        this();
        this.request = request;
    }

    // Setters and Getters
    public void setRequest(Request request) { this.request = request; }
    public Request getRequest() { return request; }

    @Override
    public String toString()
    {
        return "Event " + getSubject() + ": \"" + request + "\"";
    }    
}
