package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import client.gameclient.GameClient;
import client.gui.ClientGUI;
import logger.LogLevel;
import logger.Logger;

public class Octothorpe
{
    private static final String LOG_FOLDER = "log";
    public static void main(String[] args)
    {
        if (args.length < 2)
        {
            System.err.println("Usage: java Octothorpe <host> <port number>");
            System.exit(1);
        }

        try (BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
             PrintStream logger = new PrintStream(createLogFile()))
        {
            Logger.setLogLevel(LogLevel.Info, logger);
            GameClient client = new GameClient(args[0], getPort(args[1]));
            ClientGUI gui = new ClientGUI(client);
            gui.start();
        }
        catch (IOException e)
        {
            System.err.println("It seems that something went wrong...");
            e.printStackTrace();
        }
    }

    // Creates the logger PrintStream.
    // Attempts to initialize a log file. If not possible, return stdout.
    private static PrintStream createLogFile()
    {
        PrintStream out = new PrintStream(System.out);
        try
        {
            String dateString = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String fileName = LOG_FOLDER + "/" + "log_" + dateString + ".log";
            Files.createDirectories(Paths.get(LOG_FOLDER));
            out = new PrintStream(fileName);
        }
        catch (IOException e)
        {
            System.err.println("It was not possible to create log file...");
            e.printStackTrace();
        }
        return out;
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
