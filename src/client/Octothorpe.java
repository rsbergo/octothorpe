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
        try (BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
             PrintStream logger = new PrintStream(new File(getLogFileName())))
        {
            Logger.setLogLevel(LogLevel.Debug, logger);
            GameClient game = new GameClient();
            game.run("172.31.178.59", 7777, stdIn);
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
}
