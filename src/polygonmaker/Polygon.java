package polygonmaker;

import java.util.Vector;

public class Polygon implements Comparable<Polygon> {

	public int id;
	public Vector<PolygonPoint> points;
	public double distance;
	public int uuid;

	public Polygon() {
		// TODO Auto-generated constructor stub
	}

	public Polygon(int id, Vector<PolygonPoint> points) {
		this.id = id;
		this.points = points;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Vector<PolygonPoint> getPoints() {
		return points;
	}

	public void setPoints(Vector<PolygonPoint> points) {
		this.points = points;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("[Polygon] id:" + id);
		for(int i=0; i<points.size(); i++) {
			PolygonPoint point = points.get(i);
			buf.append(", polyId:" + point.polyId + ", id:" + point.id + ", point: " + point.point.toString());
		}
		buf.append(", distance:" + distance);
		return buf.toString();
	}
	
	@Override
	public int compareTo(Polygon o) {

		if (this.points.elementAt(0).polyId < o.points.elementAt(0).polyId) {
			return -1;
		} else if (this.points.elementAt(0).polyId > o.points.elementAt(0).polyId) {
			return 1;
		} else if (this.distance < o.distance) {
			return -1;
		} else if (this.distance > o.distance) {
			return 1;
		} else if (this.points.elementAt(0).id < o.points.elementAt(0).id) {
			return -1;
		} else if (this.points.elementAt(0).id > o.points.elementAt(0).id) {
			return 1;
		} else if (this.points.elementAt(1).polyId < o.points.elementAt(1).polyId) {
			return -1;
		} else if (this.points.elementAt(1).polyId > o.points.elementAt(1).polyId) {
			return 1;
		}
		return 0;
	}
}
