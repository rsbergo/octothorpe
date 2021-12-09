package server.eventhandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import server.event.Subject;
import server.logger.LogLevel;
import server.logger.Logger;

/**
 * Manages a list of event handlers.
 */
public class EventHandlerManager
{
    private Map<Subject, EventHandler> handlers = new HashMap<Subject, EventHandler>(); // list of handlers
    
    /**
     * Installs a new event handler.
     * If there is already an event handler for the subject specified, this event handler will be replaced with the
     * new event handler specified.
     * 
     * @param subject the event subject handler will process
     * @param handler the new event handler
     */
    public void installEventHandler(Subject subject, EventHandler handler)
    {
        Logger.log(LogLevel.Info, "Installing event handler for " + subject);
        handlers.put(subject, handler);
    }
    
    /**
     * Retrieves the event handler installed for the subject specified.
     * 
     * @param subject the subject whose event handler is to be retrieved
     * @return The event handler installed for subject; null if no event handler has been installed for the subject
     *         specified.
     */
    public EventHandler getEventHandler(Subject subject)
    {
        return handlers.get(subject);
    }

    public List<EventHandler> getEventHandlerList()
    {
        List<EventHandler> list = new ArrayList<EventHandler>();
        for (Subject subject : handlers.keySet())
            list.add(handlers.get(subject));
        return list;
    }
    
    /**
     * Removes an event handler that has been installed for the subject specified.
     * Has no effect if no event handler has been installed for subject.
     * 
     * @param subject the subject whose event handler should be uninstalled
     */
    public void removeEventHandler(Subject subject)
    {
        Logger.log(LogLevel.Info, "Removing event handler for " + subject);
        handlers.remove(subject);
    }
}
