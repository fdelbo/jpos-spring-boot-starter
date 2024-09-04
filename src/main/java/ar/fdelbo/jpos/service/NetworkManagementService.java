package ar.fdelbo.jpos.service;

import org.jpos.iso.ISODate;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Date;

/**
 * Manages network echo-test/sign-on/sign-off messages (see 0800 messages documentation for more info)
 */
public class NetworkManagementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkManagementService.class);
    private static final String ECHO_MESSAGE_MTI = "0800";
    private static final String SIGN_ON_INFORMATION_CODE = "061";
    private static final String SIGN_OFF_INFORMATION_CODE = "062";

    private final JposService jposService;

    public NetworkManagementService(final JposService jposService) {
        this.jposService = jposService;
    }

    /**
     * Performs a network sign-on in order to connect and be able to send transactions to the network
     * @return true if successful, false otherwise
     */
    public boolean signOn() {
        return performSignInOrSignOff(SIGN_ON_INFORMATION_CODE);
    }

    /**
     * Performs a network sign-off in order to disconnect from the network
     * @return true if successful, false otherwise
     */
    public boolean signOff() {
        return performSignInOrSignOff(SIGN_OFF_INFORMATION_CODE);
    }

    /**
     * Given a fixed delay time, this service sends echo-test messages to the network in order to keep
     * the connection up and running.
     *
     * This service is based on documentation "Network Connection Status/Member Generated" from mastercard documentation
     */
    public void sendEchoTest() {
        try {
            final ISOMsg request = buildEchoTestRequest();
            final ISOMsg response = jposService.performRequest(request);
            final var responseCode = response.getString(39);

            if(!"00".equals(responseCode)) {
                LOGGER.error("Echo response hasn't been successful: {}", responseCode);
            }

        } catch (Exception e) {
            LOGGER.error("Error sending echo to network", e);
        }
    }

    private boolean performSignInOrSignOff(final String informationCode) {
        try {
            final ISOMsg request = buildSignOnSignOffRequest(informationCode);
            final ISOMsg response = jposService.performRequest(request);

            return "00".equals(response.getString(39));
        } catch (ISOException e) {
            LOGGER.error("Error performing sign-on/sign-off request", e);
            return false;
        }
    }

    private ISOMsg buildSignOnSignOffRequest(final String informationCode) throws ISOException {
        final var transmissionDateTime = Date.from(Instant.now());
        final String currentTimeMs = String.valueOf(System.currentTimeMillis());
        final ISOMsg request = new ISOMsg();

        request.setMTI(ECHO_MESSAGE_MTI);
        //request.set(2, );
        request.set(7, ISODate.getDateTime(transmissionDateTime));
        request.set(11, currentTimeMs.substring(currentTimeMs.length() - 6)); //system-trace-audit-number
        request.set(33, "011051");
        request.set(70, informationCode);
        //request.set(94, );
        //request.set(96, );
        return request;
    }

    private ISOMsg buildEchoTestRequest() throws ISOException {
        final var transmissionDateTime = Date.from(Instant.now());
        final String currentTimeMs = String.valueOf(System.currentTimeMillis());
        final ISOMsg request = new ISOMsg();

        request.setMTI(ECHO_MESSAGE_MTI);
        //request.set(2, );
        request.set(7, ISODate.getDateTime(transmissionDateTime));
        request.set(11, currentTimeMs.substring(currentTimeMs.length() - 6));
        request.set(33, "011051");
        request.set(70, "270");
        return request;
    }


}
