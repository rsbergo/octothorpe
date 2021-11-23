package game.command.commandhandler;

import java.util.List;

import game.command.Action;
import game.command.Command;
import game.command.Result;
import game.command.ResultCode;

/**
 * Provides a mechanism to process commands for a game.
 * The processCommand method receives the command to be processed and an instance of Result to be updated with the
 * result of processing the command.
 * Each command handler should validate the command received according to its own rules. Validation should include
 * whether the command's action is the action expected to be processed and whether the args received are appropriate for
 * expected action. Default implementations for these validations are provided but may be overwritten. A default
 * implementation for verifying if the command received is valid is also provided; it checks if the command contains the
 * player who issued it and if the command's action is a supported action.
 */
public interface CommandHandler
{
    /**
     * Processes the command. Command handlers should validate command before processing it.
     * Updates result with the outcome of processing command.
     * 
     * @param command the command to be processed
     * @param result  the result to be returned once the command has been processed
     */
    public abstract void processCommand(Command command, Result result);
    
    /**
     * Checks whether the command received is valid.
     * Provided for convenience. Condenses all default command checkers into a single function.
     * Updates result according to the checkers performed.
     * 
     * @param command the command to be processed
     * @param result  the result to be returned once the command has been processed
     * @return true if command passes all chackers; false if command fails any checkers
     */
    
    /**
     * Checks whether the command received is valid.
     * Provided for convenience. Condenses all default command checkers into a single function.
     * Updates result according to the checkers performed.
     * 
     * @param command           the command to be processed
     * @param expectedAction    the action expected by the command handler
     * @param expectedArgsCount the number of arguments expected by the action
     * @param result            the result to be returned once the command has been processed
     * @return true if command passes all chackers; false if command fails any checkers
     */
    default boolean isValidCommand(Command command, Action expectedAction, int expectedArgsCount, Result result)
    {
        return hasValidPlayer(command.getPlayer(), result)
               && hasValidAction(command.getAction(), result) 
               && isExpectedAction(expectedAction, command.getAction(), result)
               && hasExpectedNumberofArguments(command.getArgs(), expectedArgsCount, result);
    }
    
    /**
     * Checks whether the action from command is the expected action.
     * If not, updates result to communicate the unexpected value for action. Result returns a 500-ServerError code.
     * 
     * @param expectedAction the expected action for the command handler
     * @param commandAction  the action received in the command being processed
     * @param result         the result to be returned once the command has been processed
     * @return true if the command action matches the expected action; false otherwise
     */
    default boolean isExpectedAction(Action expectedAction, Action commandAction, Result result)
    {
        if (commandAction == expectedAction)
            return true;
        result.setResultCode(ResultCode.ServerError);
        result.setMessage("Internal error. Unable to process command.");
        return false;
    }
    
    /**
     * Checks whether the number of arguments is the expected number of arguments.
     * If not, updates result to communicate the unexpected arguments. Result returns a 400-BadRequest code.
     * 
     * @param args              the list of arguments received in the command being processed
     * @param expectedArgsCount the expected argument count
     * @param result            the result to be returned once the command has been processed
     * @return true if the number of arguments received matches the expected arguments count; false otherwise
     */
    default boolean hasExpectedNumberofArguments(List<String> args, int expectedArgsCount, Result result)
    {
        if (args.size() == expectedArgsCount)
            return true;
        result.setResultCode(ResultCode.BadRequest);
        result.setMessage("Error. Unexpected number of arguments.");
        return false;
    }
    
    /**
     * Checks whether the player has been identified in the command received.
     * If not, updates result to communicate the malformed command. Result returns a 400-BadRequest code.
     * 
     * @param player the player information received in the command
     * @param result the result to be returned once the command has been processed
     * @return true if the player received is not null; false otherwise
     */
    default boolean hasValidPlayer(String player, Result result)
    {
        if (player != null)
            return true;
        result.setResultCode(ResultCode.BadRequest);
        result.setMessage("Error. You must log in first.");
        return false;
    }
    
    /**
     * Checks whether the command being processed contains a valid action.
     * If not (i.e. action is Unknown), updates result to communicate the malformed command. Result returns a
     * 400-BadRequest code.
     * 
     * @param action the action received in the command being processed
     * @param result the result to be returned once the command has been processed
     * @return true if action is a valid game action; false otherwise
     */
    default boolean hasValidAction(Action action, Result result)
    {
        if (action != Action.Unknown)
            return true;
        result.setResultCode(ResultCode.BadRequest);
        result.setMessage("Error. Unknown command.");
        return false;
    }
}
