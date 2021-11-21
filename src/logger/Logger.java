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
    
    private static LogLevel level; // minium log level to be logged
    
    /**
     * Sets the lowest log level to be logged.
     * Higher levels are not logged.
     * 
     * @param level the lowest level to be logged
     */
    public static void setLogLevel(LogLevel level)
    {
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
            System.out.println(sb.toString());
        }
    }
}
