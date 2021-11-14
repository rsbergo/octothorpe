package server;

public interface Protocol
{
    /**
     * Runs the protocol on the client handler provided.
     * 
     * @param client the client handler on which the protocol will be run
     */
    public abstract void run(ClientHandler client);
}
