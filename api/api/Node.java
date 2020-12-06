package api;

import java.util.Collection;
import java.util.HashMap;

public class Node implements node_data ,Comparable<node_data> {

	private int key;
	private int tag;
	private String info;
	private geo_location location;
	private double weight;
	private HashMap<Integer,edge_data> neighbors;
	
	//*****  constructors  *****
	
	/**
	 * constructor with given key and weight
	 * @param key
	 * @param weight
	 */
	public Node(int key , double weight) {
	this.key = key;
	this.tag = 0;
	this.info = "";
	this.location = null;
	this.weight = weight;
	this.neighbors = new HashMap<Integer,edge_data>();
	}
	
	/**
	 * constructor with given key and default weight, info, weight, and location
	 * @param key

	 */ 
	public Node(int key) {
		this.key = key;
		this.tag = 0;
		this.weight=0;
		this.info = "";
		this.location = null;
		this.weight = 0;
		this.neighbors = new HashMap<Integer,edge_data>();
		}
	
	/**
	 * constructor with given key, weight, info, and location
	 * @param key
	 * @param weight
	 * @param info
	 * @param location
	 */
	public Node(int key , double weight , String info , geo_location location) {
		this.key = key;
		this.tag = 0;
		this.info = info;
		this.location = location;
		this.weight = weight;
		this.neighbors = new HashMap<Integer,edge_data>();
	}
	
	
	//*****  methods  ******
	
	/**
	 * @return the key (id) of this node
	 */
	@Override
	public int getKey() {
		return this.key;
	}
	
	/**
 	* @return the location of this node 
 	*/
	@Override
	public geo_location getLocation() {
		return this.location;
	}

	/**
	 * set the location of the node to the given location
	 * @param p - the new location
	 */
	@Override
	public void setLocation(geo_location p) {
		this.location = p;
	}

	/**
	 * @return the weight of this node
	 */
	@Override
	public double getWeight() {
		return this.weight;
	}

	/**
	 * set the weight of this node to  the given weight
	 * @param w - the new weight
	 */
	@Override
	public void setWeight(double w) {
		this.weight = w;
	}

	/**
	 * @return the info of this node
	 */
	@Override
	public String getInfo() {
		return this.info;
	}

	/**
	 * set the info of this node to the given info
	 * @param s - the new info
	 */
	@Override
	public void setInfo(String s) {
		this.info = s;
	}

	/**
	 * @return the tag of this node
	 */
	@Override
	public int getTag() {
		return this.tag;
	}

	/**
	 * set the tag of this node to the given tag
	 * @param t - the new tag
	 */
	@Override
	public void setTag(int t) {
		this.tag = t;	
	}
	
	/**
	 * @return a collection of edges for all of this node neighbors
	 */
	public Collection<edge_data> getEdges(){
		return this.neighbors.values();
	}
	
	/**
	 * @return a collection of all of the keys of this node neighbors 
	 */
	public Collection<Integer> getEdgesKeys(){
		return this.neighbors.keySet();
	}
	
	/**
	 * add the given edge to the matching list
	 * @param edge
	 */
	public void add_edge(edge_data edge) {
		//if the edge's source is this node, add it to the neighbors list
		if(edge.getSrc() == this.key) {
			this.neighbors.put(edge.getDest(), edge);
		}
		//if the edge has nothing to do with this node, throw an exception
		else {
			throw new IllegalArgumentException("the given edge has nothnig to do with this node!");
		}
	}
	
	/**
	 * remove the edge this-neighbor from this node neighbors list
	 * @param nei_key - the key of the neighbor to remove
	 * @return the edge_data of the removed edge
	 */
	public edge_data removeNeighbor(int nei_key) {
		return this.neighbors.remove(nei_key);
	}
	
	
	/**
	 * compare between two nodes tag (e.g. tag represent the distance from the src node)
	 * used for dijkstra algorithm
	 */
	@Override
	public int compareTo(node_data o) {	
	if(this.getWeight() > o.getWeight())		//greater than 0
		return 1;
	
	if(this.getWeight()< o.getWeight())		//lower than 0
		return -1;

		return 0;
	}
	
	@Override
	public boolean equals(Object other) {		
		node_data oth = (node_data)other;
		
		if(oth.getKey() == this.getKey())
			return true;
		return false;
	}
	
	/**
	 * @return a string representing the node
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("key: "); sb.append(key+ " ");					//add the key
		
		sb.append("neighbors: [");							//add the keys of all the neighbors
		for(int nei : neighbors.keySet()) {
			sb.append(nei);sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1); sb.append("]");
		
		return sb.toString();								//return the string
	}
	
	
}
