package last;

import last.NodeIndexIDAssociater;
import org.addition.epanet.network.Network;
import org.addition.epanet.network.structures.Link;
import org.addition.epanet.network.structures.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: bergl_000
 * Date: 3/25/13
 * Time: 1:12 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * Class used to draw the water distribution network being simulated
 * from within the GUI
 */
public class NetworkDrawer {

    private static final Color pressureSensorColor = new Color(30,140,20);
    private static final Color pressureViolationColor = new Color(150,25,25);
    private static final Color leakColor = new Color(255,50,0);

    static NodeIndexIDAssociater nodeIndexIDAssociater;
    static MyDrawPanel drawPanel = new MyDrawPanel();
    static NetworkKey networkKey = new NetworkKey();
    static Network network;

    static Point2D.Double point2DOne = new Point2D.Double();
    static Point2D.Double point2DTwo = new Point2D.Double();

    static ArrayList<String> pressureViolations = new ArrayList<>();
    static ArrayList<Point2D> nodeArrayList = new ArrayList<>();
    static ArrayList<String> pressureSensors = new ArrayList<>();
    static HashMap<Link, ArrayList<Point2D>> linkNodeHashMap = new HashMap<>();

    static double maxX = 0;
    static double maxY = 0;
    static double minX = 0;
    static double minY = 0;
    static double networkWidth = 0;
    static double networkHeight = 0;
    static double panelWidth = 0;
    static double panelHeight = 0;
    static double widthRatio = 0;
    static double heightRatio = 0;
    static double displayAdjustFactor = 0;
    static int leakLocation = -1;

    /**
     * Constructor for the NetworkDrawer
     * @param net The network being modeled
     */
    public NetworkDrawer(Network net){

        network = net;
        nodeIndexIDAssociater = new NodeIndexIDAssociater(network);

    }

    /**
     * Stores point information for the nodes and links in the network
     */
    static void configureNodesAndLinks(){

        normalizeCoordinates();

        for (Node node: network.getNodes()){
            Point2D tempPoint = new Point2D.Double();
            tempPoint.setLocation((node.getPosition().getX() + minX) , (node.getPosition().getY() + minY) );
            nodeArrayList.add(tempPoint);
        }

        for (Link link: network.getLinks()){
            ArrayList<Point2D> vertices = new ArrayList<>();
            Point2D point1 = new Point2D.Float();
            Point2D point2 = new Point2D.Float();
            point1.setLocation(link.getFirst().getPosition().getX() + minX, link.getFirst().getPosition().getY() + minY);
            point2.setLocation(link.getSecond().getPosition().getX() + minX, link.getSecond().getPosition().getY() + minY);
            vertices.add(point1);
            vertices.add(point2);
            linkNodeHashMap.put(link,vertices);
        }

    }

    /**
     * Determines the maximum and minimum x and y coordinates
     * and the height and width of a network
     * <p></p>
     * This is necessary as EPANET allows negative coordinates.
     * The drawn network must be shifted to begin at (0,0) to
     * display properly.
     */
    static void normalizeCoordinates(){

        for (Node node: network.getNodes()){
            double xCoord = node.getPosition().getX();
            double yCoord = node.getPosition().getY();
            if (xCoord > maxX)
                maxX = xCoord;
            if (yCoord > maxY)
                maxY = yCoord;
            if (xCoord < minX)
                minX = xCoord;
            if (yCoord < minY)
                minY = yCoord;
        }

        networkWidth = maxX - minX;
        networkHeight = maxY - minY;

        minX = Math.abs(minX);
        maxX = Math.abs(maxX);
        minY = Math.abs(minY);
        maxY = Math.abs(maxY);
    }

    /**
     * Updates the network image to show the location of the current leak
     * @param leakLocation index of the node at which a leak is simulated
     */
    public void setLeak(int leakLocation){
        this.leakLocation = leakLocation;
        drawPanel.repaint();
    }

    /**
     * Updates the network image to show the location of all pressure sensors
     * @param pressureSensors an ArrayList of node IDs being considered as pressure sensors
     */
    public void setPressureSensors(ArrayList<String> pressureSensors){
        this.pressureSensors = pressureSensors;
        drawPanel.repaint();
    }

    /**
     * Updates the network image to show which nodes are reporting pressure violations
     * at the current time step.
     * @param pressureViolations an ArrayList of node IDs for all nodes that report a pressure below
     *                           a specified pressure threshold
     */
    public void setPressureViolations(ArrayList<String> pressureViolations){
        this.pressureViolations = pressureViolations;
        drawPanel.repaint();
    }

    /**
     * Configures and draws the network in an instance of the modified JPanel
     * class, MyDrawPanel
     * @see MyDrawPanel
     */
    public void drawNetwork(){

        linkNodeHashMap.clear();
        nodeArrayList.clear();
        configureNodesAndLinks();
        drawPanel.repaint();

    }

    /**
     * Getter method for the instantiated inner class, MyDrawPanel
     * @return the currently instantiated MyDrawPanel
     * @see MyDrawPanel
     */
    public MyDrawPanel getDrawPanel(){
        return this.drawPanel;
    }

    /**
     * Getter method for the instantiated inner class, NetworkKey
     * @return the currently instantiated NetworkKey
     * @see NetworkKey
     */
    public NetworkKey getNetworkKey(){
        return this.networkKey;
    }

