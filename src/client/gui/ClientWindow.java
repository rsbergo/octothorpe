package client.gui;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import client.connector.Request;
import client.event.Event;
import client.event.RequestEvent;
import client.event.Subject;
import client.event.SynchronousResponseEvent;
import client.gameclient.NotificationManager;
import client.observer.Observer;

/**
 * The top-level container for the GUI.
 */
public class ClientWindow extends JFrame implements Observer
{
    private NotificationManager notifier = null; // event generator

    JLabel responsLabel = null;
    JTextField commandField = null;

    public ClientWindow(NotificationManager notifier)
    {
        this.notifier = notifier;
        notifier.subscribe(this);
        initUI();
    }

    private void initUI()
    {
        responsLabel = initResponseLabel();
        commandField = new JTextField(100);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener((event) -> sendCommand());
        createLayout(commandField, sendButton, responsLabel);
        initMainWindow();
    }

    // Initializes the main window for the application
    private void initMainWindow()
    {
        setTitle("Octothorpe # - The Game");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    // Initializes the label that shows responses from the game server
    private JLabel initResponseLabel()
    {
        return new JLabel("My label!");
    }

    private void createLayout(JComponent... components)
    {
        var pane = getContentPane();
        var layoutManager = new GroupLayout(pane);
        pane.setLayout(layoutManager);
        layoutManager.setAutoCreateContainerGaps(true);
        layoutManager.setAutoCreateGaps(true);

        layoutManager.setHorizontalGroup(layoutManager.createParallelGroup()
                .addComponent(components[0])
                .addComponent(components[1])
                .addComponent(components[2])
                .addGap(250)
        );

        layoutManager.setVerticalGroup(layoutManager.createSequentialGroup()
                .addComponent(components[0], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(components[1])
                .addComponent(components[2])
                .addGap(150)
        );
        pack();
    }

    @Override
    public void processEvent(Event event)
    {
        if (event.getSubject() == Subject.SynchronousResponse)
            responsLabel.setText(((SynchronousResponseEvent) event).getResponse().toString());
    }

    private void sendCommand()
    {
        String command = commandField.getText();
        Request request = new Request(command);
        notifier.notify(new RequestEvent(request));
    }
}
