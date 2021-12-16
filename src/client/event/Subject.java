package client.event;

/**
 * List of event subjects supported.
 */
public enum Subject
{
    Unknown,
    ItemData,
    ItemTaken,
    Login,
    MapData,
    MapUpdated,
    Move,
    PlayerDisconnected,
    PlayerListUpdated,
    PlayerUpdated,
    Request,
    Quit,
    Response,
    SynchronousResponse,
    UpdateMap,
    UpdatePlayers;
}
