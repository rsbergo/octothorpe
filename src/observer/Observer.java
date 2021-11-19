package observer;

/**
 * Defines a callback function to handle event notifications.
 */
public interface Observer<TEvent>
{
    /**
     * Process an event when notified.
     * 
     * @param event the event to be processed
     */
    public abstract void processEvent(TEvent event);
}
