package rvo2example;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;

import rvo2.Goal;
import rvo2.Line;
import rvo2.Simulator;

public class LargeSpace06 extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Store the goals of the agents.
	//private final List<Vector2D> goals = new ArrayList<>();
	private List<Goal> goalsList = new ArrayList<Goal>();

	// Random number generator.
	private final Random random = new Random();

	public static int w = 1920;
	public static int h = 1080;
	List<Obs> obsList;

	public LargeSpace06() {
		setPreferredSize(new Dimension(w, h));
		setOpaque(true);
		setBackground(Color.WHITE);
		setForeground(Color.RED);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		for (int agentNo = 0; agentNo < Simulator.instance.getNumAgents(); agentNo++) {
			Vector2D agentPos = Simulator.instance.getAgentPosition(agentNo);
			//List<Line> agentLine = Simulator.instance.getAgentLines(agentNo);
			
			int ax = (int) agentPos.getX();
			int ay = (int) agentPos.getY();

			Vector2D vector1 = translateCoordinateBackVector(ax, ay);

			Vector2D goalPos = goalsList.get(agentNo).getGlobalGoal();
			Vector2D goalVector = goalsList.get(agentNo).getGlobalGoal().subtract(Simulator.instance.getAgentPosition(agentNo));
			int subGoalSize = goalsList.get(agentNo).getSubGoalSize(); 
			double lengthSq = goalVector.getNormSq();

			int gx = (int) goalPos.getX();
			int gy = (int) goalPos.getY();

			goalVector = goalVector.scalarMultiply(1 / FastMath.sqrt(lengthSq));

			int gvx = (int) goalVector.getX();
			int gvy = (int) goalVector.getY();
			
			int sgx = 0;
			int sgy = 0;
			
			if(subGoalSize > 0) {
				sgx = (int)goalsList.get(agentNo).getSubGoal().get(0).getX();
				sgy = (int)goalsList.get(agentNo).getSubGoal().get(0).getY();
				Vector2D vector4 = translateCoordinateBackVector(sgx, sgy);
				g2.setColor(Color.RED);
				g2.drawLine((int) vector1.getX(), (int) vector1.getY(), (int) vector4.getX(), (int) vector4.getY());
			}
				

			Vector2D vector2 = translateCoordinateBackVector(gx, gy);
			Vector2D vector3 = translateCoordinateBackVector(gvx, gvy);
			

			//g2.setColor(Color.BLUE);
			//g2.drawLine((int) vector1.getX(), (int) vector1.getY(), (int) vector2.getX(), (int) vector2.getY());
			//g2.setColor(Color.ORANGE);
			//g2.drawLine((int) vector1.getX(), (int) vector1.getY(), (int) vector3.getX(), (int) vector3.getY());
			
			
			

			/*for (int i = 0; i < agentLine.size(); i++) {
				Line line = agentLine.get(i);
				
				Vector2D point = translateCoordinateBackVector(line.point);
				Vector2D direction = translateCoordinateBackVector(line.direction);
				
				g2.setColor(Color.BLACK);
				g2.drawLine((int)point.getX(), (int)point.getY(), (int)direction.getX(), (int)direction.getY());
				//System.out.println(point.toString() + "===========================================>" + direction.toString());
				//System.exit(0);
			}*/
			
			// agentPos.scalarMultiply(a);
			int redius = (int)Simulator.instance.getAgentRadius(agentNo);
			
			g2.setColor(Color.RED);
			g2.drawOval((int) vector1.getX() - (2), (int) vector1.getY() - (2), (2) * 2, (2) * 2);
			
			g2.setColor(Color.BLACK);
			g2.drawOval((int) vector1.getX() - (redius), (int) vector1.getY() - (redius), (redius) * 2, (redius) * 2);
		}
	}

	private void setupScenario() {
		// Specify the global time step of the simulation.
		Simulator.instance.setTimeStep(0.25);

		// Specify the default parameters for agents that are subsequently
		// added.
		Simulator.instance.setAgentDefaults(15.0, 10, 10.0, 10.0, 7.0, 0.05, Vector2D.ZERO);

		// Add agents, specifying their start position, and store their goals on
		// the opposite side of the environment.
		double angle = 0;
		int redius = 10;
		
		for (int i = 0; i < 24; i++) {
			Simulator.instance.addAgent(new Vector2D(0 + redius * FastMath.cos(angle * FastMath.PI / 180), 0 + redius * FastMath.sin(angle * FastMath.PI / 180)).scalarMultiply(30));
			goalsList.add(new Goal(Simulator.instance.getAgentPosition(i).negate()));
			angle+=15;
		}
		
		
	/*	Simulator.instance.addAgent(new Vector2D(0 + redius * FastMath.cos(0 * FastMath.PI / 180), 0 + redius * FastMath.sin(0 * FastMath.PI / 180)).scalarMultiply(30));
		goalsList.add(new Goal(Simulator.instance.getAgentPosition(0).negate()));
		
		Simulator.instance.addAgent(new Vector2D(0 + redius * FastMath.cos(30 * FastMath.PI / 180), 0 + redius * FastMath.sin(30 * FastMath.PI / 180)).scalarMultiply(30));
		goalsList.add(new Goal(Simulator.instance.getAgentPosition(1).negate()));
		
		Simulator.instance.addAgent(new Vector2D(0 + redius * FastMath.cos(60 * FastMath.PI / 180), 0 + redius * FastMath.sin(60 * FastMath.PI / 180)).scalarMultiply(30));
		goalsList.add(new Goal(Simulator.instance.getAgentPosition(2).negate()));

		Simulator.instance.addAgent(new Vector2D(0 + redius * FastMath.cos(90 * FastMath.PI / 180), 0 + redius * FastMath.sin(90 * FastMath.PI / 180)).scalarMultiply(30));
		goalsList.add(new Goal(Simulator.instance.getAgentPosition(3).negate()));
		
		Simulator.instance.addAgent(new Vector2D(0 + redius * FastMath.cos(120 * FastMath.PI / 180), 0 + redius * FastMath.sin(120 * FastMath.PI / 180)).scalarMultiply(30));
		goalsList.add(new Goal(Simulator.instance.getAgentPosition(4).negate()));
		
		Simulator.instance.addAgent(new Vector2D(0 + redius * FastMath.cos(150 * FastMath.PI / 180), 0 + redius * FastMath.sin(150 * FastMath.PI / 180)).scalarMultiply(30));
		goalsList.add(new Goal(Simulator.instance.getAgentPosition(5).negate()));
		
		Simulator.instance.addAgent(new Vector2D(0 + redius * FastMath.cos(180 * FastMath.PI / 180), 0 + redius * FastMath.sin(180 * FastMath.PI / 180)).scalarMultiply(30));
		goalsList.add(new Goal(Simulator.instance.getAgentPosition(6).negate()));
		
		Simulator.instance.addAgent(new Vector2D(0 + redius * FastMath.cos(210 * FastMath.PI / 180), 0 + redius * FastMath.sin(210 * FastMath.PI / 180)).scalarMultiply(30));
		goalsList.add(new Goal(Simulator.instance.getAgentPosition(7).negate()));
		
		Simulator.instance.addAgent(new Vector2D(0 + redius * FastMath.cos(240 * FastMath.PI / 180), 0 + redius * FastMath.sin(240 * FastMath.PI / 180)).scalarMultiply(30));
		goalsList.add(new Goal(Simulator.instance.getAgentPosition(8).negate()));
		
		Simulator.instance.addAgent(new Vector2D(0 + redius * FastMath.cos(270 * FastMath.PI / 180), 0 + redius * FastMath.sin(270 * FastMath.PI / 180)).scalarMultiply(30));
		goalsList.add(new Goal(Simulator.instance.getAgentPosition(9).negate()));
		
		Simulator.instance.addAgent(new Vector2D(0 + redius * FastMath.cos(300 * FastMath.PI / 180), 0 + redius * FastMath.sin(300 * FastMath.PI / 180)).scalarMultiply(30));
		goalsList.add(new Goal(Simulator.instance.getAgentPosition(10).negate()));
		
		Simulator.instance.addAgent(new Vector2D(0 + redius * FastMath.cos(330 * FastMath.PI / 180), 0 + redius * FastMath.sin(330 * FastMath.PI / 180)).scalarMultiply(30));
		goalsList.add(new Goal(Simulator.instance.getAgentPosition(11).negate()));*/

		// Process the obstacles so that they are accounted for in the
		// simulation.
		Simulator.instance.processObstacles();
	}

	public Vector2D translateCoordinateVector(int x, int y) {
		int xp = x - (w / 2);
		int yp = ((y - (h / 2)) * (-1));
		Vector2D vector = new Vector2D(xp, yp);
		// System.out.println(x+" -- "+y+" translate to " + xp + ", " + yp);

		return vector;
	}

	public Vector2D translateCoordinateBackVector(int x, int y) {
		int xp = x + (w / 2);
		int yp = ((y - (h / 2)) * (-1));
		Vector2D vector = new Vector2D(xp, yp);
		// System.out.println(x+" -- "+y+" translate to " + xp + ", " + yp);

		return vector;
	}
	
	public Vector2D translateCoordinateBackVector(Vector2D v) {
		int xp = (int)v.getX() + (w / 2);
		int yp = (((int)v.getY() - (h / 2)) * (-1));
		Vector2D vector = new Vector2D(xp, yp);
		// System.out.println(x+" -- "+y+" translate to " + xp + ", " + yp);

		return vector;
	}
	
	public static float getAngle(Vector2D p1, Vector2D p2) {
	    float angle = (float) Math.toDegrees(Math.atan2(p1.getY() - p2.getY(), p1.getY() - p2.getX()));
	    
	    if(angle < 0){
	        angle += 360;
	    }

	    return angle;
	}
	
	public static double angleBetween2CartesianPoints(Vector2D p1, Vector2D p2) {
	    double angle = Math.atan2((p2.getX() - p1.getX()), (p2.getY() - p1.getY())) * 180 / Math.PI;
	    if (angle < 0) {
	        return (360 + angle);
	    } else {
	        return (angle);
	    }
	}
	
	public static double bearing(Vector2D p1, Vector2D p2) {
	    double TWOPI = FastMath.PI * 2; //6.283185307179586
	    double RAD2DEG = 180 / FastMath.PI; // (180 / FastMath.PI) - 57.2957795130823209
	    // if (a1 = b1 and a2 = b2) throw an error 
	    double theta = FastMath.atan2(p2.getX() - p1.getX(), p1.getY() - p2.getY());
	    if (theta < 0.0) {
	        theta += TWOPI;
	    }
	    return RAD2DEG * theta;
	}

	private void updateVisualization() {
		// Output the current global time.
		System.out.println(Simulator.instance.getGlobalTime());
		repaint();
	}

	private void setPreferredVelocities() {
		// Set the preferred velocity to be a vector of unit magnitude (speed)
		// in the direction of the goal.
		for (int agentNo = 0; agentNo < Simulator.instance.getNumAgents(); agentNo++) {
			List<Line> agentLine = Simulator.instance.getAgentLines(agentNo);
			Vector2D agentPos = Simulator.instance.getAgentPosition(agentNo);

			// this command return vector: (x - p.x, y - p.y)
			Vector2D goalVector;
			if(goalsList.get(agentNo).getSubGoalSize() > 0) {
				goalVector = goalsList.get(agentNo).getSubGoal().get(0).subtract(agentPos);
			} else {
				goalVector = goalsList.get(agentNo).getGlobalGoal().subtract(agentPos);
			}

			// this command return double: (x * x + y * y)
			final double lengthSq = goalVector.getNormSq();

			System.out.println(agentNo+") lengthSq: " + lengthSq + ", goalVector: " + goalVector + ", Global Goal: " + goalsList.get(agentNo).getGlobalGoal() + ", Agent Position: " + agentPos);

			if (lengthSq > 1.0) {
				// this command return vector: (scalar * x, scalar * y)
				goalVector = goalVector.scalarMultiply(1 / FastMath.sqrt(lengthSq));
			}

			System.out.println(agentNo+") lengthSq: " + lengthSq + ", goalVector: " + goalVector + ", Global Goal: " + goalsList.get(agentNo).getGlobalGoal() + ", Agent Position: " + agentPos);

			Simulator.instance.setAgentPreferredVelocity(agentNo, goalVector);
		
			
			
		/*	int nearAgent = Simulator.instance.getAgentNumAgentNeighbors(agentNo);
			if(nearAgent > 0) {
				
				if(goalsList.get(agentNo).getSubGoalSize() <= 0) {
					double sAngle = bearing(agentPos, goalsList.get(agentNo).getGlobalGoal()) - (random.nextInt(20 - (10)) + (10));
					double sDistance = (random.nextInt(40 - (20)) + (20));
					
					double goalX = agentPos.getX() + sDistance * FastMath.cos(sAngle * FastMath.PI / 180);
					double goalY = agentPos.getY() + sDistance * FastMath.sin(sAngle * FastMath.PI / 180);
					
					Vector2D subGoal = new Vector2D(goalX, goalY);
					goalsList.get(agentNo).add(subGoal);
					System.out.println("subGoal added!");
				}
			}
			
			System.out.println("nearAgent: " + nearAgent + ", subGoal: " + goalsList.get(agentNo).getSubGoalSize());*/
			
			// Perturb a little to avoid deadlocks due to perfect symmetry.
			final double angle = random.nextDouble() * 2.0 * FastMath.PI;
			final double distance = random.nextDouble() * 0.0001;
			System.out.println("angle: " + angle + ", distance: " + distance + ", orcaLine: " + agentLine.size());
			
			double goalX = FastMath.cos(angle);
			double goalY = FastMath.sin(angle);
			Vector2D goalSym = new Vector2D(goalX, goalY);
			
			System.out.println("x="+goalX+", y=" + goalY + ", scalar="+goalSym.scalarMultiply(distance)+ ", subgoal: " + goalsList.get(agentNo).getSubGoalSize());
			
			Simulator.instance.setAgentPreferredVelocity(agentNo, Simulator.instance.getAgentPreferredVelocity(agentNo).add(goalSym.scalarMultiply(distance)));
			
			
		}
		System.out.println();
	}

	private boolean reachedGoal() {
		// Check if all agents have reached their goals.
		for (int agentNo = 0; agentNo < Simulator.instance.getNumAgents(); agentNo++) {
			Vector2D agentPos = Simulator.instance.getAgentPosition(agentNo);	
			if (agentPos.distanceSq(goalsList.get(agentNo).getGlobalGoal()) > 100.0) {
				return false;
			}
		}
		return true;
	}
	
	private void updateSubGoal() {
		// Check if all agents have reached their subgoals.
		for (int agentNo = 0; agentNo < Simulator.instance.getNumAgents(); agentNo++) {
			Vector2D agentPos = Simulator.instance.getAgentPosition(agentNo);
			
			if (goalsList.get(agentNo).getSubGoalSize() > 0) {
			//	System.out.println(">");
				double distSq = agentPos.distanceSq(goalsList.get(agentNo).getSubGoal().get(0));
			//	System.out.println("distSq: " + distSq);
				if(distSq < 10.0) {
			//		System.out.println(">>");
					goalsList.get(agentNo).remove();
				}
			}	
		}
	}

	public static void main(String[] args) {
		final LargeSpace06 blocks = new LargeSpace06();

		// Set up the scenario.
		blocks.setupScenario();

		JFrame frame = new JFrame();
		frame.setSize(w, h);
		frame.add(blocks);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		// Perform (and manipulate) the simulation.
		do {
			blocks.updateVisualization();
			blocks.setPreferredVelocities();
			blocks.updateSubGoal();
			Simulator.instance.doStep();
		} while (!blocks.reachedGoal());
	}
}
