package server;

/**
 * Encapsulates the thread running the protocol for a ClientHandler.
 */
public class ClientThread extends Thread
{
    private ClientHandler client; // the client handler associated with this thread
    private Protocol protocol;    // the protocol running on this thread

    /**
     * Constructor.
     * 
     * @param client the client handler associated with this thread
     */
    public ClientThread(ClientHandler client, Protocol protocol)
    {
        super();
        this.client = client;
        this.protocol = protocol;
    }

    // Setters and Getters
    public ClientHandler getClientHandler() { return client; }

    @Override
    public void run()
    {
        System.out.println("Client " + client.getClientId() + " - Starting thread");
        protocol.run(client);
        client.close();
        System.out.println("Client " + client.getClientId() + " - Thread finished");
    }
}
