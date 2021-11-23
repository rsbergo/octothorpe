package game.command;

/**
 * Represents the outcome of an action executed by a player on the game.
 * A result identifies the player who requested the action to be executed, contains the type of outcome, and a message
 * with additional information about the outcome of the action.
 */
public class Result
{
    private String player;   // the player who requested the action to be executed
    private ResultCode code; // the type of result
    private String message;  // additional information about the result

    /**
     * Default constructor.
     */
    public Result()
    {}

    /**
     * Constructor.
     * Sets the player that executed the action, the type of result, and the result message.
     * 
     * @param player  the player that executed the action
     * @param code    the result type
     * @param message the result message
     */
    public Result(String player, ResultCode code, String message)
    {
        this.player = player;
        this.code = code;
        this.message = message;
    }

    // Setters and Getters
    public void setPlayer(String player) { this.player = player; }
    public String getPlayer() { return player; }
    public void setResultCode(ResultCode code) { this.code = code; }
    public ResultCode getResultCode() { return code; }
    public void setMessage(String message) { this.message = message; }
    public String getMessage() { return message; }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(player);
        sb.append(" " + code.getCode() + ":");
        sb.append(" " + message);
        return sb.toString();
    }
}
