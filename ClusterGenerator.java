import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class ClusterGenerator {
    // creates synthetic 2D data points
    // to demonstrate and test clustering
    public static List<Point3D> generateCluster(
        double centreX, 
        double centreY, 
        double centreZ,
        double radius, 
        int numPoints, 
        Random random) {
        List<Point3D> cluster = new ArrayList<>();

        for (int i = 0; i < numPoints; i++) { 
            // generate rando angle and distance in cluster
            double phi = random.nextDouble() * 2 * Math.PI;
            double theta = Math.acos(2 * random.nextDouble() - 1);
            double distance = radius * Math.cbrt(random.nextDouble());

            // convert spherical coords 2 cartesian coordinates
            double x = centreX + distance * Math.sin(theta) * Math.cos(phi);
            double y = centreY + distance * Math.sin(theta) * Math.sin(phi);
            double z = centreZ + distance * Math.cos(theta);

            cluster.add(new Point3D(x,y,z));
        }

        return cluster;
    }

    // example usage... 
    public static void main(String[] args) {
        Random random = new Random(42); 

        // generate 3 test clusters 
        List<Point3D> allPoints = new ArrayList<>();
        allPoints.addAll(generateCluster(0,0,0,1.0,100,random));
        allPoints.addAll(generateCluster(5,5,5,1.0,100,random));
        allPoints.addAll(generateCluster(-3,-3,-3,1.0,100,random));

        // use these points w/ KMeans3d
        KMeans3D kmeans = new KMeans3D(allPoints,3,100);
        kmeans.fit();
        kmeans.printResults();
    }

}
