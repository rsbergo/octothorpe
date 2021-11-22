package game.command.commandhandlers;

import game.OctothorpeGame;
import game.command.Action;
import game.command.Command;
import game.command.Result;
import game.command.ResultCode;

/**
 * A function that processes a command and returns a result for the command.
 * 
 * TODO: review the need to have an instance of the game. Maybe just receive what is actually needed (e.g. list of players, map, etc.)
 */
public abstract class CommandHandler
{
    protected OctothorpeGame game = null; // instance of the game running
    
    /**
     * Constructor.
     * Gets a reference of the game running.
     * 
     * @param game the game to which this command handler was installed
     */
    public CommandHandler(OctothorpeGame game)
    {
        this.game = game;
    }
    
    /**
     * Processes the command and returns the result of the command.
     * 
     * @param command the command to be processed
     * @param result  the result to be returned once the command has been processed
     */
    public abstract void processCommand(Command command, Result result);
    
    /**
     * Checks whether the action from command is the expected action.
     * If not, prepares a result to communicate the unexpected value for action.
     * 
     * @param command        the command to be verified
     * @param result         the result to be returned once the command has been processed
     * @param expectedAction the expected action for the command handler
     * @return true if the action from command matches the expected action; false otherwise
     */
    protected boolean isActionExpected(Command command, Result result, Action expectedAction)
    {
        if (command.getAction() != expectedAction)
        {
            result.setResultCode(ResultCode.ServerError);
            result.setMessage("Internal error. Unable to process command.");
            return false;
        }
        return true;
    }
}
