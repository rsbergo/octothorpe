package client.gui;

import client.connector.Command;
import client.connector.Request;
import client.connector.Response;
import client.connector.ResponseCode;
import client.event.Event;
import client.event.ItemTakenEvent;
import client.event.LoginEvent;
import client.event.MapUpdatedEvent;
import client.event.MessageReceivedEvent;
import client.event.MoveEvent;
import client.event.PlayerListUpdateEvent;
import client.event.RequestEvent;
import client.event.ResponseEvent;
import client.event.SendMessageEvent;
import client.event.Subject;
import client.game.Game;
import client.game.Item;
import client.game.Map;
import client.game.Player;
import client.game.Position;
import client.gameclient.GameClient;
import client.observer.Observable;
import client.observer.Observer;
import logger.LogLevel;
import logger.Logger;

/**
 * Manages the client GUI.
 * Generates RequestEvents from the GUI.
 */
public class ClientGUI extends Observable implements Observer
{
    private GameClient client = null; // the game client connected to the server
    private Game game = new Game();   // representation of the game state
    
    private MainWindow mainWindow = new MainWindow(); // the application main window

    /**
     * Constructor.
     * Sets up the game client running the game.
     * 
     * @param client the game client connected to the game server
     */
    public ClientGUI(GameClient client)
    {
        super("ClientGUI");
        Logger.log(LogLevel.Info, "Initializing ClientGUI...");
        this.client = client;
        bindEvents();
        displayLoginPanel();
        Logger.log(LogLevel.Info, "ClientGUI initialized");
    }
    
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
    public synchronized void processEvent(Event event)
    {
        if (event.getSubject() == Subject.Login)
            sendLoginRequest(event);
        if (event.getSubject() == Subject.Quit)
            sendQuitRequest();
        if (event.getSubject() == Subject.Response)
            handleResponseEvent(event);
        if (event.getSubject() == Subject.Move)
            sendMoveRequest(event);
        if (event.getSubject() == Subject.SendMessage)
            sendMessageRequest(event);
    }
    
    // Defines the events generated by the GUI.
    // Subscribes game client to the events generated by the GUI.
    // Subscribes to events generated by the game client.
    // Subscribes to events generated by the main window.
    private void bindEvents()
    {
        Logger.log(LogLevel.Info, "Binding ClientGUI events...");

        // events generated by GUI
        registerSubject(Subject.Request);
        registerSubject(Subject.Response);
        registerSubject(Subject.PlayerListUpdated);
        registerSubject(Subject.MapUpdated);
        registerSubject(Subject.MessageReceived);
        registerSubject(Subject.ItemTaken);

        // subscribe to events generated by the game client
        client.subscribe(this, Subject.Response); 
        
        // subscribe game client to events generated by GUI
        subscribe(client, Subject.Request);
        
        // subscribe to events generated by main window
        mainWindow.subscribe(this, Subject.Login);
        mainWindow.subscribe(this, Subject.Quit);
        mainWindow.subscribe(this, Subject.Move);
        mainWindow.subscribe(this, Subject.SendMessage);

        // subscribe main window to events generated by GUI
        subscribe(mainWindow, Subject.Response);
        subscribe(mainWindow, Subject.PlayerListUpdated);
        subscribe(mainWindow, Subject.MapUpdated);
        subscribe(mainWindow, Subject.MessageReceived);
        subscribe(mainWindow, Subject.ItemTaken);

        Logger.log(LogLevel.Info, "Finished binding ClientGUI events");
    }

    // Sends a request event to the game client.
    private void sendRequestEvent(Request request)
    {
        RequestEvent event = new RequestEvent(request);
        Logger.log(LogLevel.Debug, "Sending request event to game client: \"" + event + "\"");
        notify(client, event);
    }

