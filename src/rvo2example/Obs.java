package rvo2example;

import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Obs {
	final List<Vector2D> coord1;
	final List<Vector2D> coord2;

	public Obs(List<Vector2D> coord1, List<Vector2D> coord2) {
		this.coord1 = coord1;
		this.coord2 = coord2;
	}
}
