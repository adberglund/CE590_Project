package last; /**
 * Created with IntelliJ IDEA.
 * User: bergl_000
 * Date: 4/26/13
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */

import org.addition.epanet.hydraulic.HydraulicSim;
import org.addition.epanet.network.FieldsMap;
import org.addition.epanet.network.Network;
import org.addition.epanet.network.PropertiesMap;
import org.addition.epanet.util.ENException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Updates the network image each simulation time step to indicate where
 * pressure violations are occurring.
 * Currently not working.
 * @see NetworkDrawer.MyDrawPanel
 */
public class ScenarioAnimator{

    private long hydraulicTime, hydraulicTimeStep, runTime;
    public HydraulicSim hydraulicSimulation;
    Network network;
    public Leak leak;
    public int leakLocation;
    public double emitterCoeff;
    double pressureArray[];
    static FieldsMap fieldsMap;
    static PropertiesMap propertiesMap;
    public String nodeID = new String();
    private String emitterString = new String();
    ArrayList<String> pressureSensors = new ArrayList<>();
    ArrayList<String> pressureViolations = new ArrayList<>();
    NodeIndexIDAssociater nodeIndexIDAssociater;
    NetworkDrawer networkDrawer;

    public ScenarioAnimator(Network network, NetworkDrawer networkDrawer) {
        this.network = network;
        leakLocation = -1;
        emitterCoeff = -1;
        this.networkDrawer = networkDrawer;
        nodeIndexIDAssociater = new NodeIndexIDAssociater(network);
        pressureArray = new double[network.getNodes().size()];
        fieldsMap = network.getFieldsMap();
        propertiesMap = network.getPropertiesMap();

    }

    public ScenarioAnimator(Network network, NetworkDrawer networkDrawer, ArrayList<String> pressureSensors ) {
        this.network = network;
        leakLocation = -1;
        emitterCoeff = -1;
        this.pressureSensors = pressureSensors;
        this.networkDrawer = networkDrawer;
        nodeIndexIDAssociater = new NodeIndexIDAssociater(network);
        pressureArray = new double[pressureSensors.size()];
        fieldsMap = network.getFieldsMap();
        propertiesMap = network.getPropertiesMap();
    }

    public ScenarioAnimator(Network network, NetworkDrawer networkDrawer, int leakLoc, double emitterCoeff ) {
        this.network = network;
        this.leakLocation = leakLoc;
        this.emitterCoeff = emitterCoeff;
        this.networkDrawer = networkDrawer;
        emitterString = "" + emitterCoeff;
        emitterString = emitterString.replace(".",",");

        leak = new Leak(network, leakLocation, this.emitterCoeff);
        nodeIndexIDAssociater = new NodeIndexIDAssociater(network);
        pressureArray = new double[network.getNodes().size()];
        fieldsMap = network.getFieldsMap();
        propertiesMap = network.getPropertiesMap();
    }

    public ScenarioAnimator(Network network, NetworkDrawer networkDrawer, ArrayList<String> pressureSensors, int leakLoc, double emitterCoeff) {
        this.network = network;
        this.leakLocation = leakLoc;
        this.emitterCoeff = emitterCoeff;
        this.networkDrawer = networkDrawer;
        emitterString = "" + emitterCoeff;
        emitterString = emitterString.replace(".",",");
        this.pressureSensors = pressureSensors;
        leak = new Leak(network, leakLocation, this.emitterCoeff);
        nodeIndexIDAssociater = new NodeIndexIDAssociater(network);

        pressureArray = new double[pressureSensors.size()];
        fieldsMap = network.getFieldsMap();
        propertiesMap = network.getPropertiesMap();
    }

    public void animateSimulation() {//throws ENException, IOException {

        Logger log = Logger.getLogger(runnableScenarioSimulator.class.toString());
        log.setUseParentHandlers(false);

        try {
            hydraulicSimulation = new HydraulicSim(network, log);
        } catch (ENException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        if(leak != null)
            leak.setSimulationLeak(hydraulicSimulation);

        runTime = 0;
        hydraulicTimeStep = -1;
        int counter = 0;

        while (hydraulicTimeStep != 0) {

            hydraulicTime = hydraulicSimulation.getHtime();
            pressureViolations.clear();

            try {
                hydraulicTimeStep = hydraulicSimulation.simulateSingleStep();
            } catch (ENException | IOException ioException) {
                ioException.printStackTrace();
            }

            try {
                if(hydraulicTime%propertiesMap.getHstep() == 0){

                    if(pressureSensors.size() == 0){
                        counter = 0;
                        for(int i = 0; i < pressureArray.length; i++){

                            try {
                                pressureArray[i] = hydraulicSimulation.getnNodes().get(i).getSimHead()
                                        - hydraulicSimulation.getnNodes().get(i).getElevation();
                                pressureArray[i] = fieldsMap.revertUnit(FieldsMap.Type.PRESSURE,pressureArray[i]);
                                if(pressureArray[i] < 30){
                                    pressureViolations.add(counter, hydraulicSimulation.getnNodes().get(i).getId());
                                    //System.out.println(pressureViolations.get(counter));
                                    counter++;
                                }
                            } catch (ENException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                    else{
                        counter = 0;
                        for(int i = 0; i < pressureSensors.size(); i++){

                            try {
                                int index = nodeIndexIDAssociater.getNodeIndex(pressureSensors.get(i)) - 1 ;
                                pressureArray[i] = hydraulicSimulation.getnNodes().get(index).getSimHead()
                                        - hydraulicSimulation.getnNodes().get(index).getElevation();
                                pressureArray[i] = fieldsMap.revertUnit(FieldsMap.Type.PRESSURE,pressureArray[i]);
                                if(pressureArray[i] < 30){
                                    pressureViolations.add(counter, hydraulicSimulation.getnNodes().get(i).getId());
                                    counter++;
                                }
                            } catch (ENException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                    networkDrawer.setPressureViolations(pressureViolations);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
            } catch (ENException e) {
                e.printStackTrace();
            }

            if (hydraulicTime >= runTime) {

                try {
                    runTime += network.getPropertiesMap().getRstep();
                } catch (ENException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
