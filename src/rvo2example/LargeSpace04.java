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

import rvo2.Line;
import rvo2.Simulator;

public class LargeSpace04 extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Store the goals of the agents.
	private final List<Vector2D> goals = new ArrayList<>();
	// Random number generator.
	private final Random random = new Random();

	public static int w = 1920;
	public static int h = 1080;
	List<Obs> obsList;

	public LargeSpace04() {
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

			Vector2D goalPos = goals.get(agentNo);
			Vector2D goalVector = goals.get(agentNo).subtract(Simulator.instance.getAgentPosition(agentNo));
			final double lengthSq = goalVector.getNormSq();

			int gx = (int) goalPos.getX();
			int gy = (int) goalPos.getY();

			goalVector = goalVector.scalarMultiply(1 / FastMath.sqrt(lengthSq));

			int gvx = (int) goalVector.getX();
			int gvy = (int) goalVector.getY();

			Vector2D vector2 = translateCoordinateBackVector(gx, gy);
			Vector2D vector3 = translateCoordinateBackVector(gvx, gvy);

			g2.setColor(Color.BLUE);
			g2.drawLine((int) vector1.getX(), (int) vector1.getY(), (int) vector2.getX(), (int) vector2.getY());
			g2.setColor(Color.ORANGE);
			g2.drawLine((int) vector1.getX(), (int) vector1.getY(), (int) vector3.getX(), (int) vector3.getY());
			//g2.setColor(Color.BLACK);
			//g2.drawString(goalVector.toString() + " " + goalPos.toString(), (int) vector3.getX(), (int) vector3.getY());

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
			//g2.drawArc((int) vector1.getX() - (redius), (int) vector1.getY() - (redius), redius * 2, redius * 2, 0, 360);
			//g2.drawOval((int) vector1.getX() - (redius / 2), (int) vector1.getY() - (redius / 2), redius * 2, redius * 2);

		}
	}

	private void setupScenario() {
		// Specify the global time step of the simulation.
		Simulator.instance.setTimeStep(0.25);

		// Specify the default parameters for agents that are subsequently
		// added.
		Simulator.instance.setAgentDefaults(15.0, 10, 5.0, 5.0, 7.0, 0.05, Vector2D.ZERO);

		// Add agents, specifying their start position, and store their goals on
		// the opposite side of the environment.
		Simulator.instance.addAgent(new Vector2D(200, 0));
		goals.add(new Vector2D(-200, 0));

		Simulator.instance.addAgent(new Vector2D(-200, 0));
		goals.add(new Vector2D(200, 0));
		
		Simulator.instance.addAgent(new Vector2D(0, 200));
		goals.add(new Vector2D(0, -200));
		
		Simulator.instance.addAgent(new Vector2D(0, -200));
		goals.add(new Vector2D(0, 200));
		
		Simulator.instance.addAgent(new Vector2D(300, -300));
		goals.add(new Vector2D(-300, 300));

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
			Vector2D goalVector = goals.get(agentNo).subtract(agentPos);

			// this command return double: (x * x + y * y)
			final double lengthSq = goalVector.getNormSq();

			System.out.println(agentNo+") lengthSq: " + lengthSq + ", goalVector: " + goalVector + ", Global Goal: " + goals.get(agentNo) + ", Agent Position: " + agentPos);

			if (lengthSq > 1.0) {
				// this command return vector: (scalar * x, scalar * y)
				goalVector = goalVector.scalarMultiply(1 / FastMath.sqrt(lengthSq));
			}

			System.out.println(agentNo+") lengthSq: " + lengthSq + ", goalVector: " + goalVector + ", Global Goal: " + goals.get(agentNo) + ", Agent Position: " + agentPos);

			Simulator.instance.setAgentPreferredVelocity(agentNo, goalVector);
		
			// Perturb a little to avoid deadlocks due to perfect symmetry.
			final double angle = random.nextDouble() * 10.0 * FastMath.PI;
			final double distance = random.nextDouble() * 0.0001;
			System.out.println("angle: " + angle + ", " + distance + ", " + agentLine.size());

			
			//if(Simulator.instance.getAgentNumAgentNeighbors(agentNo) > 0) {
			//	Simulator.instance.setAgentRadius(agentNo, 4);
				//goalVector = goalVector.scalarMultiply(360 / FastMath.sqrt(lengthSq));
				//Simulator.instance.setAgentPreferredVelocity(agentNo, goalVector);
			//}
			
			double goalX = FastMath.cos(angle);
			double goalY = FastMath.sin(angle);
			Vector2D goalSym = new Vector2D(goalX, goalY);
			
			System.out.println("x="+goalX+", y=" + goalY + ", scalar="+goalSym.scalarMultiply(distance));
			
			Simulator.instance.setAgentPreferredVelocity(agentNo, Simulator.instance.getAgentPreferredVelocity(agentNo).add(goalSym.scalarMultiply(distance)));
			
		}
		System.out.println();
	}

	private boolean reachedGoal() {
		// Check if all agents have reached their goals.
		for (int agentNo = 0; agentNo < Simulator.instance.getNumAgents(); agentNo++) {
			if (Simulator.instance.getAgentPosition(agentNo).distanceSq(goals.get(agentNo)) > 100.0) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		final LargeSpace04 blocks = new LargeSpace04();

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
			Simulator.instance.doStep();
		} while (!blocks.reachedGoal());
	}
}
