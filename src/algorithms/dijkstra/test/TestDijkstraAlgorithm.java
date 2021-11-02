package algorithms.dijkstra.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import algorithms.dijkstra.engine.DijkstraAlgorithm;
import algorithms.dijkstra.model.Edge;
import algorithms.dijkstra.model.Graph;
import algorithms.dijkstra.model.Vertex;
import polygonmaker.JSONGraph;

public class TestDijkstraAlgorithm {
	private List<Vertex> nodes;
    private List<Edge> edges;

    public void testExcute() {
        nodes = new ArrayList<Vertex>();
        edges = new ArrayList<Edge>();
        
        try {
			Reader reader = Files.newBufferedReader(Paths.get("graph-connects.json"));
			
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			ArrayList<JSONGraph> jsonGraph = gson.fromJson(reader, new TypeToken<ArrayList<JSONGraph>>(){}.getType());
			reader.close();

			List<JSONGraph> jsonGraphFilter = jsonGraph.stream().filter(g->g.isNode).collect(Collectors.toList());

			for (int i = 0; i < jsonGraphFilter.size(); i++) {
				JSONGraph graph = jsonGraphFilter.get(i);
				Vertex location = new Vertex(
					"node_" + graph.getId(), 
					i, 
					graph.getPolyId(), 
					"polyId_" + graph.getPolyId(), 
					graph.getVertex1(), 
					graph.getVertex2(), 
					graph.getDistance());
				nodes.add(location);
			}
			
			System.out.println(nodes.size());
			System.out.println("---------------------------------------------->first");
			for (int i = 0; i < nodes.size() - 1; i++) {
			//	Vertex node1 = nodes.get(i);
			//	Vertex node2 = nodes.get(i + 1);
				
			//	List<Vertex> v1 = nodes.stream().filter(n->n.getVertex1().equals(node1.getVertex2())).collect(Collectors.toList());
			//	List<Vertex> v2 = nodes.stream().filter(n->n.getVertex1().equals(node2.getVertex1())).collect(Collectors.toList());
				
			//	System.out.println(v1.size() + ", " + v1.get(0).getIID() + ", " + v1.get(0).getVertex1());
			//	System.out.println(v2.size() + ", " + v2.get(0).getIID() + ", " + v2.get(0).getVertex1());
				
				Vertex node = nodes.get(i);
				for (int j = 0; j < nodes.size(); j++) {
					Vertex node2 = nodes.get(j);
					if(node.getVertex2().equals(node2.getVertex1())) {
						addLane("Edge0_" + i + "" + j, i, j, node.getDistance());
						addLane("Edge0_" + j + "" + i, j, i, node.getDistance());
					}
				}
			}
			
			System.out.println("---------------------------------------------->secound");
			List<JSONGraph> otherConnected = jsonGraph.stream()
				    .filter(g->!g.isNode())
				    .collect(Collectors.toList());
			
			//System.out.println(otherConnected.size());
			for (int i = 0; i < otherConnected.size(); i++) {
				JSONGraph node = otherConnected.get(i);
				
				List<Vertex> v1 = nodes.stream().filter(n->n.getVertex1().equals(node.getVertex2())).collect(Collectors.toList());
				List<Vertex> v2 = nodes.stream().filter(n->n.getVertex1().equals(node.getVertex1())).collect(Collectors.toList());
				
			//	System.out.println(v1.size() + ", " + v1.get(0).getIID() + ", " + v1.get(0).getVertex1());
			//	System.out.println(v2.size() + ", " + v2.get(0).getIID() + ", " + v2.get(0).getVertex1());
				
				addLane("Edge1_" + v2.get(0).getIID() + "" + v1.get(0).getIID(), v2.get(0).getIID(), v1.get(0).getIID(), node.getDistance());
				addLane("Edge1_" + v1.get(0).getIID() + "" + v2.get(0).getIID(), v1.get(0).getIID(), v2.get(0).getIID(), node.getDistance());
			}
			
			System.out.println("----------------------------------------------edges: "+edges.size());
			
			for (int i = 0; i < edges.size(); i++) {
				Edge edge = edges.get(i);
				
				System.out.println(edge.getId() + ", " +edge.getWeight() + ", " + edge.getSource().getIID() + ", " + edge.getDestination().getIID() + ", " + edge.getSource().getVertex1() + ", " + edge.getDestination().getVertex1());
			}
			
			 // Lets check from location Loc_1 to Loc_10
	        Graph graph = new Graph(nodes, edges);
	        System.out.println();
	        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
	        dijkstra.execute(nodes.get(8));
	        LinkedList<Vertex> path = dijkstra.getPath(nodes.get(14));

	        
	        for (Vertex vertex : path) {
	            System.out.println(vertex.getId() + ": " + vertex.getVertex1());
	        }
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
   /*     for (int i = 0; i < 11; i++) {
            Vertex location = new Vertex("Node_" + i, 0, "Node_" + i, new Point(0, 0), 0, 0);
            nodes.add(location);
            
            System.out.println(location.getId() + ", " + location.getName());
        }

        addLane("Edge_0", 0, 1, 85);
        addLane("Edge_1", 0, 2, 217);
        addLane("Edge_2", 0, 4, 173);
        addLane("Edge_3", 2, 6, 186);
        addLane("Edge_4", 2, 7, 103);
        addLane("Edge_5", 3, 7, 183);
        addLane("Edge_6", 5, 8, 250);
        addLane("Edge_7", 8, 9, 84);
        addLane("Edge_8", 7, 9, 167);
        addLane("Edge_9", 4, 9, 502);
        addLane("Edge_10", 9, 10, 40);
        addLane("Edge_11", 1, 10, 600);

        // Lets check from location Loc_1 to Loc_10
        Graph graph = new Graph(nodes, edges);
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        dijkstra.execute(nodes.get(0));
        LinkedList<Vertex> path = dijkstra.getPath(nodes.get(10));

        for (Vertex vertex : path) {
            System.out.println(vertex.getName() + ": " + vertex.getPoint().x + ", " + vertex.getPoint().y);
        }*/
    }

    private void addLane(String laneId, int sourceLocNo, int destLocNo, double duration) {
        Edge lane = new Edge(laneId, nodes.get(sourceLocNo), nodes.get(destLocNo), duration);
        edges.add(lane);
        
        System.out.println(laneId + ", " + sourceLocNo + ", " + destLocNo +", " + duration);
    }
    
    public static void main(String[] args) {
		new TestDijkstraAlgorithm().testExcute();
	}
}
