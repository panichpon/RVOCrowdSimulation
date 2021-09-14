package rvo2example;

import java.util.Random;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import rvo2.Simulator;

/**
 * @author PoN
 *
 */
public class BuildTreeAgentPerformance {

	private long seed = 6;
	private Random rand;
	public BuildTreeAgentPerformance() {
		// TODO Auto-generated constructor stub
	}
	
	private void setupScenario() {
		rand = new Random(seed);
		 Simulator.instance.setTimeStep(0.25);
		 Simulator.instance.setAgentDefaults(15.0, 10, 5.0, 5.0, 2.0, 2.0, Vector2D.ZERO);
		
		
		for (int i = 0; i < 1000000; i++) {
			int x = rand.nextInt(5000 - (-5000)) + (-5000);
			int y = rand.nextInt(5000 - (-5000)) + (-5000);
			
			System.out.println(x + ", " + y);
			Simulator.instance.addAgent(new Vector2D(x, y));
	
		}
		
		System.out.println(Simulator.instance.getNumAgents());
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BuildTreeAgentPerformance agentTree = new BuildTreeAgentPerformance();
		
		agentTree.setupScenario();

	}

}
