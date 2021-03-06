package server.eventmanager;

import server.event.Event;

/**
 * Provides a mechanism to process events generated by an object.
 */
public interface EventListener
{
    /**
     * Processes the event specified, consuming it.
     * 
     * @param event the event to be processed
     */
    public abstract void processEvent(Event event);
}
