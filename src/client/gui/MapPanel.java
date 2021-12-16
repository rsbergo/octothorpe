package client.gui;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import client.event.Event;
import client.event.MapUpdatedEvent;
import client.event.MoveDirection;
import client.event.MoveEvent;
import client.event.Subject;

public class MapPanel extends ContentPanel
{
    private JTextArea mapArea = new JTextArea();           // display the map
    private JScrollPane mapAreaScroll = new JScrollPane(); // scrolling pane for the map area
    
    public MapPanel()
    {
        super("MapPanel");
        registerSubject(Subject.Move); // TODO: add overload that receives a list of events, call from geteventsproduced.
        initComponents();
        createLayout();
    }

    @Override
    public void processEvent(Event event)
    {
        if (event.getSubject() == Subject.MapUpdated)
        {
            MapUpdatedEvent mapUpdatedEvent = (MapUpdatedEvent) event;
            updateMap(mapUpdatedEvent.getMap());
        }
    }
    
    @Override
    public List<Subject> getEventsProduced()
    {
        return List.of(Subject.Move);
    }

    @Override
    public List<Subject> getSubjectsConsumed()
    {
        return List.of(Subject.MapUpdated);
    }
    
    // Initialize components of the game panel
    private void initComponents()
    {
        initMapArea();
        initMapAreaScroll();
    }
    
    // Creates the layout in the content planel.
    private void createLayout()
    {
        content.setLayout(new BorderLayout());
        content.add(mapAreaScroll);
    }
    
    // Initializes the text area where the map is displayed
    private void initMapArea()
    {
        mapArea.setFont(DefaultFont.getPlain());
        mapArea.setEditable(false);
        mapArea.addKeyListener(new KeyAdapter()
        {
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_W)
                    generateMoveEvent(MoveDirection.North);;
                if (e.getKeyCode() == KeyEvent.VK_A)
                    generateMoveEvent(MoveDirection.West);;
                if (e.getKeyCode() == KeyEvent.VK_S)
                    generateMoveEvent(MoveDirection.South);;
                if (e.getKeyCode() == KeyEvent.VK_D)
                    generateMoveEvent(MoveDirection.East);;
            }
        });
    }
    
    // Initializes the scroll pane attached to the map area
    private void initMapAreaScroll()
    {
        mapAreaScroll.setViewportView(mapArea);
        mapAreaScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mapAreaScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    // Updates the map in the map area
    private void updateMap(String map)
    {
        if (map != null)
            mapArea.setText(map);
    }

    // Generates a move event at the direction specified.
    private void generateMoveEvent(MoveDirection direction)
    {
        MoveEvent event = new MoveEvent();
        event.setDirection(direction);
        notify(event);
    }
}
