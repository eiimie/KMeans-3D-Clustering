import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVExporter {
    public static void exportToCSV(String filename, KMeans3D kmeans) {
        try (FileWriter writer = new FileWriter(filename)) {
            // Write header
            writer.append("X,Y,Z,Cluster\n");

            // Write points and their cluster assignments using getters
            List<Point3D> points = kmeans.getPoints();
            int[] assignments = kmeans.getAssignments();

            for (int i = 0; i < points.size(); i++) {
                Point3D point = points.get(i);
                writer.append(String.format("%.4f,%.4f,%.4f,%d\n", point.x, point.y, point.z, assignments[i]));
            }

            System.out.println("Results exported to " + filename);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
