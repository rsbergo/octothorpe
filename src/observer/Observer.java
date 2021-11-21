package observer;

/**
 * Defines a callback function to handle event notifications.
 */
public interface Observer
{
    /**
     * Process an event when notified.
     * 
     * @param event the event to be processed
     */
    public abstract void processEvent(Event event);
}
