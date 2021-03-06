package server.event;

/**
 * An event generated by an entity in the application.
 * Events must have a subject.
 * Objects can extend Event to add more information.
 */
public class Event
{
    protected Subject subject = Subject.Unknown; // the event's subject
    
    /**
     * Default constructor.
     */
    public Event()
    {
    }
    
    /**
     * Constructor.
     * Sets the event subject.
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
