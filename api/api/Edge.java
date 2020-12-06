package api;

public class Edge implements edge_data {
	
	private int src; 
	private int dest;
	private double weight;
	private String info;
	private int tag;
	
	//constructor
	
	public Edge(int src , int dest ,double weight) {
		this.src = src;
		this.dest = dest;
		this.weight = weight;
		this.tag=0;
		this.info= "";
	}
	
	//methods
	
	/**
	 * @return the id of the source node of this edge
	 */
	@Override
	public int getSrc() {
		return this.src;
	}

	/**
	 * @return the id of the destination node of this edge 
	 */
	@Override
	public int getDest() {
		return this.dest;
	}

	/**
	 * the weight of this edge
	 */
	@Override
	public double getWeight() {
		return this.weight;
	}

	/**
	 * @return meta data associated with this edge
	 */
	@Override
	public String getInfo() {
		return this.info;
	}
	
	/**
	 * @param changing meta data that associated with this edge
	 */
	@Override
	public void setInfo(String s) {
		this.info =s ;
	}

	/**
	 * @return contain Temporal data (aka color: e,g, white, gray, black) 
	 */
	@Override
	public int getTag() {
		return this.tag;
	}
	
	/**
	 * @param can setting the temporal data usually using for mark an edge
	 */
	@Override
	public void setTag(int t) {
		this.tag =t ;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Edge)) {return false;}
		Edge O = (Edge)other;
		return (this.dest == O.dest && this.src == O.src);
	}
	
	
	public String toString() {
		return "Edge: src: " + this.src + " dest: " + this.dest +" weight: " + this.weight;  
	}

}
