
public class Point3D {
	// class to represent 3D points w/ 3 parameters: x, y, z
	double x, y, z;
	
	public Point3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double distanceTo(Point3D other) {
		return Math.sqrt(
			Math.pow(this.x - other.x, 2) +
			Math.pow(this.y - other.y, 2) +
			Math.pow(this.z - other.z, 2));
	}
	
	@Override
	public String toString() { 
		return String.format("%.2f,%.2f,%.2f", x, y, z);
	};
}
