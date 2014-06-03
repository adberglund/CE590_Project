package last.old;

import org.addition.epanet.network.Network;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: bergl_000
 * Date: 1/27/13
 * Time: 7:07 PM
 * To change this template use File | Settings | File Templates.
 */

public class WDSMain {

    /*public static void main(String[] args) {


        NetworkLoader networkLoader;
        networkLoader = new NetworkLoader();
        Network network;
        network = new Network();

        File inputFile;
        inputFile = new File("C:\\Users\\bergl_000\\IdeaProjects\\WDSLeakDetection\\hanoi-1.inp");
        //inputFile = new File("C:\\Users\\bergl_000\\IdeaProjects\\WDSLeakDetection\\Micropolis_NoWarnings2.inp");

        network = networkLoader.loadNetwork(inputFile);

        ExecutorService threadPool = Executors.newCachedThreadPool();
        Leak leak;
        double time1 = System.currentTimeMillis();

        for (int i = 0; i < 10; i++){

            //leak = new Leak(network,5,1);
            //leak.setLeak();

            //System.out.println("Leak: " + leak.getEmitterCoefficient());
            //System.out.println("Leak ID: " + leak.getLeakLocationID());
            //System.out.println("Leak: " + network.getNode(leak.getLeakLocationID()).getKe());

            //for( Node node:network.getNodes() ){
            //    System.out.println("Inside Main: " + "Node: " + node.getId() + " Ke: " + node.getKe());
            //}

            threadPool.submit(new runnableScenarioSimulator(network, i+1, Math.random()*100));
            //System.out.println(network == runnyTheSimulationRunner.network);

            //leak.fixLeak();
        }
        double time2 = System.currentTimeMillis();
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        //System.out.println("Terminated? : " + threadPool.isTerminated() + " shut down?: " + threadPool.isShutdown());

        while(!threadPool.isShutdown()){

        }

        System.out.println("Time diff: " + ((time2 - time1)/1000.0));
        //System.out.println("Main Network: " + network.hashCode());
        //simulationRunner.run();

        //NetworkDrawer drawNetwork = new NetworkDrawer(network);
        //drawNetwork.drawNetwork();

    }*/
}
