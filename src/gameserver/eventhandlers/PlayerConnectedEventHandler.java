package gameserver.eventhandlers;

import game.Response;
import game.ResponseStatus;
import game.events.PlayerConnectedEvent;
import observer.Event;
import observer.EventHandler;

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
