import java.util.List;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;

public class KMeans3D {
	// main class 2 implement the clustering algorithm
    private List<Point3D> points;
    private Point3D[] centroids;
    private int[] assignments; 
    private int k;
    private int maxIterations; 

    public KMeans3D(List<Point3D> points, int k, int maxIterations) {
        this.points = points;
        this.k = k;
        this.maxIterations = maxIterations; 
        this.centroids = new Point3D[k];
        this.assignments = new int[points.size()];
    }

    public void fit() { 
        // initialise centroids randomly from existing points
        Random rand = new Random();
        Set<Integer> usedIndices = new HashSet<>();
        for (int i = 0; i < k; i++) {
            int index; 
            do { 
                index = rand.nextInt(points.size()); 
            } while (usedIndices.contains(index));
            usedIndices.add(index);
            centroids[i] = points.get(index);  // Assign centroid
        }
        
        boolean changed; 
        int iteration = 0;
        
        do { 
            changed = false;
            
            // assign points to nearest centroid 
            for (int i = 0; i < points.size(); i++) {
                int nearestCentroid = findNearestCentroid(points.get(i));
                if (assignments[i] != nearestCentroid) {
                    assignments[i] = nearestCentroid;
                    changed = true;
                }
            }
            
            // update centroids
            updateCentroids();
            
            iteration++;
        } while (changed && iteration < maxIterations); 
    }

    // Moved findNearestCentroid method outside fit()
    private int findNearestCentroid(Point3D point) { 
        int nearest = 0;
        double minDistance = Double.MAX_VALUE;
        
        for (int i = 0; i < k; i++) {
            double distance = point.distanceTo(centroids[i]);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = i;
            }
        }
        
        return nearest;
    }

    // Moved updateCentroids method outside fit()
    private void updateCentroids() { 
        double[] sumX = new double[k];
        double[] sumY = new double[k];
        double[] sumZ = new double[k];
        int[] counts = new int[k];
        
        // calculates sums for each cluster
        for (int i = 0; i < points.size(); i++) {
            Point3D point = points.get(i);
            int cluster = assignments[i];
            
            sumX[cluster] += point.x;
            sumY[cluster] += point.y;
            sumZ[cluster] += point.z;
            counts[cluster]++;
        }
        
        // update centroid positions
        for (int i = 0; i < k; i++) {
            if (counts[i] > 0) {
                centroids[i] = new Point3D(
                        sumX[i] / counts[i],
                        sumY[i] / counts[i],
                        sumZ[i] / counts[i]
                );
            }
        }
    }

    public void printResults() {
        System.out.println("X,Y,Z,Cluster");
        for (int i = 0; i < points.size(); i++) {
            System.out.println(points.get(i).toString() + "," + assignments[i]);
        }
        
        System.out.println("\nCentroid Locations:");
        System.out.println("Cluster,X,Y,Z");
        for (int i = 0; i < k; i++) {
            System.out.println(i + "," + centroids[i].toString());
        }
    }
    
    public static void main(String[] args) {
        // Example usage
        List<Point3D> points = new ArrayList<>();
        points.add(new Point3D(1.0, 1.0, 1.0));
        points.add(new Point3D(1.5, 1.5, 1.5));
        points.add(new Point3D(3.0, 3.0, 3.0));
        points.add(new Point3D(3.5, 3.5, 3.5));
        points.add(new Point3D(5.0, 5.0, 5.0));
        points.add(new Point3D(5.5, 5.5, 5.5));
        
        KMeans3D kmeans = new KMeans3D(points, 3, 100);
        kmeans.fit();
        kmeans.printResults();
    }
}
