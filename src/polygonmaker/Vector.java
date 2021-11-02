package polygonmaker;

public class Vector {
	double x;
	double y;

	/**
	 * 
	 */
	public Vector() {
		;
	}

	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public void printPoint(Vector vector) {
		System.out.println(vector.getX() + ", " + vector.getY());
	}
}
