package last.io; /**
 * Created with IntelliJ IDEA.
 * User: bergl_000
 * Date: 4/13/13
 * Time: 2:08 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * CSV file output writer class. Extends the abstract class OutputWriter
 */
public class CSVWriter extends OutputWriter {

    /**
     * Constructor for CSVWriter class
     * @param fileName A string giving the name of the output CSV text file
     * @see OutputWriter
     */
    public CSVWriter(String fileName) {

        super(fileName);
    }

    /**
     * Method to output the name of all nodes reporting pressure data
     * @param nodeNames An array containing the name of each node reporting pressure data
     */
    public void writeOutput(String[] nodeNames){

        pressureString = ""+nodeNames[0] + ",";
        for(int i = 1; i < nodeNames.length; i++) {
            pressureString += nodeNames[i] +",";
        }
        printWriter.println(pressureString);
    }

    /**
     * Method to output pressure data for a single time step
     * @param pressureArray An array containing pressure data for each reporting node
     */
    public void writeOutput(double pressureArray[]){

        pressureString = ""+pressureArray[0] + ",";
        for(int i = 1; i < pressureArray.length; i++) {
            pressureString += pressureArray[i] +",";
        }
        printWriter.println(pressureString);
    }
}
