package ar.fdelbo.jpos.custom;

import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.iso.ISOChannel;
import org.jpos.iso.ISOFilter;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.XMLPackager;
import org.jpos.util.LogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Filters incoming/outgoing network messages in order to log the ISO
 */
public class JPOSChannelLoggingFilter implements ISOFilter, Configurable {

    private static final Logger LOGGER = LoggerFactory.getLogger(JPOSChannelLoggingFilter.class);
    private boolean debugMode;
    private String adapterName;

    @Override
    public final ISOMsg filter(final ISOChannel isoChannel,
                         final ISOMsg isoMsg,
                         final LogEvent logEvent) {

        try {
            final var direction = isoMsg.getDirection() == 1 ? "Incoming" : "Outgoing";
            final ISOMsg clone = obfuscate((ISOMsg) isoMsg.clone());
            clone.setPackager(new XMLPackager());
            final var message = new String(clone.pack());

            LOGGER.info("{} {} Msg -> {}", adapterName, direction, message);
        } catch(Exception e) {
            LOGGER.warn("{} Error -> Cannot log incoming/outgoing iso message", adapterName, e);
        }

        return isoMsg;
    }

    @Override
    public void setConfiguration(Configuration cfg) {
        this.debugMode = cfg.getBoolean("debugMode", false);
        this.adapterName = cfg.get("adapterName", "not-configured");
    }

    protected ISOMsg obfuscate(final ISOMsg isoMsg) {
        if(debugMode) {
            return isoMsg;
        }

        final var pan = isoMsg.getString(2);

        if(pan != null) {
            final var builder = new StringBuilder(pan);
            builder.replace(6,12, "******");
            isoMsg.set(2, builder.toString());
        }

        return isoMsg;
    }
}
