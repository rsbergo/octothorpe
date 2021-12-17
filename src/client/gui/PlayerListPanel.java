package client.gui;

import java.awt.Dimension;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
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
    // TODO: adjust model so player information can be customized instead of using toString()
    private JList<Player> playerList = new JList<Player>();   // list of players connected
    private JScrollPane playerListScroll = new JScrollPane(); // scrolling pane for the list of players
    
    private DefaultListModel<Player> playerListModel = new DefaultListModel<Player>(); // the players data
    
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

        layoutManager.setHorizontalGroup(layoutManager.createSequentialGroup()
            .addComponent(playerListScroll, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
        );

        layoutManager.setVerticalGroup(layoutManager.createSequentialGroup()
            .addComponent(playerListScroll, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
        );

        content.setLayout(layoutManager);
    }

    // Initializes the cmponents of the player list panel
    private void initComponents()
    {
        initPlayerList();
        initPlayerListScroll();
    }

    // Initializes the list of players
    private void initPlayerList()
    {
        playerList.setFont(DefaultFont.getPlain(12));
        playerList.setEnabled(false);
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
        Collections.sort(players, new ScoreComparator());
        playerListModel.clear();
        playerListModel.addAll(players);
    }

    /**
     * Allows sorting based on the score.
     */
    private static class ScoreComparator implements Comparator<Player>
    {
        @Override
        public int compare(Player player1, Player player2)
        {
            return player1.getScore() - player2.getScore();
        }

    }
}
