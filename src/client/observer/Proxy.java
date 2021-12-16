package client.observer;

import client.event.Event;
import client.event.Subject;

/**
 * The proxy is an observer and an observable.
 * When it receives an event, it simply forwards this event to its observers.
 * Events are forwarded as received.
 */
public class Proxy extends Observable implements Observer
{
    /**
     * Default constructor.
     */
    public Proxy()
    {}

    /**
     * Constructor.
     * Sets a name for the proxy.
     * 
     * @param name the proxy name
     */
    public Proxy(String name)
    {
        super(name);
    }

    /**
     * Constructor.
     * Register the list of subjects specified so it can generate events with these subjects.
     */
    public Proxy(Subject... subjects)
    {
        for (Subject subject : subjects)
            registerSubject(subject);;
    }

    @Override
    public synchronized void processEvent(Event event)
    {
        notify(event);
    }
    
}
