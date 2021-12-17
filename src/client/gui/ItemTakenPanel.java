package client.gui;

import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import client.event.Event;
import client.event.ItemTakenEvent;
import client.event.Subject;

/**
 * Displays a log of items taken by the user.
 */
public class ItemTakenPanel extends ContentPanel
{
    private JLabel titleLabel = new JLabel();                    // list title
    private JTextArea itemTakenArea = new JTextArea();           // display the items taken
    private JScrollPane itemTakenAreaScroll = new JScrollPane(); // scrolling pane for the item taken area
    
    /**
     * Constructor.
     * Initializes components and defines the layout.
     */
    public ItemTakenPanel()
    {
        super("ItemTakenPanel");
        initComponents();
        createLayout();
    }

    @Override
    public synchronized void processEvent(Event event)
    {
        if (event.getSubject() == Subject.ItemTaken)
        {
            ItemTakenEvent itemTakenEvent = (ItemTakenEvent) event;
            updateLog(itemTakenEvent);
        }
    }
    
    @Override
    public List<Subject> getSubjectsConsumed()
    {
        return List.of(Subject.ItemTaken);
    }
    
    // Initialize components of the item taken panel
    private void initComponents()
    {
        initTitleLabel();
        initItemTakenArea();
        initItemTakenAreaScroll();
    }
    
    // Creates the layout in the content planel.
    private void createLayout()
    {
        GroupLayout layoutManager = new GroupLayout(content);
        layoutManager.setAutoCreateContainerGaps(true);
        layoutManager.setAutoCreateGaps(true);

        layoutManager.setHorizontalGroup(layoutManager.createParallelGroup(Alignment.LEADING)
            .addComponent(titleLabel)
            .addComponent(itemTakenAreaScroll, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
        );

        layoutManager.setVerticalGroup(layoutManager.createSequentialGroup()
            .addComponent(titleLabel)
            .addComponent(itemTakenAreaScroll, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
        );

        content.setLayout(layoutManager);
    }
    
    // Initialize the title label.
    private void initTitleLabel()
    {
        titleLabel.setFont(DefaultFont.getBold());
        titleLabel.setText("Items Taken");
    }
    
    // Initializes the text area where the map is displayed
    private void initItemTakenArea()
    {
        itemTakenArea.setFont(DefaultFont.getPlain());
        itemTakenArea.setEditable(false);

        // auto scrolling
        DefaultCaret caret = (DefaultCaret) itemTakenArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    }
    
    // Initializes the scroll pane attached to the map area
    private void initItemTakenAreaScroll()
    {
        itemTakenAreaScroll.setViewportView(itemTakenArea);
        itemTakenAreaScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        itemTakenAreaScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    // Updates the map in the map area
    private void updateLog(ItemTakenEvent event)
    {
        String log = event.getPlayerName() + " found item " + event.getItemId() + " (" + event.getItemValue() + " points)";
        itemTakenArea.append(log + "\r\n");
    }
}
