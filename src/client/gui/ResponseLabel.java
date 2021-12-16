package client.gui;

import java.awt.Color;
import javax.swing.JLabel;

import client.connector.Response;
import client.connector.ResponseCode;
import client.event.Event;
import client.event.ResponseEvent;
import client.event.Subject;
import client.observer.Observer;

/**
 * Label on the map panel that displays outcomes of user commands.
 * Observers ResponseEvents and updates its text with the response message if it is a synchronous response (response 
 * code >= 200).
 */
public class ResponseLabel implements Observer
{
    private JLabel label = new JLabel();

    /**
     * Default constructor.
     * Sets basic formatting for the label
     */
    public ResponseLabel()
    {
        label.setText(" ");
    }

    // Setters and Getters
    public JLabel getLabel() { return label; }

    @Override
    public synchronized void processEvent(Event event)
    {
        if (event.getSubject() == Subject.Response)
        {
            ResponseEvent responseEvent = (ResponseEvent) event;
            Response response = responseEvent.getResponse();
            if (response.getResponseCode().getCode() >= 200)
                setText(response);
        }
    }

    @Override
    public String toString()
    {
        return "ResponseLabel";
    }

    // Sets the label text according to the response received.
    private void setText(Response response)
    {
        if (response.getResponseCode() == ResponseCode.Success)
            formatSuccess();
        else
            formatError();
        label.setText(getMessageText(response.getMessage()));
    }

    // Formats the label for a success message
    private void formatSuccess()
    {
        label.setFont(DefaultFont.getPlain(14));
        label.setForeground(Color.BLACK);
    }

    // Formats the label for an error message
    private void formatError()
    {
        label.setFont(DefaultFont.getBold(14));
        label.setForeground(Color.RED);
    }

    // Extracts the message text from a response message
    // Assumes that a response message is in the form "<response code>:<message>"
    private String getMessageText(String message)
    {
        return message.substring(message.indexOf(":") + 1);
    }
}
