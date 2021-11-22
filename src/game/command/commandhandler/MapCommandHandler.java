package game.command.commandhandler;

import game.OctothorpeGame;
import game.command.Action;
import game.command.Command;
import game.command.Result;
import game.command.ResultCode;

public class MapCommandHandler extends CommandHandler
{
    /**
     * Constructor.
     * Gets a reference of the game running.
     * 
     * @param game the game to which this command handler was installed
     */
    public MapCommandHandler(OctothorpeGame game)
    {
        super(game);
    }

    @Override
    public void processCommand(Command command, Result result)
    {
        if (isArgsEmpty(command, result) && isActionExpected(command, result, Action.Map))
        {
            result.setResultCode(ResultCode.Success);
            StringBuilder sb = new StringBuilder();
            sb.append("OK. Game world if ");
            sb.append(game.getMap().getNumberOfRows());
            sb.append("x");
            sb.append(game.getMap().getNumberOfColumns());
            sb.append(" spaces");
            result.setMessage(sb.toString());
            // TODO: start sending map synchronously
            game.sendMapSize(command.getPlayer());
            game.sendMapData(command.getPlayer());
        }
    }
    
    // Checks whether args in command are empty.
    // Action.Map doesn't expect any arguments.
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
