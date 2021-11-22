package old.observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generates events and notifies its observers.
 * Supports multiple events, and multiple observers for each event.
 */
public abstract class Observable
{
    private Map<String, List<Observer>> registrar = new HashMap<String, List<Observer>>(); // list of events
    
    /**
     * Registers a new subject that can be observed.
     * 
     * @param subject the event subject to be registered
     */
    public void register(String subject)
    {
        if (registrar.get(subject) == null)
            registrar.put(subject, new ArrayList<Observer>());
    }
    
    /**
     * Unregisters a subject.
     * This Observable will no long generate events with this subject.
     * 
     * @param subject the subject to be unregistered
     */
    public void unregister(String subject)
    {
        registrar.remove(subject);
    }
    
    /**
     * Subscribes the Observer specified to events generated by this Observable.
     * 
     * @param subject  the subject the new observer is listening to
     * @param observer the observer that will listen to this Observable's events
     */
    public void subscribe(String subject, Observer observer)
    {
        if (registrar.containsKey(subject))
            registrar.get(subject).add(observer);
    }
    
    /**
     * Unsubscribes the Observer specified from all events generated by this Observable.
     * 
     * @param observer the observer that will stop listening to this Observable's events
     */
    public void unsubscribe(Observer observer)
    {
        for (String subject : registrar.keySet())
            registrar.get(subject).remove(observer);
    }
    
    /**
     * Unsubscribes the Observer specified from events of the subject specfied.
     * 
     * @param subject  the subject the observer is unsubscribing from
     * @param observer the observer unsubscribing
     */
    public void unsubscribe(String subject, Observer observer)
    {
        if (registrar.containsKey(subject))
            registrar.get(subject).remove(observer);
    }
    
    /**
     * Notifies the event specified to all observers listening to this Observable's events.
     * 
     * @param event the event to be notified
     */
    public void notifyAll(Event event)
    {
        if (registrar.containsKey(event.getSubject()))
        {
            for (Observer observer : registrar.get(event.getSubject()))
                observer.queueEvent(event);
        }
    }
    
    /**
     * Notifies the event specified to the observer specified.
     * 
     * @param observer the observer to be notified
     * @param event    the event to be notified
     */
    public void notify(Observer observer, Event event)
    {
        if (registrar.containsKey(event.getSubject()) && registrar.get(event.getSubject()).contains(observer))
            observer.processEvent(event);
    }
}