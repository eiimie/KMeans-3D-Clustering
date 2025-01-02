import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClusterGenerator {
    public static List<Point3D> generateCluster(
        double centreX, 
        double centreY, 
        double centreZ, 
        double radius, 
        int numPoints, 
        Random random
    ) {
        List<Point3D> cluster = new ArrayList<>();

        for (int i = 0; i < numPoints; i++) {
            // generate points using Gaussian distribution
            double x = centreX + random.nextGaussian() * radius;
            double y = centreY + random.nextGaussian() * radius;
            double z = centreZ + random.nextGaussian() * radius;

            cluster.add(new Point3D(x, y, z));
        }

        return cluster;
    }

    public static List<Point3D> generateOverlappingClusters(
        int numClusters, 
        double baseRadius, 
        int pointsPerCluster, 
        Random random
    ) {
        List<Point3D> allPoints = new ArrayList<>();
        Point3D[] centers = new Point3D[numClusters];

        // generate centres in triangle formation
        double triangleRadius = baseRadius * 3; // distance from centre to vertices
        double angleStep = 2 * Math.PI / numClusters;

        for (int i = 0; i < numClusters; i++) {
            double angle = i * angleStep;
            // create triangle vertices with different Z coordinates
            centers[i] = new Point3D(
                triangleRadius * Math.cos(angle),
                triangleRadius * Math.sin(angle),
                baseRadius * (i - 1) // ensures different Z planes
            );
        }

        // verify centres aren't coplanar
        verifyNonCoplanar(centers);

        // generate clusters with controlled overlap
        for (int i = 0; i < numClusters; i++) {
            // increase radius to create ~10% overlap
            double overlapRadius = baseRadius * 1.2; // adjusted for approximate 10% overlap
            allPoints.addAll(generateCluster(
                centers[i].x, 
                centers[i].y, 
                centers[i].z, 
                overlapRadius, 
                pointsPerCluster, 
                random
            ));
        }

        return allPoints;
    }

    private static void verifyNonCoplanar(Point3D[] centers) {
        if (centers.length < 3) return;
        
        // calculate normal vector of first three points
        Point3D v1 = new Point3D(
            centers[1].x - centers[0].x,
            centers[1].y - centers[0].y,
            centers[1].z - centers[0].z
        );
        Point3D v2 = new Point3D(
            centers[2].x - centers[0].x,
            centers[2].y - centers[0].y,
            centers[2].z - centers[0].z
        );

        // cross product should not be zero
        double crossX = v1.y * v2.z - v1.z * v2.y;
        double crossY = v1.z * v2.x - v1.x * v2.z;
        double crossZ = v1.x * v2.y - v1.y * v2.x;

        double magnitude = Math.sqrt(crossX * crossX + crossY * crossY + crossZ * crossZ);
        if (magnitude < 1e-10) {
            throw new IllegalStateException("Generated centers are coplanar");
        }
    }
}
