package old.gameserver;

import gameserver.PlayerSocket;

/**
 * Thread responsible for the interactions between a player and a game.
 */
public class GameThread extends Thread
{
    private PlayerSocket socket; // socket used in the communication between the player client and the game server


    /**
     * Constructor.
     */
    public GameThread()
    {
        super();
    }

    @Override
    public void run()
    {

    }
}
