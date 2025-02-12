package util.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalLogger {
    private static final Logger logger = LoggerFactory.getLogger(GlobalLogger.class);

    private GlobalLogger() {
    }

    public static Logger getLogger() {
        return logger;
    }
}
