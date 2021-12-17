package client.event;

/**
 * Event triggered when a player sends a message through the game.
 */
public class SendMessageEvent extends Event
{
    private String message = null; // the message being sent

    /**
     * Constructor.
     * Creates a new event with the subject SendMessage.
     */
    public SendMessageEvent()
    {
        super();
        setSubject(Subject.SendMessage);
    }

    /**
     * Constructor.
     * Creates a new event based on message specified.
     * 
     * @param message the message being sent
     */
    public SendMessageEvent(String message)
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
