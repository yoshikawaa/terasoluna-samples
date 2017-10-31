package jp.yoshikawaa.gfw.doma.jdbc;

import java.util.function.Supplier;

import org.seasar.doma.jdbc.AbstractJdbcLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

public class Slf4JJdbcLogger extends AbstractJdbcLogger<Level> {

    private static final Logger logger = LoggerFactory.getLogger(Slf4JJdbcLogger.class);

    public Slf4JJdbcLogger() {
        this(Level.DEBUG);
    }

    protected Slf4JJdbcLogger(Level level) {
        super(level);
    }

    @Override
    protected void log(Level level, String callerClassName, String callerMethodName, Throwable throwable,
            Supplier<String> messageSupplier) {

        switch (level) {
        case ERROR:
            if (logger.isErrorEnabled())
                logger.error(buildLogMessage(callerClassName, callerMethodName, messageSupplier), throwable);
            break;
        case WARN:
            if (logger.isWarnEnabled())
                logger.warn(buildLogMessage(callerClassName, callerMethodName, messageSupplier), throwable);
            break;
        case INFO:
            if (logger.isInfoEnabled())
                logger.info(buildLogMessage(callerClassName, callerMethodName, messageSupplier));
            break;
        case DEBUG:
            if (logger.isDebugEnabled())
                logger.debug(buildLogMessage(callerClassName, callerMethodName, messageSupplier));
            break;
        default:
            if (logger.isTraceEnabled())
                logger.trace(buildLogMessage(callerClassName, callerMethodName, messageSupplier));
            break;
        }
    }

    protected String buildLogMessage(String callerClassName, String callerMethodName,
            Supplier<String> messageSupplier) {

        StringBuilder logMessage = new StringBuilder();
        logMessage.append(messageSupplier.get());
        return logMessage.toString();
    }

}
