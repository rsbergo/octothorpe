package client.event;

/**
 * Event triggered when a response containing a message is received from the game server.
 */
public class MessageReceivedEvent extends Event
{
    private String message = null; // the message being sent

    /**
     * Constructor.
     * Creates a new event with the subject MessageReceived.
     */
    public MessageReceivedEvent()
    {
        super();
        setSubject(Subject.MessageReceived);
    }

    /**
     * Constructor.
     * Creates a new event based on message specified.
     * 
     * @param message the message being sent
     */
    public MessageReceivedEvent(String message)
    {
        this();
        this.message = message;
    }

    @Override
    public String toString()
    {
        return "Event " + getSubject() + ": " + message;
    }

    // Setters and Getters
    public void setMessage(String message) { this.message = message; }
    public String getMessage() { return message; }
}
