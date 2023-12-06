
import java.util.ArrayList;

public class City {

    //this class is mostly just a storage class for the nodes in the graph. Contains a list of all paths from this city
    //as well as an ID value to represent it in numerical form.
    private int timesVisited;
    private int cityIndex;
    private ArrayList<Path> paths = new ArrayList<Path>();

    public City(int index) {
        cityIndex = index;
    }

    public void addPath(Path path) {
        paths.add(path);
    }

    public ArrayList<Path> getPaths() {
        return paths;
    }

    public int getCityIndex() {
        return cityIndex;
    }

    public int getNoOfPaths() {
        return paths.size();
    }

    public int getTimesVisited() {
        return timesVisited;
    }
    public void visitCity() {
        timesVisited++;
    }

}
