package client.event;

/**
 * Event generated upon a login attempt.
 */
public class LoginEvent extends Event
{
    private String name = null; // name used on a login attempt

    /**
     * Constructor.
     * Creates a new event with the subject Login.
     */
    public LoginEvent()
    {
        super();
        setSubject(Subject.Login);
    }

    /**
     * Constructor.
     * Creates a new event with the name used on a login attempt.
     * 
     * @param name the name used on a login attempt
     */
    public LoginEvent(String name)
    {
        this();
        this.name = name;
    }

    // Setters and Getters
    public void setName(String name) { this.name = name; }
    public String getName() { return name; }

    @Override
    public String toString()
    {
        return "Event " + getSubject() + ": \"" + name + "\"";
    }
}
