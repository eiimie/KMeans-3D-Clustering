import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.Color;
import java.util.Random;

public class ClusterVisualiser extends JPanel {
    private List<Point3D> points;
    private int[] assignments;
    private Point3D[] centroids;
    private int k;

    public ClusterVisualiser(List<Point3D> points, int[] assignments, Point3D[] centroids, int k) {
        this.points = points;
        this.assignments = assignments;
        this.centroids = centroids;
        this.k = k;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // define colours for clusters (add more if needed)
        Color[] colors = {Color.RED, Color.PINK, Color.ORANGE, Color.ORANGE, Color.MAGENTA};

        // ensure enough colours for clusters
        if (k > colors.length) {
            throw new IllegalArgumentException("Increase the number of predefined colors for clusters.");
        }

        // Draw points
        for (int i = 0; i < points.size(); i++) {
            Point3D point = points.get(i);
            int cluster = assignments[i];
            g2d.setColor(colors[cluster % colors.length]);

            // Project 3D to 2D (X, Y plane)
            int x = (int) (getWidth() / 2 + point.x * 50); // Scale and center
            int y = (int) (getHeight() / 2 - point.y * 50); // Invert Y for orientation
            g2d.fillOval(x - 2, y - 2, 5, 5); // Draw point
        }

        // Draw centroids
        for (int i = 0; i < centroids.length; i++) {
            Point3D centroid = centroids[i];
            g2d.setColor(Color.BLACK); // Centroids are black for distinction
            int x = (int) (getWidth() / 2 + centroid.x * 50);
            int y = (int) (getHeight() / 2 - centroid.y * 50);

            // Draw larger circle
            g2d.fillOval(x - 6, y - 6, 12, 12);

            // Draw crosshairs
            g2d.drawLine(x - 10, y, x + 10, y);
            g2d.drawLine(x, y - 10, x, y + 10);
        }
    }

    public static void visualise(List<Point3D> points, int[] assignments, Point3D[] centroids, int k) {
        JFrame frame = new JFrame("Cluster Visualisation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.add(new ClusterVisualiser(points, assignments, centroids, k));
        frame.setVisible(true);
    }
}
