package last.ui;

import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import last.*;
import last.io.NetworkLoader;
import org.addition.epanet.network.Network;
import org.addition.epanet.network.structures.Node;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: bergl_000
 * Date: 4/5/13
 * Time: 4:46 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * Graphical User Interface for the Leak Assessment Simulation Tool
 */
public class LASTGUI {

    public static File inputFile;
    public static File outputDirectory;
    public static Network network = new Network();
    public static NetworkLoader networkLoader = new NetworkLoader();
    public static NetworkDrawer networkDrawer;
    public static NodeIndexIDAssociater nodeIndexIDAssociater;
    public static ArrayList<String> leakLocationList = new ArrayList<>();
    public static ArrayList<Double> leakMagnitudeList = new ArrayList<>();
    public static ArrayList<String> pressureSensorList = new ArrayList<>();
    public static HashMap<String, Double> leakMap = new HashMap<>();
    static JFrame frame;
    public JPanel networkDrawPanel;
    private JPanel panel1;
    private JPanel optionsJPanel;
    private JPanel networkJPanel;
    private JPanel resultsJPanel;
    private JPanel networkKeyPanel;
    private JTabbedPane tabbedPane1;
    private JFileChooser fileChooser = new JFileChooser();
    private FileNameExtensionFilter inpFilter = new FileNameExtensionFilter("INP File", "inp");
    private SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(1, 1, 100, 1);
    private SpinnerNumberModel pressureNumberModel = new SpinnerNumberModel(1, 1, 100, 1);
    private SpinnerModel leaksPerSimModel = new SpinnerNumberModel(1, 1, 1, 1);
    private DefaultListModel leakListModel = new DefaultListModel();
    private DefaultListModel leakMagnitudeModel = new DefaultListModel();
    private DefaultComboBoxModel leakToDrawModel = new DefaultComboBoxModel();
    private JButton openFileButton;
    private JButton runButton;
    private JButton loadLeaksButton;
    private JButton loadSensorsButton;
    private JButton acceptLocationsButton;
    private JButton acceptMagnitudesButton;
    private JButton removePreviousButton;
    private JButton acceptPressureSensorsButton;
    private JButton acceptLeaksButton;
    private JButton runAnimationButton;
    private JRadioButton knownLocationsRadioButton;
    private JRadioButton randomLocationsRadioButton;
    private JRadioButton realTimeSensorRadioButton;
    private JRadioButton binarySensorRadioButton;
    private JCheckBox drawNetworkCheckBox;
    private JCheckBox simulateLeaksCheckBox;
    private JCheckBox pressureSensorsCheckBox;
    private JComboBox leakToDrawComboBox;
    private JSpinner numOfSimulations;
    private JSpinner leaksPerSimulation;
    private JSpinner numOfSensors;
    private JTextField leakMagnitudeTextField;
    private JList leakList;
    private JList pressureSensorDisplayList;
    private JList leakMagnitudeDisplayList;
    private JLabel leakListLabel;
    private JLabel numofSimualationsLabel;
    private JLabel leaksPerSimulationLabel;
    private JLabel numOfSensorsLabel;
    private JLabel leakMagnitudeLabel;
    private JLabel pressureListLabel;
    private JLabel networkNameLabel;
    private JButton changeOutputDirectoryButton;
    private int counter = 0;

