package client.observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import client.event.Event;
import client.event.Subject;
import logger.Logger;
import logger.LogLevel;

/**
 * Observables generate events of subjects that have been registered. Multiple subjects can be registered.
 * Observers can subscribe to events generated by Observables. Multiple Observers can subscribe to events generated.
 * An Observer can subscribe to multiple event subjects.
 * When an event is generated, the Observable notifies its subscribers.
 */
public class Observable
{
    protected String name = null; // the observable name
    private Map<Subject, List<Observer>> registrar = new HashMap<Subject, List<Observer>>(); // listeners
    
    /**
     * Default constructor.
     */
    public Observable()
    {}

    /**
     * Constructor.
     * Sets a name for this subscriber for identification purposes only.
     * 
     * @param name the identification name
     */
    public Observable(String name)
    {
        this.name = name;
    }

    /**
     * Adds the subject specified to the list of event subjects generated by this Observable.
     * The Observable can start accepting subscriptions for the new subject.
     * 
     * @param subject the subject to be added to this Observale
     */
    public void registerSubject(Subject subject)
    {
        Logger.log(LogLevel.Debug, name + " - Registering event: " + subject);
        registrar.putIfAbsent(subject, new ArrayList<Observer>());
    }
    
    /**
     * Removes the subject specified from the list of event subjects generated by this Observable.
     * The Observable stops accepting subscriptions for the subject removed and stops notifying Observers with the
     * subject removed.
     * All subscribers are removed for the event.
     * 
     * @param subject the subject to be unregistered
     */
    public void unregisterSubject(Subject subject)
    {
        Logger.log(LogLevel.Debug, name + " - Unregistering event: " + subject);
        registrar.remove(subject);
    }
    
    /**
     * Subscribes observer to all events generated by the Observable.
     * 
     * @param observer the observer to be subscribed to events generated
     */
    public synchronized void subscribe(Observer observer)
    {
        for (Subject subject : registrar.keySet())
            subscribe(observer, subject);
    }

    /**
     * Subscribes observer to events generated by the Observable.
     * Observer will only be notified of events with the subject specified.
     * 
     * @param observer the observer that will listen to this Observable's events
     * @param subjects the list of subjects the new observer is listening to
     */
    public synchronized void subscribe(Observer observer, Subject... subjects)
    {
        for (Subject subject : subjects)
        {
            if (registrar.containsKey(subject))
            {
                Logger.log(LogLevel.Debug, name + " - New subscription to " + subject + " events: " + observer);
                registrar.get(subject).add(observer);
            }
        }
    }

    /**
     * Subscribes observer to events generated by the Observable.
     * Observer will only be notified of events with the subject specified.
     * 
     * @param observer the observer that will listen to this Observable's events
     * @param subjects the list of subjects the new observer is listening to
     */
    public synchronized void subscribe(Observer observer, List<Subject> subjects)
    {
        for (Subject subject : subjects)
            subscribe(observer, subject);
    }
    
    /**
     * Unsubscribes observer from all events generated by the Observable.
     * 
     * @param observer the observer to be unsubscribed from events generated
     */
    public void unsubscribe(Observer observer)
    {
        for (Subject subject : registrar.keySet())
            unsubscribe(observer, subject);
    }
    
    /**
     * Unsubscribes observer from events of the subject specfied.
     * 
     * @param subject  the subject the observer is unsubscribing from
     * @param listener the observer unsubscribing
     */
    public synchronized void unsubscribe(Observer observer, Subject... subjects)
    {
        for (Subject subject : subjects)
        {
            if (registrar.containsKey(subject))
            {
                Logger.log(LogLevel.Debug, name + " - " + observer + " unsubscribed from " + subject + " events");
                registrar.get(subject).remove(observer);
            }
        }
    }

    /**
     * Notifies the event specified to all observers listening to this Observable's events.
     * 
     * @param event the event to be notified
     */
    public synchronized void notify(Event event)
    {
        if (registrar.containsKey(event.getSubject()))
        {
            for (Observer observer : registrar.get(event.getSubject()))
                notify(observer, event);
        }
    }
    
    /**
     * Notifies the event specified to the observer specified.
     * Only notifies observer if it has subscribed to the event's subject
     * 
     * @param observer the observer to be notified
     * @param event    the event to be notified
     */
    public synchronized void notify(Observer observer, Event event)
    {
        if (registrar.containsKey(event.getSubject()))
        {
            Logger.log(LogLevel.Debug, name + " - Notifying " + observer + ": \"" + event + "\"");
            observer.processEvent(event);
        }
    }

    @Override
    public String toString()
    {
        return name;
    }
}
