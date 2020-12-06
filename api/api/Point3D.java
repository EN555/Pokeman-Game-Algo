package api;

public class Point3D implements geo_location {

	private double x;
	private double y;
	private double z;
	
	public static final Point3D ORIGIN = new Point3D(0,0,0);
	
	//constructor
	public Point3D (double x , double y , double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	//copy constructor
	public Point3D(geo_location other) {
		this(other.x() , other.y() , other.z());
	}
	
	//constructor from string
	public Point3D(String data) {
		String[] x_y_z = data.split(",");
		this.x = Double.parseDouble(x_y_z[0]);
		this.y = Double.parseDouble(x_y_z[1]);
		this.z = Double.parseDouble(x_y_z[2]);
	}
	
	/**
	 * @return the x of this point
	 */
	@Override
	public double x() {
		return this.x;
	}

	/**
	 * @return the y of this point
	 */
	@Override
	public double y() {
		return this.y;
	}

	/**
	 * @return the z of this point
	 */
	@Override
	public double z() {
		return this.z;
	}

	/**
	 * calculate the distance from this point to the given point
	 */
	@Override
	public double distance(geo_location g) {
		double xs = Math.pow((this.x - g.x()), 2);
		double ys = Math.pow((this.y - g.y()), 2);
		double zs = Math.pow((this.z - g.z()), 2);
		
		double dis = Math.sqrt(xs + ys + zs);
		
		return dis;
	}

	public String toString() {
		return this.x+","+this.y+","+this.z;
	}
	
	public boolean equals(Object other) {
		if(!(other instanceof geo_location)) {return false;}
		geo_location otherO = (geo_location)other;
		return ((this.x == otherO.x()) && (this.y == otherO.y()) && (this.z == otherO.z()));
	}
	
	
}
