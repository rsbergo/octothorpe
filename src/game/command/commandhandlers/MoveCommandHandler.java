package game.command.commandhandlers;

import game.OctothorpeGame;
import game.Player;
import game.Position;
import game.command.Action;
import game.command.Command;
import game.command.Result;
import game.command.ResultCode;

/**
 * Handles commands whose action is "players".
 */
public class MoveCommandHandler extends CommandHandler
{
    /**
     * Constructor.
     * Gets a reference of the game running.
     * 
     * @param game the game to which this command handler was installed
     */
    public MoveCommandHandler(OctothorpeGame game)
    {
        super(game);
    }
    
    @Override
    public void processCommand(Command command, Result result)
    {
        if (isArgsValid(command, result) && isActionExpected(command, result, Action.Move))
        {
            Player player = game.getPlayer(command.getPlayer());
            MoveDirection direction = MoveDirection.fromString(command.getArgs().get(0));
            Position pos = new Position(player.getPosition());
            if (!isMovementValid(pos, direction))
            {
                result.setResultCode(ResultCode.BadRequest);
                result.setMessage("Error. Invalid movement to \"" + direction + "\"");
                return;
            }
            player.updatePosition(pos);
            result.setResultCode(ResultCode.Success);
            result.setMessage(player.toString());
        }
    }
    
    // Checks whether args in command has the correct number of arguments and the expected values.
    // Action.Move expects a single argument.
    private boolean isArgsValid(Command command, Result result)
    {
        if (command.getArgs().size() != 1)
        {
            result.setResultCode(ResultCode.BadRequest);
            result.setMessage("Error. Command malformed, unexpected number of arguments.");
            return false;
        }
        
        if (MoveDirection.fromString(command.getArgs().get(0)) == MoveDirection.Unknown)
        {
            result.setResultCode(ResultCode.BadRequest);
            result.setMessage("Error. Invalid argument.");
            return false;
        }
        return true;
    }
    
    // Checks whether the movement leads the player to a valid position
    private boolean isMovementValid(Position pos, MoveDirection direction)
    {
        if (direction == MoveDirection.North)
            pos.moveY(-1);
        else if (direction == MoveDirection.South)
            pos.moveY(1);
        else if (direction == MoveDirection.East)
            pos.moveX(1);
        else if (direction == MoveDirection.West)
            pos.moveX(-1);
        return game.getMap().isValidPosition(pos);
    }
}
