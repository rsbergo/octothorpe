package server.eventhandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import server.command.ResultCode;
import server.event.Event;
import server.event.PlayerDisconnectedEvent;
import server.eventmanager.EventQueue;
import server.gameserver.PlayerHandler;
import server.gameserver.Response;
import server.logger.LogLevel;
import server.logger.Logger;

public class PlayerDisconnectedEventHandler extends EventQueue implements EventHandler
{
    private PlayerHandler handler;
    
    public PlayerDisconnectedEventHandler(PlayerHandler handler)
    {
        this.handler = handler;
    }
    
    @Override
    public List<Response> eventToResponse(Event event)
    {
        List<Response> list = new ArrayList<Response>();
        if (event instanceof PlayerDisconnectedEvent)
        {
            Response response = new Response();
            PlayerDisconnectedEvent playerConnected = (PlayerDisconnectedEvent) event;
            response.setResponseCode(ResultCode.PlayerUpdate);
            StringBuilder data = new StringBuilder();
            data.append(playerConnected.getPlayer().getName());
            data.append(", -1");
            data.append(", -1");
            data.append(", 0");
            data.append(", disconnected");
            response.setMessage(data.toString());
            list.add(response);
        }
        return list;
    }
    
    @Override
    protected void processQueuedEvent(Event event)
    {
        if (event instanceof PlayerDisconnectedEvent)
        {
            try
            {
                List<Response> responses = eventToResponse(event);
                for (Response response : responses)
                {
                    Logger.log(LogLevel.Info, "Sending notification: \"" + response.toString() + "\"");
                    handler.getSocket().send(response);
                    Logger.log(LogLevel.Info, "Notification sent");
                }
            }
            catch (IOException e)
            {
                Logger.log(LogLevel.Error, "Socket error sending message", e);
                handler.setConnected(false);
            }
        }
    }
}
