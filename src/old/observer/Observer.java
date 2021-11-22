package old.observer;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Defines a callback function to handle event notifications.
 */
public abstract class Observer
{
    protected Queue<Event> eventQueue = new LinkedList<Event>(); // queue of events to be processed

    /**
     * Adds an event to this Observer's event queue.
     * 
     * @param event the event to be queued
     */
    public void queueEvent(Event event)
    {
        eventQueue.add(event);
    }

    /**
     * Processes all events queued for this Oberver.
     */
    public void processEvents()
    {
        while (!eventQueue.isEmpty())
            processEvent(eventQueue.poll());
    }

    /**
     * Removes all events from the event queue.
     * These events will be lost.
     */
    public void clearEventQueue()
    {
        eventQueue.clear();
    }

    /**
     * Process an event when notified.
     * 
     * @param event the event to be processed
     */
    protected abstract void processEvent(Event event);
}
