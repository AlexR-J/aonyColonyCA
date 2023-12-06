import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.SimpleTimeZone;

public class Ant {

    private Path lastPath;
    private int currentCityIndex;
    private double totalDistance;
    private ArrayList<Path> pathsTaken = new ArrayList<Path>();
    private ArrayList<City> citiesVisited = new ArrayList<City>();
    private ArrayList<Double> pheromoneChanges = new ArrayList<Double>();
    private City startingCity;

    //method used to find the total path this ant will complete in this iteration.
    public void findPath(City startingCity, int noOfCities, ArrayList<City> cities) throws Exception {

        //set the given starting city to the current location.
        this.startingCity = startingCity;
        City currentCity = startingCity;
        //for however many cities there are attempt to find a new city to go to.
        for(int i=0; i<noOfCities;i++) {
            //getNextPath() returns the next path to follow.
            Path nextPath = this.getNextPath(currentCity);
            double nextPathDistance = nextPath.getDistance();
            totalDistance += nextPathDistance;
            //add the next path to all the appropriate arrays, then move current location to the next one on the path.
            pathsTaken.add(nextPath);
            City nextCity = nextPath.getDestinationCity(currentCity);
            citiesVisited.add(nextCity);
            currentCity = nextCity;
            currentCity.visitCity();
            //take path counts how many times a path has been taken provides no value beyond debugging.
            nextPath.takePath();
        }
    }

    private Path getNextPath(City currentCity) {

        /*create a list of all paths from the current city. Then create a new array containing only the paths to cities
        that we have not yet been to.*/
        ArrayList<Path> possiblePaths = new ArrayList<Path>();
        ArrayList<Path> allPaths = currentCity.getPaths();
        for(Path path : allPaths) {
            City pathDestination = path.getDestinationCity(currentCity);
            if(!(citiesVisited.contains(pathDestination))) {
                possiblePaths.add(path);
            }
        }

        //if this is the last step then find the path back to the starting city and use that one.
        if(possiblePaths.size() == 1) {
            for(Path path : allPaths) {
                if(path.getDestinationCity(currentCity) == startingCity) {
                    return path;
                }
            }
        }

        /*if this is not the last step. Then shuffle the list (this is mostly unnecessary just to increase the randomness)
        of the path chosen by the ant.*/
        Collections.shuffle(possiblePaths);
        double randomNumber = Math.random();
        ArrayList<Double> desires = new ArrayList<Double>();
        ArrayList<Double> probabilities = new ArrayList<Double>();
        Double sumOfDesires = (double) 0;
        //now fine the desire for each of the paths, and add it to the total desire for this decision.#
        //(desire here means (pheromone^alpha * (1/distance)^beta))
        for(Path path: possiblePaths) {
            Double desire = path.getDesire();
            desires.add(desire);
        }
        for(Double desire : desires) {
            sumOfDesires += desire;
        }
        //now calculate the probabilities of the different paths using : individual desire / total desire
        for(int i =0;i<desires.size();i++) {
            double probability = desires.get(i)/sumOfDesires;
            probabilities.add(probability);
        }

        /*pick a random number and move through the array until the sum of all values bellow it are greater it.
        then save i at that value. This is the index of the next path to be taken.*/
        double sumOfProb = 0;
        for(int i=0;i<probabilities.size();i++) {
            sumOfProb += probabilities.get(i);
            if(sumOfProb >= randomNumber) {
                return possiblePaths.get(i);
            }
        }
        //iff all fails then return null, throwing an exception.
        return null;

    }

    /*############################################################################################################*/
    //get and set methods

    public double getTotalDistance() {
        return totalDistance;
    }

    public ArrayList<Path> getPathsTaken(){
        return pathsTaken;
    }

}
