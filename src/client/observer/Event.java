package client.observer;

/**
 * Represents an event trigerred by a response received from the game server.
 * Events must have a subject.
 * Classes can extend Event to add more information.
 */
public class Event
{
    private String subject; // the event subject

    /**
     * Default constructor.
     */
    public Event()
    {}

    /**
     * Constructor.
     * Creates a new Event with the subject specified.
     * 
     * @param subject the event subject
     */
    public Event(String subject)
    {
        this.subject = subject;
    }

    // Setters and Getters
    public void setSubject(String subject) { this.subject = subject; }
    public String getSubject() { return subject; }
}
