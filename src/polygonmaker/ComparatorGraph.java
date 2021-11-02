package polygonmaker;

import java.util.Comparator;

public class ComparatorGraph implements Comparator<Polygon>{

	@Override
	public int compare(Polygon a, Polygon b) {
		if(a.distance < b.distance)
			return -1;
		else if(a.distance > b.distance)
			return 1;
		return 0;
	}
	
}
