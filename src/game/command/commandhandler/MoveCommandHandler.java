package game.command.commandhandler;

import java.util.Map;

import game.GameMap;
import game.Player;
import game.Position;
import game.command.Action;
import game.command.Command;
import game.command.Result;
import game.command.ResultCode;

/**
 * Processes commands whose action is Action.Move.
 * Receives an instance of the list of players in the game and an instance of the game map when installed.
 * Action.Move expects a single argument: the movement direction.
 * The movement attempts to update the player's position according to the movement direction:
 * - North: decrements the player's y coordinate by 1
 * - South: increments the player's y coordinate by 1
 * - East: increments the player's x coordinate by 1
 * - West: decrements the player's x coordinate by 1
 * Initiates synchronous treasure_found event for the player.
 * Initiates an asynchronous player_update event containing the updated player information.
 * 
 * TODO: Also receive game's event manager, so it can trigger treasure_found and player_update events
 */
public class MoveCommandHandler implements CommandHandler
{
    private final Action EXPECTED_ACTION = Action.Move; // expected command action
    private final int EXPECTED_ARGS_COUNT = 1;          // expected number of args for the Move action
    
    private Map<String, Player> players = null; // reference to the list of players in the game
    private GameMap map = null;                 // reference to the map the game is running
    
    /**
     * Constructor.
     * Receives a reference to the list of players in the game and a reference to the map the game is using.
     * 
     * @param players the list of players in the game
     * @param map     the game map
     */
    public MoveCommandHandler(Map<String, Player> players, GameMap map)
    {
        this.players = players;
        this.map = map;
    }
    
    @Override
    public void processCommand(Command command, Result result)
    {
        if (isValidCommand(command, EXPECTED_ACTION, EXPECTED_ARGS_COUNT, result))
        {
            Player player = players.get(command.getPlayer());
            MoveDirection direction = MoveDirection.fromString(command.getArgs().get(0));
            Position currentPos = player.getPosition();
            Position newPos = getNewPosition(currentPos, direction);
            movePlayer(player, newPos, result);
            // TODO: create treasure_found event
            // TODO: trigger synchronous treasure_found event
            // TODO: create player_update event
            // TODO: trigger player_update asynchronous event
        }
    }
    
    @Override
    public boolean isValidCommand(Command command, Action expectedAction, int expectedArgsCount, Result result)
    {
        return CommandHandler.super.isValidCommand(command, expectedAction, expectedArgsCount, result)
               && isValidDirection(command.getArgs().get(0), result);
    }
    
    /**
     * Checks whether the direction argument received is valid (i.e. a value of MoveDirection).
     * If not, updates result to communicate the unexpected argument value. Result returns a 400-BadRequest code.
     * 
     * @param direction the movement direction in the command
     * @param result    the result to be returned once the command has been processed
     * @return true if the movement direction is a valid direction; false otherwise
     */
    public boolean isValidDirection(String direction, Result result)
    {
        if (MoveDirection.fromString(direction) != MoveDirection.Unknown)
            return true;
        result.setResultCode(ResultCode.BadRequest);
        result.setMessage("Error. \"" + direction + "\" is an invalid argument for the \"move\" command");
        return false;
    }
    
    // Executes player movement.
    // Updates result with the appropriate outcome.
    private void movePlayer(Player player, Position newPos, Result result)
    {
        if (map.isValidPosition(newPos))
        {
            player.updatePosition(newPos);
            getValidMovementResult(result, player);
        }
        else
            getInvalidMovementResult(result);
    }
    
    // Updates the position according to the movement direction
    private Position getNewPosition(Position pos, MoveDirection direction)
    {
        if (direction == MoveDirection.North)
            return new Position(pos.getX(), pos.getY() - 1);
        if (direction == MoveDirection.South)
            return new Position(pos.getX(), pos.getY() + 1);
        if (direction == MoveDirection.North)
            return new Position(pos.getX() + 1, pos.getY());
        if (direction == MoveDirection.North)
            return new Position(pos.getX() - 1, pos.getY());
        return null;
    }
    
    // Updates result with the outcome of an invalid movement.
    private void getInvalidMovementResult(Result result)
    {
        result.setResultCode(ResultCode.BadRequest);
        result.setMessage("Error. Cannot move in that direction.");
    }
    
    // Updates result with the outcome of a valid movement.
    private void getValidMovementResult(Result result, Player player)
    {
        result.setResultCode(ResultCode.Success);
        StringBuilder sb = new StringBuilder();
        sb.append(player.getName());
        sb.append(", " + player.getPosition().getX());
        sb.append(", " + player.getPosition().getY());
        sb.append(", " + player.getScore());
        result.setMessage(sb.toString());
    }
}
