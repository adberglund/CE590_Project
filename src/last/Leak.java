package last;

import org.addition.epanet.hydraulic.HydraulicSim;
import org.addition.epanet.network.Network;


/**
 * Created with IntelliJ IDEA.
 * User: bergl_000
 * Date: 2/4/13
 * Time: 11:59 AM
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class holds leak information and can send leak information
 * to the network being simulated.
 */
public class Leak {

    public int leakLocation;
    public String leakLocationID;
    public double emitter;
    public Network network;
    NodeIndexIDAssociater iDConverter;

    /**
     * Leak constructor sets basic information about a leak
     * using  node indices
     * @param net The network in which a leak is being simulated
     * @param nodeIndex The node index where a leak is being simulated
     * @param emitterCoefficient The coefficient used to calculate the magnitude of the leak
     */
    public Leak (Network net, int nodeIndex, double emitterCoefficient){

        network = net;
        iDConverter = new NodeIndexIDAssociater(network);
        leakLocation = nodeIndex;
        leakLocationID = iDConverter.getNodeID(leakLocation);
        emitter = emitterCoefficient;
    }

    /**
     * Leak constructor sets basic information about a leak
     * using  node IDs
     * @param net The network in which a leak is being simulated
     * @param nodeID The node ID taken from the EPANET input file
     * @param emitterCoefficient The coefficient used to calculate the magnitude of the leak
     */
    public Leak (Network net, String nodeID, double emitterCoefficient){

        network = net;
        iDConverter = new NodeIndexIDAssociater(network);
        leakLocation = iDConverter.getNodeIndex(nodeID);
        leakLocationID = nodeID;
        emitter = emitterCoefficient;
    }

    /**
     * Getter method to find the location of a leak
     * @return the index of the node at which a leak is simulated
     */
    public int getLeakLocationIndex(){

        return leakLocation;
    }

    /**
     * Getter method to find the location of a leak
     * @return the ID of the node at which a leak is simulated
     */
    public String getLeakLocationID(){

        return leakLocationID;
    }

    /**
     * Getter method to find the emitter coefficient of a leak
     * @return the emitter coefficient of a leak
     */
    public double getEmitterCoefficient(){

        //return hydraulicSim.getnNodes().get(leakLocation).getSimEmitter();
        return network.getNode(leakLocationID).getKe();
    }

    /**
     * Setter method sets the emitter coefficient in the instantiated Network
     */
    public void setLeak(){

        network.getNode(leakLocationID).setKe(emitter);
    }

    /**
     * Setter method sets the emitter coefficient for a unique simulation.
     * Does not alter the original Network
     * @param hydraulicSim holds information for a simulation and runs the hydraulic simulation
     */
    public void setSimulationLeak(HydraulicSim hydraulicSim){

        hydraulicSim.getnNodes().get(leakLocation - 1).setSimEmitter(emitter);
    }

    /**
     * Setter method to set an emitter coefficient back to zero
     */
    public void fixLeak(){

        emitter = 0;
        network.getNode(leakLocationID).setKe(emitter);
    }
}
