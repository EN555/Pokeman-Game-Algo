package api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class DWGraph_DS implements directed_weighted_graph {

	private HashMap<Integer, node_data> nodes;
	private HashMap<String, edge_data> edges; // the key composed of "src,dest"
	private int ModeCount = 0;

	// ***** constructors ****

	public DWGraph_DS() {
		this.edges = new HashMap<String, edge_data>();
		this.nodes = new HashMap<Integer, node_data>();
	}

	/**
	 * @return the node_data for the given key, null is not in the graph
	 * @param key
	 */
	@Override
	public node_data getNode(int key) {
		return this.nodes.get(key);
	}

	/**
	 * @return the edge_data for the given src-dest, null if does'nt exist
	 * @param src
	 * @param dest
	 */
	@Override
	public edge_data getEdge(int src, int dest) {
		return this.edges.get(src + "," + dest);
	}

	/**
	 * add the given node to the graph
	 * 
	 * @param n - node_data to add
	 */
	@Override
	public void addNode(node_data n) {
		if (n == null) {
			throw new NullPointerException("can not add null to the graph");
		} // make sure the node is not null

		if (!this.nodes.containsKey(n.getKey())) { // check if have node with same key

			Node node = new Node(n.getKey(), n.getWeight(), n.getInfo(), n.getLocation()); // for to use at local
																							// function, need to convert
																							// the node to Node class

			this.nodes.put(node.getKey(), node); // put the node in the nodes list
			this.ModeCount++; // increase the counter
		}
	}

	/**
	 * add the edge src-dest with weight w, does noting if nodes does'nt exist in
	 * the graph of the edge already exist. if the edge exist, but with different
	 * weight, update the weight
	 * 
	 * @param src
	 * @param dest
	 * @param w    - the edge weight
	 */
	@Override
	public void connect(int src, int dest, double w) {
		if (!this.nodes.containsKey(src) || !this.nodes.containsKey(dest) || w <= 0) {
			return;
		} // check for valid input
		if (this.edges.containsKey(src + "," + dest) && this.getEdge(src, dest).getWeight() == w) {
			return;
		} // if the edge is in the graph, do nothing //i

		Edge new_edge = new Edge(src, dest, w); // create the new edge

		((Node) this.nodes.get(src)).add_edge(new_edge); // add the edge to src list

		this.edges.put(src + "," + dest, new_edge); // add the edge to the graph list
		this.ModeCount++; // increase the counter
	}

	/**
	 * @return a collection of all the nodes in the graph(shallow copy)
	 */
	@Override
	public Collection<node_data> getV() {
		return this.nodes.values();
	}

	/**
	 * @return a collection of all the edges starting at the given node
	 * @param node_id
	 */
	@Override
	public Collection<edge_data> getE(int node_id) {
		if (!this.nodes.containsKey(node_id)) {
			return null;
		} // make sure the node is in the graph
		Node n = (Node) this.nodes.get(node_id); // get the node as Node object
		return n.getEdges(); // return collection of the edges from the node
	}

	/**
	 * remove a node from the graph
	 * 
	 * @return the node_data of the removed node
	 * @param key - the node to remove
	 */
	@Override
	public node_data removeNode(int key) {
		if (!this.nodes.containsKey(key)) {
			return null;
		} // make sure the node is in the graph

		Node n = (Node) this.nodes.get(key); // get the node as Node object

		Iterator<edge_data> ite = this.edges.values().iterator();
		while (ite.hasNext()) { // go through the edges
			edge_data e = ite.next();

			if (e.getSrc() == key) { // if the removed node is the source
				ite.remove(); // remove
				this.ModeCount++;
			}

			if (e.getDest() == key) { // if the removed node is the destination
				ite.remove(); // remove
				((Node) this.getNode(e.getSrc())).removeNeighbor(key); // remove from src list
				this.ModeCount++;
			}
		}

		this.nodes.remove(key); // remove the node from the nodes list
		this.ModeCount++; // increase the counter
		return n; // return the removed node
	}

	/**
	 * remove an edge (src-dest) from the graph
	 * 
	 * @return the edge_data of the removed edge
	 * @param src
	 * @param dest
	 */
	@Override
	public edge_data removeEdge(int src, int dest) {
		if (!this.edges.containsKey(src + "," + dest)) {
			return null;
		} // make sure the edge exist in the graph

		((Node) this.nodes.get(src)).removeNeighbor(dest); // remove the edge from src list

		this.ModeCount++; // increase the counter
		return this.edges.remove(src + "," + dest); // remove the edge from graph list, and return it
	}

	/**
	 * @return the number of nodes in the graph
	 */
	@Override
	public int nodeSize() {
		return this.nodes.size();
	}

	/**
	 * @return the number of edges in the graph
	 */
	@Override
	public int edgeSize() {
		return this.edges.size();
	}

	/**
	 * @return the number of changes to the inner state of the graph
	 */
	@Override
	public int getMC() {
		return this.ModeCount;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof DWGraph_DS)) {
			return false;
		}

		DWGraph_DS O = (DWGraph_DS) other;

		return (this.nodes.keySet().equals(O.nodes.keySet()) && this.edges.keySet().equals(O.edges.keySet()));
	}

	/**
	 * @return a string representing the graph
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (node_data node : nodes.values()) { // add all the nodes
			sb.append(node.toString());
			sb.append("\n");
		}
		return sb.toString(); // return the string
	}

}
