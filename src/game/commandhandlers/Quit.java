package game.commandhandlers;

import game.Action;
import game.Command;
import game.CommandHandler;
import game.OctothorpeGame;
import game.Player;
import game.Result;
import game.ResultCode;

public class Quit extends CommandHandler
{
    /**
     * Constructor.
     * Gets a reference of the game running.
     * 
     * @param game the game to which this command handler was installed
     */
    public Quit(OctothorpeGame game)
    {
        super(game);
    }

    @Override
    public void processCommand(Command command, Result result)
    {
        if (isArgsEmpty(command, result) && isActionExpected(command, result, Action.Quit))
        {
            game.removePlayer(command.getPlayer());
            result.setResultCode(ResultCode.Success);
            Player player = new Player(command.getPlayer(), -1, -1, 0);
            result.setMessage(player + ", disconnected");
            // TODO: send asynchronous player information. Maybe manage through PlayerHandler, similar to login.
        }
    }
    
    // Checks whether args in command are empty.
    // Action.Quit doesn't expect any arguments.
    private boolean isArgsEmpty(Command command, Result result)
    {
        if (!command.getArgs().isEmpty())
        {
            result.setResultCode(ResultCode.BadRequest);
            result.setMessage("Error. Command malformed, no arguments expected.");
            return false;
        }
        return true;
    }
}
