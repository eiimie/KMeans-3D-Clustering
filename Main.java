import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Random random = new Random(); 

        // generate three clusters with a 10% overlap:
        int numClusters = 3; 
        double clusterRadius = 0.8;
        int pointsPerCluster = 100; 
        
        List<Point3D> allPoints = ClusterGenerator.generateOverlappingClusters(numClusters, clusterRadius, pointsPerCluster, random);

        // print original points to copy into Excel
        System.out.println("Original Points (X,Y,Z):");
        for (Point3D point : allPoints) {
            System.out.printf("%.4f,%.4f,%.4f\n", point.x, point.y, point.z);
        }

        // run k-means implementation
        KMeans3D kmeans = new KMeans3D(allPoints, 3, 100);
        kmeans.fit();
        kmeans.printResults();
        kmeans.printPerformanceReport();

        // export to Excel
        String outputFilename = "kmeans_3d_results.csv";
        CSVExporter.exportToCSV(outputFilename, kmeans); 
        
        // cluster visualisation
        ClusterVisualiser.visualise(kmeans.getPoints(), kmeans.getAssignments(), kmeans.getCentroids(), 3);
    }
}
