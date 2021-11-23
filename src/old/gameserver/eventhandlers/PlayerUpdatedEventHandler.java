package old.gameserver.eventhandlers;

import command.ResultCode;
import gameserver.Response;
import old.game_old.events.PlayerUpdatedEvent;
import old.observer.Event;
import old.observer.EventHandler;

public class PlayerUpdatedEventHandler implements EventHandler
{
    @Override
    public Response processEvent(Event event)
    {
        Response response = new Response();
        if (event instanceof PlayerUpdatedEvent)
        {
            PlayerUpdatedEvent playerUpdated = (PlayerUpdatedEvent) event;
            response.setResponseCode(ResultCode.PlayerUpdate);
            StringBuilder data = new StringBuilder();
            data.append(playerUpdated.getPlayer().getName());
            data.append(", " + playerUpdated.getPlayer().getPositionX());
            data.append(", " + playerUpdated.getPlayer().getPositionY());
            data.append(", " + playerUpdated.getPlayer().getPoints());
            response.setMessage(data.toString());
        }
        return response;
    }
}
