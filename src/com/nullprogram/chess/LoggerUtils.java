package com.nullprogram.chess;

import java.util.logging.Logger;

/**
 * Allows Loggers to be created without declaring their class name.
 */
public class LoggerUtils extends SecurityManager {
    /** Singleton instance of the SecurityManager. */
    private static final LoggerUtils INSTANCE = new LoggerUtils();

    /**
     * Determine's the calling class's name and returns its logger.
     *
     * @return the requested logger
     */
    public static Logger getLogger() {
        String className = INSTANCE.getClassName();
        Logger logger = Logger.getLogger(className);
        return logger;
    }

    /**
     * Determine's the calling class's name.
     *
     * @return the calling class's name
     */
    private String getClassName() {
        return getClassContext()[2].getName();
    }
}
