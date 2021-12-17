package client.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import client.event.QuitEvent;
import client.event.Subject;
import client.observer.Proxy;

/**
 * The main window for the application.
 * It contains a content panel that is updated according to the application state and a response label that displays
 * responses received from the game.
 */
public class MainWindow extends Proxy
{
    private JFrame mainWindow = new JFrame();                  // main window
    private JPanel contentPanel = new JPanel();                // content panel
    private ResponseLabel responseLabel = new ResponseLabel(); // display synchronous responses to commands
    
    private ContentPanel currentContent = null; // current content being displayed
    
    /**
     * Default constructor.
     * Binds the events proxied by the main window.
     * Initializes the main window components and defines its layout.
     */
    public MainWindow()
    {
        super("MainWindow");
        bindEvents();
        initComponents();
        createLaytou();
    }

    /**
     * Updates the content of the main window's content panel.
     * 
     * @param newContent the new content to be displayed
     */
    public void updateContentPanel(ContentPanel newContent)
    {
        if (currentContent != null)
            currentContent.unsubscribe(this); // stop listening to events generated by current content
        contentPanel.removeAll();
        
        currentContent = newContent;
        newContent.subscribe(this, newContent.getEventsProduced()); // listens to events generated by new content
        subscribe(newContent, newContent.getSubjectsConsumed());    // notifies new content of events received
        contentPanel.add(newContent.getContent());

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    /**
     * Shows or hides the main window.
     * 
     * @param flag if true, makes the Window visible, otherwise hides the Window
     */
    public void setVisible(boolean flag)
    {
        mainWindow.setVisible(flag);
    }

    // Initializes the components for the main window
    private void initComponents()
    {
        initMainWindow();
        initContentPanel();
        initResponseLabel();
    }
    
    // Creates the layout for the main window
    private void createLaytou()
    {
        Container pane = mainWindow.getContentPane();
        GroupLayout layoutManager = new GroupLayout(pane);
        
        layoutManager.setAutoCreateContainerGaps(true);
        layoutManager.setAutoCreateGaps(true);
        
        layoutManager.setHorizontalGroup(layoutManager.createParallelGroup()
            .addComponent(contentPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
            .addComponent(responseLabel.getLabel()));
        
        layoutManager.setVerticalGroup(layoutManager.createSequentialGroup()
            .addComponent(contentPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
            .addComponent(responseLabel.getLabel()));
        
        pane.setLayout(layoutManager);
    }
    
    // Initializes the main window for the application
    // TODO: add host address and port to the title bar
    private void initMainWindow()
    {
        mainWindow.setTitle("Octothorpe # - The Game");
        mainWindow.setSize(800, 600);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // TODO: try to make it work with DISPOSE_ON_CLOSE, but tear down is blocking on receiveRequest (GameClient) and receive (Connector)
        mainWindow.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                generateQuitEvent();
            }
        });
    }

    // Initializes the content panel.
    private void initContentPanel()
    {
        contentPanel.setLayout(new BorderLayout());
    }

    // Initializes the response label.
    private void initResponseLabel()
    {
        subscribe(responseLabel, Subject.Response);
    }

    // Generates a quit event and notifies subscribers.
    // TODO: review, this is going beyond the scope of a proxy
    private void generateQuitEvent()
    {
        QuitEvent event = new QuitEvent();
        notify(event);
    }

    // Events generated by the main window
    private void bindEvents()
    {
        registerSubject(Subject.Response);
        registerSubject(Subject.Quit);
        registerSubject(Subject.Login);
        registerSubject(Subject.PlayerListUpdated);
        registerSubject(Subject.MapUpdated);
        registerSubject(Subject.Move);
        registerSubject(Subject.MessageReceived);
        registerSubject(Subject.SendMessage);
        registerSubject(Subject.ItemTaken);
    }
}
