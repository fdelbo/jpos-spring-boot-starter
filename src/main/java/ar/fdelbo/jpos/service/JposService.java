package ar.fdelbo.jpos.service;

import ar.fdelbo.jpos.exception.JPosException;
import org.jpos.iso.ISOMsg;
import org.jpos.q2.iso.QMUX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * jPOS services
 */
public class JposService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JposService.class);

    private final Long networkTimeout;
    private final QMUX jposMux;

    public JposService(final Long networkTimeout, final QMUX mux) {
        this.networkTimeout = networkTimeout;
        this.jposMux = mux;
    }

    /**
     * Performs a request to jpos's mux.
     * Sending a message to this mux means jpos will multiplex it before send it to
     * the network.
     * @param isoMsg to be sent
     * @return isoMsg as the response
     */
    public final ISOMsg performRequest(final ISOMsg isoMsg) {
        try {
            return jposMux.request(isoMsg, networkTimeout);
        } catch (Exception e) {
            LOGGER.error("jPOS error while performing request to the network", e);
            throw new JPosException("jPOS error while performing request to the network", e);
        }
    }
}
