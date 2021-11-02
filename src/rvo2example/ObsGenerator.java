package rvo2example;

import java.awt.Rectangle;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import polygonmaker.JSONPolygon;
import polygonmaker.Polygon;
import polygonmaker.PolygonPoint;

/**
 * @author PoN
 *
 */
public class ObsGenerator {
	

	public static long seed = 120;
	static int width = 1920, height = 1080;
	public int row = 0;
	public int col = 0;
	public List<Obs> obs;
	
	public ObsGenerator(final int r, final int c) {
		this.row = r;
		this.col = c;
		
		
	}
	
	public static int translateCoordinate(double x, double y) {
		double xp = x - (width/2);
		double yp = ((y - (height/2)) * (-1));
		System.out.println(x+" -- "+y+" translate to " + xp + ", " + yp);
		return 0;
	}
	
	public static Vector2D translateCoordinateVector(double x, double y) {
		double xp = x - (width/2);
		double yp = ((y - (height/2)) * (-1));
		Vector2D vector = new Vector2D(xp, yp);
		return vector;
	}
	
	public List<Obs> genSquareBlocksList() {
		
		List<Obs> obsList = new ArrayList<Obs>();
		
		Random rand = new Random(seed);

		int width = 0;
		int height = 0;
		int xGap = 10;
		int yGap = 10;
		int x = 0;
		int y = 0;
		
		
		for(int i=0; i<row; i++) {
			height = rand.nextInt(15-10) + 10;
			y += height + yGap;
			x = 0;
			for(int j=0; j<col; j++) {
				width = rand.nextInt(15-10) + 10;
				x += width + xGap;				
				Rectangle rect = new Rectangle(x, y, width, height);
		

		/*		System.out.print("i:" + i + ", j:" + j 
						+ ", rect.x:" + rect.x + ", rect.y:" + rect.y 
						+ ", rect.width:" + rect.width 
						+ ", rect.height:" + rect.height + " ("+(rect.x)+", "+(rect.y)+"), ("+(rect.x+rect.width)+", "+(rect.y)+"), ("+(rect.x+rect.width)+", "+(rect.y+rect.height)+"), ("+(rect.x)+", "+(rect.y+rect.height)+");  \n");*/
				
				List<Vector2D> coord1 = new ArrayList<Vector2D>();
				coord1.add(new Vector2D(rect.x, rect.y));
				coord1.add(new Vector2D(rect.x+rect.width, rect.y));
				coord1.add(new Vector2D(rect.x+rect.width, rect.y+rect.height));
				coord1.add(new Vector2D(rect.x, rect.y+rect.height));
				
				List<Vector2D> coord2 = new ArrayList<Vector2D>();
				coord2.add(translateCoordinateVector(rect.x, rect.y));
				coord2.add(translateCoordinateVector(rect.x, rect.y+rect.height));
				coord2.add(translateCoordinateVector(rect.x+rect.width, rect.y+rect.height));
				coord2.add(translateCoordinateVector(rect.x+rect.width, rect.y));
				
				Obs obs1 = new Obs(coord1, coord2);
				
				obsList.add(obs1);
				
				//System.out.println();
			}
			//System.out.println("---row["+row+"]---");
		}
		
		return obsList;
	}
	
	public List<Obs> genSquareBlocksList2(int rectRowEnable, int rectColEnable) {
		List<Obs> obsList = new ArrayList<Obs>();
		
		Random rand = new Random(seed);

		int width = 0;
		int height = 0;
		int xGap = 10;
		int yGap = 10;
		int x = 0;
		int y = 0;
		
		
		
		for(int i=0; i<row; i++) {
			int rectRowEnableRand = rand.nextInt(rectRowEnable);
			if(rectRowEnableRand % rectRowEnable == 0) {
				
				height = rand.nextInt(15-10) + 10;
				y += height + yGap;
				x = 0;
				for(int j=0; j<col; j++) {
					width = rand.nextInt(15-10) + 10;
					x += width + xGap;		
					int rectColEnableRand = rand.nextInt(rectColEnable);
					if(rectColEnableRand % rectColEnable == 0) {
						Rectangle rect = new Rectangle(x, y, width, height);
		
				/*		System.out.print("i:" + i + ", j:" + j 
								+ ", rect.x:" + rect.x + ", rect.y:" + rect.y 
								+ ", rect.width:" + rect.width 
								+ ", rect.height:" + rect.height + " ("+(rect.x)+", "+(rect.y)+"), ("+(rect.x+rect.width)+", "+(rect.y)+"), ("+(rect.x+rect.width)+", "+(rect.y+rect.height)+"), ("+(rect.x)+", "+(rect.y+rect.height)+");  \n");*/
						
						List<Vector2D> coord1 = new ArrayList<Vector2D>();
						coord1.add(new Vector2D(rect.x, rect.y));
						coord1.add(new Vector2D(rect.x+rect.width, rect.y));
						coord1.add(new Vector2D(rect.x+rect.width, rect.y+rect.height));
						coord1.add(new Vector2D(rect.x, rect.y+rect.height));
						
						List<Vector2D> coord2 = new ArrayList<Vector2D>();
						coord2.add(translateCoordinateVector(rect.x, rect.y));
						coord2.add(translateCoordinateVector(rect.x, rect.y+rect.height));
						coord2.add(translateCoordinateVector(rect.x+rect.width, rect.y+rect.height));
						coord2.add(translateCoordinateVector(rect.x+rect.width, rect.y));
						
						Obs obs1 = new Obs(coord1, coord2);
						
						obsList.add(obs1);
					}
					
					//System.out.println();
				}
				//System.out.println("---row["+row+"]---");
			} else {
				y += height + yGap;
				x += width + xGap;	
			}
		}
		
		return obsList;
	}
	
