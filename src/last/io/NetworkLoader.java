package last.io;

import org.addition.epanet.network.Network;
import org.addition.epanet.network.io.input.InputParser;
import org.addition.epanet.util.ENException;

import java.io.File;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: bergl_000
 * Date: 2/4/13
 * Time: 1:17 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * A class providing a simplified way of instantiating an EPANET network.
 * <p></p>
 * The network creation scheme in the underlying Java EPANET implementation
 * requires multiple steps. This class provides a convenient one method call for
 * instantiating a network.
 */
public class NetworkLoader {

    public NetworkLoader(){

    }

    /**
     * Takes an INP file and parses input data into a network object
     * @param inputFile An EPANET input file
     * @return the Configured Network object
     */
    public Network loadNetwork(File inputFile){

        Network network = new Network();
        Logger LOGGER;
        LOGGER = Logger.getLogger(NetworkLoader.class.getName());
        InputParser inpParser = InputParser.create(Network.FileType.INP_FILE, LOGGER);

        try {
            inpParser.parse(network, inputFile);
        } catch (ENException en_ex) {
            inputFile = null;
            return null;
        }

        return network;
    }
}