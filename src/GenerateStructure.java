

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GenerateStructure {
    //class used to create a new structure from xml and then run the algorithm.
    private String testType;
    private String filename;
    private ArrayList<Path> bestPath;
    private double bestPathFitness = 0;
    private double bestFitnessFound = 0;
    private double worstFitnessFound = 0;
    private double alpha;
    private double beta;
    private int q;
    private double evaporationValue;
    private ArrayList<City> cities = new ArrayList<City>();
    private ArrayList<Integer> iterationNo = new ArrayList<Integer>();
    private ArrayList<Double> fitnessValues = new ArrayList<Double>();
    private ArrayList<Path> allPaths = new ArrayList<Path>();
    private ArrayList<Double> bestPerGeneration = new ArrayList<Double>();
    private Boolean elitism = false;

    public GenerateStructure(String sourceFile, double alpha, double beta, int q, double evaporationValue, double minPheromone, double maxPheromone,String filename, String testType) {
        this.filename = filename;
        this.q = q;
        this.testType = testType;
        this.evaporationValue = evaporationValue;
        try {


            //creating document stream and loading file into memory
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new File(sourceFile));
            doc.getDocumentElement().normalize();
            //get a list of vertexes
            NodeList vertexes = doc.getElementsByTagName("vertex");

            for(int b=0;b < vertexes.getLength(); b++) {
                City tempCity = new City(b);
                cities.add(tempCity);
            }
            //for every city
            for(int i =0; i < vertexes.getLength(); i++) {
                Element element = (Element) vertexes.item(i);
                NodeList edges = element.getElementsByTagName("edge");
                int startingCityIndex = i;

                //for every path from current city
                for(int a=0; a< edges.getLength(); a++) {

                    //creating path objects and assigning a random small pheromone value.
                    Element edge = (Element) edges.item(a);
                    Double cost = Double.valueOf(edge.getAttribute("cost"));
                    int destinationCityIndex = Integer.parseInt(edge.getTextContent());
                    City destinationCity = this.getCityByIndex(destinationCityIndex);
                    City startingCity = this.getCityByIndex(startingCityIndex);
                    Boolean cityAdded = false;


                    //find out if a path between the two cities already exists.
                    Boolean noPathBetween = true;
                    for(Path path : allPaths) {
                        City cityA = path.getCityA();
                        City cityB = path.getCityB();
                        if(cityA == startingCity && cityB == destinationCity) {
                            noPathBetween = false;
                            break;
                        } else if(cityB == startingCity && cityA == destinationCity) {
                            noPathBetween = false;
                            break;
                        }
                    }

                    //if no path between selected cities, create a new path and add it to those cities lists.
                    if(noPathBetween) {
                        double randomPheromone = maxPheromone;
                        Path newPath = new Path(cost, randomPheromone,alpha, beta, q, startingCity, destinationCity, minPheromone, maxPheromone);
                        startingCity.addPath(newPath);
                        destinationCity.addPath(newPath);
                        allPaths.add(newPath);
                    }
                }
            }

            
        } catch (ParserConfigurationException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } catch (SAXException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    //method to be run every iteration of the GA. creates new ants and finds their paths.
    public double runIteration(int noOfAnts) throws Exception {
        try{
            ArrayList<Ant> ants = new ArrayList<Ant>();
            ArrayList<Double> listOfFitness = new ArrayList<Double>();
            //create a specified number of ants. And get them to calculate their paths.
            for(int i=0;i<noOfAnts;i++) {
                Ant ant = new Ant();
                ants.add(ant);
                Random rand = new Random();
                int randomCityIndex = rand.nextInt(cities.size());
                City randomCity = cities.get(randomCityIndex);
                ant.findPath(randomCity, cities.size(), cities);
                listOfFitness.add(ant.getTotalDistance());

                //if the ant has a better fitness than the current best then replace it.
                if(ant.getTotalDistance() <= bestPathFitness || bestPathFitness == 0) {
                    bestPath = ant.getPathsTaken();
                    bestPathFitness = ant.getTotalDistance();
                }
            }
            updateTree(ants);

            //return the best fitness of the iteration.
            return Collections.min(listOfFitness);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //method used to update the pheromone values in the paths.
    public void updateTree(ArrayList<Ant> ants) {
        ArrayList<Path> updatedPaths = new ArrayList<Path>();

        //for all given ants go through their path and add the correct amount of pheromone respectively.
        for(Ant ant : ants){
            ArrayList<Path> route = ant.getPathsTaken();
            double newPheromone = q / ant.getTotalDistance();
            for(Path path : route) {
                path.addPheromone(newPheromone);
            }
        }
        //after updating the taken paths. update all paths with the evaporation constant.
        for(Path path : allPaths) {

            path.setPheromone(path.getPheromone() * evaporationValue);

        }
    }

    //the main command used to start the colony.
    public double runAntColony(int noOfIterations, int noOfAnts) throws Exception {
        //create lists of iterationsIDs, and corresponding fitness values.
        ArrayList<Integer> iterationNo = new ArrayList<Integer>();
        ArrayList<Double> fitnessValues = new ArrayList<Double>();
        //for a specified number of iterations create run a new iteration, increment the iteration number.
        for(int i =0;i<noOfIterations;i++) {
            double iterationFitness  =  runIteration(noOfAnts);
            iterationNo.add(i);
            fitnessValues.add(iterationFitness);
            //after all normal updates, check if elitism is active if so run the elitist ant.
            if(elitism == true) {
                this.elitistUpdate();
            }
        }
        //send output data to the writer to be written to file.
        Writer writer = new Writer();
        writer.writeOut(iterationNo, fitnessValues, filename, testType);
        return bestFitnessFound;
    }

    //method to handle the update done by the elitist ant after everything else.
    private void elitistUpdate() {
        //for all paths in the best path add the new pheromone value.
        for(Path path : bestPath){
            double newPheromone = q / bestPathFitness;
            path.addPheromone(newPheromone);
        }
    }

    /*###############################################################################################################*/
    //get and set methods

    private City getCityByIndex(int index) {
        //search through all cities and find the one with matching id to index.
        for(City city : cities) {
            if(city.getCityIndex() == index) {
                return city;
            }
        }
        return null;
    }


    public ArrayList<Integer> getIterationNumbers(){
        return iterationNo;
    }

    public ArrayList<Double> getFitnessValues() {
        return fitnessValues;
    }

    public void setElitism(Boolean b) {
        elitism = b;
    }

    public ArrayList<Path> getBestPath() {
        return bestPath;
    }
}
