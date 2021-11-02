package algorithms.dijkstra.model;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Vertex {
	final private String id;
	final private int iId;
	final private int polyId;
	final private String name;
	final private Vector2D vertex1;
	final private Vector2D vertex2;
	final private double distance;

	public Vertex(String id, int iId, int polyId, String name, Vector2D vertex1, Vector2D vertex2, double distance) {
		this.id = id;
		this.iId = iId;
		this.polyId = polyId;
		this.name = name;
		this.vertex1 = vertex1;
		this.vertex2 = vertex2;
		this.distance = distance;
	}

	public String getId() {
		return id;
	}

	public int getIID() {
		return iId;
	}
	
	public int getPolyId() {
		return polyId;
	}
	
	public String getName() {
		return name;
	}
	
	public Vector2D getVertex1() {
		return vertex1;
	}
	
	public Vector2D getVertex2() {
		return vertex2;
	}
	
	public double getDistance() {
		return distance;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}
		Vertex other = (Vertex) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return name;
	}
}
