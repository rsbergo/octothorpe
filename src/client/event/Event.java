package client.event;

/**
 * Represents an event trigerred by a response received from the game server.
 * Events must have a subject.
 * Classes can extend Event to add more information.
 */
public class Event
{
    private Subject subject; // the event subject

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
    public Event(Subject subject)
    {
        this.subject = subject;
    }

    // Setters and Getters
    public void setSubject(Subject subject) { this.subject = subject; }
    public Subject getSubject() { return subject; }
}
