import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ClusterVisualiser extends JPanel {
    private List<Point3D> points;
    private int[] assignments;
    private int k;

    public ClusterVisualiser(List<Point3D> points, int[] assignments, int k) {
        this.points = points;
        this.assignments = assignments;
        this.k = k;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Define colors for clusters
        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA};
        
        // Make sure we have enough colors for clusters
        if (k > colors.length) {
            throw new IllegalArgumentException("Increase the number of predefined colors for clusters.");
        }

        // draw each point
        for (int i = 0; i < points.size(); i++) {
            Point3D point = points.get(i);
            int cluster = assignments[i];
            g2d.setColor(colors[cluster % colors.length]);

            // project 3D to 2D (X, Y plane) for simplicity - no Z 
            int x = (int) (getWidth() / 2 + point.x * 50); // scale and centre
            int y = (int) (getHeight() / 2 - point.y * 50); // scale and centre (invert Y for graphical orientation)
            g2d.fillOval(x - 2, y - 2, 5, 5); // draw point
        }
    }

    public static void visualise(List<Point3D> points, int[] assignments, int k) {
        JFrame frame = new JFrame("Cluster Visualiation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.add(new ClusterVisualiser(points, assignments, k));
        frame.setVisible(true);
    }
}
