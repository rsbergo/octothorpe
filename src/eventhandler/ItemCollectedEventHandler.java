package eventhandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import command.ResultCode;
import event.Event;
import event.ItemCollectedEvent;
import eventmanager.EventQueue;
import gameserver.PlayerHandler;
import gameserver.Response;
import logger.Logger;
import logger.LogLevel;

public class ItemCollectedEventHandler extends EventQueue implements EventHandler
{
    private PlayerHandler handler;
    
    public ItemCollectedEventHandler(PlayerHandler handler)
    {
        this.handler = handler;
    }
    
    @Override
    public List<Response> eventToResponse(Event event)
    {
        List<Response> list = new ArrayList<Response>();
        if (event instanceof ItemCollectedEvent)
        {
            ItemCollectedEvent itemCollectedEvent = (ItemCollectedEvent) event;
            {
                Response response = new Response();
                response.setResponseCode(ResultCode.ItemNotification);
                StringBuilder data = new StringBuilder();
                data.append(itemCollectedEvent.getItem().getId());
                data.append(", " + itemCollectedEvent.getItem().getValue());
                data.append(", " + itemCollectedEvent.getPlayer().getName());
                data.append(" found ");
                data.append(itemCollectedEvent.getItem().getId());
                response.setMessage(data.toString());
                list.add(response);
            }
        }
        return list;
    }
    
    @Override
    protected void processQueuedEvent(Event event)
    {
        if (event instanceof ItemCollectedEvent)
        {
            try
            {
                List<Response> responses = eventToResponse(event);
                for (Response response : responses)
                {
                    Logger.log(LogLevel.Info, "Sending notification: \"" + response.toString() + "\"");
                    handler.getSocket().send(response);
                    Logger.log(LogLevel.Info, "Sending notification: \"" + response.toString() + "\"");
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
