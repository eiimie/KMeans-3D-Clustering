import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class ClusterVisualiser extends JPanel {
    private List<Point3D> points;
    private int[] assignments;
    private Point3D[] centroids;
    private int k;
    private Color[] clusterColors; // store our random colours

    public ClusterVisualiser(List<Point3D> points, int[] assignments, Point3D[] centroids, int k) {
        this.points = points;
        this.assignments = assignments;
        this.centroids = centroids;
        this.k = k;
        
        // generate random colours when the visualiser is created
        generateRandomColors();
    }
    
    private void generateRandomColors() {
        Random rand = new Random();
        clusterColors = new Color[k];
        
        for (int i = 0; i < k; i++) {
            // make sure colours aren't too dark or too light
            float hue = rand.nextFloat();
            float saturation = (rand.nextFloat() * 0.5f) + 0.5f; // 0.5 to 1.0
            float brightness = (rand.nextFloat() * 0.5f) + 0.5f; // 0.5 to 1.0
            
            clusterColors[i] = Color.getHSBColor(hue, saturation, brightness);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // turn on antialiasing for smoother drawing
        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        // draw points
        for (int i = 0; i < points.size(); i++) {
            Point3D point = points.get(i);
            int cluster = assignments[i];
            g2d.setColor(clusterColors[cluster]);

            // project 3d to 2d (x, y plane)
            int x = (int) (getWidth() / 2 + point.x * 50);
            int y = (int) (getHeight() / 2 - point.y * 50);
            g2d.fillOval(x - 2, y - 2, 5, 5);
        }

        // draw centroids
        g2d.setStroke(new BasicStroke(2)); // thicker lines for centroids
        for (int i = 0; i < centroids.length; i++) {
            Point3D centroid = centroids[i];
            
            // draw centroid in black with coloured border
            int x = (int) (getWidth() / 2 + centroid.x * 50);
            int y = (int) (getHeight() / 2 - centroid.y * 50);

            // coloured outer circle
            g2d.setColor(clusterColors[i]);
            g2d.drawOval(x - 6, y - 6, 12, 12);
            
            // black inner circle
            g2d.setColor(Color.BLACK);
            g2d.fillOval(x - 4, y - 4, 8, 8);
        }
        
        // add a legend
        drawLegend(g2d);
    }
    
    private void drawLegend(Graphics2D g2d) {
        int legendX = 20;
        int legendY = 20;
        int swatchSize = 15;
        
        for (int i = 0; i < k; i++) {
            // draw colour swatch
            g2d.setColor(clusterColors[i]);
            g2d.fillRect(legendX, legendY + (i * 25), swatchSize, swatchSize);
            
            // draw label
            g2d.setColor(Color.BLACK);
            g2d.drawString("Cluster " + i, legendX + swatchSize + 10, legendY + (i * 25) + swatchSize);
        }
    }

    public static void visualise(List<Point3D> points, int[] assignments, Point3D[] centroids, int k) {
        JFrame frame = new JFrame("cluster visualisation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.add(new ClusterVisualiser(points, assignments, centroids, k));
        frame.setVisible(true);
    }
}