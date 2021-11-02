package polygonmaker;

import java.util.ArrayList;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class PGCalculator {

	// Return True if the polygon is convex.
	public boolean polygonIsConvex(ArrayList<Vector2D> point) {
		// For each set of three adjacent points A, B, C,
		// find the cross product AB · BC. If the sign of
		// all the cross products is the same, the angles
		// are all positive or negative (depending on the
		// order in which we visit them) so the polygon
		// is convex.
		boolean got_negative = false;
		boolean got_positive = false;
		boolean isConvex = true;
		int num_points = point.size();
		int B, C;
		for (int A = 0; A < num_points; A++) {
			B = (A + 1) % num_points;
			C = (B + 1) % num_points;

			double cross_product = crossProductLength(point.get(A).getX(), point.get(A).getY(), point.get(B).getX(),
					point.get(B).getY(), point.get(C).getX(), point.get(C).getY());
			if (cross_product < 0) {

				got_negative = true;
				System.out.println("got_negative: " + got_negative);
			} else if (cross_product > 0) {

				got_positive = true;
				System.out.println("got_positive: " + got_positive);
			}

			if (got_negative && got_positive) {
				System.err.println("Concave! @ x: " + point.get(A).getX() + ", y: " + point.get(A).getY());
				got_negative = false;
				got_positive = false;
				isConvex = false;
				// return false;
			}
		}

		// If we got this far, the polygon is convex.
		return isConvex;
	}

	// Return the cross product AB x BC.
	// The cross product is a vector perpendicular to AB
	// and BC having length |AB| * |BC| * Sin(theta) and
	// with direction given by the right-hand rule.
	// For two vectors in the X-Y plane, the result is a
	// vector with X and Y components 0 so the Z component
	// gives the vector's length and direction.
	public static double crossProductLength(double Ax, double Ay, double Bx, double By, double Cx, double Cy) {
		// Get the vectors' coordinates.

		System.out.println("Ax: " + Ax + ", Ay: " + Ay + ", Bx: " + Bx + ", By: " + By + ", Cx: " + Cx + ", Cy: " + Cy);
		double BAx = Ax - Bx;
		double BAy = Ay - By;
		double BCx = Cx - Bx;
		double BCy = Cy - By;

		System.out.println("BAx: " + BAx + ", BAy: " + BAy + ", BCx: " + BCx + ", BCy: " + BCy);

		// Calculate the Z coordinate of the cross product.
		System.out.println((BAx * BCy) - (BAy * BCx));
		return ((BAx * BCy) - (BAy * BCx));
	}

	public static Vector Normalize(Vector vector) {
		double distance = Math.sqrt(vector.getX() * vector.getX() + vector.getY() * vector.getY());
		//System.out.println(distance + ", x: " + vector.getX() / distance + ", y: " + vector.getY() / distance);
		return new Vector(vector.getX() / distance, vector.getY() / distance);
	}

	// Return Enlarge Polygon
	public ArrayList<Vector2D> getEnlargedPolygon(ArrayList<Vector2D> old_points, double offset) {
		ArrayList<Vector2D> enlarged_points = new ArrayList<Vector2D>();
		int num_points = old_points.size();

		for (int j = 0; j < num_points; j++) {
			System.out.println("x: " + old_points.get(j).getX() + ", y: " + old_points.get(j).getY());
			// Find the new location for point j.
			// Find the points before and after j.
			int i = (j - 1);
			if (i < 0) {
				i += num_points;
			}
			int k = (j + 1) % num_points;
			
			Vector v1 = new Vector(old_points.get(j).getX() - old_points.get(i).getX(), old_points.get(j).getY() - old_points.get(i).getY());
			v1 = Normalize(v1);
			v1.setX(v1.getX() * offset);
			v1.setY(v1.getY() * offset);

			Vector2D n1 = new Vector2D(v1.getY(), -v1.getX());
			Vector2D pij1 = new Vector2D((double) (old_points.get(i).getX() + n1.getX()), (double) (old_points.get(i).getY() + n1.getY()));
			Vector2D pij2 = new Vector2D((double) (old_points.get(j).getX() + n1.getX()), (double) (old_points.get(j).getY() + n1.getY()));
			
			Vector v2 = new Vector(old_points.get(k).getX() - old_points.get(j).getX(), old_points.get(k).getY() - old_points.get(j).getY());
			v2 = Normalize(v2);
			v2.setX(v2.getX() * offset);
			v2.setY(v2.getY() * offset);

			Vector2D n2 = new Vector2D(v2.getY(), -v2.getX());
			Vector2D pjk1 = new Vector2D((double) (old_points.get(j).getX() + n2.getX()), (double) (old_points.get(j).getY() + n2.getY()));
			Vector2D pjk2 = new Vector2D((double) (old_points.get(k).getX() + n2.getX()), (double) (old_points.get(k).getY() + n2.getY()));
				
			// See where the shifted lines ij and jk intersect.
			
			Vector2D poi = findIntersection(pij1, pij2, pjk1, pjk2);
			
			//System.out.println("[lines_intersect]" + ", Edges " + i + "-->" + j + " and " + j + "-->" + k + " are parallel");
			//System.out.println("===");
			enlarged_points.add(poi);
		}

		return enlarged_points;
	}

	// Find the point of intersection between
	// the lines p1 --> p2 and p3 --> p4.
	private Vector2D findIntersection(Vector2D p1, Vector2D p2, Vector2D p3, Vector2D p4) {
		//boolean lines_intersect = false, segments_intersect = false;
		//Point  close_p1 = null, close_p2 = null;
		Vector2D intersection = null;
		// Get the segments' parameters.
		double dx12 = p2.getX() - p1.getX();
		double dy12 = p2.getY() - p1.getY();
		double dx34 = p4.getX() - p3.getX();
		double dy34 = p4.getY() - p3.getY();

		// Solve for t1 and t2
		double denominator = (dy12 * dx34 - dx12 * dy34);

		double t1 = ((p1.getX() - p3.getX()) * dy34 + (p3.getY() - p1.getY()) * dx34) / denominator;
		if (Double.isInfinite(t1)) {
			// The lines are parallel (or close enough to it).
			//lines_intersect = false;
			//segments_intersect = false;
			intersection = new Vector2D(Double.NaN, Double.NaN);
			//close_p1 = new Point(Double.NaN, Double.NaN);
			//close_p2 = new Point(Double.NaN, Double.NaN);
			
			return intersection;
		}
		//lines_intersect = true;

		//double t2 = ((p3.getX() - p1.getX()) * dy12 + (p1.getY() - p3.getY()) * dx12) / -denominator;

		// Find the point of intersection.
		intersection = new Vector2D(p1.getX() + dx12 * t1, p1.getY() + dy12 * t1);

		// The segments intersect if t1 and t2 are between 0 and 1.
		//segments_intersect = ((t1 >= 0) && (t1 <= 1) && (t2 >= 0) && (t2 <= 1));

		// Find the closest points on the segments.
		/*if (t1 < 0) {
			t1 = 0;
		} else if (t1 > 1) {
			t1 = 1;
		}

		if (t2 < 0) {
			t2 = 0;
		} else if (t2 > 1) {
			t2 = 1;
		}*/

		//close_p1 = new Point(p1.getX() + dx12 * t1, p1.getY() + dy12 * t1);
		//close_p2 = new Point(p3.getX() + dx34 * t2, p3.getY() + dy34 * t2);
		//System.out.println("=================");
		//System.out.println("lines_intersect: " + lines_intersect + ", segments_intersect: " + segments_intersect);
		//System.out.println("close_p1: " + close_p1.getX() + ", " + close_p1.getY());
		//System.out.println("close_p2: " + close_p2.getX() + ", " + close_p2.getY());
		
		
		return intersection;
	}
}
