package client.event;

/**
 * Event raised when Fog of War is enabled or disabled.
 */
public class FogOfWarEvent extends Event
{
    private boolean enabled = false; // the state of Fog of War

    /**
     * Constructor.
     * Creates a new event with the subject FogOfWar.
     */
    public FogOfWarEvent()
    {
        super();
        setSubject(Subject.FogOfWar);
    }

    /**
     * Constructor.
     * Creates a new event based on the flag specified.
     * 
     * @param enabled true if Fog of War is enabled
     */
    public FogOfWarEvent(boolean enabled)
    {
        this();
        this.enabled = enabled;
    }

    @Override
    public String toString()
    {
        return "Event " + getSubject() + ": " + enabled;
    }

    // Setters and Getters
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public boolean getEnabled() { return enabled; }
}
