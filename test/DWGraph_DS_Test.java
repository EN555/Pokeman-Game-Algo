

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import api.DWGraph_DS;
import api.Node;
import api.directed_weighted_graph;
import api.node_data;

class DWGraph_DS_Test {

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

	// 1----(4)---->2
	// ^ <--
	// - --(1)
	// (3) -->3
	// - -------------->
	// 4 -------(5)

	private directed_weighted_graph Graph() {

		directed_weighted_graph graph = new DWGraph_DS();

		node_data a = new Node(1, 1.3);
		node_data b = new Node(2, 1.5);
		node_data c = new Node(3, 1.6);
		node_data d = new Node(4, 1.7);

		graph.addNode(a);
		graph.addNode(b);
		graph.addNode(c);
		graph.addNode(d);

		graph.connect(4, 1, 3);
		graph.connect(4, 3, 5);
		graph.connect(3, 2, 1);
		graph.connect(2, 3, 1);
		graph.connect(1, 2, 4);

		return graph;
	}

	@Test
	void empthyGraphTest() {
		directed_weighted_graph graph = new DWGraph_DS();

		assertNull(graph.getEdge(0, 1), "the node is null!!");

		graph.connect(0, 3, 3.2);

		assertTrue(graph.getMC() == 0, "you have bug at connection function");
		assertThrows(NullPointerException.class, () -> {
			graph.addNode(null);
		});
		assertTrue(graph.getV().size() == 0, "you add null node to the graph!!");
		assertNull(graph.getE(5));
		assertNull(graph.removeEdge(1, 2));

	}

	@Test
	void removeGraphTest() {
		DWGraph_DS g = (DWGraph_DS) Graph();

		assertTrue(g.nodeSize() == 4);

		g.removeEdge(1, 2);
		assertNull(g.getEdge(1, 2), "this edge removed");

		g.removeEdge(2, 1);
		assertTrue(g.edgeSize() == 4);

		g.removeNode(1);
		assertTrue(g.nodeSize() == 3);

		g.removeEdge(2, 3);
//		assertTrue(g.edgeSize() ==2);

		g.removeNode(6);
		assertTrue(g.nodeSize() == 3);
	}

	@Test
	void connectGraphTest() {
		DWGraph_DS g = (DWGraph_DS) Graph();
		int numberOfmodes = g.getMC();
		int numberOfedges = g.edgeSize();
		int numberOfNodes = g.nodeSize();

		g.connect(1, 2, 1.2);
		assertTrue(g.getMC() == numberOfmodes + 1);
		assertTrue(g.edgeSize() == numberOfedges, "you don't need to add edge!");
		assertTrue(g.nodeSize() == numberOfNodes, "you don't need to add edge!");

		g.connect(1, 2, -1);
		assertTrue(g.getMC() == numberOfmodes + 1);

		g.connect(1, 2, 0);
		assertTrue(g.getMC() == numberOfmodes + 1);

	}

	@Test
	void timeTest() {
		long start = System.currentTimeMillis();
		directed_weighted_graph g = new DWGraph_DS();

		for (int i = 0; i < 1000000; i++) {
			g.addNode(new Node(i, i + 1));
		}

		for (int j = 0; j < 100000; j++) {
			g.connect(j, j + 1, j + 1);
		}

		long end = System.currentTimeMillis();
		assertTrue((end - start) < 60000, "you did it up to 10 seconds!");
	}

	/**
	 * generate a simple graph
	 * 
	 * @return
	 */
	static directed_weighted_graph simple_graph_generator() {

		directed_weighted_graph graph = new DWGraph_DS();

		node_data a = new Node(0);
		node_data b = new Node(1);
		node_data c = new Node(2);
		node_data d = new Node(3);

		graph.addNode(a);
		graph.addNode(b);
		graph.addNode(c);
		graph.addNode(d);

		graph.connect(0, 1, 1);
		graph.connect(1, 2, 2);
		graph.connect(2, 3, 3);
		graph.connect(0, 2, 4);
		graph.connect(2, 0, 5);

		return graph;
	}

	@Test
	void simpleMethodsTest() {
		directed_weighted_graph graph = simple_graph_generator();

		assertEquals(4, graph.nodeSize(), "wrong node size");
		assertEquals(5, graph.edgeSize(), "wrong edge size");

		assertEquals(4, graph.getEdge(0, 2).getWeight(), "wrong edge weight");

		int curretnMC = graph.getMC();
		graph.connect(0, 1, 1);
		assertEquals(curretnMC, graph.getMC(), "added action when no action is needed");

		graph.connect(1, 2, 2.5);
		assertEquals(2.5, graph.getEdge(1, 2).getWeight(), "not updeted the weight");

		graph.removeEdge(2, 3);
		assertFalse(graph.getE(2).contains(graph.getEdge(2, 3)),
				"did not remove the edge from the node neighbors list");
		assertEquals(4, graph.edgeSize(), "wrong edge size");

		graph.removeNode(2);
		assertEquals(1, graph.edgeSize(), "wrong edge size");
		assertNull(graph.getE(2), "did'nt propoly removed the node");

	}

}
