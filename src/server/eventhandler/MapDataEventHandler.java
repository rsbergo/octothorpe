package server.eventhandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import server.command.ResultCode;
import server.event.Event;
import server.event.MapDataEvent;
import server.eventmanager.EventQueue;
import server.gameserver.PlayerHandler;
import server.gameserver.Response;
import server.logger.LogLevel;
import server.logger.Logger;

public class MapDataEventHandler extends EventQueue implements EventHandler
{
    private PlayerHandler handler;
    
    public MapDataEventHandler(PlayerHandler handler)
    {
        this.handler = handler;
    }
    
    @Override
    public List<Response> eventToResponse(Event event)
    {
        List<Response> list = new ArrayList<Response>();
        if (event instanceof MapDataEvent)
        {
            MapDataEvent mapDataEvent = (MapDataEvent) event;
            {
                Response response = new Response();
                response.setResponseCode(ResultCode.MapData);
                response.setMessage(mapDataEvent.getMap().getRowsCount() + "," + mapDataEvent.getMap().getColsCount());
                list.add(response);
            }
            
            for (int i = 0; i < mapDataEvent.getMap().getRowsCount(); i++)
            {
                Response response = new Response();
                response.setResponseCode(ResultCode.MapData);
                response.setMessage(mapDataEvent.getMap().getMapRow(i));
                list.add(response);
            }
        }
        return list;
    }
    
    @Override
    protected void processQueuedEvent(Event event)
    {
        if (event instanceof MapDataEvent)
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
