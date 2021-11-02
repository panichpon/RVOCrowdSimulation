package rvo2example;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import algorithms.dijkstra.model.Edge;
import algorithms.dijkstra.model.Graph;
import algorithms.dijkstra.model.Vertex;
import polygonmaker.JSONGraph;

public class GraphGenerator {
	private List<Vertex> nodes;
	private List<Edge> edges;

	public Graph genGraph(String jsonFile) {
		nodes = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();

		try {
			Reader reader = Files.newBufferedReader(Paths.get(jsonFile));

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			ArrayList<JSONGraph> jsonGraph = gson.fromJson(reader, new TypeToken<ArrayList<JSONGraph>>(){}.getType());
			reader.close();

			List<JSONGraph> jsonGraphFilter = jsonGraph.stream().filter(g -> g.isNode).collect(Collectors.toList());

			for (int i = 0; i < jsonGraphFilter.size(); i++) {
				JSONGraph graph = jsonGraphFilter.get(i);
				Vertex location = new Vertex("node_" + graph.getId(), i, graph.getPolyId(),
						"polyId_" + graph.getPolyId(), graph.getVertex1(), graph.getVertex2(), graph.getDistance());
				nodes.add(location);
			}

			//System.out.println(nodes.size());
			//System.out.println("---------------------------------------------->first");

			for (int i = 0; i < nodes.size() - 1; i++) {
				Vertex node = nodes.get(i);
				for (int j = 0; j < nodes.size(); j++) {
					Vertex node2 = nodes.get(j);
					if (node.getVertex2().equals(node2.getVertex1())) {
						addLane("Edge0_" + i + "" + j, i, j, node.getDistance());
						addLane("Edge0_" + j + "" + i, j, i, node.getDistance());
					}
				}
			}

			//System.out.println("---------------------------------------------->secound");
			List<JSONGraph> otherConnected = jsonGraph.stream().filter(g -> !g.isNode()).collect(Collectors.toList());

			for (int i = 0; i < otherConnected.size(); i++) {
				JSONGraph node = otherConnected.get(i);

				List<Vertex> v1 = nodes.stream().filter(n -> n.getVertex1().equals(node.getVertex2()))
						.collect(Collectors.toList());
				List<Vertex> v2 = nodes.stream().filter(n -> n.getVertex1().equals(node.getVertex1()))
						.collect(Collectors.toList());

				addLane("Edge1_" + v2.get(0).getIID() + "" + v1.get(0).getIID(), v2.get(0).getIID(), v1.get(0).getIID(), node.getDistance());
				addLane("Edge1_" + v1.get(0).getIID() + "" + v2.get(0).getIID(), v1.get(0).getIID(), v2.get(0).getIID(), node.getDistance());
			}

			//System.out.println("----------------------------------------------edges: " + edges.size());

			for (int i = 0; i < edges.size(); i++) {
				Edge edge = edges.get(i);

			//	System.out.println(edge.getId() + ", " + edge.getWeight() + ", " + edge.getSource().getIID() + ", "
			//			+ edge.getDestination().getIID() + ", " + edge.getSource().getVertex1() + ", "
			//			+ edge.getDestination().getVertex1());
			}
			Graph graph = new Graph(nodes, edges);

			return graph;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private void addLane(String laneId, int sourceLocNo, int destLocNo, double duration) {
		Edge lane = new Edge(laneId, nodes.get(sourceLocNo), nodes.get(destLocNo), duration);
		edges.add(lane);

		System.out.println(laneId + ", " + sourceLocNo + ", " + destLocNo + ", " + duration);
	}
}
