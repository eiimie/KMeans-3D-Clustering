import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Random random = new Random(42); // Fixed seed for reproducibility
        List<Point3D> allPoints = new ArrayList<>();

        // generate three clusters in triangle formation, each at different z levels
        allPoints.addAll(ClusterGenerator.generateCluster(0, 0, 0, 0.5, 100, random));
        allPoints.addAll(ClusterGenerator.generateCluster(5, 5, 3, 0.5, 100, random));
        allPoints.addAll(ClusterGenerator.generateCluster(-4, 4, -2, 0.5, 100, random));

        // print original points to copy into Excel
        System.out.println("Original Points (X,Y,Z):");
        for (Point3D point : allPoints) {
            System.out.printf("%.4f,%.4f,%.4f\n", point.x, point.y, point.z);
        }

        // run k-means implementation
        KMeans3D kmeans = new KMeans3D(allPoints, 3, 100);
        kmeans.fit();
        kmeans.printResults();

        // export to excel
        String outputFilename = "kmeans_3d_results.csv";
        CSVExporter.exportToCSV(outputFilename, kmeans);  // Passing KMeans3D object
    }
}
