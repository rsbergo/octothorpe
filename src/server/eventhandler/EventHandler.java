package server.eventhandler;

import java.util.List;

import server.event.Event;
import server.eventmanager.EventListener;
import server.gameserver.Response;

/**
 * Defines a method to process an event and return a response built based on the event.
 */
public interface EventHandler extends EventListener
{
    /**
     * Processes the event received.
     * 
     * @param event the event received
     * @return A Response containing the result of the event processing
     */
    public abstract List<Response> eventToResponse(Event event);
}
