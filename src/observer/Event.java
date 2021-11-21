package observer;

/**
 * Events are used for asynchronous communications.
 * The base class defines the event subject that is used for subscribing and reacting to an event.
 */
public class Event
{
    protected String subject = ""; // this event's subject

    /**
     * Constructor.
     * Sets this event's subject.
     * 
     * @param subject the event subject
     */
    public Event(String subject)
    {
        this.subject = subject;
    }

    //Setters and Getters
    public String getSubject() { return subject; }
}
