package client.event;

import client.game.Map;

/**
 * Event raised when a message containing map information is received.
 * Multiple messages are expected when map data is received.
 * The first message is expected to contain the map size in the form "104:<cols>, <rows>"
 * Following messages are expected to contain <cols> characters with the map layout, one message for each <row>.
 * The MapDataEvent accumulates map information in a 2-D array of characters.
 */
public class MapDataEvent extends Event
{
    Map map = null; // map received from the game server

    /**
     * Constructor.
     * Creates a new MapDataEvent with the subject MapData.
     */
    public MapDataEvent()
    {
        super();
        setSubject(Subject.MapData);
    }

    /**
     * Constructor.
     * Creates a new event holding the map information provided.
     * 
     * @param map the map received from the game server
     */
    public MapDataEvent(Map map)
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
    public Map getMap() { return map; }
}
