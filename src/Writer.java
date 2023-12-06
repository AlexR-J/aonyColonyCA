import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Writer {
    //class used to write data to file.
    public void writeOut(ArrayList<Integer> iterationNumbers, ArrayList<Double> fitnessValues,String filename, String testType) throws IOException {
        try {

            //create a new file with the given file name and directory.
            File file = new File(testType, filename);
            BufferedWriter myWriter = new BufferedWriter(new FileWriter(file));
            myWriter.write("iteration,fitnessValue" + System.getProperty("line.separator"));

            //print all values given to printer. This is done with csv syntax.
            for(int i=0; i<iterationNumbers.size();i++) {
                myWriter.write(String.valueOf(iterationNumbers.get(i)));
                myWriter.write(",");
                myWriter.write(String.valueOf(fitnessValues.get(i)) + System.getProperty("line.separator"));

            }
            myWriter.close();


        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void writeOutFinals(ArrayList<String> testTypes, ArrayList<Double> fitnessValues,String filename, String testType) throws IOException {
        try {

            //create a new file with the given file name and directory.
            File file = new File(testType, filename);
            BufferedWriter myWriter = new BufferedWriter(new FileWriter(file));
            myWriter.write("testType,fitnessValue" + System.getProperty("line.separator"));

            //print all values given to printer. This is done with csv syntax.
            for(int i=0; i<testTypes.size();i++) {
                myWriter.write(String.valueOf(testTypes.get(i)));
                myWriter.write(",");
                myWriter.write(String.valueOf(fitnessValues.get(i)) + System.getProperty("line.separator"));

            }
            myWriter.close();


        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}

