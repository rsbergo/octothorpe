package game.command.commandhandler;

import game.OctothorpeGame;
import game.command.Action;
import game.command.Command;
import game.command.Result;
import game.command.ResultCode;

/**
 * Handles commands whose action is "players".
 */
public class PlayersCommandHandler extends CommandHandler
{
    /**
     * Constructor.
     * Gets a reference of the game running.
     * 
     * @param game the game to which this command handler was installed
     */
    public PlayersCommandHandler(OctothorpeGame game)
    {
        super(game);
    }

    @Override
    public void processCommand(Command command, Result result)
    {
        if (isArgsEmpty(command, result) && isActionExpected(command, result, Action.Players))
        {
            // TODO: start notifying player about players in the game
            result.setResultCode(ResultCode.Success);
            StringBuilder sb = new StringBuilder();
            sb.append("OK. ");
            sb.append(game.getPlayerCount());
            sb.append(game.getPlayerCount() == 1 ? " player" : " players");
            sb.append(" in the game.");
            result.setMessage(sb.toString());
        }
    }

    // Checks whether args in command are empty.
    // Action.Players doesn't expect any arguments.
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
