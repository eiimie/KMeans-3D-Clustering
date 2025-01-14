import java.util.List;
import java.util.ArrayList;
import java.time.Instant;
import java.time.Duration;

public class KMeansPerformanceLogger {
    private List<IterationMetrics> iterationMetrics;
    private Instant startTime;
    private Instant endTime;
    private int totalIterations;
    private double[][] centroids;
    
    public KMeansPerformanceLogger(int numClusters, int numFeatures) {
        // Initialize the iterationMetrics list in constructor
        this.iterationMetrics = new ArrayList<>();
        
        // Initialize centroids array
        this.centroids = new double[numClusters][numFeatures];
    }
    
    public void startLogging() {
        this.startTime = Instant.now();
    }
    
    public void endLogging() {
        this.endTime = Instant.now();
    }
    
    public void logIteration(List<Point3D> points, Point3D[] centroids, int[] assignments, int iteration) {
        double distortion = calculateDistortion(points, centroids, assignments);
        double silhouetteScore = calculateSilhouetteScore(points, centroids, assignments);
        
        iterationMetrics.add(new IterationMetrics(
            iteration,
            Instant.now(),
            distortion,
            silhouetteScore
        ));
        
        totalIterations = iteration;
    }
    
    private double calculateDistortion(List<Point3D> points, Point3D[] centroids, int[] assignments) {
        double totalDistortion = 0.0;
        for (int i = 0; i < points.size(); i++) {
            Point3D point = points.get(i);
            Point3D centroid = centroids[assignments[i]];
            totalDistortion += Math.pow(point.distanceTo(centroid), 2);
        }
        return totalDistortion;
    }
    
    private double calculateSilhouetteScore(List<Point3D> points, Point3D[] centroids, int[] assignments) {
        double totalSilhouette = 0.0;
        
        for (int i = 0; i < points.size(); i++) {
            Point3D point = points.get(i);
            int cluster = assignments[i];
            
            // Calculate a (average distance to points in same cluster)
            double a = calculateAverageIntraClusterDistance(point, points, assignments, cluster);
            
            // Calculate b (average distance to points in nearest neighbor cluster)
            double b = calculateNearestNeighborClusterDistance(point, points, assignments, cluster);
            
            // Calculate silhouette coefficient for this point
            double silhouette = (b - a) / Math.max(a, b);
            totalSilhouette += silhouette;
        }
        
        return totalSilhouette / points.size();
    }
    
    private double calculateAverageIntraClusterDistance(Point3D point, List<Point3D> points, 
                                                      int[] assignments, int cluster) {
        double totalDistance = 0.0;
        int count = 0;
        
        for (int i = 0; i < points.size(); i++) {
            if (assignments[i] == cluster && points.get(i) != point) {
                totalDistance += point.distanceTo(points.get(i));
                count++;
            }
        }
        
        return count > 0 ? totalDistance / count : 0.0;
    }
    
    private double calculateNearestNeighborClusterDistance(Point3D point, List<Point3D> points, 
                                                         int[] assignments, int cluster) {
        double minAverageDistance = Double.MAX_VALUE;
        
        for (int otherCluster = 0; otherCluster < centroids.length; otherCluster++) {
            if (otherCluster == cluster) continue;
            
            double totalDistance = 0.0;
            int count = 0;
            
            for (int i = 0; i < points.size(); i++) {
                if (assignments[i] == otherCluster) {
                    totalDistance += point.distanceTo(points.get(i));
                    count++;
                }
            }
            
            if (count > 0) {
                double averageDistance = totalDistance / count;
                minAverageDistance = Math.min(minAverageDistance, averageDistance);
            }
        }
        
        return minAverageDistance;
    }
    
    public void printPerformanceReport() {
        if (startTime == null || endTime == null || iterationMetrics.isEmpty()) {
            System.out.println("No performance data available.");
            return;
        }

        Duration totalTime = Duration.between(startTime, endTime);
        
        System.out.println("\nK-Means Performance Report");
        System.out.println("==========================");
        System.out.println("Total execution time: " + totalTime.toMillis() + "ms");
        System.out.println("Number of iterations: " + totalIterations);
        System.out.println("\nIteration Metrics:");
        System.out.println("Iteration,Time(ms),Distortion,Silhouette Score");
        
        Instant firstIteration = iterationMetrics.get(0).timestamp;
        for (IterationMetrics metrics : iterationMetrics) {
            System.out.printf("%d,%d,%.4f,%.4f\n",
                metrics.iteration,
                Duration.between(firstIteration, metrics.timestamp).toMillis(),
                metrics.distortion,
                metrics.silhouetteScore
            );
        }
        
        // Print final metrics
        IterationMetrics final_metrics = iterationMetrics.get(iterationMetrics.size() - 1);
        System.out.println("\nFinal Metrics:");
        System.out.printf("Final Distortion: %.4f\n", final_metrics.distortion);
        System.out.printf("Final Silhouette Score: %.4f\n", final_metrics.silhouetteScore);
    }
    
    private static class IterationMetrics {
        int iteration;
        Instant timestamp;
        double distortion;
        double silhouetteScore;
        
        IterationMetrics(int iteration, Instant timestamp, double distortion, double silhouetteScore) {
            this.iteration = iteration;
            this.timestamp = timestamp;
            this.distortion = distortion;
            this.silhouetteScore = silhouetteScore;
        }
    }
}
