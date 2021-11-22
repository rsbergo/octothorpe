package old.gameserver.eventhandlers;

import old.game_old.Response;
import old.game_old.ResponseStatus;
import old.game_old.events.PlayerConnectedEvent;
import old.observer.Event;
import old.observer.EventHandler;

public class PlayerConnectedEventHandler implements EventHandler
{
    @Override
    public Response processEvent(Event event)
    {
        Response response = new Response();
        if (event instanceof PlayerConnectedEvent)
        {
            PlayerConnectedEvent playerConnected = (PlayerConnectedEvent) event;
            response.setResponseStatus(ResponseStatus.PlayerUpdate);
            StringBuilder data = new StringBuilder();
            data.append(playerConnected.getPlayer().getName());
            data.append(", " + playerConnected.getPlayer().getPositionX());
            data.append(", " + playerConnected.getPlayer().getPositionY());
            data.append(", " + playerConnected.getPlayer().getPoints());
            data.append(", connected");
            response.setData(data.toString());
        }
        return response;
    }
}
