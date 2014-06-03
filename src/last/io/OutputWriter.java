package last.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created with IntelliJ IDEA.
 * User: bergl_000
 * Date: 4/6/13
 * Time: 12:35 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * Abstract class for a writing output files
 */
public abstract class OutputWriter {

    File outputFile;
    PrintWriter printWriter;
    FileWriter out;
    String fileName;
    String pressureString;

    /**
     * Creates a file and instantiates a FileWriter and PrintWriter.
     * @param fileName the name of the file output will be written to
     */
    public OutputWriter(String fileName){

        this.fileName = fileName;
        createFile();
        try{
            out = new FileWriter(outputFile, true);
        }catch(IOException e){
            e.printStackTrace();
        }
        printWriter = new PrintWriter(out);
    }

    /**
     * Creates a file using the designated file name.
     */
    public void createFile(){

        outputFile = new File(fileName);
    }

    /**
     * Method skeleton for outputting pressure data.
     * @param pressureArray An array of pressure data
     */
    public void writeOutput(double pressureArray[]){

    }

    /**
     * Method skeleton for outputting node names.
     * @param nodeNames An array of node names
     */
    public void writeOutput(String[] nodeNames){

    }

    /**
     * Closes the PrintWriter and FileWriter
     */
    public void closeFile(){

        printWriter.close();
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
