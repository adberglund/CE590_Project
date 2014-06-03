package last;

import last.io.CSVWriter;
import org.addition.epanet.hydraulic.HydraulicSim;
import org.addition.epanet.network.FieldsMap;
import org.addition.epanet.network.Network;
import org.addition.epanet.network.PropertiesMap;
import org.addition.epanet.util.ENException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: bergl_000
 * Date: 2/4/13
 * Time: 1:07 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * Scenario simulation class designed for multi-threaded simulations
 */
public class runnableScenarioSimulator implements Runnable {

    private HydraulicSim hydraulicSimulation;
    private Leak leak;
    private CSVWriter csvWriter;
    static Network network;
    static NodeIndexIDAssociater nodeIndexIDAssociater;
    static FieldsMap fieldsMap;
    static PropertiesMap propertiesMap;

    long hydraulicTime, hydraulicTimeStep, runTime;
    int leakLocation;
    double emitterCoeff;
    double pressureArray[];
    String nodeID = new String();
    String emitterString = new String();
    ArrayList<String> pressureSensors = new ArrayList<>();

    /**
     * Constructor for the base case scenario with no leaks.
     * where all nodes report pressure
     * @param network EPANET network
     * @param filePath The path of the directory where output is stored
     */
    public runnableScenarioSimulator(Network network, String filePath) {
        this.network = network;
        leakLocation = -1;
        emitterCoeff = -1;

        csvWriter = new CSVWriter(filePath + "\\Base Case.csv");
        nodeIndexIDAssociater = new NodeIndexIDAssociater(network);
        pressureArray = new double[network.getNodes().size()];
        fieldsMap = network.getFieldsMap();
        propertiesMap = network.getPropertiesMap();
    }

    /**
     * Constructor for the base case scenario with no leaks where
     * only nodes selected as pressure sensors report pressure.
     * @param network EPANET network
     * @param pressureSensors ArrayList of node IDs established as pressure sensors
     * @param filePath The path of the directory where output is stored
     */
    public runnableScenarioSimulator(Network network, ArrayList<String> pressureSensors, String filePath) {
        this.network = network;
        leakLocation = -1;
        emitterCoeff = -1;
        this.pressureSensors = pressureSensors;

        csvWriter = new CSVWriter(filePath + "\\Base Case with Pressure Sensors.csv");
        nodeIndexIDAssociater = new NodeIndexIDAssociater(network);
        pressureArray = new double[pressureSensors.size()];
        fieldsMap = network.getFieldsMap();
        propertiesMap = network.getPropertiesMap();
    }

    /**
     * Constructor for a leak scenario where all nodes report pressure
     * @param network EPANET network
     * @param leakLoc index of the leak node
     * @param emitterCoeff Emitter coefficient used to calculate leak magnitude
     * @param filePath The path of the directory where output is stored
     */
    public runnableScenarioSimulator(Network network, int leakLoc, double emitterCoeff, String filePath) {

        this.network = network;
        this.leakLocation = leakLoc;
        this.emitterCoeff = emitterCoeff;
        emitterString = "" + emitterCoeff;
        emitterString = emitterString.replace(".",",");

        leak = new Leak(network, leakLocation, this.emitterCoeff);
        csvWriter = new CSVWriter(filePath + "\\Node_" + (leakLocation+1) + "_Emitter_" + emitterString +".csv");
        nodeIndexIDAssociater = new NodeIndexIDAssociater(network);
        pressureArray = new double[network.getNodes().size()];
        fieldsMap = network.getFieldsMap();
        propertiesMap = network.getPropertiesMap();
    }

    /**
     * Constructor for a leak scenario with pressure sensors selected.
     * @param network EPANET network
     * @param pressureSensors ArrayList of node IDs established as pressure sensors
     * @param leakLoc index of the leak node
     * @param emitterCoeff Emitter coefficient used to calculate leak magnitude
     * @param filePath The path of the directory where output is stored
     */
    public runnableScenarioSimulator(Network network, ArrayList<String> pressureSensors, int leakLoc,
                                     double emitterCoeff, String filePath) {

        this.network = network;
        this.leakLocation = leakLoc;
        this.emitterCoeff = emitterCoeff;
        this.pressureSensors = pressureSensors;
        emitterString = "" + emitterCoeff;
        emitterString = emitterString.replace(".",",");
        pressureArray = new double[pressureSensors.size()];

        leak = new Leak(network, leakLocation, this.emitterCoeff);
        csvWriter = new CSVWriter(filePath + "\\Node_" + (leakLocation+1) + "_Emitter_" + emitterString +".csv");
        nodeIndexIDAssociater = new NodeIndexIDAssociater(network);
        fieldsMap = network.getFieldsMap();
        propertiesMap = network.getPropertiesMap();
    }