	public List<Obs> genSquareBlocksList3() {
		
		List<Obs> obsList = new ArrayList<Obs>();
		
		int maxCol = 5;
		Random rand = new Random(seed);
		Rectangle[][] rects = new Rectangle[row][col];
		int[][] blocks = new int[row][col];
		
		for(int i=0; i<row; i++) {
			for(int j=0; j<col; j++) {
				// check if previous row is empty
				if (i > 0 && blocks[i - 1][j] != 0) {
					//System.out.println("blocks[" + i + "][" + j + "]:" + blocks[i][j] + ", is adjacent to previous row!");
					continue;
				}
				// check if previous col is empty
				if (j > 0 && blocks[i][j - 1] != 0) {
					//System.out.println("blocks[" + i + "][" + j + "]:" + blocks[i][j] + ", is adjacent to previous col!");
					continue;
				}
				if(i > 0 && j > 0 && blocks[i-1][j-1] != 0) {
					//System.out.println("blocks[" + i + "][" + j + "]:" + blocks[i][j] + ", is adjacent to previous diagonal cell!");
					continue;
				}
				// check if this row col is empty
				if (blocks[i][j] != 0) {
					//System.out.println("blocks[" + i + "][" + j + "]:" + blocks[i][j] + ", is not empty!");
					continue;
				}

				int availCol = col - j;
				int possibleCol = (int)Math.min(availCol, maxCol);
				
				int width = possibleCol;
				if(width > 1) {
					width = rand.nextInt(possibleCol) + 1;
				}

				for (int k = 0; k < width; k++) {
					blocks[i][j + k] = 1;
				}

				int x = j;
				int y = i;
				Rectangle rect = new Rectangle(x, y, width, 1);
				rects[i][j] = rect;
			}
		}
		
		int blockSize = 20;
		
		for (Rectangle[] row : rects) {
			for (Rectangle cell : row) {
				if(cell == null) {
					continue;
				}
				int x = blockSize * (cell.x + 1);
				int y = blockSize * (cell.y + 1);
				int width = blockSize * (cell.width);
				int height = blockSize * (cell.height);
			//	g.drawRect(x, y, width, height);
				
				List<Vector2D> coord1 = new ArrayList<Vector2D>();
				coord1.add(new Vector2D(x, y));
				coord1.add(new Vector2D(x+width, y));
				coord1.add(new Vector2D(x+width, y+height));
				coord1.add(new Vector2D(x, y+height));
				
				List<Vector2D> coord2 = new ArrayList<Vector2D>();
				coord2.add(translateCoordinateVector(x, y));
				coord2.add(translateCoordinateVector(x, y+height));
				coord2.add(translateCoordinateVector(x+width, y+height));
				coord2.add(translateCoordinateVector(x+width, y));
				
				Obs obs1 = new Obs(coord1, coord2);
				
				obsList.add(obs1);

			}
		}
		
		return obsList;
	}
	
