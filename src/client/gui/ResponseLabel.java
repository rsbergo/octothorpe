package client.gui;

import java.awt.Color;
import javax.swing.JLabel;

import client.connector.Response;
import client.connector.ResponseCode;

/**
 * Label on the map panel that displays outcomes of user commands.
 */
public class ResponseLabel
{
    private JLabel label = new JLabel();

    /**
     * Default constructor.
     * Sets basic formatting for the label
     */
    public ResponseLabel()
    {
        label.setFont(DefaultFont.getBold(14));
        label.setText(" ");
    }

    // Setters and Getters
    public JLabel getLabel() { return label; }

    /**
     * Sets the label text according to the response received.
     * 
     * @param response the response received from the game server
     */
    public void setText(Response response)
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
        label.setForeground(Color.BLACK);
    }

    // Formats the label for an error message
    private void formatError()
    {
        label.setForeground(Color.RED);
    }

    // Extracts the message text from a response message
    // Assumes that a response message is in the form "<response code>:<message>"
    private String getMessageText(String message)
    {
        return message.substring(message.indexOf(":") + 1);
    }
}
