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
    MessageReceived,
    Move,
    PlayerDisconnected,
    PlayerListUpdated,
    PlayerUpdated,
    Request,
    Quit,
    Response,
    SendMessage,
    SynchronousResponse,
    UpdateMap,
    UpdatePlayers;
}
