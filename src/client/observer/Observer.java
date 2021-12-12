package client.observer;

/**
 * An observer listens to events and processes them.
 */
public interface Observer
{
    /**
     * Processes an event received.
     * 
     * @param event the event to be processed
     */
    public abstract void processEvent(Event event);
}
