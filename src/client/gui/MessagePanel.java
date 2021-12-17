package client.gui;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.Dimension;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import client.event.Event;
import client.event.MessageReceivedEvent;
import client.event.SendMessageEvent;
import client.event.Subject;

/**
 * Displays messages received and allow players to send messages.
 */
public class MessagePanel extends ContentPanel
{
    private JLabel titleLabel = new JLabel();                  // list title
    private JTextArea messageArea = new JTextArea();           // display the messages
    private JScrollPane messageAreaScroll = new JScrollPane(); // scrolling pane for the message area
    private JTextField messageField = new JTextField();        // message input field
    private JButton sendButton = new JButton();                // button to post a message

    /**
     * Constructor.
     * Initializes components and defines the layout.
     */
    public MessagePanel()
    {
        super("MessagePanel");
        registerSubject(Subject.SendMessage);
        initComponents();
        createLayout();
    }

    @Override
    public synchronized void processEvent(Event event)
    {
        if (event.getSubject() == Subject.MessageReceived)
        {
            MessageReceivedEvent messageReceivedEvent = (MessageReceivedEvent) event;
            postMessage(messageReceivedEvent.getMessage());
        }
    }
    
    @Override
    public List<Subject> getEventsProduced()
    {
        return List.of(Subject.SendMessage);
    }

    @Override
    public List<Subject> getSubjectsConsumed()
    {
        return List.of(Subject.MessageReceived);
    }
    
    // Creates the layout in the content planel.
    private void initComponents()
    {
        initTitleLabel();
        initMessageArea();
        initMessageAreaScroll();
        initMessageField();
        initSendButton();
    }

    // Initialize components of the game panel
    private void createLayout()
    {
        GroupLayout layoutManager = new GroupLayout(content);
        layoutManager.setAutoCreateContainerGaps(true);
        layoutManager.setAutoCreateGaps(true);

        layoutManager.setHorizontalGroup(layoutManager.createParallelGroup(Alignment.LEADING)
            .addComponent(titleLabel)
            .addComponent(messageAreaScroll, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
            .addGroup(layoutManager.createSequentialGroup()
                .addComponent(messageField, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                .addComponent(sendButton)
            )
        );

        layoutManager.setVerticalGroup(layoutManager.createSequentialGroup()
            .addComponent(titleLabel)
            .addComponent(messageAreaScroll, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
            .addGroup(layoutManager.createParallelGroup(Alignment.BASELINE)
                .addComponent(messageField)
                .addComponent(sendButton)
            )
        );

        layoutManager.linkSize(SwingConstants.VERTICAL, messageField, sendButton);
        content.setLayout(layoutManager);
    }

    private void initTitleLabel()
    {
        titleLabel.setFont(DefaultFont.getBold());
        titleLabel.setText("Messages");
    }
    
    // Initializes the message area.
    private void initMessageArea()
    {
        messageArea.setLineWrap(true);
        messageArea.setEditable(false);
        messageArea.setFont(DefaultFont.getPlain(10));
    }

    // Initializes the message area scroll pane.
    private void initMessageAreaScroll()
    {
        messageAreaScroll.setViewportView(messageArea);
        messageAreaScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        messageAreaScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        messageAreaScroll.setPreferredSize(new Dimension(200, 100));
        messageAreaScroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener()
        {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e)
            {
                e.getAdjustable().setValue(e.getAdjustable().getMaximum());                
            }
        });;
    }

    // Initializes the message field.
    private void initMessageField()
    {
        messageField.setFont(DefaultFont.getPlain(10));
        messageField.setColumns(30);
        messageField.addActionListener((event) -> sendMessage());
    }

    private void initSendButton()
    {
        sendButton.setText("Send");
        sendButton.setFont(DefaultFont.getBold(10));
        sendButton.addActionListener((event) -> sendMessage());
    }

    // Posts the message on thw message area
    private void postMessage(String message)
    {
        messageArea.append("\r\n" + message);
    }

    // Generates an event to send the message in the message field.
    private void sendMessage()
    {
        String message = messageField.getText();
        SendMessageEvent event = new SendMessageEvent();
        event.setMessage(message);
        notify(event);
        messageField.setText("");
    }
}
