package polygonmaker;

import java.util.*;

public class ComparatorConnect implements Comparator<Polygon>{
	
	public int compare(Polygon a, Polygon b) {
		if(a.distance < b.distance)
			return -1;
		else if(a.distance > b.distance)
			return 1;
		return 0;
	}

}