    /**
     * Inner class that draws an image of a network in a modified JPanel
     *
     */
     static class MyDrawPanel extends JPanel {

        /**
         * Draws the network image
         * @param g Graphics object responsible for drawing
         */
        @Override
        public void paintComponent(Graphics g){

            if(network == null){

            }
            else{
                panelWidth = drawPanel.getWidth();
                panelHeight = drawPanel.getHeight();

                if (networkWidth/panelWidth > 1){
                    widthRatio = panelWidth/networkWidth;
                }
                else
                    widthRatio = networkWidth/panelWidth;
                if (networkHeight/panelHeight > 1){
                    heightRatio = panelHeight/networkHeight;
                }
                else
                    heightRatio = networkHeight/panelHeight;
                if (widthRatio > heightRatio)
                    displayAdjustFactor = heightRatio * 0.9;
                else
                    displayAdjustFactor = widthRatio * 0.9;

                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                for (Point2D pointy: nodeArrayList){
                    g2.drawRect( (int)((pointy.getX() /*+ minX*/) * displayAdjustFactor ),
                            (int)((pointy.getY() /*+ minY*/) * displayAdjustFactor ), 5, 5);
                }

                for (ArrayList<Point2D> vertex: linkNodeHashMap.values()){
                    point2DOne.setLocation( (vertex.get(0).getX() /*+ minX*/) * displayAdjustFactor,
                            (vertex.get(0).getY() /*+ minY*/) * displayAdjustFactor);
                    point2DTwo.setLocation( (vertex.get(1).getX() /*+ minX*/) * displayAdjustFactor,
                            (vertex.get(1).getY() /*+ minY*/) * displayAdjustFactor);
                    g2.draw(new Line2D.Double(point2DOne, point2DTwo));
                }

                if(leakLocation != -1){
                    g2.setColor(leakColor);
                    Point2D pointy = nodeArrayList.get(leakLocation);

                    g2.fill(new Rectangle2D.Double(((pointy.getX()) * displayAdjustFactor),
                            ((pointy.getY()) * displayAdjustFactor), 15, 15));
                }

                if(pressureSensors.size() > 0){
                    int pressureSensorIndex = -1;
                    int counter = 0;
                    ArrayList<Ellipse2D> drawablePressureSenors = new ArrayList<>();
                    g2.setColor(pressureSensorColor);

                    for (String string: pressureSensors){
                        pressureSensorIndex = nodeIndexIDAssociater.getNodeIndex(string);
                        Point2D pointy = nodeArrayList.get(pressureSensorIndex);
                        drawablePressureSenors.add(counter, new Ellipse2D.Double(((pointy.getX())
                                * displayAdjustFactor), ((pointy.getY()) * displayAdjustFactor ), 10, 10));
                        counter++;
                    }

                    for(Ellipse2D sensor:drawablePressureSenors){
                        g2.fill(sensor);
                    }
                }

                if(pressureViolations.size() > 0){
                    int pressureViolationIndex = -1;
                    int counter = 0;
                    ArrayList<Rectangle2D> drawablePressureViolations = new ArrayList<>();
                    g2.setColor(pressureViolationColor);

                    for (String string: pressureViolations){
                        pressureViolationIndex = nodeIndexIDAssociater.getNodeIndex(string);
                        Point2D pointy = nodeArrayList.get(pressureViolationIndex);
                        drawablePressureViolations.add(counter, new Rectangle2D.Double(((pointy.getX())
                                * displayAdjustFactor ), ((pointy.getY()) * displayAdjustFactor ), 15, 15));
                    }

                    for(Rectangle2D violation:drawablePressureViolations){
                        g2.draw(violation);
                    }
                }
            }
        }
    }

    /**
     * Inner class that draws a key to help interpret the network image
     */
    static class NetworkKey extends JPanel{

        static double keyWidth = 0;
        static double keyHeight = 0;

        /**
         * Draws the network key
         * @param g Graphics object responsible for drawing
         */
        @Override
        public void paintComponent(Graphics g){

            if(network==null){
            }
            else{

                keyWidth = networkKey.getWidth();
                keyHeight = networkKey.getHeight();

                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(pressureSensorColor);
                g2.drawOval((int)(keyWidth * 0.1), (int)(keyHeight * 0.1), 10, 10 );
                g2.setColor(Color.BLACK);
                g2.drawString("Pressure Sensor", (float)((keyWidth * 0.1) + 10), (float)((keyHeight * 0.1) + 10));

                g2.setColor(leakColor);
                g2.drawRect((int)(keyWidth * 0.1), (int)(keyHeight * 0.2), 10, 10 );
                g2.setColor(Color.BLACK);
                g2.drawString("Leak Location", (float)((keyWidth * 0.1) + 15), (float)((keyHeight * 0.2) + 10));

                g2.setColor(pressureViolationColor);
                g2.drawLine((int) (keyWidth * 0.1), (int) (keyHeight * 0.3),
                        (int) (keyWidth * 0.1), (int) (keyHeight * 0.3) + 20);
                g2.setColor(Color.BLACK);
                g2.drawString("Pressure Violation", (float)((keyWidth * 0.1) + 10), (float)((keyHeight * 0.3) + 10));
            }
        }
    }
}
