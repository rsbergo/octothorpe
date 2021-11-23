package old.gameserver.eventhandlers;

import command.ResultCode;
import gameserver.Response;
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
            response.setResponseCode(ResultCode.PlayerUpdate);
            StringBuilder data = new StringBuilder();
            data.append(playerConnected.getPlayer().getName());
            data.append(", " + playerConnected.getPlayer().getPositionX());
            data.append(", " + playerConnected.getPlayer().getPositionY());
            data.append(", " + playerConnected.getPlayer().getPoints());
            data.append(", connected");
            response.setMessage(data.toString());
        }
        return response;
    }
}
