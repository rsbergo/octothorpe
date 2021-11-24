package event;

/**
 * Event generated to send a message.
 */
public class SendMessageEvent extends Event
{
    private String message; // the message to be sent
    
    /**
     * Default constructor.
     * Sets the event subject to Subject.SendMessage.
     */
    public SendMessageEvent()
    {
        super(Subject.SendMessage);
    }
    
    /**
     * Default constructor.
     * Sets the event subject to Subject.SendMessage.
     * Sets the message to be sent.
     * 
     * @param message the message to be sent
     */
    public SendMessageEvent(String message)
    {
        this();
        this.message = message;   
    }
    
    // Setters and Getters
    public void setMessage(String message) { this.message = message; }
    public String getMessage() { return message; }

    @Override
    public String toString()
    {
        return "Event " + subject + ": \"" + message + "\"";
    }
}
