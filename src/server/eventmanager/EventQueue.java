package server.eventmanager;

import java.util.LinkedList;
import java.util.Queue;

import server.event.Event;
import server.logger.LogLevel;
import server.logger.Logger;

/**
 * Provides a mechanism to process events asynchronously.
 * Events can be queued to be processed. An internal thread polls the event queue and processes the events queued,
 * consuming them. Events are processed in the order they are queued.
 * The event processor thread is started when a new instance is created.
 */
public abstract class EventQueue implements EventListener
{
    private Queue<Event> eventQueue = new LinkedList<Event>(); // queue of events to be processed
    private Thread eventProcessor = null;                        // internal thread that processes events
    private boolean running = false;                             // synchronization flag to start/stop internal thread
    
    /**
     * Constructor.
     * Initializes the internal event processor thread.
     */
    public EventQueue()
    {
        startProcessing();
    }
    
    /**
     * Adds the event specified to the event queue.
     * 
     * @param event the event to be queued
     */
    public void processEvent(Event event)
    {
        eventQueue.add(event);
        if (!isRunning())
            startProcessing();
    }
    
    /**
     * Starts processing events from the event queue.
     */
    public void startProcessing()
    {
        if (!isRunning())
        {
            Logger.log(LogLevel.Debug, "Starting event processor thread");
            setRunning(true);
            eventProcessor = new Thread(() ->
            {
                while (isRunning() && !eventQueue.isEmpty())
                    processQueuedEvent(eventQueue.poll());
                stopProcessing();
            });
            eventProcessor.start();
            Logger.log(LogLevel.Debug, "Event processor thread started");
        }
    }
    
    /**
     * Stops processing events from the event queue.
     */
    public void stopProcessing()
    {
        if (isRunning())
        {
            try
            {
                Logger.log(LogLevel.Debug, "Stopping event processor thread");
                setRunning(false);
                eventProcessor.join();
                Logger.log(LogLevel.Error, "Event processor thread stopped");
            }
            catch (InterruptedException e)
            {
                Logger.log(LogLevel.Error, "Error joining event processor thread", e);
            }
        }
    }
    
    /**
     * Processes an event that has been added to the event queue.
     * 
     * @param event the event to be processed
     */
    protected abstract void processQueuedEvent(Event event);
    
    // Updates the event queue state.
    private synchronized void setRunning(boolean running)
    {
        this.running = running;
    }
    
    // Retrieves the event queue state.
    private synchronized boolean isRunning()
    {
        return running;
    }
}
