package client.event;

/**
 * Event generated when exiting the game.
 */
public class QuitEvent extends Event
{
    /**
     * Constructor.
     * Creates a new event with the subject Quit.
     */
    public QuitEvent()
    {
        super();
        setSubject(Subject.Quit);
    }

    @Override
    public String toString()
    {
        return "Event " + getSubject();
    }
}
