import java.util.ArrayList;

public class Path {
    /*class is a storage object containing a path element to link two cities. Contains two cities cityA and cityB
    as well as all the pheromone and cost information of that connection.*/

    private double alpha;
    private double beta;
    private double distance;
    private double pheromone;
    private int q;
    private Path reversePath;
    private City cityA;
    private City cityB;
    private int timesTaken = 0;
    private double minPheromone;
    private double maxPheromone;


    public Path(double distance, double pheromone, double alpha, double beta, int q, City cityA, City cityB, double minPheromone, double maxPheromone) {
        this.distance = distance;
        this. pheromone = pheromone;
        this.alpha = alpha;
        this.beta = beta;
        this.q = q;
        this.cityA = cityA;
        this.cityB = cityB;
        this.minPheromone = minPheromone;
        this.maxPheromone = maxPheromone;

    }

    public City getDestinationCity(City originCity) {
        //method used to get the other city of the path given one city.
        if(originCity == cityA) {
            return cityB;
        } else if (originCity == cityB) {
            return cityA;
        }
        return null;
    }

    public void setPheromone(double p) {
        //checking to see if changes
        if(p < minPheromone) {
            pheromone = minPheromone;
        } else {
            pheromone = p;
        }
    }
    public void addPheromone(double addedValue) {
        //add new pheromone values, if it would move past the limit then set it to the limit.
        double newPheromone = pheromone + addedValue;
        if (newPheromone > maxPheromone) {
            pheromone = maxPheromone;
        } else {
            pheromone = newPheromone;
        }
    }

    public double getDesire() {
        double desire = Math.pow(pheromone, alpha) * (Math.pow((1/distance), beta));
        return desire;
    }

    public double getDistance() {
        return distance;
    }

    public double getPheromone() {
        return pheromone;
    }

    public City getCityA() {
        return cityA;
    }

    public City getCityB() {
        return cityB;
    }

    public void takePath() {
        timesTaken ++;
    }

}
