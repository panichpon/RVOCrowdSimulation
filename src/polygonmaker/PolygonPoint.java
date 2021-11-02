package polygonmaker;

import java.util.Vector;
import java.awt.Point;

public class PolygonPoint implements Comparable<PolygonPoint> {
	public int polyId; // polygon id to which this point belongs
	public int id;
	public Point point;
	public Vector<ConnectedPoints> connectedList;
	public PolygonPoint(int pid, int id, Point p) {
		polyId = pid;
		this.id = id;
		point = new Point(p.x, p.y);
		
		connectedList = new Vector<ConnectedPoints>();
		
	}
	
	@Override
	public int compareTo(PolygonPoint o) {
		if(this.point.x < o.point.x) {
			return -1;
		} else if (this.point.x > o.point.x) {
			return 1;
		} else if (this.point.y < o.point.y) {
			return -1;
		} else if (this.point.y > o.point.y) {
			return 1;
		} 
		
		return 0;
	}

	@Override
	public String toString() {
		
		return "[polyId: "+polyId+", id: "+id+", point: "+point.toString()+"]";
	}
	
	
}
