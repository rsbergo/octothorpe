package game.command.commandhandlers;

import game.OctothorpeGame;
import game.command.Action;
import game.command.Command;
import game.command.Result;
import game.command.ResultCode;

public class MessageCommandHandler extends CommandHandler
{
    /**
     * Constructor.
     * Gets a reference of the game running.
     * 
     * @param game the game to which this command handler was installed
     */
    public MessageCommandHandler(OctothorpeGame game)
    {
        super(game);
    }

    @Override
    public void processCommand(Command command, Result result)
    {
        if (isArgsValid(command, result) && isActionExpected(command, result, Action.Message))
        {
            result.setResultCode(ResultCode.Success);
            result.setMessage("OK. Message sent.");

            StringBuilder sb = new StringBuilder();
            sb.append("[" + command.getPlayer() + "] ");
            for (String token : command.getArgs())
                sb.append(token + " ");
            sb.deleteCharAt(sb.length() - 1); // remove extra " " added after last token
            // TODO: send message asynchronously (broadcast)
            game.sendMessage(command.getPlayer(), sb.toString());
        }
    }
    
    // Checks whether args in command has the expected number of arguments.
    // Action.Message expects a non-empty arg list.
    private boolean isArgsValid(Command command, Result result)
    {
        if (command.getArgs().isEmpty())
        {
            result.setResultCode(ResultCode.BadRequest);
            result.setMessage("Error. Command malformed, unexpected number of arguments.");
            return false;
        }
        return true;
    }
}
