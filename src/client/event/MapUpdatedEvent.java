package client.event;

/**
 * Event raised when some element in the game map is updated.
 */
public class MapUpdatedEvent extends Event
{
    private String map = null; // string representation of the game map

    /**
     * Constructor.
     * Creates a new event with the subject MapUpdated.
     */
    public MapUpdatedEvent()
    {
        super();
        setSubject(Subject.MapUpdated);
    }

    /**
     * Constructor.
     * Creates a new event containing updated map.
     * 
     * @param map the string representation of the map
     */
    public MapUpdatedEvent(String map)
    {
        this();
        this.map = map;
    }

    @Override
    public String toString()
    {
        return "Event " + getSubject() + ":\r\n" + map;
    }

    // Setters and Getters
    public void setMap(String map) { this.map = map; }
    public String getMap() { return map; }
}
