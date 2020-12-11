package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import api.DWGraph_Algo;
import api.DWGraph_DS;
import api.Node;
import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import api.node_data;

class DWGrapg_Algo_Test {
	@BeforeAll
	static void runOnceBeforeClass() {
		System.out.println("Graph_Tests");
	}

	@AfterAll
	static void runOnceAfterAll() {
		System.out.println("The Test Finished!");
	}

	@BeforeEach
	void runBeforeEach() {
		System.out.println("The Next Test");
	}

	@AfterEach
	void runAfterEach() {
		System.out.println("The  Current Test Finished!");
	}

	// check an empty graph

	@Test
	void empthyGraphTest() {
		directed_weighted_graph graph = new DWGraph_DS();
		dw_graph_algorithms algo = new DWGraph_Algo();
		algo.init(graph);

		assertNotNull(algo.copy());
		assertTrue(algo.isConnected(), "it's empty graph!");
		assertTrue(algo.shortestPathDist(1, 3) == -1);
		assertNull(algo.shortestPath(0, 1));
		assertNull(algo.shortestPath(0, 0));
		assertFalse(algo.load("blalal"));
//		assertFalse(algo.save("emptyGraph.txt"));	//can save an empty graph as empty file
	}

	// check case of null graph

	@Test
	void nullGraphTest() {
		dw_graph_algorithms algo = new DWGraph_Algo();
		algo.init(null);

		algo.init(null);
		assertNull(algo.copy(), "it's null!!");
		assertFalse(algo.isConnected(), "it's null graph - can't be connect!");
		assertTrue(algo.shortestPathDist(1, 3) == -1, "no path at null graph!");
		assertNull(algo.shortestPath(0, 1));
		assertNull(algo.shortestPath(0, 0));
		assertFalse(algo.load("blalal"));
		assertFalse(algo.save("nullGraph.txt")); // can't save null graph
	}

	// create a simple graph

	// 1----(1)------>2--(22)->5
	// ^ <-- ->
	// - -(1) (2)
	// - -
	// (3) -->3
	// - ------>
	// 4 <-------(5)

	directed_weighted_graph simpleGraphGenerator() {

		directed_weighted_graph graph = new DWGraph_DS();

		node_data a = new Node(1, 1.3);
		node_data b = new Node(2, 1.5);
		node_data c = new Node(3, 1.6);
		node_data d = new Node(4, 1.7);
		node_data e = new Node(5, 1.7);

		graph.addNode(a);
		graph.addNode(b);
		graph.addNode(c);
		graph.addNode(d);
		graph.addNode(e);

		graph.connect(4, 1, 3);
		graph.connect(4, 3, 5);
		graph.connect(3, 2, 1);
		graph.connect(2, 3, 1);
		graph.connect(1, 2, 1);
		graph.connect(2, 5, 22);
		graph.connect(3, 5, 2);
		graph.connect(3, 4, 5);

		return graph;
	}

	@Test
	void graphConnect() {

		directed_weighted_graph graph = simpleGraphGenerator();
		DWGraph_Algo alg = new DWGraph_Algo();
		alg.init(graph);

		assertTrue(alg.shortestPathDist(1, 5) == 4);
		assertTrue(alg.shortestPathDist(1, 4) == 7);
		assertTrue(alg.shortestPathDist(1, 6) == -1, "no path between 1 to 6!");

		assertFalse(alg.isConnected(), "the graph isn't connected!");

	}

	@Test
	void testSimpleGraph() {
		directed_weighted_graph graph = DWGraph_DS_Test.simple_graph_generator();
		dw_graph_algorithms ga = new DWGraph_Algo();
		ga.init(graph);

		// Copy
		directed_weighted_graph copy = ga.copy();
		assertTrue(graph.equals(copy), "not an exact copy");

		// isConnected
		assertFalse(ga.isConnected(), "the graph is not connected");
		graph.connect(3, 2, 9);
		assertTrue(ga.isConnected(), "the graph is connected");
		graph.removeEdge(3, 2);

		// sortestPathDist
		assertEquals(3, ga.shortestPathDist(0, 2), "wrong distance");
		assertEquals(-1, ga.shortestPathDist(3, 1), "not such path");

		// sortestPath
		assertNull(ga.shortestPath(3, 2));
		LinkedList<node_data> temp = new LinkedList<node_data>();
		temp.add(graph.getNode(0));
		assertEquals(ga.shortestPath(0, 0), temp, "path from node to it self is itself only");
		temp.add(graph.getNode(1));
		temp.add(graph.getNode(2));
		assertEquals(ga.shortestPath(0, 2), temp, "wrong path");

		// save - load
		directed_weighted_graph graph1 = ga.getGraph();
		assertTrue(ga.save("g1.txt"), "graph did'nt save");
		assertTrue(ga.load("g1.txt"), "garph did'nt load");
		assertEquals(graph1, ga.getGraph(), "loaded wrong graph");
	}

	@Test
	void testEmptyGraph() {
		directed_weighted_graph graph = new DWGraph_DS();
		dw_graph_algorithms ga = new DWGraph_Algo();
		ga.init(graph);

		// copy
		assertEquals(graph, ga.copy(), "not copy");
		// isConnected
		assertTrue(ga.isConnected(), "empty graph is conneted");
		// sortestPathDist
		assertEquals(-1, ga.shortestPathDist(0, 1), "no path in empty graph");
		// sortestPath
		assertNull(ga.shortestPath(0, 0), "no path in empty graph");
		// save - load
		directed_weighted_graph graph1 = ga.getGraph();
		assertTrue(ga.save("g.txt"), "graph did'nt save");
		assertTrue(ga.load("g.txt"), "garph did'nt load");
		assertEquals(graph1, ga.getGraph(), "loaded wrong graph");
	}

	@Test
	void testNullGraph() {
		dw_graph_algorithms ga = new DWGraph_Algo();
		ga.init(null);

		// copy
		assertNull(ga.copy(), "copy of null is null");
		// isConnected
		assertFalse(ga.isConnected(), "null graph is not connected");
		// sortestPathDist
		assertEquals(-1, ga.shortestPathDist(0, 1), "no path in null graph");
		// sortestPath
		assertNull(ga.shortestPath(0, 0), "no path in null graph");
		// save
		assertFalse(ga.save("null.txt"), "null can not be saved");
	}

}
