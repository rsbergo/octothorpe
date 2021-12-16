package client.gui;

import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import client.connector.Command;
import client.connector.Request;
import client.connector.Response;
import client.connector.ResponseCode;
import client.event.Event;
import client.event.LoginEvent;
import client.event.RequestEvent;
import client.event.ResponseEvent;
import client.event.Subject;
import client.game.Game;
import client.gameclient.GameClient;
import client.observer.Observable;
import client.observer.Observer;
import logger.Logger;
import logger.LogLevel;

/**
 * Manages the client GUI.
 * Generates RequestEvents from the GUI.
 */
public class ClientGUI extends Observable implements Observer
{
    private GameClient client = null; // the game client connected to the server
    private Game game = null;         // representation of the game state
    
    private JFrame mainWindow = new JFrame();                  // main window
    private JPanel contentPanel = new JPanel();                // content panel
    private ResponseLabel responseLabel = new ResponseLabel(); // display synchronous responses to commands

    private Observable currentNotifier = null; // the current GUI element generating events

    /**
     * Constructor.
     * Sets up the game client running the game.
     * 
     * @param client the game client connected to the game server
     */
    public ClientGUI(GameClient client)
    {
        Logger.log(LogLevel.Info, "Initializing ClientGUI...");
        this.client = client;
        bindEvents();
        initComponents();
        createLaytou();
        Logger.log(LogLevel.Info, "ClientGUI initialized");
    }
    
    // Setters and Getters
    public void setContentPanel(JPanel contentPanel) { this.contentPanel = contentPanel; }

    /**
     * Initializes the GUI for the game client.
     * 
     * @param notifier the notifications manager used by the game client
     */
    public void start()
    {
        java.awt.EventQueue.invokeLater(() -> mainWindow.setVisible(true));
        client.run(); //TODO: potentially needs to run in a separate thread
    }

    @Override
    public void processEvent(Event event)
    {
        if (event.getSubject() == Subject.Response)
            handleResponseEvent(event);
        if (event.getSubject() == Subject.Login)
            handleLoginEvent(event);
    }
    
    private void bindEvents()
    {
        Logger.log(LogLevel.Info, "Binding ClientGUI events...");
        registerSubject(Subject.Request);
        client.subscribe(Subject.Response, this);
        subscribe(Subject.Request, client);
        Logger.log(LogLevel.Info, "Finished binding ClientGUI events");
    }

    // Initializes the components for the main window
    private void initComponents()
    {
        initMainWindow();
        initContentPanel();
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
            .addComponent(responseLabel.getLabel())
        );

        layoutManager.setVerticalGroup(layoutManager.createSequentialGroup()
            .addComponent(contentPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
            .addComponent(responseLabel.getLabel())
        );

        pane.setLayout(layoutManager);
    }

    // Initializes the main window for the application
    private void initMainWindow()
    {
        mainWindow.setTitle("Octothorpe # - The Game | " + client.getHost() + ":" + client.getPort());
        mainWindow.setSize(800, 600);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainWindow.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                if (game != null)
                    generateQuitRequest();
            }
        });
    }

    // Initializes the content panel to be the login panel.
    private void initContentPanel()
    {
        displayLoginPanel();
    }

    // Replaces the current content panel with the new panel specified.
    private void replaceContentPanel(JPanel panel)
    {
        contentPanel.removeAll();
        contentPanel.add(panel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // Replaces the content panel with a new login panel.
    private void displayLoginPanel()
    {
        LoginPanel panel = new LoginPanel();
        updateCurrentNotifier(panel, Subject.Login);
        replaceContentPanel(panel.getPanel());
    }

    // Replaces the content panel with a new game panel.
    private void displayGamePanel()
    {
        GamePanel panel = new GamePanel();
        updateCurrentNotifier(panel); // TODO: add list of events listening to
        replaceContentPanel(panel.getPanel());
    }
 
    // Replaces the current notifier with the notifier specified.
    // Unsubscribes from the current notifier before replacing it.
    // Subscribes to all events generated by the specified notifier.
    private void updateCurrentNotifier(Observable notifier, Subject... subjects)
    {
        Logger.log(LogLevel.Info, "Updating currently Notifier...");
        if (currentNotifier != null)
            currentNotifier.unsubscribe(this);
        currentNotifier = notifier;
        for (Subject subject : subjects)
            currentNotifier.subscribe(subject, this);
        Logger.log(LogLevel.Info, "Currently notifier updated");
    }

    // Generates a request event with a Quit command.
    private void generateQuitRequest()
    {
        Request request = new Request();
        request.setCommand(Command.Quit);
        notify(new RequestEvent(request));
    }

    // Handles ResponseEvent.
    // If synchronous response, update response label.
    // If synchronous successful response for login request, initialize the game.
    private void handleResponseEvent(Event event)
    {
        if (event.getSubject() == Subject.Response)
        {
            ResponseEvent responseEvent = (ResponseEvent) event;
            Request request = responseEvent.getRequest();
            Response response = responseEvent.getResponse();
            updateResponseLabel(response);
            processLogin(request, response);
            processQuit(request, response);
            //TODO: generate asynchronous events
        }
    }

    // Updates the response label if a synchronous response was received.
    private void updateResponseLabel(Response response)
    {
        if (response.getResponseCode().getCode() >= 200)
            responseLabel.setText(response);
    }

    // Initializes the game state.
    // Loads the map panel.
    private void processLogin(Request request, Response response)
    {
        if (response.getResponseCode() == ResponseCode.Success
            && request != null
            && request.getCommand() == Command.Login)
        {
            String name = request.getArgs().get(0);
            Logger.log(LogLevel.Info, "Processing login: " + name);
            game = new Game(name);
            displayGamePanel();
        }
    }

    // Terminates the game client.
    private void processQuit(Request request, Response response)
    {
        if (response.getResponseCode() == ResponseCode.Success
            && request != null
            && request.getCommand() == Command.Quit)
        {
            client.stop();
        }
    }

    // Handles LoginEvent.
    // Generate a new RequestEvent with a request for a login attempt.
    private void handleLoginEvent(Event event)
    {
        if (event.getSubject() == Subject.Login)
        {
            LoginEvent loginEvent = (LoginEvent) event;
            Request request = new Request();
            request.setCommand(Command.Login);
            request.setArgs(loginEvent.getName());
            notify(new RequestEvent(request));
        }
    }
}
