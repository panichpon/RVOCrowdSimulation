package polygonmaker;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class JSONGraph {
	public int id;
	public int polyId;
	public Vector2D vertex1;
	public Vector2D vertex2;
	public double distance;
	public boolean isNode;

	public JSONGraph() {
		// TODO Auto-generated constructor stub
	}
	
	public JSONGraph(int id, int polyId, Vector2D vertex1, Vector2D vertex2, double distance, boolean isNode) {
		super();
		this.id = id;
		this.polyId = polyId;
		this.vertex1 = vertex1;
		this.vertex2 = vertex2;
		this.distance = distance;
		this.isNode = isNode;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Vector2D getVertex1() {
		return vertex1;
	}

	public void setVertex1(Vector2D vertex1) {
		this.vertex1 = vertex1;
	}

	public Vector2D getVertex2() {
		return vertex2;
	}

	public void setVertex2(Vector2D vertex2) {
		this.vertex2 = vertex2;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public boolean isNode() {
		return isNode;
	}

	public void setNode(boolean isNode) {
		this.isNode = isNode;
	}
	
	public int getPolyId() {
		return polyId;
	}

	public void setPolyId(int polyId) {
		this.polyId = polyId;
	}

	@Override
	public String toString() {
		return "JSONGraph [id=" + id + ", vertex1=" + vertex1.toString() + ", vertex2=" + vertex2.toString() + ", distance=" + distance + "]";
	}

}
