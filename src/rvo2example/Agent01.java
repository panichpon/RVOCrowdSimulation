package rvo2example;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;

import rvo2.Simulator;

public class Agent01 extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int w = 1920;
    public static int h = 1080;
	
    // Store the goals of the agents.
    private final List<Vector2D> goals = new ArrayList<>();
    
    public Agent01() {
    	setPreferredSize(new Dimension(w, h));
		setOpaque(true);
		setBackground(Color.CYAN);
		setForeground(Color.RED);
	}
    
    @Override
	protected void paintComponent(Graphics g) {
    	super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		for (int agentNo = 0; agentNo < Simulator.instance.getNumAgents(); agentNo++) {
			Vector2D agentPos = Simulator.instance.getAgentPosition(agentNo);
			
			int ax = (int) agentPos.getX();
            int ay = (int) agentPos.getY();
            
    		Vector2D vector1 = translateCoordinateBackVector(ax, ay);
    		
    		g2.drawOval((int)vector1.getX() - (4/2), (int)vector1.getY() - (4/2), 4, 4);
		}
    
    }
    
    public Vector2D translateCoordinateVector(int x, int y) {
		int xp = x - (w/2);
		int yp = ((y - (h/2)) * (-1));
		Vector2D vector = new Vector2D(xp, yp);
		//System.out.println(x+" -- "+y+" translate to " + xp + ", " + yp);
		
		return vector;
	}
    
    public Vector2D translateCoordinateBackVector(int x, int y) {
		int xp = x + (w/2);
		int yp = ((y - (h/2)) * (-1));
		Vector2D vector = new Vector2D(xp, yp);
		//System.out.println(x+" -- "+y+" translate to " + xp + ", " + yp);
		
		return vector;
	}

    private void setupScenario() {
        // Specify the global time step of the simulation.
        Simulator.instance.setTimeStep(0.25);

        // Specify the default parameters for agents that are subsequently
        // added.
        Simulator.instance.setAgentDefaults(15.0, 10, 10.0, 10.0, 1.5, 2.0, Vector2D.ZERO);

        // Add agents, specifying their start position, and store their goals on
        // the opposite side of the environment.
        final double angle = 0.008 * FastMath.PI;

        for (int i = 0; i < 250; i++) {
            Simulator.instance.addAgent(new Vector2D(FastMath.cos(i * angle), FastMath.sin(i * angle)).scalarMultiply(200.0));
            goals.add(Simulator.instance.getAgentPosition(i).negate());
        }
    }

    private void updateVisualization() {
        // Output the current global time.
        System.out.print(Simulator.instance.getGlobalTime());

        // Output the current position of all the agents.
        for (int agentNo = 0; agentNo < Simulator.instance.getNumAgents(); agentNo++) {
            System.out.print(" " + Simulator.instance.getAgentPosition(agentNo));
        }

        System.out.println();
        repaint();
    }

    private void setPreferredVelocities() {
        // Set the preferred velocity to be a vector of unit magnitude (speed)
        // in the direction of the goal.
        for (int agentNo = 0; agentNo < Simulator.instance.getNumAgents(); agentNo++) {
            Vector2D goalVector = goals.get(agentNo).subtract(Simulator.instance.getAgentPosition(agentNo));
            final double lengthSq = goalVector.getNormSq();

            if (lengthSq > 1.0) {
                goalVector = goalVector.scalarMultiply(1.0 / FastMath.sqrt(lengthSq));
            }

            Simulator.instance.setAgentPreferredVelocity(agentNo, goalVector);
        }
    }

    private boolean reachedGoal() {
        // Check if all agents have reached their goals.
        for (int agentNo = 0; agentNo < Simulator.instance.getNumAgents(); agentNo++) {
            if (Simulator.instance.getAgentPosition(agentNo).distanceSq(goals.get(agentNo)) > Simulator.instance.getAgentRadius(agentNo) * Simulator.instance.getAgentRadius(agentNo)) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        final Agent01 circle = new Agent01();

        // Set up the scenario.
        circle.setupScenario();
        
        JFrame frame = new JFrame();
		frame.setSize(w, h);
		frame.add(circle);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Perform (and manipulate) the simulation.
        do {
            circle.updateVisualization();
            circle.setPreferredVelocities();
            Simulator.instance.doStep();
        }
        while (!circle.reachedGoal());
    }
}
