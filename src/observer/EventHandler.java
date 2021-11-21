package observer;

import game.Response;

/**
 * Defines a method to process an event and return a response built based on the event.
 */
public interface EventHandler
{
    /**
     * Processes the event received.
     * 
     * @param event the event received
     * @return A Response containing the result of the event processing
     */
    public abstract Response processEvent(Event event);
}
