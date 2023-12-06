import java.io.File;
import java.util.ArrayList;

public class TestRunner {
    //class used to run tests on the different parameters.
    private ArrayList<String> testTypes = new ArrayList<String>();
    private ArrayList<Double> bestResults = new ArrayList<Double>();
    private String xmlFilepath;

    public TestRunner(String xmlFilepath) {
        //set the xml filepath for all tests.
        this.xmlFilepath = xmlFilepath;
    }

    public Boolean runAllTests() throws Exception {

            //run all tests. They handle their own output individually.
            runEvaporationTests();
            runColonySizeTests();
            runAlphaTests();
            runBetaTests();
            runNoMMASTest();
            runElitistTest();
            Writer writer = new Writer();
            writer.writeOutFinals(testTypes, bestResults, "results.csv", "finalResults");

        return false;
    }

    public void runEvaporationTests() throws Exception {

        //Tests to show the effect of evaporation constant on results.
        //range of 0-0.9 is used. (shown in csv files with e notation)
        String testType = "evaporationTests";
        for(int i =0; i < 10; i++) {
            int testNo = i+1;
            System.out.print("Evaporation Test No.: " + testNo);
            double evaporationConstant = i/10;
            String filename = i + "e-1.csv";
            GenerateStructure structure = new GenerateStructure(xmlFilepath, 1,1,1, evaporationConstant, 0.0000001, 4, filename, testType);
            Double bestFitness = structure.runAntColony(200, 50);
            this.bestResults.add(bestFitness);
            this.testTypes.add(testType);
            System.out.println(" : DONE!");
        }

    }

    public void runColonySizeTests() throws Exception{

        //Tests to show the effect of colony size on output.
        //values 1-90 are used.
        String testType = "colonySizeTests";
        for(int i =0; i < 10; i++) {
            int testNo = i+1;
            System.out.print("Colony Size Test No.: " + testNo);
            int colonySize = i * 10;
            //i=0 is used as a test for 1 ant. As 0 ants would not  be possible.
            if(i==0) {
                colonySize = 1;
            }
            String filename = colonySize + ".csv";
            GenerateStructure structure = new GenerateStructure(xmlFilepath, 1,1,1, 0.5, 0.0000001, 4, filename, testType);
            Double bestFitness = structure.runAntColony(200, colonySize);
            this.bestResults.add(bestFitness);
            this.testTypes.add(testType);
            System.out.println(" : DONE!");

        }

    }

    public void runAlphaTests() throws Exception{

        //Test to test a range of alpha vales
        //values 1-0.1 are used.
        String testType = "alphaTests";
        for(double i =0; i < 10; i++) {
            int testNo = (int)i+1;
            System.out.print("Alpha Test No.: " + testNo);
            double alphaValue = i/10;
            //index = 0 is used as a max alpha value as alpha = 0 is not possilbe.
            if(i==0){
                alphaValue = 1;
            }
            String filename = alphaValue + ".csv";
            GenerateStructure structure = new GenerateStructure(xmlFilepath, alphaValue,1,1, 0.5, 0.0000001, 4, filename, testType);
            Double bestFitness = structure.runAntColony(200, 50);
            this.bestResults.add(bestFitness);
            this.testTypes.add(testType);
            System.out.println(" : DONE!");
        }


    }

    public void runBetaTests() throws Exception{
        //Test to test a range of beta values.
        //values 1through 10 are used.
        String testType = "betaTests";
        for(int i =0; i < 10; i++) {
            int testNo = i+1;
            System.out.print("Alpha Test No.: " + testNo);
            int betaValue = i+1;
            String filename = betaValue + ".csv";
            GenerateStructure structure = new GenerateStructure(xmlFilepath, 1,betaValue,1, 0.5, 0.0000001, 4, filename, testType);
            Double bestFitness = structure.runAntColony(200, 50);
            this.bestResults.add(bestFitness);
            this.testTypes.add(testType);
            System.out.println(" : DONE!");
        }

    }

    public void runNoMMASTest() throws Exception {
        //Test without using MAS everything else standard settings.
        //by setting min and max values of pheromone to the system limit we are effectively removing the cap on pheromone levels.
        String testType = "MMAS";
        String filename = "noMMASTest.csv";
        System.out.print("No MMAS Test : 1");
        GenerateStructure structure = new GenerateStructure(xmlFilepath, 1, 1,1, 0.5, -(Double.MIN_VALUE), Double.MAX_VALUE, filename, testType);
        Double bestFitness = structure.runAntColony(200, 50);
        this.bestResults.add(bestFitness);
        this.testTypes.add(testType);
        System.out.println(" : DONE!");

    }

    public void runElitistTest() throws Exception{
        String testType = "elitistTest";
        System.out.print("Evaporation Test No.: 1");
        String filename = "elitistTest.csv";
        GenerateStructure structure = new GenerateStructure(xmlFilepath, 1,1,1, 0.5, 0.0000001, 54, filename, testType);
        structure.setElitism(true);
        Double bestFitness = structure.runAntColony(200, 50);
        this.bestResults.add(bestFitness);
        this.testTypes.add(testType);
        System.out.println(" : DONE!");

    }
}
