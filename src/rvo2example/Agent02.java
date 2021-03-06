/*
 * Blocks.java
 * RVO2 Library Java
 *
 * Copyright 2008 University of North Carolina at Chapel Hill
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Please send all bug reports to <geom@cs.unc.edu>.
 *
 * The authors may be contacted via:
 *
 * Jur van den Berg, Stephen J. Guy, Jamie Snape, Ming C. Lin, Dinesh Manocha
 * Dept. of Computer Science
 * 201 S. Columbia St.
 * Frederick P. Brooks, Jr. Computer Science Bldg.
 * Chapel Hill, N.C. 27599-3175
 * United States of America
 *
 * <http://gamma.cs.unc.edu/RVO2/>
 */

package rvo2example;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;

import rvo2.Simulator;

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

// Example showing a demo with 100 agents split in four groups initially
// positioned in four corners of the environment. Each agent attempts to move to
// other side of the environment through a narrow passage generated by four
// obstacles. There is no road map to guide the agents around the obstacles.
class Agent02 extends JPanel{
    // Store the goals of the agents.
    private final List<Vector2D> goals = new ArrayList<>();

    // Random number generator.
    private final Random random = new Random();
    
	private static final long serialVersionUID = 1L;
	public static int w = 1920;
    public static int h = 1080;
    
    List<List<Vector2D>> obsList;

    public Agent02() {
    	setPreferredSize(new Dimension(w, h));
		setOpaque(true);
		setBackground(Color.CYAN);
		setForeground(Color.RED);
    	
	}
    
    @Override
	protected void paintComponent(Graphics g) {
    	super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
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
    
    public Vector2D translateCoordinateBackVector(Vector2D v) {
		int xp = (int)v.getX() + (w/2);
		int yp = (((int)v.getY() - (h/2)) * (-1));
		Vector2D vector = new Vector2D(xp, yp);
		//System.out.println(x+" -- "+y+" translate to " + xp + ", " + yp);
		
		return vector;
	}
    
    private void setupScenario() {
        // Specify the global time step of the simulation.
        Simulator.instance.setTimeStep(0.25);

        // Specify the default parameters for agents that are subsequently
        // added.
        Simulator.instance.setAgentDefaults(15.0, 10, 5.0, 5.0, 2.0, 2.0, Vector2D.ZERO);

        // Add agents, specifying their start position, and store their goals on
        // the opposite side of the environment.
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Simulator.instance.addAgent(new Vector2D(55.0 + i * 10.0, 55.0 + j * 10.0));
                goals.add(new Vector2D(-75.0, -75.0));

                Simulator.instance.addAgent(new Vector2D(-55.0 - i * 10.0, 55.0 + j * 10.0));
                goals.add(new Vector2D(75.0, -75.0));

                Simulator.instance.addAgent(new Vector2D(55.0 + i * 10.0, -55.0 - j * 10.0));
                goals.add(new Vector2D(-75.0, 75.0));

                Simulator.instance.addAgent(new Vector2D(-55.0 - i * 10.0, -55.0 - j * 10.0));
                goals.add(new Vector2D(75.0, 75.0));
            }
        }

        obsList = new ArrayList<List<Vector2D>>();
        // Add (polygonal) obstacles, specifying their vertices in
        // counterclockwise order.
        final List<Vector2D> obstacle1 = new ArrayList<>();
        obstacle1.add(new Vector2D(-10.0, 40.0));
        obstacle1.add(new Vector2D(-40.0, 40.0));
        obstacle1.add(new Vector2D(-40.0, 10.0));
        obstacle1.add(new Vector2D(-10.0, 10.0));
        Simulator.instance.addObstacle(obstacle1);
        obsList.add(obstacle1);

        final List<Vector2D> obstacle2 = new ArrayList<>();
        obstacle2.add(new Vector2D(10.0, 40.0));
        obstacle2.add(new Vector2D(10.0, 10.0));
        obstacle2.add(new Vector2D(40.0, 10.0));
        obstacle2.add(new Vector2D(40.0, 40.0));
        Simulator.instance.addObstacle(obstacle2);
        obsList.add(obstacle2);

        final List<Vector2D> obstacle3 = new ArrayList<>();
        obstacle3.add(new Vector2D(10.0, -40.0));
        obstacle3.add(new Vector2D(40.0, -40.0));
        obstacle3.add(new Vector2D(40.0, -10.0));
        obstacle3.add(new Vector2D(10.0, -10.0));
        Simulator.instance.addObstacle(obstacle3);
        obsList.add(obstacle3);
        
        final List<Vector2D> obstacle4 = new ArrayList<>();
        obstacle4.add(new Vector2D(-10.0, -40.0));
        obstacle4.add(new Vector2D(-10.0, -10.0));
        obstacle4.add(new Vector2D(-40.0, -10.0));
        obstacle4.add(new Vector2D(-40.0, -40.0));
        Simulator.instance.addObstacle(obstacle4);
        obsList.add(obstacle4);

        // Process the obstacles so that they are accounted for in the
        // simulation.
        Simulator.instance.processObstacles();
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

            // Perturb a little to avoid deadlocks due to perfect symmetry.
            final double angle = random.nextDouble() * 2.0 * FastMath.PI;
            final double distance = random.nextDouble() * 0.0001;

            Simulator.instance.setAgentPreferredVelocity(agentNo, Simulator.instance.getAgentPreferredVelocity(agentNo).add(new Vector2D(FastMath.cos(angle), FastMath.sin(angle)).scalarMultiply(distance)));
        }
    }

    private boolean reachedGoal() {
        // Check if all agents have reached their goals.
        for (int agentNo = 0; agentNo < Simulator.instance.getNumAgents(); agentNo++) {
            if (Simulator.instance.getAgentPosition(agentNo).distanceSq(goals.get(agentNo)) > 400.0) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        final Agent02 blocks = new Agent02();

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
        }
        while (!blocks.reachedGoal());
    }
}
