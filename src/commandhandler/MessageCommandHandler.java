package commandhandler;

import java.util.List;

import command.Action;
import command.Command;
import command.Result;
import command.ResultCode;
import event.SendMessageEvent;
import eventmanager.EventManager;
import logger.LogLevel;
import logger.Logger;

/**
 * Processes commands whose action is Action.Message.
 * Receives an instance of the game's event manager in order to generate events.
 * Action.Message expects a non-empty list of arguments. These arguments are assembled in a string, separated by a
 * whitespace (' ') to re-build the message to be sent.
 * Initiates an asynchronous send_message event containing the message to be sent.
 */
public class MessageCommandHandler implements CommandHandler
{
    private final Action EXPECTED_ACTION = Action.Message; // expected command action

    private EventManager eventManager = null; // reference to the game's event manager

    /**
     * Constructor.
     * 
     * @param eventManager the game's event manager
     */
    public MessageCommandHandler(EventManager eventManager)
    {
        this.eventManager = eventManager;
    }
    
    @Override
    public void processCommand(Command command, Result result)
    {
        if (isValidCommand(command, EXPECTED_ACTION, result))
        {
            Logger.log(LogLevel.Debug, "Start processing command: \"" + command + "\"");
            result.setResultCode(ResultCode.Success);
            result.setMessage("OK. Message sent.");
            eventManager.notify(new SendMessageEvent(buildMessage(command.getPlayer(), command.getArgs())));
            Logger.log(LogLevel.Debug, "Processing command finished. Result: \"" + result + "\"");
        }
    }
    
    /**
     * Checks whether the command received is valid.
     * Condenses all default command checkers into a single function.
     * Updates result according to the checkers performed.
     * 
     * @param command        the command to be processed
     * @param expectedAction the action expected by the command handler
     * @param result         the result to be returned once the command has been processed
     * @return true if command passes all chackers; false if command fails any checkers
     */
    public boolean isValidCommand(Command command, Action expectedAction, Result result)
    {
        return hasValidAction(command.getAction(), result) 
               && hasValidPlayer(command.getPlayer(), result)
               && isExpectedAction(expectedAction, command.getAction(), result)
               && isArgsNotEmpty(command.getArgs(), result);
    }
    
    /**
     * Checks whether the list of arguments is not empty.
     * If empty, updates result to communicate the unexpected number of arguments. Result returns a 400-BadRequest code.
     * 
     * @param args   the list of arguments received in the command being processed
     * @param result the result to be returned once the command has been processed
     * @return true if the list of arguments is not empty; false otherwise
     */
    public boolean isArgsNotEmpty(List<String> args, Result result)
    {
        if (!args.isEmpty())
            return true;
        result.setResultCode(ResultCode.BadRequest);
        result.setMessage("Error. Unexpected number of arguments.");
        return false;
    }

    // Re-builds the message to be sent from the list of arguments from the command.
    private String buildMessage(String player, List<String> tokens)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("[" + player + "] ");
        for (String token : tokens)
            sb.append(" " + token);
        return sb.toString();
    }
}
