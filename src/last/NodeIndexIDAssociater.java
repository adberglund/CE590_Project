package last;

import org.addition.epanet.network.Network;
import org.addition.epanet.network.structures.Node;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: bergl_000
 * Date: 2/15/13
 * Time: 11:16 AM
 * To change this template use File | Settings | File Templates.
 */

/**
 * Helper class that converts between a node's index and ID
 */
public class NodeIndexIDAssociater {

    protected ArrayList<String> nodeIDArray = new ArrayList<>();
    protected Network network;
    static String[] nodeNames;
    int counter = 0;

    /**
     * Constructor that establishes the relationship between the indices and
     * IDs of all nodes in a network
     * <p></p>
     * Nodes are represented by their IDs within EPANET;
     * however, it is often useful to iterate over the set of nodes
     * by index.
     * @param net An EPANET network
     */
    public NodeIndexIDAssociater(Network net){

        network = net;
        counter = 0;
        nodeNames = new String[network.getNodes().size()];

        for(Node node:network.getNodes()){
            nodeIDArray.add(counter, node.getId());
            nodeNames[counter] = node.getId();
            counter++;
        }
    }

    /**
     * Getter method to retrieve the ID of a node
     * @param nodeIndex the index of the node of interest
     * @return the ID string of the node of interest
     */
    public String getNodeID(int nodeIndex){

        //EPANET begins indexing at 1, use EPANET index for this method (hence the -1 in the return value)
        return nodeNames[nodeIndex];
    }

    /**
     * Getter method to retrieve the index of a node
     * @param nodeID the ID string of the node of interest
     * @return the index of the node of interest
     */
    public int getNodeIndex(String nodeID){

        return  nodeIDArray.indexOf(nodeID);
    }

    /**
     * Prints the list of nodes by index and ID.
     * Used mostly for testing
     */
    public void printNodeList(){

        for(String string:nodeIDArray){
            System.out.println("Node Index: " + (nodeIDArray.indexOf(string) + 1) + "\t\t" + "Node ID: " + string);
        }
    }

    /**
     * Prints information for a particular node
     * @param nodeIndex the index of the node of interest
     */
    public void printNode(int nodeIndex){

        System.out.println("Node Index: " + nodeIndex + "\t\t" + "Node ID: " + nodeIDArray.get(nodeIndex));
    }

    /**
     * Prints information for a particular node
     * @param nodeID the ID of the node of interest
     */
    public void printNode(String nodeID){

        System.out.println("Node Index: " + nodeIDArray.indexOf(nodeID) + "\t\t" + "Node ID: " + nodeID);
    }
}