    public LASTGUI() {

        numOfSimulations.setModel(spinnerNumberModel);
        leaksPerSimulation.setModel(leaksPerSimModel);
        fileChooser.setFileFilter(inpFilter);
        leakListLabel.setText("Potential leak locations will be displayed here.");
        leakMagnitudeLabel.setText("Enter leak magnitudes here.");
        pressureListLabel.setText("Select pressure sesnsors here.");
        leakListLabel.setEnabled(false);
        leakMagnitudeLabel.setEnabled(false);
        pressureListLabel.setEnabled(false);
        acceptLocationsButton.setEnabled(false);
        leakMagnitudeTextField.setEnabled(false);
        acceptMagnitudesButton.setEnabled(false);
        acceptPressureSensorsButton.setEnabled(false);
        acceptLeaksButton.setEnabled(false);
        removePreviousButton.setEnabled(false);
        networkNameLabel.setVisible(false);
        runButton.setEnabled(false);
        simulateLeaksCheckBox.setEnabled(false);
        pressureSensorsCheckBox.setEnabled(false);
        numOfSimulations.setEnabled(false);
        numofSimualationsLabel.setEnabled(false);
        leaksPerSimulationLabel.setEnabled(false);
        numOfSensorsLabel.setEnabled(false);
        knownLocationsRadioButton.setEnabled(false);
        randomLocationsRadioButton.setEnabled(false);
        leaksPerSimulation.setEnabled(false);
        loadLeaksButton.setEnabled(false);
        drawNetworkCheckBox.setEnabled(false);
        numOfSensors.setEnabled(false);
        realTimeSensorRadioButton.setEnabled(false);
        binarySensorRadioButton.setEnabled(false);
        loadSensorsButton.setEnabled(false);
        runAnimationButton.setEnabled(false);

        openFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                leakListModel.removeAllElements();
                leakMagnitudeModel.removeAllElements();
                leakToDrawModel.removeAllElements();
                leakMap.clear();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int returnVal = fileChooser.showOpenDialog(openFileButton);

                if (returnVal == JFileChooser.APPROVE_OPTION) {

                    inputFile = fileChooser.getSelectedFile();
                    network = networkLoader.loadNetwork(inputFile);
                    networkNameLabel.setVisible(true);
                    networkNameLabel.setText(inputFile.getName());
                    networkDrawer = new NetworkDrawer(network);
                    nodeIndexIDAssociater = new NodeIndexIDAssociater(network);
                    networkDrawPanel.add(networkDrawer.getDrawPanel(), BorderLayout.CENTER);
                    networkKeyPanel.add(networkDrawer.getNetworkKey(), BorderLayout.CENTER);

                    if (drawNetworkCheckBox.isSelected()) {
                        networkDrawer.drawNetwork();
                    }

                    spinnerNumberModel.setMaximum(network.getNodes().size());
                    pressureNumberModel.setMaximum(network.getNodes().size());
                    leakMagnitudeDisplayList.setModel(leakMagnitudeModel);

                    drawNetworkCheckBox.setEnabled(true);
                    simulateLeaksCheckBox.setEnabled(true);
                    pressureSensorsCheckBox.setEnabled(true);
                    numofSimualationsLabel.setEnabled(true);
                    runButton.setEnabled(true);
                    leakListLabel.setText("Select " + numOfSimulations.getValue() + " leak locations(s).");
                }

                if (network != null) {

                    for (Node node : network.getNodes()) {
                        leakListModel.addElement(node.getId());

                    }

                    leakList.setModel(leakListModel);
                    pressureSensorDisplayList.setModel(leakListModel);
                    leakList.setEnabled(false);
                    pressureSensorDisplayList.setEnabled(false);
                }
            }
        });

        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ExecutorService threadPool = Executors.newCachedThreadPool();

                if (!simulateLeaksCheckBox.isSelected() && !pressureSensorsCheckBox.isSelected()) {
                    if(outputDirectory != null)
                        threadPool.submit(new runnableScenarioSimulator(network, outputDirectory.getAbsolutePath()));
                    else
                        threadPool.submit(new runnableScenarioSimulator(network, System.getProperty("user.dir")));
                }

                if (!simulateLeaksCheckBox.isSelected() && pressureSensorList.size() > 0) {
                    if(outputDirectory != null)
                        threadPool.submit(new runnableScenarioSimulator(network, pressureSensorList,
                                outputDirectory.getAbsolutePath()));
                    else
                        threadPool.submit(new runnableScenarioSimulator(network, pressureSensorList,
                                System.getProperty("user.dir")));
                }

                if (leakMap.size() > 0 && !pressureSensorsCheckBox.isSelected()) {

                    for (String string : leakMap.keySet()) {

                        int leakLocation = nodeIndexIDAssociater.getNodeIndex(string);
                        if(outputDirectory != null)
                            threadPool.submit(new runnableScenarioSimulator(network, leakLocation, leakMap.get(string),
                                    outputDirectory.getAbsolutePath()));
                        else
                            threadPool.submit(new runnableScenarioSimulator(network, leakLocation, leakMap.get(string),
                                    System.getProperty("user.dir")));
                    }
                }

                if (leakMap.size() > 0 && pressureSensorList.size() > 0) {

                    for (String string : leakMap.keySet()) {

                        int leakLocation = nodeIndexIDAssociater.getNodeIndex(string);
                        if(outputDirectory != null)
                            threadPool.submit(new runnableScenarioSimulator(network, pressureSensorList,
                                    leakLocation, leakMap.get(string), outputDirectory.getAbsolutePath()));
                        else
                            threadPool.submit(new runnableScenarioSimulator(network, pressureSensorList,
                                    leakLocation, leakMap.get(string), System.getProperty("user.dir")));
                    }
                }

                threadPool.shutdown();

                while (!threadPool.isShutdown()) {
                }
            }
        });

        drawNetworkCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                if (e.getStateChange() == ItemEvent.DESELECTED) {
                    leakToDrawModel.removeAllElements();
                    leakToDrawComboBox.setEnabled(false);
                    networkDrawPanel.setVisible(false);

                }
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    leakToDrawComboBox.setEnabled(true);
                    networkDrawPanel.setVisible(true);
                    leakToDrawModel.removeAllElements();
                    for (int i = 0; i < leakLocationList.size(); i++) {
                        leakToDrawModel.addElement("Leak node ID: " + leakLocationList.get(i));
                        //System.out.println("Leak node ID: " + leakLocationList.get(i));
                    }
                    leakToDrawComboBox.setModel(leakToDrawModel);
                    networkDrawer.drawNetwork();
                }
            }
        });

        leakToDrawComboBox.addActionListener(new ActionListener() {

            int nodeIndex = -1;

            @Override
            public void actionPerformed(ActionEvent e) {

                if (leakToDrawModel.getSize() != 0) {

                    String leakText = (String) leakToDrawComboBox.getSelectedItem();
                    String[] leakTokens = leakText.split(" ");
                    nodeIndex = nodeIndexIDAssociater.getNodeIndex(leakTokens[3]);

                    networkDrawer.setLeak(nodeIndex);
                }
            }
        });


        simulateLeaksCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                if (e.getStateChange() == ItemEvent.DESELECTED) {
                    knownLocationsRadioButton.setEnabled(false);
                    randomLocationsRadioButton.setEnabled(false);
                    leaksPerSimulation.setEnabled(false);
                    loadLeaksButton.setEnabled(false);
                    leaksPerSimulationLabel.setEnabled(false);
                    leakListLabel.setEnabled(false);
                    leakList.setEnabled(false);
                    runButton.setEnabled(true);
                    leakToDrawModel.removeAllElements();

                    leakMap.clear();

                } else {
                    numOfSimulations.setEnabled(true);
                    knownLocationsRadioButton.setEnabled(true);
                    randomLocationsRadioButton.setEnabled(true);
                    leaksPerSimulation.setEnabled(true);
                    loadLeaksButton.setEnabled(true);
                    leaksPerSimulationLabel.setEnabled(true);
                    leakListLabel.setEnabled(true);
                    leakList.setEnabled(true);
                    runButton.setEnabled(false);
                }
            }
        });

        numOfSimulations.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                leakListLabel.setText("Select " + numOfSimulations.getValue() + " leak locations(s).");

                if (leakList.getSelectedIndices().length < (int) numOfSimulations.getValue()) {
                    runButton.setEnabled(false);

                }

                if (leakList.getSelectedIndices().length == (int) numOfSimulations.getValue()) {
                    acceptLeaksButton.setEnabled(true);
                }
            }
        });

        leakList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (e.getValueIsAdjusting() == true)
                    leakToDrawModel.removeAllElements();

                if (e.getValueIsAdjusting() == false) {

                    if (leakList.getSelectedIndices().length < (int) numOfSimulations.getValue()) {

                        leakListLabel.setText("Select " + ((int) numOfSimulations.getValue() -
                                leakList.getSelectedIndices().length) + " more leak location(s).");
                        runButton.setEnabled(false);
                        acceptLocationsButton.setEnabled(false);

                    } else if (leakList.getSelectedIndices().length > (int) numOfSimulations.getValue()) {

                        leakListLabel.setText("Too many nodes selected, select only " + numOfSimulations.getValue() + " location(s).");
                        runButton.setEnabled(false);
                        acceptLocationsButton.setEnabled(false);

                    } else {

                        leakListLabel.setText(numOfSimulations.getValue() + " leak locations(s) selected.");
                        acceptLocationsButton.setEnabled(true);
                    }
                }
            }
        });

        acceptLocationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                for (int i = 0; i < leakLocationList.size(); i++) {
                    leakLocationList.remove(i);
                }

                for (int i = 0; i < leakMagnitudeList.size(); i++) {
                    leakMagnitudeList.remove(i);
                }

                for (int i = 0; i < leakList.getSelectedIndices().length; i++) {

                    leakLocationList.add(i, (String) leakList.getSelectedValuesList().get(i));
                }

                if (drawNetworkCheckBox.isSelected()) {

                    leakToDrawModel.removeAllElements();

                    for (int i = 0; i < leakLocationList.size(); i++) {

                        leakToDrawComboBox.addItem("Leak node ID: " + leakLocationList.get(i));
                    }

                    leakToDrawComboBox.setModel(leakToDrawModel);
                }

                leakMagnitudeLabel.setText("Please select the magnitude for the leak at node: " + leakLocationList.get(0));
                leakMagnitudeLabel.setEnabled(true);
                leakMagnitudeTextField.setEnabled(true);
                acceptMagnitudesButton.setEnabled(true);
            }
        });

        acceptMagnitudesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    double magnitude = Double.parseDouble(leakMagnitudeTextField.getText());
                    if (magnitude < 0) {

                        JOptionPane.showMessageDialog(frame,
                                "Please select a positive real number leak magnitude.",
                                "Negative-number error",
                                JOptionPane.ERROR_MESSAGE);
                    } else {

                        leakMagnitudeModel.addElement("Location: " + leakLocationList.get(counter)
                                + ", Magnitude: " + magnitude);

                        leakMagnitudeList.add(leakMagnitudeList.size(), magnitude);
                        counter++;
                        removePreviousButton.setEnabled(true);
                    }

                    if (leakLocationList.size() > counter) {

                        leakMagnitudeLabel.setText("Please select the magnitude for the leak at node: " +
                                leakLocationList.get(counter));
                    }

                    leakMagnitudeTextField.setText("");

                } catch (NumberFormatException nfe) {

                    JOptionPane.showMessageDialog(frame,
                            "Please select a positive real number leak magnitude.",
                            "Non-number error",
                            JOptionPane.ERROR_MESSAGE);
                }

                if (counter == leakLocationList.size() && leakLocationList.size() != 0) {
                    acceptMagnitudesButton.setEnabled(false);
                    leakMagnitudeTextField.setEnabled(false);
                    leakMagnitudeLabel.setText("Leak magnitudes set, accept all leaks?");
                    acceptLeaksButton.setEnabled(true);
                }
            }
        });

        removePreviousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (counter == leakLocationList.size()) {

                    acceptMagnitudesButton.setEnabled(false);
                    leakMagnitudeTextField.setEnabled(true);
                    leakMagnitudeLabel.setText("Leak magnitudes set, accept all leaks?");
                    acceptLeaksButton.setEnabled(false);
                    acceptLeaksButton.setBackground(Color.gray);
                }

                if (leakMagnitudeList.size() > 0) {

                    if (counter > 0) {

                        counter--;
                    }

                    acceptMagnitudesButton.setEnabled(true);
                    leakMagnitudeList.remove(counter);
                    leakMagnitudeModel.remove(counter);

                    if (leakLocationList.size() >= counter) {

                        leakMagnitudeLabel.setText("Please select the magnitude for the leak at node: " + leakLocationList.get(counter));
                    }

                    leakMagnitudeTextField.setText("");
                }

                if (counter == 0) {

                    acceptMagnitudesButton.setEnabled(true);
                    removePreviousButton.setEnabled(false);
                }
            }
        });

        acceptLeaksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                for (int i = 0; i < leakLocationList.size(); i++) {

                    leakMap.put(leakLocationList.get(i), leakMagnitudeList.get(i));
                    acceptLeaksButton.setBackground(Color.GREEN);
                }
                if (!pressureSensorsCheckBox.isSelected()) {

                    runButton.setEnabled(true);
                }
                if (pressureSensorsCheckBox.isSelected() && pressureSensorList.size() > 0) {

                    runButton.setEnabled(true);
                }
            }
        });

        pressureSensorsCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                if (e.getStateChange() == ItemEvent.DESELECTED) {
                    numOfSensors.setEnabled(false);
                    loadSensorsButton.setEnabled(false);
                    numOfSensors.setEnabled(false);
                    numOfSensorsLabel.setEnabled(false);
                    realTimeSensorRadioButton.setEnabled(false);
                    binarySensorRadioButton.setEnabled(false);
                    pressureSensorDisplayList.setEnabled(false);
                    pressureListLabel.setEnabled(false);
                    pressureSensorList.clear();

                    if (simulateLeaksCheckBox.isSelected() && leakMap.size() > 0) {

                        runButton.setEnabled(true);
                    }
                } else {

                    numOfSensors.setEnabled(true);
                    loadSensorsButton.setEnabled(true);
                    numOfSensors.setEnabled(true);
                    numOfSensorsLabel.setEnabled(true);
                    realTimeSensorRadioButton.setEnabled(true);
                    //binarySensorRadioButton.setEnabled(true);
                    numOfSensors.setModel(pressureNumberModel);
                    pressureSensorDisplayList.setEnabled(true);
                    pressureListLabel.setEnabled(true);

                    if (!simulateLeaksCheckBox.isSelected() && pressureSensorList.size() == 0) {

                        runButton.setEnabled(false);
                    }
                }
            }
        });

        pressureSensorDisplayList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (pressureSensorDisplayList.getSelectedIndices().length < (int) numOfSensors.getValue()) {

                    pressureListLabel.setText("Select " + ((int) numOfSensors.getValue() -
                            pressureSensorDisplayList.getSelectedIndices().length) + " more pressure sensor(s).");

                    runButton.setEnabled(false);
                    acceptPressureSensorsButton.setEnabled(false);

                } else if (pressureSensorDisplayList.getSelectedIndices().length > (int) numOfSensors.getValue()) {

                    pressureListLabel.setText("Too many sensors selected, select only " + numOfSensors.getValue()
                            + " sensor(s).");

                    runButton.setEnabled(false);
                    acceptPressureSensorsButton.setEnabled(false);

                } else {

                    pressureListLabel.setText(numOfSensors.getValue() + " pressure sensor(s) selected.");
                    //runButton.setEnabled(true);
                    acceptPressureSensorsButton.setEnabled(true);
                }
            }
        });

        acceptPressureSensorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (pressureSensorList.size() > 0) {

                    for (int i = 0; i < pressureSensorList.size(); i++) {

                        pressureSensorList.remove(i);
                    }

                }

                for (int i = 0; i < pressureSensorDisplayList.getSelectedIndices().length; i++) {

                    pressureSensorList.add(i, (String) pressureSensorDisplayList.getSelectedValuesList().get(i));
                }

                if (simulateLeaksCheckBox.isSelected() && leakMap.size() != 0) {

                    runButton.setEnabled(true);
                }
                if (!simulateLeaksCheckBox.isSelected()) {

                    runButton.setEnabled(true);
                }
                if (drawNetworkCheckBox.isSelected()) {

                    networkDrawer.setPressureSensors(pressureSensorList);
                }
            }
        });

        numOfSensors.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                pressureListLabel.setText("Select " + numOfSensors.getValue() + " pressure sensor(s).");
                if (pressureSensorDisplayList.getSelectedIndices().length < (int) numOfSensors.getValue()) {

                    runButton.setEnabled(false);
                }
            }
        });

        runAnimationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String leakText = (String) leakToDrawComboBox.getSelectedItem();
                String[] leakTokens = leakText.split(" ");
                int leakLocation = nodeIndexIDAssociater.getNodeIndex(leakTokens[3]);
                //System.out.println("leak map: " + leakMap.get(leakTokens[3]));

                ScenarioAnimator scenarioAnimator = new ScenarioAnimator(network, networkDrawer, leakLocation, leakMap.get(leakTokens[3]));
                scenarioAnimator.animateSimulation();
            }
        });

        changeOutputDirectoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                //outputDirectoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = fileChooser.showOpenDialog(changeOutputDirectoryButton);

                if (returnVal == JFileChooser.APPROVE_OPTION) {

                    outputDirectory = fileChooser.getSelectedFile();
                    System.out.println(outputDirectory.isDirectory() + outputDirectory.getAbsolutePath());
                    try {
                        System.out.println(outputDirectory.getCanonicalPath());
                    } catch (IOException e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
        });
    }

    /**
     * Sets the look and feel for the UI and sets and packs the frame.
     * @param args
     */
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        frame = new JFrame("Leak Assessment Simulation Tool");
        frame.setContentPane(new LASTGUI().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}
