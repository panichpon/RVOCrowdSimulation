package rvo2example;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;

import rvo2.Goal;
import rvo2.Simulator;

public class LargeSpace10 extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Store the goals of the agents.
	//private final List<Vector2D> goals = new ArrayList<>();
	private List<Goal> goalsList = new ArrayList<Goal>();

	// Random number generator.
	private final Random random = new Random();
	
	List<List<Vector2D>> obsList;
	public static boolean ended = false;

	public static int w = 1920;
	public static int h = 1080;

	public LargeSpace10() {
		setPreferredSize(new Dimension(w, h));
		setOpaque(true);
		setBackground(Color.WHITE);
		setForeground(Color.RED);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		for (int i = 0; i < obsList.size(); i++) {
        	List<Vector2D> polygonPoint = obsList.get(i);
        	Polygon p = new Polygon();
        	for (int j = 0; j < polygonPoint.size(); j++) {
        		Vector2D v = translateCoordinateBackVector(polygonPoint.get(j));
        		p.addPoint((int)v.getX(), (int)v.getY());
			}
        	g2.setColor(Color.RED);
        	g2.drawPolygon(p);
		}
		

		for (int agentNo = 0; agentNo < Simulator.instance.getNumAgents(); agentNo++) {
			
			
			
			Vector2D agentPos = Simulator.instance.getAgentPosition(agentNo);
			//Vector2D velocity = Simulator.instance.getAgentVelocity(agentNo);
			//List<Line> agentLine = Simulator.instance.getAgentLines(agentNo);
			
			Vector2D agentPos_cood = translateCoordinateBackVector(agentPos);
			//Vector2D velocity_cood = translateCoordinateBackVector(velocity);

			
			int subGoalSize = goalsList.get(agentNo).getSubGoalSize(); 
			
			int sgx = 0;
			int sgy = 0;
			
			if(subGoalSize > 0) {
				sgx = (int)goalsList.get(agentNo).getSubGoal().get(0).getX();
				sgy = (int)goalsList.get(agentNo).getSubGoal().get(0).getY();
				Vector2D vector4 = translateCoordinateBackVector(sgx, sgy);
				g2.setColor(Color.RED);
				g2.drawLine((int) agentPos_cood.getX(), (int) agentPos_cood.getY(), (int) vector4.getX(), (int) vector4.getY());
			}
				
			//Vector2D goalPos = goalsList.get(agentNo).getGlobalGoal();
			//Vector2D goalVector = goalsList.get(agentNo).getGlobalGoal().subtract(Simulator.instance.getAgentPosition(agentNo));
			//double lengthSq = goalVector.getNormSq();

			//int gx = (int) goalPos.getX();
			//int gy = (int) goalPos.getY();

			//goalVector = goalVector.scalarMultiply(1 / FastMath.sqrt(lengthSq));

			//int gvx = (int) goalVector.getX();
			//int gvy = (int) goalVector.getY();
			//Vector2D vector2 = translateCoordinateBackVector(gx, gy);
			//Vector2D vector3 = translateCoordinateBackVector(gvx, gvy);
			//g2.setColor(Color.BLUE);
			//g2.drawLine((int) vector1.getX(), (int) vector1.getY(), (int) vector2.getX(), (int) vector2.getY());
			//g2.setColor(Color.ORANGE);
			//g2.drawLine((int) vector1.getX(), (int) vector1.getY(), (int) vector3.getX(), (int) vector3.getY());
			
			//g2.setColor(Color.ORANGE);
			//g2.drawLine((int) agentPos_cood.getX(), (int) agentPos_cood.getY(), (int) velocity_cood.getX(), (int) velocity_cood.getY());
			
			
			
			int redius = (int)Simulator.instance.getAgentRadius(agentNo);
			
			g2.setColor(goalsList.get(agentNo).getColor());
			g2.drawOval((int) agentPos_cood.getX() - (2), (int) agentPos_cood.getY() - (2), (2) * 2, (2) * 2);
			
			g2.setColor(goalsList.get(agentNo).getColor());
			g2.drawOval((int) agentPos_cood.getX() - (redius), (int) agentPos_cood.getY() - (redius), (redius) * 2, (redius) * 2);
			
			if(ended) {
				int point_size = goalsList.get(agentNo).getaddMovingStepSize();
				for (int i = 0; i < point_size; i++) {
					

					g2.setColor(goalsList.get(agentNo).getColor());
					if(i < point_size - 1) {
						Vector2D curr_moving = translateCoordinateBackVector(goalsList.get(agentNo).getMovingStep().get(i));
						Vector2D next_moving = translateCoordinateBackVector(goalsList.get(agentNo).getMovingStep().get(i + 1));
						
						g2.drawLine((int) next_moving.getX(), (int) next_moving.getY(), (int) curr_moving.getX(), (int) curr_moving.getY());
						
					}
					
					
				}
			}
		}
	}

	private void setupScenario() {
		// Specify the global time step of the simulation.
		Simulator.instance.setTimeStep(0.25);

		// Specify the default parameters for agents that are subsequently
		// added.
		Simulator.instance.setAgentDefaults(15.0, 10, 10.0, 10.0, 5.0, 2.0, Vector2D.ZERO);

		// Add agents, specifying their start position, and store their goals on
		// the opposite side of the environment.
/*		final double angle = 0.008 * FastMath.PI;

        for (int i = 0; i < 250; i++) {
            Simulator.instance.addAgent(new Vector2D(FastMath.cos(i * angle), FastMath.sin(i * angle)).scalarMultiply(500.0));
            goalsList.add(new Goal(Simulator.instance.getAgentPosition(i).negate()));
            goalsList.get(i).setColor(getColor());
        }*/
	/*	int redius = 6;
		int agentSize = 8;
		int angle = 360 / agentSize;
		
		int prefer_angle = 0;
		for (int i = 0; i < agentSize; i++) {
			Simulator.instance.addAgent(new Vector2D(0 + redius * FastMath.cos(prefer_angle * FastMath.PI / 180), 0 + redius * FastMath.sin(prefer_angle * FastMath.PI / 180)).scalarMultiply(30));
			goalsList.add(new Goal(Simulator.instance.getAgentPosition(i).negate()));
			goalsList.get(i).setColor(getColor());
			prefer_angle+=angle;
		}*/
		
		int agentIndex = 0;
		
/*		Simulator.instance.addAgent(new Vector2D(200, 0));
		goalsList.add(new Goal(Simulator.instance.getAgentPosition(agentIndex).negate()));
		goalsList.get(agentIndex).setColor(getColor());
		agentIndex++;*/
		
		Simulator.instance.addAgent(new Vector2D(0, 200));
		goalsList.add(new Goal(Simulator.instance.getAgentPosition(agentIndex).negate()));
		goalsList.get(agentIndex).setColor(getColor());
		agentIndex++;
		
/*		Simulator.instance.addAgent(new Vector2D(-200, 0));
		goalsList.add(new Goal(Simulator.instance.getAgentPosition(agentIndex).negate()));
		goalsList.get(agentIndex).setColor(getColor());
		agentIndex++;*/
		
		Simulator.instance.addAgent(new Vector2D(0, -200));
		goalsList.add(new Goal(Simulator.instance.getAgentPosition(agentIndex).negate()));
		goalsList.get(agentIndex).setColor(getColor());
		agentIndex++;
		
		for (int j = 0; j < 12; j++) {
/*			Simulator.instance.addAgent(new Vector2D(200, (j + 1) * 16.0));
			goalsList.add(new Goal(Simulator.instance.getAgentPosition(agentIndex).negate()));
			goalsList.get(agentIndex).setColor(getColor());
			agentIndex++;

			Simulator.instance.addAgent(new Vector2D(200, (-j - 1) * 16.0));
			goalsList.add(new Goal(Simulator.instance.getAgentPosition(agentIndex).negate()));
			goalsList.get(agentIndex).setColor(getColor());
			agentIndex++;*/

			Simulator.instance.addAgent(new Vector2D((j + 1) * 16, 200));
			goalsList.add(new Goal(Simulator.instance.getAgentPosition(agentIndex).negate()));
			goalsList.get(agentIndex).setColor(getColor());
			agentIndex++;

			Simulator.instance.addAgent(new Vector2D((-j - 1) * 16, 200));
			goalsList.add(new Goal(Simulator.instance.getAgentPosition(agentIndex).negate()));
			goalsList.get(agentIndex).setColor(getColor());
			agentIndex++;

/*			Simulator.instance.addAgent(new Vector2D(-200, (j + 1) * 16));
			goalsList.add(new Goal(Simulator.instance.getAgentPosition(agentIndex).negate()));
			goalsList.get(agentIndex).setColor(getColor());
			agentIndex++;

			Simulator.instance.addAgent(new Vector2D(-200, (-j - 1) * 16));
			goalsList.add(new Goal(Simulator.instance.getAgentPosition(agentIndex).negate()));
			goalsList.get(agentIndex).setColor(getColor());
			agentIndex++;*/

			Simulator.instance.addAgent(new Vector2D((j + 1) * 16, -200));
			goalsList.add(new Goal(Simulator.instance.getAgentPosition(agentIndex).negate()));
			goalsList.get(agentIndex).setColor(getColor());
			agentIndex++;

			Simulator.instance.addAgent(new Vector2D((-j - 1) * 16, -200));
			goalsList.add(new Goal(Simulator.instance.getAgentPosition(agentIndex).negate()));
			goalsList.get(agentIndex).setColor(getColor());
			agentIndex++;
		}
	
		
/*		int count = 0;
		
		for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Simulator.instance.addAgent(new Vector2D(55.0 + i * 10.0, 55.0 + j * 10.0).scalarMultiply(2));
                goalsList.add(new Goal(Simulator.instance.getAgentPosition(count).negate()));
                goalsList.get(count).setColor(getColor());
                count++;

                Simulator.instance.addAgent(new Vector2D(-55.0 - i * 10.0, 55.0 + j * 10.0).scalarMultiply(2));
                goalsList.add(new Goal(Simulator.instance.getAgentPosition(count).negate()));
                goalsList.get(count).setColor(getColor());
                count++;

                Simulator.instance.addAgent(new Vector2D(55.0 + i * 10.0, -55.0 - j * 10.0).scalarMultiply(2));
                goalsList.add(new Goal(Simulator.instance.getAgentPosition(count).negate()));
                goalsList.get(count).setColor(getColor());
                count++;

                Simulator.instance.addAgent(new Vector2D(-55.0 - i * 10.0, -55.0 - j * 10.0).scalarMultiply(2));
                goalsList.add(new Goal(Simulator.instance.getAgentPosition(count).negate()));
                goalsList.get(count).setColor(getColor());
                count++;
            }
        }*/

		obsList = new ArrayList<List<Vector2D>>();
		
	/*	final List<Vector2D> obstacle1 = new ArrayList<>();
		obstacle1.add(new Vector2D(-10.0, 40.0).scalarMultiply(2));
		obstacle1.add(new Vector2D(-40.0, 40.0).scalarMultiply(2));
		obstacle1.add(new Vector2D(-40.0, 10.0).scalarMultiply(2));
		obstacle1.add(new Vector2D(-10.0, 10.0).scalarMultiply(2));
		Simulator.instance.addObstacle(obstacle1);
		obsList.add(obstacle1);

		final List<Vector2D> obstacle2 = new ArrayList<>();
		obstacle2.add(new Vector2D(10.0, 40.0).scalarMultiply(2));
		obstacle2.add(new Vector2D(10.0, 10.0).scalarMultiply(2));
		obstacle2.add(new Vector2D(40.0, 10.0).scalarMultiply(2));
		obstacle2.add(new Vector2D(40.0, 40.0).scalarMultiply(2));
		Simulator.instance.addObstacle(obstacle2);
		obsList.add(obstacle2);

		final List<Vector2D> obstacle3 = new ArrayList<>();
		obstacle3.add(new Vector2D(10.0, -40.0).scalarMultiply(2));
		obstacle3.add(new Vector2D(40.0, -40.0).scalarMultiply(2));
		obstacle3.add(new Vector2D(40.0, -10.0).scalarMultiply(2));
		obstacle3.add(new Vector2D(10.0, -10.0).scalarMultiply(2));
		Simulator.instance.addObstacle(obstacle3);
		obsList.add(obstacle3);

		final List<Vector2D> obstacle4 = new ArrayList<>();
		obstacle4.add(new Vector2D(-10.0, -40.0).scalarMultiply(2));
		obstacle4.add(new Vector2D(-10.0, -10.0).scalarMultiply(2));
		obstacle4.add(new Vector2D(-40.0, -10.0).scalarMultiply(2));
		obstacle4.add(new Vector2D(-40.0, -40.0).scalarMultiply(2));
		Simulator.instance.addObstacle(obstacle4);
		obsList.add(obstacle4);*/
		
		// Process the obstacles so that they are accounted for in the
		// simulation.
		Simulator.instance.processObstacles();
	}
	
	public Color getColor() {
		int r = random.nextInt(255);
		int g = random.nextInt(255);
		int b = random.nextInt(255);
		Color col = new Color(r, g, b);
		return col;
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
		System.out.print(Simulator.instance.getGlobalTime());
		for (int agentNo = 0; agentNo < Simulator.instance.getNumAgents(); agentNo++) {
			goalsList.get(agentNo).addMovingStep(Simulator.instance.getAgentPosition(agentNo));
			Vector2D agentPos = Simulator.instance.getAgentPosition(agentNo);
			System.out.print(agentPos.getX() + " "  + agentPos.getY() + " ");
		}
		
		System.out.println();
		repaint();
	}

	private void setPreferredVelocities() {
		// Set the preferred velocity to be a vector of unit magnitude (speed)
		// in the direction of the goal.
		for (int agentNo = 0; agentNo < Simulator.instance.getNumAgents(); agentNo++) {
			
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

			//System.out.println(agentNo+") lengthSq: " + lengthSq + ", goalVector: " + goalVector + ", Global Goal: " + goalsList.get(agentNo).getGlobalGoal() + ", Agent Position: " + agentPos);

			if (lengthSq > 1.0) {
				// this command return vector: (scalar * x, scalar * y)
				goalVector = goalVector.scalarMultiply(1 / FastMath.sqrt(lengthSq));
			}

			//System.out.println(agentNo+") lengthSq: " + lengthSq + ", goalVector: " + goalVector + ", Global Goal: " + goalsList.get(agentNo).getGlobalGoal() + ", Agent Position: " + agentPos);

			Simulator.instance.setAgentPreferredVelocity(agentNo, goalVector);
		
			
			
			int nearAgent = Simulator.instance.getAgentNumAgentNeighbors(agentNo);
			if(nearAgent > 0) {
				
				if(goalsList.get(agentNo).getSubGoalSize() <= 0 && nearAgent > 3) {
					
					if(agentPos.distanceSq(goalsList.get(agentNo).getGlobalGoal()) > 500.0) {
						double sAngle = bearing(agentPos, goalsList.get(agentNo).getGlobalGoal()) - (random.nextInt(20 - (10)) + (10));
						double sDistance = (random.nextInt(40 - (20)) + (20));
						
						double goalX = agentPos.getX() + sDistance * FastMath.cos(sAngle * FastMath.PI / 180);
						double goalY = agentPos.getY() + sDistance * FastMath.sin(sAngle * FastMath.PI / 180);
						
						Vector2D subGoal = new Vector2D(goalX, goalY);
						goalsList.get(agentNo).add(subGoal);
						//System.out.println("subGoal added!");
					}
				}
			}
			
			//System.out.println("nearAgent: " + nearAgent + ", subGoal: " + goalsList.get(agentNo).getSubGoalSize());*/
			
			// Perturb a little to avoid deadlocks due to perfect symmetry.
			final double angle = random.nextDouble() * 2.0 * FastMath.PI;
			final double distance = random.nextDouble() * 0.0001;
			//System.out.println("angle: " + angle + ", distance: " + distance);
			
            Simulator.instance.setAgentPreferredVelocity(agentNo, 
            		Simulator.instance.getAgentPreferredVelocity(agentNo).add(new Vector2D(FastMath.cos(angle), FastMath.sin(angle)).scalarMultiply(distance)));
			
			
		}
		//System.out.println();
	}

	private boolean reachedGoal() {
		// Check if all agents have reached their goals.
		for (int agentNo = 0; agentNo < Simulator.instance.getNumAgents(); agentNo++) {
			Vector2D agentPos = Simulator.instance.getAgentPosition(agentNo);	
			if (agentPos.distanceSq(goalsList.get(agentNo).getGlobalGoal()) > 10.0) {
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
				double distSq = agentPos.distanceSq(goalsList.get(agentNo).getSubGoal().get(0));
				
				if((distSq < 10.0) || (distSq > 10 && distSq < 50) || (Simulator.instance.getAgentNumAgentNeighbors(agentNo) <= 3)) {
					goalsList.get(agentNo).remove();
					break;
				}
			}	
		}
	}

	public static void main(String[] args) {
		long startSearchTime = System.currentTimeMillis();
        System.out.println("start: " + startSearchTime); 
        
		final LargeSpace10 blocks = new LargeSpace10();
		
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
		ended = true;
		blocks.updateVisualization();
		
		long endSearchTime = System.currentTimeMillis();
		System.out.println("end: " + endSearchTime);
        long elapsedTime = endSearchTime - startSearchTime;
        System.out.println("elapsedTime: " + elapsedTime);
	}
}