	public List<Obs> genSquareBlocksList4() {
		
		List<Obs> obsList = new ArrayList<Obs>();
		
		int maxCol = 5;
		int maxRow = 5;
		
		Random rand = new Random(seed);
		Rectangle[][] rects = new Rectangle[row][col];
		int[][] blocks = new int[row][col];
		
		for(int i=0; i<row; i++) {
			for(int j=0; j<col; j++) {

				// check if previous row is empty
				if ((i > 0 && j > 0 && blocks[i - 1][j - 1] != 0) ||
						(i > 0 && blocks[i - 1][j] != 0) || 
						(i > 0 && j < blocks[i].length - 1 && blocks[i - 1][j + 1] != 0)) {
					System.out.println("blocks[" + i + "][" + j + "]:" + blocks[i][j] + ", is adjacent to previous row!");
					continue;
				} 
				
				// check if this row is empty
				if ((j > 0 && blocks[i][j - 1] != 0) || 
						(blocks[i][j] != 0) || 
						(j < blocks[i].length - 1 && blocks[i][j + 1] != 0)) {
					System.out.println("blocks[" + i + "][" + j + "]:" + blocks[i][j] + ", is adjacent to previous col!");
					continue;
				}
				

				int availWidth = col - j;
				int possibleWidth = (int) Math.min(availWidth, maxCol);
				System.out.println(">availWidth: "+availWidth+", maxCol: "+maxCol+", possibleWidth: " + possibleWidth);
				
				int width = possibleWidth;
				if (availWidth > maxCol) {
					width = rand.nextInt(possibleWidth) + 1;
				}
				
				int availHeight = row - i;
				int possibleHeight = (int) Math.min(availHeight, maxRow);
				System.out.println(">availHeight: "+availHeight+", maxRow: "+maxRow+", possibleHeight: " + possibleHeight);
				
				int height = possibleHeight;
				if (availHeight > maxRow) {
					height = rand.nextInt(possibleHeight) + 1;
				}
				
				int minHeight = height;
				int minWidth = width;
				// scan for empty blocks
				for (int ar = 0; ar < height; ar++) {
					for (int ac = 0; ac < width; ac++) {
						if (blocks[i+ar][j+ac] == 1) {
							// if(ar < minHeight) { 
							//  	minHeight = ar; 
							// }
							if (ac < minWidth) {
								minWidth = ac;
							}
							
						}
						
						//if(blocks[i+ar][j+ac] == 1) {
						//	break;
						//}
						
					}
				}
				
				for (int br = 0; br < minHeight; br++) {
					for (int bc = 0; bc < minWidth; bc++) {
						System.out.println("block i:" + i + ", br:" + br + "[" + (i+br) + "]" 
								+ ", minHeight:" + minHeight
								+ " - j:" + j + ", bc:" + bc + "[" + (j+bc) + "]"
								+ ", minWidth:" + minWidth);
						blocks[i+br][j+bc] = 1;
					}
				}

				int x = j;
				int y = i;
				width = minWidth;
				height = minHeight;
				
				Rectangle rect = new Rectangle(x, y, width, height);
				System.out.println("rectangle i:" + i + ", j:" + j + ", rect.x:" + rect.x + ", rect.y:" + rect.y + ", rect.width:"
						+ rect.width + ", rect.height:" + rect.height + ";  ");
				rects[i][j] = rect;
			}
		}
		
		int blockSize = 20;
		
		for (Rectangle[] row : rects) {
			for (Rectangle cell : row) {
				if(cell == null) {
					continue;
				}
				int x = blockSize * (cell.x + 1);
				int y = blockSize * (cell.y + 1);
				int width = blockSize * (cell.width);
				int height = blockSize * (cell.height);
			//	g.drawRect(x, y, width, height);
				
				List<Vector2D> coord1 = new ArrayList<Vector2D>();
				coord1.add(new Vector2D(x, y));
				coord1.add(new Vector2D(x+width, y));
				coord1.add(new Vector2D(x+width, y+height));
				coord1.add(new Vector2D(x, y+height));
				
				List<Vector2D> coord2 = new ArrayList<Vector2D>();
				coord2.add(translateCoordinateVector(x, y));
				coord2.add(translateCoordinateVector(x, y+height));
				coord2.add(translateCoordinateVector(x+width, y+height));
				coord2.add(translateCoordinateVector(x+width, y));
				
				Obs obs1 = new Obs(coord1, coord2);
				
				obsList.add(obs1);

			}
		}
		
		return obsList;
	}

	public List<Obs> genSquareBlocksList5() {
        
		List<Obs> obsList = new ArrayList<Obs>();
		
		try {
			Reader reader = Files.newBufferedReader(Paths.get("polygons-connects.json"));
			
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JSONPolygon jsonp = gson.fromJson(reader, JSONPolygon.class);
			//ArrayList<JSONPolygon> jsonPolygons = gson.fromJson(reader, new TypeToken<ArrayList<JSONPolygon>>(){}.getType());
			reader.close();
			
		//	List<JSONPolygon> jsonPolygonFilter = jsonGraph.stream().filter(g->g.isNode).collect(Collectors.toList());
			
			for (int i = 0; i < jsonp.polygons.size(); i++) {
				Polygon polygon = jsonp.polygons.get(i);
				List<Vector2D> coord1 = new ArrayList<Vector2D>();
				List<Vector2D> coord2 = new ArrayList<Vector2D>();
				
				for (int j = 0; j < polygon.points.size(); j++) {
					PolygonPoint point = polygon.points.get(j);
				//	System.out.println(point.point.getX() + ", " + point.point.getY());
					
					coord1.add(new Vector2D(point.point.getX(), point.point.getY()));
					coord2.add(translateCoordinateVector(point.point.getX(), point.point.getY()));
				}
				Collections.reverse(coord1);
				Collections.reverse(coord2);
				
				//System.out.println(coord1.size() + "<>" + coord2.size());
				Obs obs1 = new Obs(coord1, coord2);
				obsList.add(obs1);
				//System.out.println("--------------------");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return obsList;
	}
	
	public static void main(String[] args) {
		ObsGenerator obsG = new ObsGenerator(48, 85);
		
		for (int i = 1; i <= 48; i++) {
			for (int j = 1; j <= 85; j++) {
				List<Obs> obs = obsG.genSquareBlocksList2(i, j);
				//System.out.println("i->"+ i + ", j->" + j + " = " + obs.size());
			}
		}
	}
	
}
