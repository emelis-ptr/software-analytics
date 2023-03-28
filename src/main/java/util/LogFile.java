package util;

import java.util.logging.*;

public class LogFile {
    private LogFile() {
        throw new IllegalStateException("LoggerFile class");
    }

    private static final Logger myLog = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static void setupLogger() {

        LogManager.getLogManager().reset();
        myLog.setLevel(Level.ALL);

        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(Level.FINE);
        myLog.addHandler(ch);

        try {
            FileHandler fh = new FileHandler("log.log", true);
            fh.setLevel(Level.FINE);
            myLog.addHandler(fh);
        } catch (java.io.IOException e) {
            // don't stop my program but log out to console.
            myLog.log(Level.SEVERE, "File logger not working.", e);
        }
        /*
         * Different Levels in order. OFF SEVERE WARNING INFO CONFIG FINE FINER FINEST
         * ALL
         */
    }

    public static void infoLog(String msg) {
        myLog.info(msg);
    }

    public static void errorLog(String msg) {
        myLog.severe(msg);
    }

    public static void warnLog(String msg) {
        myLog.warning(msg);
    }
}