    // Sends a login request to the game client.
    private void sendLoginRequest(Event event)
    {
        if (event.getSubject() == Subject.Login)
        {
            Logger.log(LogLevel.Debug, "Received login event: \"" + event + "\"");
            LoginEvent loginEvent = (LoginEvent) event;
            Request request = new Request();
            request.setCommand(Command.Login);
            request.setArgs(loginEvent.getName());
            sendRequestEvent(request);
        }
    }

    // Sends a quit request to the game client.
    private void sendQuitRequest()
    {
        Request request = new Request();
        request.setCommand(Command.Quit);
        sendRequestEvent(request);
    }

    // Sends a move request to the game client.
    private void sendMoveRequest(Event event)
    {
        if (event.getSubject() == Subject.Move)
        {
            Logger.log(LogLevel.Debug, "Received move event: \"" + event + "\"");
            MoveEvent moveEvent = (MoveEvent) event;
            Request request = new Request();
            request.setCommand(Command.Move);
            request.setArgs(moveEvent.getDirection().toString());
            sendRequestEvent(request);
        }
    }

    // Sends a message request to the game client
    private void sendMessageRequest(Event event)
    {
        if (event.getSubject() == Subject.SendMessage)
        {
            Logger.log(LogLevel.Debug, "Received send message event: \"" + event + "\"");
            SendMessageEvent sendMessageEvent = (SendMessageEvent) event;
            Request request = new Request();
            request.setCommand(Command.Message);
            request.setArgs(sendMessageEvent.getMessage());
            sendRequestEvent(request);
        }
    }

    // Replaces the content panel with a new login panel.
    private void displayLoginPanel()
    {
        ContentPanel loginPanel = new LoginPanel();
        mainWindow.updateContentPanel(loginPanel);
    }

    // Replaces the content panel with a new game panel.
    private void displayGamePanel()
    {
        ContentPanel gamePanel = new GamePanel();
        mainWindow.updateContentPanel(gamePanel);
        sendPlayerListUpdate();
        sendMapUpdate();
    }
 
