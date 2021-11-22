package game.command.commandhandler;

import java.util.HashMap;
import java.util.Map;

import game.command.Action;
import logger.LogLevel;
import logger.Logger;

/**
 * Manages command handlers for the game.
 * Stores references for command handlers used by the game and allows retrieving the appropriate command handler.
 * Supports one handler for each action in the game.
 */
public class CommandHandlerManager
{
    private Map<Action, CommandHandler> handlers = new HashMap<Action, CommandHandler>(); // list of handlers
    
    /**
     * Installs a new command handler.
     * If there is already a command handler for the action specified, this command handler will be replaced with the
     * new command handler specified.
     * 
     * @param action  the action for which the handler is being added
     * @param handler the handler being added
     */
    public void installCommandHandler(Action action, CommandHandler handler)
    {
        Logger.log(LogLevel.Info, "Installing command handler for " + action);
        handlers.put(action, handler);
    }
    
    /**
     * Retrieves the command handler installed for the action specified.
     * 
     * @param action the action whose command handler is to be retrieved
     * @return The command handler installed for action; null if no command handler has been installed for the action
     *         specified.
     */
    public CommandHandler getCommandHandler(Action action)
    {
        return handlers.get(action);
    }

    /**
     * Removes a command handler that has been installed for the action specified.
     * Has no effect if no command handler has been installed for action.
     * 
     * @param action the action whose command handler should be uninstalled
     */
    public void removeCommandHandler(Action action)
    {
        Logger.log(LogLevel.Info, "Removing command handler for " + action);
        handlers.remove(action);
    }
}
