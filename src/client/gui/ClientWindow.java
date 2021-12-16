package client.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;

import client.connector.Request;
import client.event.Event;
import client.event.ItemDataEvent;
import client.event.MapDataEvent;
import client.event.PlayerUpdatedEvent;
import client.event.RequestEvent;
import client.event.Subject;
import client.event.SynchronousResponseEvent;
import client.game.Item;
import client.game.Player;
import client.game.Position;
import client.gameclient.NotificationManager;
import client.observer.Observer;

/**
 * The top-level container for the GUI.
 */
public class ClientWindow extends JFrame implements Observer
{
    private NotificationManager notifier = null; // event generator
    private List<Player> players = new ArrayList<Player>();
    private List<Item> items = new ArrayList<Item>();

    ResponseLabel responsLabel = null;
    JTextField commandField = null;
    JButton sendButton = null;
    JTextArea mapArea = null;
    JScrollPane scroll = null;
    char[][] map = null;

    public ClientWindow(NotificationManager notifier)
    {
        // TODO: receive the game client instead of notifier.
        // TODO: add a "sendCommand()" to game client and call it when sending a command. 
        this.notifier = notifier;
        notifier.subscribe(this);
        initUI();
    }

    private void initUI()
    {
        responsLabel = new ResponseLabel();

        commandField = new JTextField(100);
        commandField.addActionListener((event) -> sendCommand());
        sendButton = new JButton("Send");
        sendButton.addActionListener((event) -> sendCommand());
        mapArea = new JTextArea();
        mapArea.setAlignmentX(CENTER_ALIGNMENT);
        mapArea.setAlignmentY(CENTER_ALIGNMENT);
        mapArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        mapArea.setEditable(false);
        mapArea.addKeyListener(new KeyAdapter()
        {
            public void keyReleased( KeyEvent e )
            {
                if( e.getKeyCode() == KeyEvent.VK_W )
                    notifier.notify(new RequestEvent(new Request("move north")));
                if( e.getKeyCode() == KeyEvent.VK_A )
                    notifier.notify(new RequestEvent(new Request("move west")));
                if( e.getKeyCode() == KeyEvent.VK_S )
                    notifier.notify(new RequestEvent(new Request("move south")));
                if( e.getKeyCode() == KeyEvent.VK_D )
                    notifier.notify(new RequestEvent(new Request("move east")));
            }
        });
        scroll = new JScrollPane(mapArea);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        createLayout();
        initMainWindow();
    }

    // Initializes the main window for the application
    private void initMainWindow()
    {
        setTitle("Octothorpe # - The Game");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e)
            {
                notifier.notify(new RequestEvent(new Request("quit")));
                // TODO: handle exception. Quit without being logged in
            }
        });
    }

    private void createLayout()
    {
        var pane = getContentPane();
        var layoutManager = new GroupLayout(pane);
        pane.setLayout(layoutManager);
        layoutManager.setAutoCreateContainerGaps(true);
        layoutManager.setAutoCreateGaps(true);

        layoutManager.setHorizontalGroup(
            layoutManager.createParallelGroup()
                .addComponent(scroll)
                .addGroup
                (
                    layoutManager.createSequentialGroup()
                    .addComponent(commandField) 
                    .addComponent(sendButton)
                )
                .addComponent(responsLabel.getLabel())
        );

        layoutManager.setVerticalGroup(
            layoutManager.createSequentialGroup()
                .addComponent(scroll)
                .addGroup
                (
                    layoutManager.createParallelGroup(Alignment.BASELINE)
                    .addComponent(commandField, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(sendButton)
                )
                .addComponent(responsLabel.getLabel())
                .addGap(150)
        );
        pack();
    }

    @Override
    public void processEvent(Event event)
    {
        if (event.getSubject() == Subject.SynchronousResponse)
            responsLabel.setText(((SynchronousResponseEvent) event).getResponse());
        else if (event.getSubject() == Subject.MapData)
        {
            map = ((MapDataEvent) event).getMap().getMapArray();
            System.out.println(drawMap());
            mapArea.setText(drawMap());
            // TODO: add players
        }
        else if (event.getSubject() == Subject.PlayerUpdated)
        {
            Player player = ((PlayerUpdatedEvent) event).getPlayer();
            if (map == null)
            {
                players.remove(player);   
                players.add(player);
            }      
            else
            {
                for (Player p : players)
                    map[p.getPosition().getY()][p.getPosition().getX()] = p.getName().charAt(0);
                    // TODO: if I need to go through all of them, maybe just use the map.getMapArray()...
                Position oldPos = player.getPosition();
                if (players.contains(player))
                    oldPos = players.get(players.indexOf(player)).getPosition();
                map[oldPos.getY()][oldPos.getX()] = ' ';
                map[player.getPosition().getY()][player.getPosition().getX()] = player.getName().charAt(0);
                players.remove(player);
                players.add(player);
                mapArea.setText(drawMap());
            }
            // TODO: handle disconnections
        }
        else if (event.getSubject() == Subject.ItemData)
        {
            if (map == null)
                items.add(((ItemDataEvent) event).getItem());
            else
            {
                for (Item i : items)
                    map[i.getPosition().getY()][i.getPosition().getX()] = '⋆';
            }
        }
    }

    private void sendCommand()
    {
        String command = commandField.getText();
        Request request = new Request(command);
        notifier.notify(new RequestEvent(request));
        commandField.setText("");
    }

    private String drawMap()
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < map.length; i++)
        {
            for (int j = 0; j < map[i].length; j++)
                sb.append(map[i][j]);
            sb.append("\r\n");
        }
        sb.delete(sb.length() - 1, sb.length()); // remove extra new line
        return sb.toString();
    }
}