    /**
     * Run method for the Runnable implementation.
     * Calls runSimulation()
     */
    public void run(){
        runSimulation();
    }

    /**
     * Runs a complete hydraulic simulation.
     * If the simulation run includes a leak, the leak is set
     * and a hydraulic simulation is instantiated. The
     * simulation proceeds, generating pressure output for
     * each time step.
     */
    public void runSimulation() {

        Logger log = Logger.getLogger(runnableScenarioSimulator.class.toString());
        log.setUseParentHandlers(false);

        if(pressureSensors.size() == 0){
            String[] nodeNames = new String[network.getNodes().size()];

            for(int i = 0; i < network.getNodes().size(); i++){
                nodeID = nodeIndexIDAssociater.getNodeID(i);
                nodeNames[i] = nodeID;
            }

            csvWriter.writeOutput(nodeNames);
        }

        if(pressureSensors.size() > 0){
            String[] nodeNames = new String[pressureSensors.size()];

            for(int i = 0; i < pressureSensors.size(); i++){
                nodeID = pressureSensors.get(i);
                nodeNames[i] = nodeID;
            }

            csvWriter.writeOutput(nodeNames);
        }

        try {
            hydraulicSimulation = new HydraulicSim(network, log);
        } catch (ENException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        if(leak != null)
            leak.setSimulationLeak(hydraulicSimulation);

            //System.out.println("leak mag: " + leak.getEmitterCoefficient() + " network Ke: " +
            // network.getNode(leak.getLeakLocationID()).getKe());
            //leak.fixLeak();
            //System.out.println("leak mag: " + leak.getEmitterCoefficient() + " network Ke: " +
            // network.getNode(leak.getLeakLocationID()).getKe());
        //}

        /* try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }*/
        /*for (SimulationNode node: hydraulicSimulation.getnNodes()){
            //System.out.println("Sim Node: " + node.getId() + " " + node.getSimEmitter());
            Node node2;
            node2 = node.getNode();
            //System.out.println("Network node: " + node2.getId() + " " + node2.getKe());
            if(node.getSimEmitter() != 0){
                System.out.println("Thread: " + Thread.currentThread().getName() +
                " Node ID: " + node.getId() + " emitter: " + node.getSimEmitter() + " Ke: " + node.getKe());
            }
        }*/
        //System.out.println("Thread: " + Thread.currentThread().getName() +
        // " Leak: " + leak.getLeakLocationID() + " mag: " +
        // hydraulicSimulation.getnNodes().get(leakLocation - 1).getKe());

        runTime = 0;
        hydraulicTimeStep = -1;
        int counter = 0;

        while (hydraulicTimeStep != 0) {

            hydraulicTime = hydraulicSimulation.getHtime();
            //System.out.println("Hydraulic Time: " + hydraulicTime);

            try {
                hydraulicTimeStep = hydraulicSimulation.simulateSingleStep();
            } catch (ENException | IOException ioException) {
                ioException.printStackTrace();
            }

            try {
                if(hydraulicTime%propertiesMap.getHstep() == 0){

                    if(pressureSensors.size() == 0){

                        for(int i = 0; i < pressureArray.length; i++){

                            try {
                                pressureArray[i] = hydraulicSimulation.getnNodes().get(i).getSimHead()
                                        - hydraulicSimulation.getnNodes().get(i).getElevation();
                                pressureArray[i] = fieldsMap.revertUnit(FieldsMap.Type.PRESSURE,pressureArray[i]);
                            } catch (ENException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    else{
                        for(int i = 0; i < pressureSensors.size(); i++){

                            try {
                                int index = nodeIndexIDAssociater.getNodeIndex(pressureSensors.get(i)) - 1 ;
                                pressureArray[i] = hydraulicSimulation.getnNodes().get(index).getSimHead()
                                        - hydraulicSimulation.getnNodes().get(index).getElevation();
                                pressureArray[i] = fieldsMap.revertUnit(FieldsMap.Type.PRESSURE,pressureArray[i]);
                            } catch (ENException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                    //CSV Output for simulation
                    csvWriter.writeOutput(pressureArray);
                }
            } catch (ENException e) {
                e.printStackTrace();
            }

            if (hydraulicTime >= runTime) {

                try {
                    runTime += network.getPropertiesMap().getRstep();
                } catch (ENException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
        csvWriter.closeFile();
    }
}
