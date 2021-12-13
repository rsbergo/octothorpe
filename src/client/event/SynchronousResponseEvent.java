package client.event;

import client.connector.Response;

/**
 * Event raised when a synchronous response is received.
 * Synchronous responses have response code greater than or equal to 200.
 */
public class SynchronousResponseEvent extends Event
{
    Response response = null; // the response received

    /**
     * Constructor.
     * Creates a new PlayerUpdatedEvent with the subject PlayerUpdated.
     */
    public SynchronousResponseEvent()
    {
        super();
        setSubject(Subject.SynchronousResponse);
    }

    /**
     * Constructor.
     * Creates a new event based on the response received.
     * 
     * @param response a response received from the game server
     */
    public SynchronousResponseEvent(Response response)
    {
        this();
        this.response = response;
    }

    // Setters and Getters
    public void setResponse(Response response) { this.response = response; }
    public Response getResponse() { return response; }

    @Override
    public String toString()
    {
        return "Event " + getSubject() + ": \"" + response + "\"";
    }
}
