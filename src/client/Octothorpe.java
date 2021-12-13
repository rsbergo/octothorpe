package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import client.gameclient.GameClient;
import logger.LogLevel;
import logger.Logger;

public class Octothorpe
{
    private static final String LOG_FOLDER = "out";
    public static void main(String[] args)
    {
        if (args.length < 2)
        {
            System.err.println("Usage: java Octothorpe <host> <port number>");
            System.exit(1);
        }

        try (BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
             PrintStream logger = new PrintStream(new File(getLogFileName())))
        {
            Logger.setLogLevel(LogLevel.Debug, logger);
            GameClient game = new GameClient();
            game.run(args[0], getPort(args[1]), stdIn);
        }
        catch (IOException e)
        {
            System.err.println("It seems that something went wrong...");
            e.printStackTrace();
        }
    }

    // Returns the file name for the log file.
    private static String getLogFileName()
    {
        String dateString = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        return (LOG_FOLDER + "/" + "log_" + dateString + ".log");
    }

    // Gets the port number from command line arguments
    private static int getPort(String portString)
    {
        try
        {
            int port = Integer.parseInt(portString);
            if (port < 0 || port > 65535)
            {
                System.err.println("Port value out of range: " + port);
                System.err.println("Port number should be a number in the range [0,65535]");
                System.exit(1);
            }
            return port;
        }
        catch (NumberFormatException e)
        {
            System.err.println("Invalid value for port: \"" + portString + "\"");
            System.err.println("Port number should be a number in the range [0,65535]");
            System.exit(1);
        }
        return 0;
    }
}
