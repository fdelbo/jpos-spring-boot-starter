package ar.fdelbo.jpos.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jpos.q2.QBeanSupport;
import org.jpos.util.LogEvent;
import org.jpos.util.LogListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Consumer;

/**
 * Custom listener used to log jpos related logs along with spring's log system
 */
public final class JPOSLogListener implements LogListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(JPOSLogListener.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final Map<String, Consumer<LogEvent>> LOGGERS = Map.of(
            "info", (logEvent) -> LOGGER.info(convertToString(logEvent)),
            "error", (logEvent) -> LOGGER.error(convertToString(logEvent)),
            "warn", (logEvent) -> LOGGER.warn(convertToString(logEvent)),
            "debug", (logEvent) -> LOGGER.debug(convertToString(logEvent)),
            "trace", (logEvent) -> LOGGER.trace(convertToString(logEvent))
    );

    @Override
    public LogEvent log(LogEvent logEvent) {
        final var logger = LOGGERS.getOrDefault(logEvent.getTag(),
                (logEvn) -> LOGGER.info(convertToString(logEvn)));
        logger.accept(logEvent);
        return null;
    }

    /**
     * Tries to convert a jpos log-event to a string so then log it with spring's log system
     * @param logEvent to be converted
     * @return converted log-event
     */
    private static String convertToString(final LogEvent logEvent) {
        try {
            if(logEvent.getPayLoad().size() == 1) {
                final var event = logEvent.getPayLoad().getFirst();
                if(event instanceof QBeanSupport) {
                    return ((QBeanSupport) event).getDump();
                }
            }

            return OBJECT_MAPPER.writeValueAsString(logEvent.getPayLoad());
        } catch (final Exception e) {
            LOGGER.warn("Unable to convert log-event to string", e);
            return logEvent.getPayLoad().toString();
        }
    }
}
