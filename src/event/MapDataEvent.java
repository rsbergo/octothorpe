package event;

import game.GameMap;

/**
 * Event generated to provide game map information.
 */
public class MapDataEvent extends Event
{
    private GameMap map; // the game map
    
    /**
     * Default constructor.
     * Sets the event subject to Subject.MapData.
     */
    public MapDataEvent()
    {
        super(Subject.MapData);
    }
    
    /**
     * Default constructor.
     * Sets the event subject to Subject.MapData.
     * Sets the map.
     * 
     * @param map the game map
     */
    public MapDataEvent(GameMap map)
    {
        this();
        this.map = map;   
    }
    
    // Setters and Getters
    public void setMap(GameMap map) { this.map = map; }
    public GameMap getMap() { return map; }

    @Override
    public String toString()
    {
        return "Event " + subject + ": " + map;
    }
}
