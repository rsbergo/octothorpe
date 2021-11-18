package logger;

/**
 * Provides logging functionality.
 * 
 */
public class Logger
{
    // TODO: Add log level
    // TODO: Implement addConsoleLogger() to initialize logger to standard out.
    // TODO: Implement addFileLogger(File) to initialize logger to a file.
    // TODO: Add thread name to log
    // TODO: Add file to log
    // TODO: Add line number to log 

    /**
     * Prints message into standard output
     * @param message
     */
    public static void log(String message)
    {
        System.out.println("Log: " + message);
    }
}
