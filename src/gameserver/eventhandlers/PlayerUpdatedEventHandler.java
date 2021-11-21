package gameserver.eventhandlers;

import game.Response;
import game.ResponseStatus;
import game.events.PlayerUpdatedEvent;
import observer.Event;
import observer.EventHandler;

public class PlayerUpdatedEventHandler implements EventHandler
{
    @Override
    public Response processEvent(Event event)
    {
        Response response = new Response();
        if (event instanceof PlayerUpdatedEvent)
        {
            PlayerUpdatedEvent playerUpdated = (PlayerUpdatedEvent) event;
            response.setResponseStatus(ResponseStatus.PlayerUpdate);
            StringBuilder data = new StringBuilder();
            data.append(playerUpdated.getPlayer().getName());
            data.append(", " + playerUpdated.getPlayer().getPositionX());
            data.append(", " + playerUpdated.getPlayer().getPositionY());
            data.append(", " + playerUpdated.getPlayer().getPoints());
            response.setData(data.toString());
        }
        return response;
    }
}
