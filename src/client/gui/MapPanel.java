package client.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import client.event.Event;
import client.event.MapUpdatedEvent;
import client.event.MoveDirection;
import client.event.MoveEvent;
import client.event.PlayerUpdatedEvent;
import client.event.Subject;
import client.game.Player;
import client.game.Position;

public class MapPanel extends ContentPanel
{
    private JLabel titleLabel = new JLabel();              // title label
    private JTextArea mapArea = new JTextArea();           // display the map
    private JScrollPane mapAreaScroll = new JScrollPane(); // scrolling pane for the map area

    private Player currentPlayer = null; // the player currently logged in
    
    /**
     * Constructor.
     * Initializes components and defines the layout.
     */
    public MapPanel(Player currentPlayer)
    {
        super("MapPanel");
        this.currentPlayer = currentPlayer;
        registerSubject(Subject.Move); // TODO: add overload that receives a list of events, call from geteventsproduced.
        initComponents();
        createLayout();
    }

    @Override
    public synchronized void processEvent(Event event)
    {
        if (event.getSubject() == Subject.MapUpdated)
        {
            MapUpdatedEvent mapUpdatedEvent = (MapUpdatedEvent) event;
            updateMap(mapUpdatedEvent.getMap());
        }
        if (event.getSubject() == Subject.PlayerUpdated)
        {
            PlayerUpdatedEvent playerUpdatedEvent = (PlayerUpdatedEvent) event;
            updateCurrentPlayerInformation(playerUpdatedEvent.getPlayer());
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
        return List.of(Subject.MapUpdated, Subject.PlayerUpdated);
    }
    
    // Initialize components of the map panel
    private void initComponents()
    {
        initTitleLabel();
        initMapArea();
        initMapAreaScroll();
    }
    
    // Creates the layout in the content planel.
    private void createLayout()
    {
        GroupLayout layoutManager = new GroupLayout(content);
        layoutManager.setAutoCreateContainerGaps(true);
        layoutManager.setAutoCreateGaps(true);

        layoutManager.setHorizontalGroup(layoutManager.createParallelGroup(Alignment.TRAILING)
            .addComponent(titleLabel)
            .addComponent(mapAreaScroll, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
        );

        layoutManager.setVerticalGroup(layoutManager.createSequentialGroup()
            .addComponent(titleLabel)
            .addComponent(mapAreaScroll, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
        );

        content.setLayout(layoutManager);
    }
    
    // Initialize the title label.
    private void initTitleLabel()
    {
        titleLabel.setFont(DefaultFont.getBold());
        updateTitleLabelText();
    }
    
    // Initializes the text area where the map is displayed
    private void initMapArea()
    {
        mapArea.setFont(DefaultFont.getBold());
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

    // Updates the title label text
    private void updateTitleLabelText()
    {
        if (currentPlayer != null)
        {
            String name = currentPlayer.getName();
            Position pos = currentPlayer.getPosition();
            String position = "(" + pos.getX() + ", " + pos.getY() + ")";
            String score = currentPlayer.getScore() + " points";
            String text = name + " " + position + " - " + score;
            titleLabel.setText(text);
        }
    }

    // Updates current player information displayed on top of the map
    private void updateCurrentPlayerInformation(Player player)
    {
        if (player != null && player.equals(currentPlayer))
            currentPlayer = player;
        updateTitleLabelText();
    }
    
    // Generates a move event at the direction specified.
    private void generateMoveEvent(MoveDirection direction)
    {
        MoveEvent event = new MoveEvent();
        event.setDirection(direction);
        notify(event);
    }
}
