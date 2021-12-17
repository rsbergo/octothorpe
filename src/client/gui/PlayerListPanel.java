package client.gui;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import client.event.Event;
import client.event.PlayerListUpdateEvent;
import client.event.Subject;
import client.game.Player;

/**
 * Displays the list of players currently logged in.
 * Consumes PlayerUpdated events
 */
public class PlayerListPanel extends ContentPanel
{
    private JLabel titleLabel = new JLabel();                                     // list title
    private JList<PlayerInformation> playerList = new JList<PlayerInformation>(); // list of players connected
    private JScrollPane playerListScroll = new JScrollPane();                     // scrolling pane for the list of players
    
    private DefaultListModel<PlayerInformation> playerListModel = new DefaultListModel<PlayerInformation>(); // the players data
    
    /**
     * Constructor.
     * Initializes the components and creates the layout of the player list panel.
     */
    public PlayerListPanel()
    {
        super("PlayerListPanel");
        initComponents();
        createLayout();
    }

    @Override
    public synchronized void processEvent(Event event)
    {
        if (event.getSubject() == Subject.PlayerListUpdated)
        {
            PlayerListUpdateEvent playerListUpdateEvent = (PlayerListUpdateEvent) event;
            updatePlayerList(playerListUpdateEvent.getPlayers());
        }
    }
    
    @Override
    public List<Subject> getSubjectsConsumed()
    {
        return List.of(Subject.PlayerListUpdated);
    }

    // Creates the layout in the content planel.
    private void createLayout()
    {
        GroupLayout layoutManager = new GroupLayout(content);
        layoutManager.setAutoCreateContainerGaps(true);
        layoutManager.setAutoCreateGaps(true);

        layoutManager.setHorizontalGroup(layoutManager.createParallelGroup(Alignment.LEADING)
            .addComponent(titleLabel)
            .addComponent(playerListScroll, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
        );

        layoutManager.setVerticalGroup(layoutManager.createSequentialGroup()
            .addComponent(titleLabel)
            .addComponent(playerListScroll, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
        );

        content.setLayout(layoutManager);
    }

    // Initializes the cmponents of the player list panel
    private void initComponents()
    {
        initTitleLabel();
        initPlayerList();
        initPlayerListScroll();
    }

    // Initialize the title label.
    private void initTitleLabel()
    {
        titleLabel.setFont(DefaultFont.getBold());
        titleLabel.setText("Players connected");
    }

    // Initializes the list of players
    private void initPlayerList()
    {
        playerList.setFont(DefaultFont.getPlain(12));
        playerList.setModel(playerListModel);
    }

    // Initializes the scroll pane for the list of players
    private void initPlayerListScroll()
    {
        playerListScroll.setViewportView(playerList);
        playerListScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        playerListScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        playerListScroll.setPreferredSize(new Dimension(200, 100));
    }

    // Updates a player information on the list of players
    private void updatePlayerList(List<Player> players)
    {
        List<PlayerInformation> list = new ArrayList<PlayerInformation>();
        for (Player player : players)
            list.add(new PlayerInformation(player));
        Collections.sort(list, Collections.reverseOrder());
        playerListModel.clear();
        playerListModel.addAll(list);
    }

    /**
     * Model used for the list of players.
     * Players are ordered based on their score.
     */
    private class PlayerInformation implements Comparable<PlayerInformation>
    {
        private Player player = null;

        // Constructor.
        private PlayerInformation(Player player)
        {
            this.player = player;
        }

        @Override
        public int compareTo(PlayerInformation player)
        {
            return this.player.getScore() - player.player.getScore();
        }

        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            sb.append(player.getName());
            sb.append(" (" + player.getPosition().getX() + ", " + player.getPosition().getY() + ")");
            sb.append(" - " + player.getScore() + " points");
            return sb.toString();
        }
    }
}
