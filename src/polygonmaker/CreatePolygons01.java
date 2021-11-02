package polygonmaker;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.*;

import javax.swing.*;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

public class CreatePolygons01 extends JPanel implements MouseListener, MouseMotionListener,
	KeyListener {

	private static final long serialVersionUID = 1L;
	static Vector<Polygon> polygons;
	static Vector<Polygon> polygons2;
	static Vector<Polygon> connects;
	static Vector<Polygon> connects2;
	static Vector<Polygon> graphConnects;
	static Vector<Polygon> graphConnects2;
	public ArrayList<JSONGraph> graphExport;
	public ArrayList<JSONGraph> graphExport2;
	
	Polygon polygon;
	Polygon polygon2;
	PolygonPoint tempPoint;
	PolygonPoint finishedPoint;
	Settings settings;
	int uuid = 0;
	int polyId = 0;
	int conId = 0;
	
	BufferedImage image = null;
	int width = 0;
	int height = 0;
	static boolean showMap = true;
	static boolean showConnects = false;
	static boolean showGraphConnects = true;
	static boolean mouseIsIn = false;

	public CreatePolygons01() {

		String imageStr = "map.png";
		try {
			image = ImageIO.read(new File(imageStr));
			width = image.getWidth();
			height = image.getHeight();
			System.out.println("image size is: " + width + "x" + height);
		} catch(IOException ex) {
			System.out.println("Error! Cannot load image (" + ex.getMessage() + ")");
		}
		
		setPreferredSize(new Dimension(width, height));
		setBackground(Color.WHITE);
		setFocusable(true);

		/*
		 * Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); int
		 * centerX = screenSize.width / 2; int centerY = screenSize.height / 2;
		 * setLocation(centerX, centerY); System.out.println("centerX: " + centerX +
		 * ", centerY: " + centerY);
		 */

		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);

		polygons = new Vector<Polygon>();
		polygons2 = new Vector<Polygon>();
		connects = new Vector<Polygon>();
		connects2 = new Vector<Polygon>();
		graphConnects = new Vector<Polygon>();
		graphConnects2 = new Vector<Polygon>();

		finishedPoint = null;

		polygon = new Polygon();
		polygon.id = -1;
		polygon.points = new Vector<PolygonPoint>();
		
		tempPoint = new PolygonPoint(-1, -1, new Point(0, 0));
		
		settings = new Settings(uuid, polyId, conId);
		
		graphExport = new ArrayList<>();
		graphExport2 = new ArrayList<>();

	}

	void connectPolygons() {
		System.out.println("1: " + polygons2.size());
		for (int i = 0; i < polygons.size() - 1; i++) {
			for (int j = i + 1; j < polygons.size(); j++) {
				System.out.println("CHECKING CROSS polygons(i:j)=" + i + ":" + j);
				for (int k = 0; k < polygons.elementAt(i).points.size(); k++) {
					for (int l = 0; l < polygons.elementAt(j).points.size(); l++) {
						System.out.println("\tPOINTS->k:l=" + k + ":" + l + " ["
								+ polygons.elementAt(i).points.elementAt(k).point.x + ":"
								+ polygons.elementAt(i).points.elementAt(k).point.y + "-"
								+ polygons.elementAt(j).points.elementAt(l).point.x + ":"
								+ polygons.elementAt(j).points.elementAt(l).point.y + "]");

						if (!hasCrossedPolygons(polygons.elementAt(i).points.elementAt(k),
								polygons.elementAt(j).points.elementAt(l))) {
							System.out.println("\t\thasCrossed:FALSE");
							Polygon p = new Polygon();
							p.id = conId++;
							p.uuid = uuid++;
							p.points = new Vector<PolygonPoint>();
							p.points.add(polygons.elementAt(i).points.elementAt(k));
							p.points.add(polygons.elementAt(j).points.elementAt(l));
							double dist = distance(polygons.elementAt(i).points.elementAt(k),
									polygons.elementAt(j).points.elementAt(l));
							p.distance = dist;

							connects.add(p);

						} else {
							System.out.println("\t\thasCrossed:TRUE");
						}
					}
					System.out.println("--------------------------------");
				}
			}
			System.out.println("================================\n");
		}

	}
	
	void connectPolygons2() {
		System.out.println("2: "+polygons2.size());
		for (int i = 0; i < polygons2.size() - 1; i++) {
			for (int j = i + 1; j < polygons2.size(); j++) {
				System.out.println("CHECKING CROSS polygons(i:j)=" + i + ":" + j);
				for (int k = 0; k < polygons2.elementAt(i).points.size(); k++) {
					for (int l = 0; l < polygons2.elementAt(j).points.size(); l++) {
						System.out.println("\tPOINTS->k:l=" + k + ":" + l + " ["
								+ polygons2.elementAt(i).points.elementAt(k).point.x + ":"
								+ polygons2.elementAt(i).points.elementAt(k).point.y + "-"
								+ polygons2.elementAt(j).points.elementAt(l).point.x + ":"
								+ polygons2.elementAt(j).points.elementAt(l).point.y + "]");

						if (!hasCrossedPolygons2(polygons2.elementAt(i).points.elementAt(k),
								polygons2.elementAt(j).points.elementAt(l))) {
							System.out.println("\t\thasCrossed:FALSE");
							Polygon p = new Polygon();
							p.id = conId++;
							p.uuid = uuid++;
							p.points = new Vector<PolygonPoint>();
							p.points.add(polygons2.elementAt(i).points.elementAt(k));
							p.points.add(polygons2.elementAt(j).points.elementAt(l));
							double dist = distance(polygons2.elementAt(i).points.elementAt(k),
									polygons2.elementAt(j).points.elementAt(l));
							p.distance = dist;

							connects2.add(p);

						} else {
							System.out.println("\t\thasCrossed:TRUE");
						}
					}
					System.out.println("--------------------------------");
				}
			}
			System.out.println("================================\n");
		}

	}

	// ****************************************************************************
	// Check if two lines intersect
	// https://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/
	// Given three colinear points p, q, r, the function checks if
	// point q lies on line segment 'pr'
	// ****************************************************************************
	static boolean onSegment(PolygonPoint p, PolygonPoint q, PolygonPoint r) {
		if (q.point.x <= Math.max(p.point.x, r.point.x) && q.point.x >= Math.min(p.point.x, r.point.x)
				&& q.point.y <= Math.max(p.point.y, r.point.y) && q.point.y >= Math.min(p.point.y, r.point.y)) {
			return true;
		}
		return false;
	}

	// To find orientation of ordered triplet (p, q, r).
	// The function returns following values
	// 0 --> p, q and r are colinear
	// 1 --> Clockwise
	// 2 --> Counterclockwise
	static int orientation(PolygonPoint p, PolygonPoint q, PolygonPoint r) {
		// See https://www.geeksforgeeks.org/orientation-3-ordered-points/
		// for details of below formula.
		int val = (q.point.y - p.point.y) * (r.point.x - q.point.x) - (q.point.x - p.point.x) * (r.point.y - q.point.y);

		if (val == 0) {
			return 0; // colinear
		}
		return (val > 0) ? 1 : 2; // clock or counterclock wise
	}

	// The main function that returns true if line segment 'p1q1'
	// and 'p2q2' intersect.
	static boolean doIntersect(PolygonPoint p1, PolygonPoint q1, PolygonPoint p2, PolygonPoint q2) {
		// Find the four orientations needed for general and
		// special cases
		int o1 = orientation(p1, q1, p2);
		int o2 = orientation(p1, q1, q2);
		int o3 = orientation(p2, q2, p1);
		int o4 = orientation(p2, q2, q1);

		// General case
		if (o1 != o2 && o3 != o4)
			return true;

		// Special Cases
		// p1, q1 and p2 are colinear and p2 lies on segment p1q1
		if (o1 == 0 && onSegment(p1, p2, q1))
			return true;

		// p1, q1 and q2 are colinear and q2 lies on segment p1q1
		if (o2 == 0 && onSegment(p1, q2, q1))
			return true;

		// p2, q2 and p1 are colinear and p1 lies on segment p2q2
		if (o3 == 0 && onSegment(p2, p1, q2))
			return true;

		// p2, q2 and q1 are colinear and q1 lies on segment p2q2
		if (o4 == 0 && onSegment(p2, q1, q2))
			return true;

		return false; // Doesn't fall in any of the above cases
	}

	// ********************************************************************
	//
	// END OF CHECK
	//
	// ********************************************************************
	
	static boolean linesIntersect(PolygonPoint p1, PolygonPoint q1, PolygonPoint p2, PolygonPoint q2) {
		// Find the four orientations needed for general cases
		int o1 = orientation(p1, q1, p2);
		int o2 = orientation(p1, q1, q2);
		int o3 = orientation(p2, q2, p1);
		int o4 = orientation(p2, q2, q1);

		// General case
		if (o1 != o2 && o3 != o4)
			return true;
		return false;
	}

	// if the line connecting these two points ever
	// - cross, or
	// - be part of
	// any other line of any polygon
	boolean hasCrossedPolygons(PolygonPoint p0, PolygonPoint p1) {
		boolean crossed = false;
		for (Iterator<Polygon> it = polygons.iterator(); it.hasNext();) {
			Polygon pol = it.next();
			int pNum = pol.points.size();
			PolygonPoint pA = null;
			PolygonPoint pB = null;
			for (int i = 0; i < pNum - 1; i++) {
				pA = pol.points.elementAt(i);
				pB = pol.points.elementAt(i + 1);
				System.out.print("checking intersect p_i=" + i + " >> " + "" + p0.point.x + ":" + p0.point.y + "-"
						+ p1.point.x + ":" + p1.point.y + " --||-- " + pA.point.x + ":" + pA.point.y + "-" + pB.point.x
						+ ":" + pB.point.y);
				if (p0.point.x == pA.point.x && p0.point.y == pA.point.y
						|| p1.point.x == pB.point.x && p1.point.y == pB.point.y
						|| p0.point.x == pB.point.x && p0.point.y == pB.point.y
						|| p1.point.x == pA.point.x && p1.point.y == pA.point.y) {
					System.out.println(" Same points! Skip");
					continue;
				}
				if (doIntersect(p0, p1, pA, pB)) {
					System.out.println("==TRUE");
					return true;
				} else {
					System.out.println("==FALSE");
				}
			}
			pA = pol.points.elementAt(0);
			pB = pol.points.elementAt(pNum - 1);
			System.out.print("checking intersect p_i=" + (pNum - 1) + " >> " + "" + p0.point.x + ":" + p0.point.y + "-"
					+ p1.point.x + ":" + p1.point.y + " --||-- " + pA.point.x + ":" + pA.point.y + "-" + pB.point.x
					+ ":" + pB.point.y);
			if (p0.point.x == pA.point.x && p0.point.y == pA.point.y
					|| p1.point.x == pB.point.x && p1.point.y == pB.point.y
					|| p0.point.x == pB.point.x && p0.point.y == pB.point.y
					|| p1.point.x == pA.point.x && p1.point.y == pA.point.y) {
				System.out.println(" Same points! Skip");
				continue;
			}
			if (doIntersect(p0, p1, pA, pB)) {
				System.out.println("==TRUE");
				return true;
			} else {
				System.out.println("==FALSE");
			}
		}
		return crossed;
	}
	
	boolean hasCrossedPolygons2(PolygonPoint p0, PolygonPoint p1) {
		boolean crossed = false;
		for (Iterator<Polygon> it = polygons2.iterator(); it.hasNext();) {
			Polygon pol = it.next();
			int pNum = pol.points.size();
			PolygonPoint pA = null;
			PolygonPoint pB = null;
			for (int i = 0; i < pNum - 1; i++) {
				pA = pol.points.elementAt(i);
				pB = pol.points.elementAt(i + 1);
				System.out.print("checking intersect p_i=" + i + " >> " + "" + p0.point.x + ":" + p0.point.y + "-"
						+ p1.point.x + ":" + p1.point.y + " --||-- " + pA.point.x + ":" + pA.point.y + "-" + pB.point.x
						+ ":" + pB.point.y);
				if (p0.point.x == pA.point.x && p0.point.y == pA.point.y
						|| p1.point.x == pB.point.x && p1.point.y == pB.point.y
						|| p0.point.x == pB.point.x && p0.point.y == pB.point.y
						|| p1.point.x == pA.point.x && p1.point.y == pA.point.y) {
					System.out.println(" Same points! Skip");
					continue;
				}
				if (doIntersect(p0, p1, pA, pB)) {
					System.out.println("==TRUE");
					return true;
				} else {
					System.out.println("==FALSE");
				}
			}
			pA = pol.points.elementAt(0);
			pB = pol.points.elementAt(pNum - 1);
			System.out.print("checking intersect p_i=" + (pNum - 1) + " >> " + "" + p0.point.x + ":" + p0.point.y + "-"
					+ p1.point.x + ":" + p1.point.y + " --||-- " + pA.point.x + ":" + pA.point.y + "-" + pB.point.x
					+ ":" + pB.point.y);
			if (p0.point.x == pA.point.x && p0.point.y == pA.point.y
					|| p1.point.x == pB.point.x && p1.point.y == pB.point.y
					|| p0.point.x == pB.point.x && p0.point.y == pB.point.y
					|| p1.point.x == pA.point.x && p1.point.y == pA.point.y) {
				System.out.println(" Same points! Skip");
				continue;
			}
			if (doIntersect(p0, p1, pA, pB)) {
				System.out.println("==TRUE");
				return true;
			} else {
				System.out.println("==FALSE");
			}
		}
		return crossed;
	}

	// are these two lines (of Polygon) share a point
	public static boolean sharedPoint(Polygon l1, Polygon l2) {
		boolean shared = false;
		boolean case1 = l1.points.elementAt(0).point.x == l2.points.elementAt(0).point.x
				&& l1.points.elementAt(0).point.y == l2.points.elementAt(0).point.y;
		boolean case2 = l1.points.elementAt(0).point.x == l2.points.elementAt(1).point.x
				&& l1.points.elementAt(0).point.y == l2.points.elementAt(1).point.y;
		boolean case3 = l1.points.elementAt(1).point.x == l2.points.elementAt(0).point.x
				&& l1.points.elementAt(1).point.y == l2.points.elementAt(0).point.y;
		boolean case4 = l1.points.elementAt(1).point.x == l2.points.elementAt(1).point.x
				&& l1.points.elementAt(1).point.y == l2.points.elementAt(1).point.y;
		
		System.out.println("======");
		System.out.println(l1.toString());
		System.out.println(l2.toString());
		System.out.println("case1:" + case1 + ", case2:" + case2 + ", case3:" + case3 + ", case4:" + case4);
		
		if(case1 ||	 case2 || case3 || case4) {
			shared = true;
		}
		return shared;
	}


	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		if(image != null && showMap) {
			g.drawImage(image, 0, 0, null);
		}

		PolygonPoint p0 = null;
		PolygonPoint p1 = null;

		for (Iterator<Polygon> it = polygons.iterator(); it.hasNext();) {
			//System.out.println("Drawing complete polygons:" + polygons.size());
			g2.setColor(Color.RED);
			Polygon pol = it.next();
			for (int i = 0; i < pol.points.size() - 1; i++) {
				p0 = pol.points.elementAt(i);
				p1 = pol.points.elementAt(i + 1);
				g2.drawLine(p0.point.x, p0.point.y, p1.point.x, p1.point.y);

				g.drawString("PID:" + p0.polyId + ", pid:" + p0.id 
						+ "(" + p0.point.x + ", " + p0.point.y + ") - " 
						+ pol.id, p0.point.x, p0.point.y);
				g.drawString("PID:" + p1.polyId + ", pid:" + p1.id 
						+ "(" + p1.point.x + ", " + p1.point.y + ") - " 
						+ pol.id, p1.point.x, p1.point.y);
				g.drawString("("+ pol.id +")", p1.point.x, p1.point.y);
			}
			p0 = pol.points.elementAt(0);
			p1 = pol.points.elementAt(pol.points.size() - 1);
			g2.drawLine(p0.point.x, p0.point.y, p1.point.x, p1.point.y);

		}
		
		for (Iterator<Polygon> it = polygons2.iterator(); it.hasNext();) {
			//System.out.println("Drawing complete polygons:" + polygons.size());
			g2.setColor(Color.ORANGE);
			Polygon pol = it.next();
			for (int i = 0; i < pol.points.size() - 1; i++) {
				p0 = pol.points.elementAt(i);
				p1 = pol.points.elementAt(i + 1);
				g2.drawLine(p0.point.x, p0.point.y, p1.point.x, p1.point.y);

				g.drawString("PID:" + p0.polyId + ", pid:" + p0.id 
						+ "(" + p0.point.x + ", " + p0.point.y + ") - " 
						+ pol.id, p0.point.x, p0.point.y);
				g.drawString("PID:" + p1.polyId + ", pid:" + p1.id 
						+ "(" + p1.point.x + ", " + p1.point.y + ") - " 
						+ pol.id, p1.point.x, p1.point.y);
				g.drawString("("+ pol.id +")", p1.point.x, p1.point.y);
			}
			p0 = pol.points.elementAt(0);
			p1 = pol.points.elementAt(pol.points.size() - 1);
			g2.drawLine(p0.point.x, p0.point.y, p1.point.x, p1.point.y);

		}

		if (polygon != null) {
			//System.out.println("Drawing incomplete polygon");
			if(polygon.points.size() > 0) {
				g2.setColor(Color.RED);
				g2.fillRect(polygon.points.elementAt(0).point.x - 2, 
						polygon.points.elementAt(0).point.y - 2, 4, 4);				
			}
			for (int i = 0; i < polygon.points.size() - 1; i++) {
				p0 = polygon.points.elementAt(i);
				p1 = polygon.points.elementAt(i + 1);
				g2.setColor(Color.RED);
				g2.fillRect(p0.point.x - 2, p0.point.y - 2, 4, 4);
				g2.drawLine(p0.point.x, p0.point.y, p1.point.x, p1.point.y);
				g2.setColor(Color.MAGENTA);
				g2.fillRect(p1.point.x - 2, p1.point.y - 2, 4, 4);
			}
		}
		
		if(showConnects) {
			//System.out.println("Drawing connects.");
			g2.setColor(Color.BLUE);
			for (Iterator<Polygon> it = connects.iterator(); it.hasNext();) {
				Polygon pol = it.next();
				int pNum = pol.points.size();
				for (int i = 0; i < pNum - 1; i++) {
					p0 = pol.points.elementAt(i);
					p1 = pol.points.elementAt(i + 1);
					g2.drawLine(p0.point.x, p0.point.y, p1.point.x, p1.point.y);

					// PolygonPoint mid = midPoint(p0, p1);
					// double dist = distance(p0, p1);

					// g.drawString("dist: " + dist, mid.point.x, mid.point.y);

			//		System.err.println("i:" + i + ", i+1:" + (i + 1) + ", p0.point.x:" + p0.point.x + ", p0.point.y:"
			//				+ p0.point.y + ", p1.point.x:" + p1.point.x + ", p1.point.y:" + p1.point.y);
				}
				p0 = pol.points.elementAt(0);
				p1 = pol.points.elementAt(pNum - 1);
				g2.drawLine(p0.point.x, p0.point.y, p1.point.x, p1.point.y);

			}
			System.err.println("connects.size:" + connects.size());
			
			for (Iterator<Polygon> it = connects2.iterator(); it.hasNext();) {
				Polygon pol = it.next();
				int pNum = pol.points.size();
				for (int i = 0; i < pNum - 1; i++) {
					p0 = pol.points.elementAt(i);
					p1 = pol.points.elementAt(i + 1);
					g2.drawLine(p0.point.x, p0.point.y, p1.point.x, p1.point.y);

					// PolygonPoint mid = midPoint(p0, p1);
					// double dist = distance(p0, p1);

					// g.drawString("dist: " + dist, mid.point.x, mid.point.y);

			//		System.err.println("i:" + i + ", i+1:" + (i + 1) + ", p0.point.x:" + p0.point.x + ", p0.point.y:"
			//				+ p0.point.y + ", p1.point.x:" + p1.point.x + ", p1.point.y:" + p1.point.y);
				}
				p0 = pol.points.elementAt(0);
				p1 = pol.points.elementAt(pNum - 1);
				g2.drawLine(p0.point.x, p0.point.y, p1.point.x, p1.point.y);

			}
			System.err.println("connects.size:" + connects2.size());
		}
		
		if(showGraphConnects) {
			//System.out.println("Drawing graphConnects.");
			g2.setColor(Color.GREEN);
			for (Iterator<Polygon> it = graphConnects.iterator(); it.hasNext();) {
				Polygon pol = it.next();
				int pNum = pol.points.size();
				for (int i = 0; i < pNum - 1; i++) {
					p0 = pol.points.elementAt(i);
					p1 = pol.points.elementAt(i + 1);
					g2.drawLine(p0.point.x, p0.point.y, p1.point.x, p1.point.y);

					// PolygonPoint mid = midPoint(p0, p1);
					// double dist = distance(p0, p1);

					// g.drawString("dist: " + dist, mid.point.x, mid.point.y);

				//	System.err.println("i:" + i + ", i+1:" + (i + 1) + ", p0.point.x:" + p0.point.x + ", p0.point.y:"
				//			+ p0.point.y + ", p1.point.x:" + p1.point.x + ", p1.point.y:" + p1.point.y);
				}
				p0 = pol.points.elementAt(0);
				p1 = pol.points.elementAt(pNum - 1);
				g2.drawLine(p0.point.x, p0.point.y, p1.point.x, p1.point.y);

			}
			//System.err.println("graphConnects.size:" + graphConnects.size());
			
			for (Iterator<Polygon> it = graphConnects2.iterator(); it.hasNext();) {
				Polygon pol = it.next();
				int pNum = pol.points.size();
				for (int i = 0; i < pNum - 1; i++) {
					p0 = pol.points.elementAt(i);
					p1 = pol.points.elementAt(i + 1);
					g2.drawLine(p0.point.x, p0.point.y, p1.point.x, p1.point.y);

					// PolygonPoint mid = midPoint(p0, p1);
					// double dist = distance(p0, p1);

					// g.drawString("dist: " + dist, mid.point.x, mid.point.y);

				//	System.err.println("i:" + i + ", i+1:" + (i + 1) + ", p0.point.x:" + p0.point.x + ", p0.point.y:"
				//			+ p0.point.y + ", p1.point.x:" + p1.point.x + ", p1.point.y:" + p1.point.y);
				}
				p0 = pol.points.elementAt(0);
				p1 = pol.points.elementAt(pNum - 1);
				g2.drawLine(p0.point.x, p0.point.y, p1.point.x, p1.point.y);

			}
			//System.err.println("graphConnects.size:" + graphConnects.size());
		}

		if (finishedPoint != null) {
			System.out.println("Drawing finishedPoint");
			g2.setColor(Color.GREEN);
			g2.fillRect(finishedPoint.point.x - 2, finishedPoint.point.y - 2, 4, 4);
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		// System.out.println("Mouse pressed; # of clicks: ");
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// System.out.println("Mouse released; # of clicks: ");
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		System.out.println("Mouse entered");
		mouseIsIn = true;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		System.out.println("Mouse exited");
		mouseIsIn = false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("Mouse clicked (# of clicks: " + e.getClickCount() + ")");
		System.out.println("x:y=" + e.getX() + ":" + e.getY());

		Point pclick = new Point(e.getX(), e.getY());
		tempPoint = new PolygonPoint(polyId, polygon.points.size(), pclick);

		if (polygon.points.size() > 2 && pointsNear(polygon.points.elementAt(0), tempPoint)) {
			polygon.id = polyId++;
			polygons.add(polygon);
			
			//enlarge polygon before add to object
			ArrayList<Vector2D> vector2D = new ArrayList<Vector2D>();
			System.out.println(polygon.points.size());
			for (int i = 0; i < polygon.points.size(); i++) {
				System.out.println("i:" + i + ", id->" + polygon.id + ", " + polygon.points.elementAt(i).point.x + ":" + polygon.points.elementAt(i).point.y);
				
				//vector2D.add(null);
				Vector2D v = new Vector2D(polygon.points.elementAt(i).point.x, polygon.points.elementAt(i).point.y);
				vector2D.add(v);
			}
			
			//Collections.reverse(vector2D); 
			PGCalculator p = new PGCalculator();
			vector2D = p.getEnlargedPolygon(vector2D, 5);
			
			polygon2 = new Polygon();
			polygon2.id = polygon.id;
			polygon2.points = new Vector<PolygonPoint>();
			for (int i = 0; i < vector2D.size(); i++) {
				Point vp = new Point((int)vector2D.get(i).getX(), (int)vector2D.get(i).getY());
				PolygonPoint point = new PolygonPoint(polygon.points.get(i).polyId, polygon.points.get(i).id, vp);
				polygon2.points.add(point);
			}
			polygons2.add(polygon2);
			 
			tempPoint = null;
			
			polygon = new Polygon();
			polygon.id = -1;
			polygon.points = new Vector<PolygonPoint>();

		} else {
			System.out.println("add tempPoint into polygon");
			polygon.points.add(tempPoint);
			tempPoint = null;
			System.out.println("Added. There are " + polygon.points.size() + " points.");
		}

		System.out.println("POLYGONS... polygons.size:" + polygons.size());
		for (int i = 0; i < polygons.size(); i++) {
			System.out.print("i:" + i + ", id:" + polygons.elementAt(i).id);
			for (int j = 0; j < polygons.elementAt(i).points.size(); j++) {
				System.out.println("\tj:" + j + " => [" + polygons.elementAt(i).points.elementAt(j).point.x + ":"
						+ polygons.elementAt(i).points.elementAt(j).point.y + "]");
			}
		}
		
		System.out.println("POLYGONS2... polygons2.size:" + polygons2.size());
		for (int i = 0; i < polygons2.size(); i++) {
			System.out.print("i:" + i + ", id:" + polygons2.elementAt(i).id);
			for (int j = 0; j < polygons2.elementAt(i).points.size(); j++) {
				System.out.println("\tj:" + j + " => [" + polygons2.elementAt(i).points.elementAt(j).point.x + ":"
						+ polygons2.elementAt(i).points.elementAt(j).point.y + "]");
			}
		}

		System.out.println("polygon... points.size:" + polygon.points.size());
		for (int i = 0; i < polygon.points.size(); i++) {
			System.out.println("i:" + i + ", id->" + polygon.id + ", " + polygon.points.elementAt(i).point.x + ":"
					+ polygon.points.elementAt(i).point.y);
		}
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		//System.out.println("Mouse moved " + e.getX() + ":" + e.getY());
		
		Point pclick = new Point(e.getX(), e.getY());
		PolygonPoint tempPoint = new PolygonPoint(-1, -1, pclick);

		if (polygon.points.size() > 0 && pointsNear(polygon.points.elementAt(0), tempPoint)) {
			finishedPoint = polygon.points.elementAt(0);
		//	System.out.println("finishedPoint.x:" + finishedPoint.point.x 
		//			+ ", finishedPoint.y:" + finishedPoint.point.y);
			repaint();
		} else {
			finishedPoint = null;
			repaint();
		}
		

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		System.out.println("Mouse dragged");
		// tempPoint = e.getPoint();
	}

	public double distance(PolygonPoint p0, PolygonPoint p1) {
		double dx = p0.point.x - p1.point.x;
		double dy = p0.point.y - p1.point.y;
		return Math.sqrt(dx * dx + dy * dy);
	}

	// give the mid point
	public Point midPoint(Point p0, Point p1) {
		return new Point((p0.x + p1.x) / 2, (p0.y + p1.y) / 2);
	}

	boolean pointsNear(PolygonPoint p0, PolygonPoint p1) {
		boolean on = false;

		int dx = p0.point.x - p1.point.x;
		int dy = p0.point.y - p1.point.y;

		double distance = Math.sqrt(dx * dx + dy * dy);

		//System.out.println("p0.point.x:" + p0.point.x + ", p1.point.x:" + p1.point.x + " - p0.point.y:" + p0.point.y
		//		+ ", p1.point.y:" + p1.point.y + ", distance:" + distance);
		if (distance < 4.0) {
			on = true;
		}

		return on;
	}


	// compare operation
	public boolean Equals(PolygonPoint p0, PolygonPoint p1) {
		if (Math.abs(p0.point.x - p1.point.x) > 0.00001) {
			return false;
		}

		if (Math.abs(p0.point.y - p1.point.y) > 0.00001) {
			return false;
		}
		return true;
	}
	
    /** Handle the key typed event from the text field. */
    public void keyTyped(KeyEvent e) {
    	/*
    	System.out.println("keyTyped");
    	if(mouseIsIn) {
    		int id = e.getID();
    		char keyChar = e.getKeyChar();
    		int keyCode = e.getKeyCode();
    		System.out.println("keyChar:" + keyChar + ", keyCode:" + keyCode);
    		// displayInfo(e, "KEY TYPED: ");
    	}
    	*/
    }
    
    /** Handle the key pressed event from the text field. */
    public void keyPressed(KeyEvent e) {
    	/*
    	System.out.println("keyPressed()");
    	if(mouseIsIn) {
    		int id = e.getID();
    		char keyChar = e.getKeyChar();
    		int keyCode = e.getKeyCode();
    		System.out.println("keyChar:" + keyChar + ", keyCode:" + keyCode);
    		// displayInfo(e, "KEY PRESSED: ");
    	}
    	*/
    }
    
    /** Handle the key released event from the text field. */
    @SuppressWarnings("unused")
	public void keyReleased(KeyEvent e) {
    	System.out.println("keyReleased()");
    	if(mouseIsIn) {
    		int id = e.getID();
    		char keyChar = e.getKeyChar();
    		int keyCode = e.getKeyCode();
    		System.out.println("keyChar:" + keyChar + ", keyCode:" + keyCode);
    		if(keyCode == 27) {
    			int size = polygon.points.size();
    			int index = size - 1;
    			System.out.println("size:" + size + ", index:" + index);
    			if(index > -1) {
    				polygon.points.removeElementAt(index);
        			size = polygon.points.size();
        			System.out.println("size:" + size + ", index:" + index);
        			tempPoint = null;
    				repaint();
    			}
    		}
    		// displayInfo(e, "KEY RELEASED: ");
    	}
    }
    
    
    /*
     * We have to jump through some hoops to avoid
     * trying to print non-printing characters
     * such as Shift.  (Not only do they not print,
     * but if you put them in a String, the characters
     * afterward won't show up in the text area.)
     */
    
	private void displayInfo(KeyEvent e, String keyStatus){
        
        //You should only rely on the key char if the event
        //is a key typed event.
        int id = e.getID();
        String keyString;
        if (id == KeyEvent.KEY_TYPED) {
            char c = e.getKeyChar();
            keyString = "key character = '" + c + "'";
        } else {
            int keyCode = e.getKeyCode();
            keyString = "key code = " + keyCode
                    + " ("
                    + KeyEvent.getKeyText(keyCode)
                    + ")";
        }
        
        int modifiersEx = e.getModifiersEx();
        String modString = "extended modifiers = " + modifiersEx;
        String tmpString = KeyEvent.getModifiersExText(modifiersEx);
        if (tmpString.length() > 0) {
            modString += " (" + tmpString + ")";
        } else {
            modString += " (no extended modifiers)";
        }
        
        String actionString = "action key? ";
        if (e.isActionKey()) {
            actionString += "YES";
        } else {
            actionString += "NO";
        }
        
        String locationString = "key location: ";
        int location = e.getKeyLocation();
        if (location == KeyEvent.KEY_LOCATION_STANDARD) {
            locationString += "standard";
        } else if (location == KeyEvent.KEY_LOCATION_LEFT) {
            locationString += "left";
        } else if (location == KeyEvent.KEY_LOCATION_RIGHT) {
            locationString += "right";
        } else if (location == KeyEvent.KEY_LOCATION_NUMPAD) {
            locationString += "numpad";
        } else { // (location == KeyEvent.KEY_LOCATION_UNKNOWN)
            locationString += "unknown";
        }
        
        System.out.println(keyStatus + "\n"
                + "    " + keyString + "\n"
                + "    " + modString + "\n"
                + "    " + actionString + "\n"
                + "    " + locationString + "\n");
    }

	public void CreateAndShowGui() {
		
		CreatePolygons01 createPolygons = new CreatePolygons01();

		JFrame frame = new JFrame("Create-Crowd-Simulator-Polygons");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Menu Bar
		JMenuBar menuBar = new JMenuBar();
		JMenu menuFile = new JMenu("File");		
		JMenuItem menuFileNew = new JMenuItem("New");
		menuFile.add(menuFileNew);

		JMenuItem menuFileOpen = new JMenuItem("Open");
		menuFile.add(menuFileOpen);
		
		JMenuItem menuFileGenerateConnects = new JMenuItem("Generate Connects");
		menuFile.add(menuFileGenerateConnects);
		
		JMenuItem menuFileGenerateGraph = new JMenuItem("Generate Graph");
		menuFile.add(menuFileGenerateGraph);
		
		JMenuItem menuFileSaveGraph = new JMenuItem("Save Graph");
		menuFile.add(menuFileSaveGraph);
		
		JMenuItem menuFileExit = new JMenuItem("Exit");
		menuFile.add(menuFileExit);
		menuBar.add(menuFile);

		JMenu menuView = new JMenu("View");
		ButtonGroup group = new ButtonGroup();
		JMenuItem menuViewConnects = new JRadioButtonMenuItem("Show Connects");
		group.add(menuViewConnects);
		menuView.add(menuViewConnects);

		JMenuItem menuViewGraph = new JRadioButtonMenuItem("Show Graph Connects");
		menuViewGraph.setSelected(true);
		group.add(menuViewGraph);
		menuView.add(menuViewGraph);
		
		menuView.addSeparator();

		JMenuItem menuViewMap = new JCheckBoxMenuItem("Show Map");
		menuViewMap.setSelected(true);
		menuView.add(menuViewMap);

		menuBar.add(menuView);
		frame.setJMenuBar(menuBar);

		menuFileNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println("New...");
				polygons.removeAllElements();
				polygons2.removeAllElements();
				connects.removeAllElements();
				connects2.removeAllElements();
				graphConnects.removeAllElements();
				graphConnects2.removeAllElements();
				createPolygons.repaint();
			}
		});
		
		menuFileOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// Event
				System.out.println("Opening...");

				try {
					Reader reader = Files.newBufferedReader(Paths.get("polygons-connects.json"));
					
					Gson gson = new GsonBuilder().setPrettyPrinting().create();
					JSONPolygon jsonp = gson.fromJson(reader, JSONPolygon.class);
					polygons = jsonp.polygons;
					connects = jsonp.connects;
					graphConnects = jsonp.graphConnects;
					settings = jsonp.settings;
					
					conId = settings.getConId();
					uuid = settings.getUuid();
					polyId = settings.getPolyId();
					
					reader.close();
					createPolygons.repaint();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		menuFileSaveGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// Event
				System.out.println("Saving...");
				System.out.println(">" + polygons.size());
				System.out.println(">" + connects.size());
				System.out.println(">" + graphConnects.size());
				settings.setConId(conId);
				settings.setUuid(uuid);
				settings.setPolyId(polyId);
				

				try {
					Writer writer = Files.newBufferedWriter(Paths.get("polygons-connects.json"));

					Gson gson = new GsonBuilder().setPrettyPrinting().create();
					JSONPolygon jsonp = new JSONPolygon();
					jsonp.polygons = polygons;
					jsonp.connects = connects;
					jsonp.graphConnects = graphConnects;
					jsonp.settings = settings;
					gson.toJson(jsonp, writer);
					writer.close();
					
					writer = Files.newBufferedWriter(Paths.get("graph-connects.json"));
					gson.toJson(graphExport, writer);
					writer.close();
					
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

		menuFileExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// Event
				System.out.println("Exit...");
				System.exit(0);
			}
		});

		menuFileGenerateConnects.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// Event
				System.out.println("Generateing Connects...");
				//connects.removeAllElements();
				connects2.removeAllElements();
				//createPolygons.connectPolygons();
				createPolygons.connectPolygons2();
				createPolygons.repaint();
			}
		});

		menuFileGenerateGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// Event

				System.out.println("Generating Graph..");
				Collections.sort(connects);
				Collections.sort(connects2);

				System.out.println("=============================================>" + connects.size());

				Vector<Polygon> tmpConnects = new Vector<Polygon>();
				
				for (int i = 0; i < connects.size(); i++) {
					Polygon iConnect = connects.get(i);
					PolygonPoint ip0 = iConnect.points.elementAt(0);
					PolygonPoint ip1 = iConnect.points.elementAt(1);
					System.out.println("INDEX: " + i + ", connect.id -> " + iConnect.id + ", ip0.polyId:"
							+ ip0.polyId + ", ip0.id:" + ip0.id + ", ip0.point.x:" + ip0.point.x
							+ ", p0.point.y:" + ip0.point.y + " <--> " + ", ip1.polyId:" + ip1.polyId
							+ ", ip1.id:" + ip1.id + ", ip1.point.x:" + ip1.point.x + ", p1.point.y:"
							+ ip1.point.y + ", iConnect.distance:" + iConnect.distance);
					// check if this point has already been connected.
					boolean connected = false;
					for (int j = 0; j < tmpConnects.size() && !connected; j++) {
						Polygon jConnect = tmpConnects.get(j);
						PolygonPoint jp0 = jConnect.points.elementAt(0);
						PolygonPoint jp1 = jConnect.points.elementAt(1);
						if ((ip0.polyId == jp0.polyId 
								&& ip1.polyId == jp1.polyId
								&& ip1.id == jp1.id) ||
								(ip0.polyId == jp0.polyId
								&& ip0.id == jp0.id
								&& ip1.polyId == jp1.polyId)) {
							connected = true;
						}
					}

					if (!connected) {
						Polygon tempPol = new Polygon();
						tempPol.points = new Vector<PolygonPoint>();
						tempPol.id = iConnect.id;
						tempPol.points.add(new PolygonPoint(iConnect.points.elementAt(0).polyId,
								iConnect.points.elementAt(0).id, new Point(iConnect.points.elementAt(0).point.x,
										iConnect.points.elementAt(0).point.y)));
						tempPol.points.add(new PolygonPoint(iConnect.points.elementAt(1).polyId,
								iConnect.points.elementAt(1).id, new Point(iConnect.points.elementAt(1).point.x,
										iConnect.points.elementAt(1).point.y)));
						tempPol.distance = iConnect.distance;
						tempPol.uuid = iConnect.uuid; 
						tmpConnects.add(tempPol);
						System.out.println("----------");
					}

				}
				
				Collections.sort(tmpConnects, new ComparatorConnect());
				
				for(int i=0; i<tmpConnects.size(); i++) {
					Polygon iConnect = tmpConnects.get(i);
					
					for(int j=tmpConnects.size()-1; j>i; j--) {
						Polygon jConnect = tmpConnects.get(j);
						System.out.println("checking crossed i=" + i + ", j=" + j);
						if(!sharedPoint(iConnect, jConnect) 
								&& linesIntersect(iConnect.points.elementAt(0), 
								iConnect.points.elementAt(1), 
								jConnect.points.elementAt(0), 
								jConnect.points.elementAt(1))) {
							System.out.println(" Intersected connects! Must be DELL");
							tmpConnects.removeElementAt(j);
						}
					}							
				}
				
				Vector<Polygon> tmpConnects2 = new Vector<Polygon>();
				
				for (int i = 0; i < connects2.size(); i++) {
					Polygon iConnect = connects2.get(i);
					PolygonPoint ip0 = iConnect.points.elementAt(0);
					PolygonPoint ip1 = iConnect.points.elementAt(1);
					System.out.println("INDEX: " + i + ", connect.id -> " + iConnect.id + ", ip0.polyId:"
							+ ip0.polyId + ", ip0.id:" + ip0.id + ", ip0.point.x:" + ip0.point.x
							+ ", p0.point.y:" + ip0.point.y + " <--> " + ", ip1.polyId:" + ip1.polyId
							+ ", ip1.id:" + ip1.id + ", ip1.point.x:" + ip1.point.x + ", p1.point.y:"
							+ ip1.point.y + ", iConnect.distance:" + iConnect.distance);
					// check if this point has already been connected.
					boolean connected = false;
					for (int j = 0; j < tmpConnects2.size() && !connected; j++) {
						Polygon jConnect = tmpConnects2.get(j);
						PolygonPoint jp0 = jConnect.points.elementAt(0);
						PolygonPoint jp1 = jConnect.points.elementAt(1);
						if ((ip0.polyId == jp0.polyId 
								&& ip1.polyId == jp1.polyId
								&& ip1.id == jp1.id) ||
								(ip0.polyId == jp0.polyId
								&& ip0.id == jp0.id
								&& ip1.polyId == jp1.polyId)) {
							connected = true;
						}
					}

					if (!connected) {
						Polygon tempPol = new Polygon();
						tempPol.points = new Vector<PolygonPoint>();
						tempPol.id = iConnect.id;
						tempPol.points.add(new PolygonPoint(iConnect.points.elementAt(0).polyId,
								iConnect.points.elementAt(0).id, new Point(iConnect.points.elementAt(0).point.x,
										iConnect.points.elementAt(0).point.y)));
						tempPol.points.add(new PolygonPoint(iConnect.points.elementAt(1).polyId,
								iConnect.points.elementAt(1).id, new Point(iConnect.points.elementAt(1).point.x,
										iConnect.points.elementAt(1).point.y)));
						tempPol.distance = iConnect.distance;
						tempPol.uuid = iConnect.uuid; 
						tmpConnects2.add(tempPol);
						System.out.println("----------");
					}

				}
				
				Collections.sort(tmpConnects2, new ComparatorConnect());
				
				for(int i=0; i<tmpConnects2.size(); i++) {
					Polygon iConnect = tmpConnects2.get(i);
					
					for(int j=tmpConnects2.size()-1; j>i; j--) {
						Polygon jConnect = tmpConnects2.get(j);
						System.out.println("checking crossed i=" + i + ", j=" + j);
						if(!sharedPoint(iConnect, jConnect) 
								&& linesIntersect(iConnect.points.elementAt(0), 
								iConnect.points.elementAt(1), 
								jConnect.points.elementAt(0), 
								jConnect.points.elementAt(1))) {
							System.out.println(" Intersected connects! Must be DELL");
							tmpConnects2.removeElementAt(j);
						}
					}							
				}

				graphConnects.removeAllElements();
				graphConnects2.removeAllElements();

				for (Iterator<Polygon> it = tmpConnects.iterator(); it.hasNext();) {
					graphConnects.add(it.next());
					System.out.println("insert tmpConnects -> connects.");
				}
				
				for (Iterator<Polygon> it = tmpConnects2.iterator(); it.hasNext();) {
					graphConnects2.add(it.next());
					System.out.println("insert tmpConnects -> connects.");
				}
				
				int [][]graphMetrix = new int[polygons2.size()][polygons2.size()];
				
				JSONGraph jsonGraph = new JSONGraph();
				int jsonId = 0;
				int graphPolyId = 0;
				for (int i = 0; i < polygons2.size(); i++) {
					Polygon p = polygons2.elementAt(i);
					
					System.out.println(p.points.size());
					PolygonPoint a;
					PolygonPoint b;
					Vector2D vertex1;
					Vector2D vertex2;
					for (int j = 0; j < p.points.size(); j++) {
						if(j < p.points.size() - 1) {
							a = p.points.elementAt(j);
							b = p.points.elementAt(j + 1);
							System.out.println(a.toString() + "==>" + b.toString());
						} else {
							a = p.points.elementAt(p.points.size() - 1);
							b = p.points.elementAt(0);
							System.out.println(a.toString() + "==>" + b.toString());
						}
						
						vertex1 = new Vector2D(a.point.getX(), a.point.getY());
						vertex2 = new Vector2D(b.point.getX(), b.point.getY());
						jsonGraph = new JSONGraph(jsonId, graphPolyId, vertex1, vertex2, vertex1.distance(vertex2), true);
						graphExport.add(jsonGraph);
						jsonId++;
						
						
						for (int k = 0; k < graphConnects2.size(); k++) {
							Polygon graph = graphConnects2.get(k);
							
							if(a.point.equals(graph.points.elementAt(0).point)) {
								System.out.println(graph.toString());
								vertex1 = new Vector2D(graph.points.elementAt(0).point.getX(), graph.points.elementAt(0).point.getY());
								vertex2 = new Vector2D(graph.points.elementAt(1).point.getX(), graph.points.elementAt(1).point.getY());
								jsonGraph = new JSONGraph(jsonId, graphPolyId, vertex1, vertex2, vertex1.distance(vertex2), false);
								graphExport.add(jsonGraph);
								
								jsonId++;
							}
						}
					}
					graphPolyId++;
					
					for (int j = 0; j < graphMetrix.length; j++) {
						for (int k = 0; k < graphMetrix.length; k++) {
							System.out.print(graphMetrix[i][j]);
						}
						System.out.println();
					}
				}
				createPolygons.repaint();
			}
		});

		menuViewConnects.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// Event
				showConnects = true;
				showGraphConnects = false;
				createPolygons.repaint();
			}
		});

		menuViewGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// Event
				showConnects = false;
				showGraphConnects = true;
				createPolygons.repaint();
			}
		});

		menuViewMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// Event
				showMap = !showMap;
				createPolygons.repaint();
			}
		});

		frame.add(createPolygons);
		frame.pack();
		frame.setVisible(true);

	}

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				CreatePolygons01 polygonMaker = new CreatePolygons01();
				polygonMaker.CreateAndShowGui();
		}});
	}

}
