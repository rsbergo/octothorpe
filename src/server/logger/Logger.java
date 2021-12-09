package server.logger;

import java.io.PrintStream;

/**
 * Provides logging functionality.
 */
public class Logger
{
    private static PrintStream out = System.out;
    private static LogLevel level = LogLevel.Debug; // minium log level to be logged
    
    /**
     * Sets the lowest log level to be logged. Higher levels are not logged.
     * Logs are sent to default out.
     * 
     * @param level the lowest level to be logged
     */
    public static void setLogLevel(LogLevel level)
    {
        Logger.setLogLevel(level, System.out);
    }
    
    /**
     * Sets the lowest log level to be logged. Higher levels are not logged.
     * Sets the log output stream.
     * 
     * @param level the lowest level to be logged
     */
    public static void setLogLevel(LogLevel level, PrintStream out)
    {
        Logger.out = out;
        Logger.level = level;
    }
    
    /**
     * Prints message into standard output
     * 
     * @param message
     */
    public static void log(LogLevel level, String message)
    {
        if (level.compareTo(Logger.level) >= 0)
        {
            StackTraceElement trace = Thread.currentThread().getStackTrace()[2];
            
            StringBuilder sb = new StringBuilder();
            sb.append(trace.getFileName() + "(" + trace.getLineNumber() + ") - ");
            sb.append(Thread.currentThread().getName() + " - ");
            sb.append(level + ": " + message);
            out.println(sb.toString());
        }
    }
    
    public static void log(LogLevel level, String message, Exception e)
    {
        if (level.compareTo(Logger.level) >= 0)
        {
            StackTraceElement trace = Thread.currentThread().getStackTrace()[2];
            
            StringBuilder sb = new StringBuilder();
            sb.append(trace.getFileName() + "(" + trace.getLineNumber() + ") - ");
            sb.append(Thread.currentThread().getName() + " - ");
            sb.append(level + ": " + message);
            sb.append("\r\n" + e.getMessage());
            out.println(sb.toString());
            e.printStackTrace(System.out);
        }
    }
}
