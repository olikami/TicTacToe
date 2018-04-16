package model.net.port;

import org.bitlet.weupnp.GatewayDevice;
import org.bitlet.weupnp.GatewayDiscover;
import org.bitlet.weupnp.PortMappingEntry;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class PortForward {

    public static void main(String[] args) {

        new PortForward();
        System.exit(1);

    }
    int SAMPLE_PORT = 12345;
    int WAIT_TIME = 10;

    PortForward() {

        Logger logger = Logger.getLogger("InfoLogging");

        logger.info("Logging an INFO-level message");

        try {


            GatewayDiscover discover = new GatewayDiscover();
            logger.info("Looking for Gateway Devices");
            discover.discover();

            GatewayDevice d = discover.getValidGateway();
            if (null != d) {
                logger.info("Found gateway device.\n{0} ({1})" +
                        Arrays.toString(new Object[]{d.getModelName(), d.getModelDescription()}));
            } else {
                logger.info("No valid gateway device found.");
                return;
            }

            InetAddress localAddress = d.getLocalAddress();
            logger.info("Using local address: {0}  "+ localAddress);
            String externalIPAddress = d.getExternalIPAddress();
            logger.info("External address: {0}  "+ externalIPAddress);

            logger.info("Attempting to map port {0} "+ SAMPLE_PORT);
            PortMappingEntry portMapping = new PortMappingEntry();

            logger.info("Querying device to see if mapping for port {0} already exists"+
                    SAMPLE_PORT);
            if (!d.getSpecificPortMappingEntry(SAMPLE_PORT,"TCP",portMapping)) {
                logger.info("Port was already mapped. Aborting test.");
            } else {
                logger.info("Sending port mapping request");
                if (!d.addPortMapping(SAMPLE_PORT, SAMPLE_PORT,
                        localAddress.getHostAddress(),"TCP","test")) {
                    logger.info("Port mapping attempt failed");
                    logger.info("Test FAILED");
                } else {        logger.info("Mapping successful: waiting {0} seconds before removing."+
                        WAIT_TIME);
                    Thread.sleep(1000*WAIT_TIME);
                    d.deletePortMapping(SAMPLE_PORT,"TCP");

                    logger.info("Port mapping removed");
                    logger.info("Test SUCCESSFUL");
                }
            }

            logger.info("Stopping weupnp");





        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
