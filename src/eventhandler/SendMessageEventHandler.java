package eventhandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import command.ResultCode;
import event.Event;
import event.SendMessageEvent;
import eventmanager.EventQueue;
import gameserver.PlayerHandler;
import gameserver.Response;
import logger.Logger;
import logger.LogLevel;

public class SendMessageEventHandler extends EventQueue implements EventHandler
{
    private PlayerHandler handler;
    
    public SendMessageEventHandler(PlayerHandler handler)
    {
        this.handler = handler;
    }
    
    @Override
    public List<Response> eventToResponse(Event event)
    {
        List<Response> list = new ArrayList<Response>();
        if (event instanceof SendMessageEvent)
        {
            SendMessageEvent sendMessageEvent = (SendMessageEvent) event;
            {
                Response response = new Response();
                response.setResponseCode(ResultCode.Message);
                response.setMessage(sendMessageEvent.getMessage());
                list.add(response);
            }
        }
        return list;
    }
    
    @Override
    protected void processQueuedEvent(Event event)
    {
        if (event instanceof SendMessageEvent)
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