    // Handles ResponseEvent.
    // If synchronous response, forward to notifiers.
    // If synchronous successful response for login request, initialize the game.
    // TODO: maybe generate a new event instead of forwarding a response event
    private void handleResponseEvent(Event event)
    {
        if (event.getSubject() == Subject.Response)
        {
            ResponseEvent responseEvent = (ResponseEvent) event;
            Request request = responseEvent.getRequest();
            Response response = responseEvent.getResponse();

            notify(mainWindow, responseEvent);

            processLogin(request, response);
            processQuit(request, response);
            
            if (response.getResponseCode() == ResponseCode.PlayerUpdate)
                handlePlayerUpdatedEvent(response);
            if (response.getResponseCode() == ResponseCode.MapData)
                handleMapDataEvent(response);
            if (response.getResponseCode() == ResponseCode.ItemNotification)
                handleItemDataEvent(response);
            if (response.getResponseCode() == ResponseCode.Message)
                sendMessageReceived(response);
            if (response.getResponseCode() == ResponseCode.ItemTaken)
                handleItemTakenEvent(response);
        }
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
            game.setCurrentPlayer(name);
            displayGamePanel();
            sendPlayerListUpdate();
            sendMapUpdate();
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

    // Generates a new MapDataEvent containing the map data.
    private void handleMapDataEvent(Response response)
    {
        MapBuilder.build(response.getMessage());
        Map map = MapBuilder.getMap();
        if (map != null)
        {
            game.setMap(map);
            sendMapUpdate();
        }
    }

    // Handles player updated events.
    // Update game with player information.
    // Sends an updates list of players to the UI.
    private void handlePlayerUpdatedEvent(Response response)
    {
        if (response.getResponseCode() == ResponseCode.PlayerUpdate)
        {
            Player player = getPlayerFromResponse(response);
            if (player != null)
            {
                if (player.getPosition().equals(new Position(-1, -1))) // player disconnected
                    game.removePlayer(player);
                else
                    game.updatePlayer(player);
                sendPlayerListUpdate();
            }
        }
    }

    // Retrieves player information from a response message
    private Player getPlayerFromResponse(Response response)
    {
        final int FIELD_COUNT = 4;
        Player player = null;

        String[] tokens = response.getMessage().split(", ");
        if (tokens.length < FIELD_COUNT)
        {
            Logger.log(LogLevel.Error, "Error getting player information from response");
            return player;
        }

        try
        {
            String name = tokens[0];
            int x = Integer.parseInt(tokens[1]);
            int y = Integer.parseInt(tokens[2]);
            int score = Integer.parseInt(tokens[3]);
            player = new Player(name, x, y, score);
        }
        catch (NumberFormatException e)
        {
            Logger.log(LogLevel.Error, "Error getting player information from response", e);
        }
        return player;
    }

    // Handles item updated events.
    // Update game with item information.
    // Sends an updated map to the UI.
    private void handleItemDataEvent(Response response)
    {
        if (response.getResponseCode() == ResponseCode.ItemNotification)
        {
            Item item = getItemFromResponse(response);
            if (item != null)
                game.addItem(item);
            sendMapUpdate();
        }
    }

    // Retrieves player information from a response message
    private Item getItemFromResponse(Response response)
    {
        final int FIELD_COUNT = 3;
        Item item = null;

        String[] tokens = response.getMessage().split(", ");
        if (tokens.length < FIELD_COUNT)
        {
            Logger.log(LogLevel.Error, "Error getting item information from response");
            return item;
        }

        try
        {
            // TODO: assumes that the item ID is a number. Could it be a string?
            int id = Integer.parseInt(tokens[0]);
            int x = Integer.parseInt(tokens[1]);
            int y = Integer.parseInt(tokens[2]);
            item = new Item(id, x, y);
        }
        catch (NumberFormatException e)
        {
            Logger.log(LogLevel.Error, "Error getting item information from response", e);
        }
        return item;
    }
    
    // Handles item taken events.
    // Sends a notification to the UI.
    private void handleItemTakenEvent(Response response)
    {
        if (response.getResponseCode() == ResponseCode.ItemTaken)
        {
            ItemTakenEvent event = getItemTakenFromResponse(response);
            if (event != null)
                notify(event);
        }
    }

    // Retrieves player information from the response message and update event with the information retrieved.
    private ItemTakenEvent getItemTakenFromResponse(Response response)
    {
        final int FIELD_COUNT = 3;
        
        ItemTakenEvent event = null;
        String[] tokens = response.getMessage().split(", ");
        if (tokens.length < FIELD_COUNT)
        {
            Logger.log(LogLevel.Error, "Error getting item information from response");
            return event;
        }
        try
        {
            event = new ItemTakenEvent();
            event.setPlayerName(tokens[0]);
            event.setItemId(Integer.parseInt(tokens[1])); // TODO: assumes that the item ID is a number. Could it be a string?
            event.setItemValue(Integer.parseInt(tokens[2]));
        }
        catch (NumberFormatException e)
        {
            event = null;
            Logger.log(LogLevel.Error, "Error getting item taken information from response", e);
        }
        return event;
    }

    // Notifies the main window of an update in the list of players.
    private void sendPlayerListUpdate()
    {
        PlayerListUpdateEvent event = new PlayerListUpdateEvent();
        event.setPlayers(game.getPlayers());
        notify(mainWindow, event);
        sendMapUpdate();
    }

    // Notifies the main window of an update in the game map.
    private void sendMapUpdate()
    {
        if (game.getMapString() != null)
        {
            MapUpdatedEvent event = new MapUpdatedEvent();
            event.setMap(game.getMapString());
            notify(mainWindow, event);
        }
    }

    // Sends the message to the UI.
    private void sendMessageReceived(Response response)
    {
        if (response.getResponseCode() == ResponseCode.Message)
        {
            String message = response.getMessage();
            MessageReceivedEvent event = new MessageReceivedEvent();
            event.setMessage(message);
            notify(mainWindow, event);
        }
    }
}
