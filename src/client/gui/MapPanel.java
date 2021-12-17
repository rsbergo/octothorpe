package client.gui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JCheckBox;
import javax.swing.GroupLayout.Alignment;
import javax.swing.text.DefaultCaret;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import client.event.Event;
import client.event.FogOfWarEvent;
import client.event.MapUpdatedEvent;
import client.event.MoveEvent;
import client.event.PlayerUpdatedEvent;
import client.event.Subject;
import client.game.MoveDirection;
import client.game.Player;
import client.game.Position;

public class MapPanel extends ContentPanel
{
    private JLabel playerInformationLabel = new JLabel();  // title label
    private JTextArea mapArea = new JTextArea();           // display the map
    private JScrollPane mapAreaScroll = new JScrollPane(); // scrolling pane for the map area
    private JCheckBox fogOfWarCheckBox = new JCheckBox();  // enables or disables fog of war

    private Player currentPlayer = null; // the player currently logged in
    
    /**
     * Constructor.
     * Initializes components and defines the layout.
     */
    public MapPanel(Player currentPlayer)
    {
        super("MapPanel");
        this.currentPlayer = currentPlayer;
        registerSubject(Subject.Move);
        registerSubject(Subject.FogOfWar);
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
        return List.of(Subject.Move, Subject.FogOfWar);
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
        initFogOfWarCheckBox();
    }
    
    // Creates the layout in the content planel.
    private void createLayout()
    {
        GroupLayout layoutManager = new GroupLayout(content);
        layoutManager.setAutoCreateContainerGaps(true);
        layoutManager.setAutoCreateGaps(true);

        layoutManager.setHorizontalGroup(layoutManager.createParallelGroup(Alignment.LEADING)
            .addGroup(layoutManager.createSequentialGroup()
                .addComponent(fogOfWarCheckBox)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(playerInformationLabel)
            )
            .addComponent(mapAreaScroll, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
        );

        layoutManager.setVerticalGroup(layoutManager.createSequentialGroup()
            .addGroup(layoutManager.createParallelGroup(Alignment.BASELINE)
                .addComponent(fogOfWarCheckBox)
                .addGap(0, 0, 0)
                .addComponent(playerInformationLabel)
            )
            .addComponent(mapAreaScroll, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
        );

        layoutManager.linkSize(SwingConstants.VERTICAL, fogOfWarCheckBox, playerInformationLabel);
        content.setLayout(layoutManager);
    }
    
    // Initialize the title label.
    private void initTitleLabel()
    {
        playerInformationLabel.setFont(DefaultFont.getBold());
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
        DefaultCaret caret = (DefaultCaret) mapArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);  
    }
    
    // Initializes the scroll pane attached to the map area
    private void initMapAreaScroll()
    {
        mapAreaScroll.setViewportView(mapArea);
        mapAreaScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mapAreaScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    // Initializes the fog of war checkbox.
    private void initFogOfWarCheckBox()
    {
        fogOfWarCheckBox.setText("Fog of War");
        fogOfWarCheckBox.setSelected(true);
        fogOfWarCheckBox.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                int selection = e.getStateChange();
                generateFogOfWarEvent(selection == ItemEvent.SELECTED);
            }
        });
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
            playerInformationLabel.setText(text);
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

    // Generates a fog of war event.
    private void generateFogOfWarEvent(boolean enabled)
    {
        FogOfWarEvent event = new FogOfWarEvent();
        event.setEnabled(enabled);
        notify(event);
    }
}
