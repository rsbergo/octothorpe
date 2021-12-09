package server.commandhandler;

import server.command.Action;
import server.command.Command;
import server.command.Result;
import server.command.ResultCode;
import server.event.ItemCollectedEvent;
import server.event.ItemDataEvent;
import server.event.PlayerUpdateEvent;
import server.event.Subject;
import server.eventmanager.EventManager;
import server.game.GameMap;
import server.game.Item;
import server.game.MoveDirection;
import server.game.Player;
import server.game.Position;
import server.logger.LogLevel;
import server.logger.Logger;

/**
 * Processes commands whose action is Action.Move.
 * Receives an instance of the list of players in the game and an instance of the game map when installed.
 * Receives an instance of the game's event manager in order to generate events.
 * Action.Move expects a single argument: the movement direction.
 * The movement attempts to update the player's position according to the movement direction:
 * - North: decrements the player's y coordinate by 1
 * - South: increments the player's y coordinate by 1
 * - East: increments the player's x coordinate by 1
 * - West: decrements the player's x coordinate by 1
 * Initiates synchronous treasure_found event for the player.
 * Initiates an asynchronous player_update event containing the updated player information.
 */
public class MoveCommandHandler implements CommandHandler
{
    private final Action EXPECTED_ACTION = Action.Move; // expected command action
    private final int EXPECTED_ARGS_COUNT = 1;          // expected number of args for the Move action
    
    private GameMap map = null;                 // reference to the map the game is running
    private EventManager eventManager = null; // reference to the game's event manager
    
    /**
     * Constructor.
     * Receives a reference to the list of players in the game and a reference to the map the game is using.
     * 
     * @param map          the game map
     * @param eventManager the game's event manager
     */
    public MoveCommandHandler(GameMap map, EventManager eventManager)
    {
        this.map = map;
        this.eventManager = eventManager;
    }
    
    @Override
    public void processCommand(Command command, Result result)
    {
        if (isValidCommand(command, EXPECTED_ACTION, EXPECTED_ARGS_COUNT, result))
        {
            Logger.log(LogLevel.Debug, "Start processing command: \"" + command + "\"");
            Player player = command.getPlayer();
            MoveDirection direction = MoveDirection.fromString(command.getArgs().get(0));
            Position currentPos = player.getPosition();
            Position newPos = getNewPosition(currentPos, direction);
            movePlayer(player, newPos, result);
            eventManager.notify(new PlayerUpdateEvent(player));
            revealItemsNearby(player);
            Logger.log(LogLevel.Debug, "Processing command finished. Result: \"" + result + "\"");
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
    // Updates the player score if movement leads to a space containing an item.
    // Updates result with the appropriate outcome.
    private void movePlayer(Player player, Position newPos, Result result)
    {
        if (map.isValidPosition(newPos))
        {
            player.updatePosition(newPos);
            grabItem(player);
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
        if (direction == MoveDirection.East)
            return new Position(pos.getX() + 1, pos.getY());
        if (direction == MoveDirection.West)
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

    // Updates player's score if movement lands player on a space that contains an item.
    // Notifies player of item collected.
    private void grabItem(Player player)
    {
        Position pos = player.getPosition();
        Item item = map.getItemAtPosition(pos);
        if (item != null)
        {
            player.updateScore(item.getValue());
            eventManager.notify(new ItemCollectedEvent(player, item));
        }
    }

    // Reveals items at positions surrounding the player's position.
    private void revealItemsNearby(Player player)
    {
        int x = player.getPosition().getX();
        int y = player.getPosition().getY();
        revealItemAtPosition(player, x - 1, y - 1); // top left
        revealItemAtPosition(player, x, y - 1);     // top
        revealItemAtPosition(player, x + 1, y - 1); // top right
        revealItemAtPosition(player, x - 1, y);     // left
        revealItemAtPosition(player, x + 1, y);     // right
        revealItemAtPosition(player, x - 1, y + 1); // bottom left
        revealItemAtPosition(player, x, y + 1);     // bottom
        revealItemAtPosition(player, x + 1, y + 1); // bottom right
    }

    // Notifies player if there is an itam at position (x, y).
    private void revealItemAtPosition(Player player, int x, int y)
    {
        Item item = map.getItemAtPosition(x, y);
        if (item != null)
            eventManager.notify(player.getEventHandlerManager().getEventHandler(Subject.ItemData), new ItemDataEvent(item));
    }
}
