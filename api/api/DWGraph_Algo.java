package api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import com.google.gson.*;

import Server.Game_Server_Ex2;
import gameClient.Arena;

public class DWGraph_Algo implements dw_graph_algorithms ,JsonDeserializer<DWGraph_DS> {
	
	private directed_weighted_graph g;
	
	public DWGraph_Algo() {
		this.g= new DWGraph_DS();
	}
	
	@Override
	public void init(directed_weighted_graph g) {
		this.g = g;
	}

	@Override
	public directed_weighted_graph getGraph() {
		return this.g;
	}
	
	/**
	 * @return deep copy of the this graph 
	 */
	@Override
	public directed_weighted_graph copy() {
		
		if(this.g == null)		//check if the load graph is null 
			return null;
		
		directed_weighted_graph h = new DWGraph_DS();			//create new graph
		
		//create the new nodes of the graph and insert them to the new graph
		
		Iterator<node_data> iterNodeCreator = this.g.getV().iterator();
		
		while(iterNodeCreator.hasNext()) {
			Node temp = (Node)iterNodeCreator.next();
			h.addNode(new Node(temp.getKey() , temp.getWeight(), temp.getInfo() ,temp.getLocation()));
		}
		
		//create the new edges of the new graph and insert them to the graph 
			
		for(node_data node : h.getV()) {				//move on all the nodes
		Iterator<edge_data> iterEdge = this.getGraph().getE(node.getKey()).iterator();	//take all the nodes that start from this integer			
			while(iterEdge.hasNext()) {
				edge_data edge = iterEdge.next();
				h.connect(edge.getSrc(), edge.getDest(), edge.getWeight());
		}
		}
		
		return h;
	}
	
	/**
	 * this method implement dijkstra algorithm for calculate the minimum path to move from one node to another
	 * @param src
	 * @param dest
	 * @return map with all the path from the src to dest if have path like that
	 */
	private HashMap<Integer, node_data> dijkstra(int src, int dest) {
		
		if(this.g== null)		//check if the graph is null
			return null;
		
		if(this.g.getNode(src) == null || this.g.getNode(dest)==null)		//check if the src exist
			return null;
		
		for(node_data paintNode : this.g.getV()) {		//sign all the nodes as white
			paintNode.setInfo(Colors.WHITE.toString());
			paintNode.setWeight(-1);   			//initial all the nodes to -1
		}
		
		PriorityQueue<node_data> minPriotiy = new PriorityQueue<node_data>();		//create minimum priority queue		
		HashMap<Integer, node_data> parents = new HashMap<Integer, node_data>();	//map to find the path from src to dest

		this.g.getNode(src).setWeight(0);
		this.g.getNode(src).setInfo(Colors.BLACK.toString());
		minPriotiy.add(this.g.getNode(src));		//add the src to the priority		
		
		while(!minPriotiy.isEmpty()) {
				
			node_data temp = minPriotiy.poll();  //pull the next check neighbors nodes
			
			for(edge_data neighborsEdges : this.g.getE(temp.getKey())) {		//move on all the neighbors
					node_data neighbor = this.g.getNode(neighborsEdges.getDest());
				
				if(neighbor.getInfo().equals(Colors.WHITE.toString())) {		//no one visit there before
					
					neighbor.setWeight(temp.getWeight() + neighborsEdges.getWeight());	
					neighbor.setInfo(Colors.GREY.toString());
					parents.put(neighbor.getKey(), temp);
					minPriotiy.add(neighbor);
				}
				
				else if(neighbor.getInfo().equals(Colors.GREY.toString())) {	//visit there but not in all his neighbors
					
					if((temp.getWeight() + neighborsEdges.getWeight()) < neighbor.getWeight()) {		//check if have another option
						
							neighbor.setWeight(temp.getWeight() + neighborsEdges.getWeight());
							parents.put(neighbor.getKey(), temp);
					}
				}
				
			}
			temp.setInfo(Colors.BLACK.toString());		//finished to move on all the neighbors of this node
		}
		
		return parents;
	}
	
	
	private void dfs(node_data node , directed_weighted_graph graph) {
		
		node.setInfo(Colors.GREY.toString());		//mark the current node as grey, and add one to its tag
		node.setTag(node.getTag() + 1);
		
		//for each of the current node neighbors, if it is not yet visited, visit it
		for(edge_data e : graph.getE(node.getKey())) {					//go though the neighbors
			node_data neighbor = graph.getNode(e.getDest());				
			if(neighbor.getInfo().equals(Colors.WHITE.toString())) {	//if not visited
				dfs(neighbor , graph);									//visit
			}	
		}
		node.setInfo(Colors.BLACK.toString());		//mark the current node as black (done)
	}
	
	/**
	 * @return true iff the graph is strongly connected
	 */
	@Override
	public boolean isConnected() {
		
		if(this.g == null) {return false;} //check if it's null graph 
		if(this.g.nodeSize() == 0) {return true;} // if the graph is empty, return true
		
		//initiate all the nodes
		for(node_data n : this.g.getV()) {
			n.setTag(0);
			n.setInfo(Colors.WHITE.toString());
		}
		
		//pick a random node in the graph
		Node randomNode = (Node)this.g.getV().iterator().next();
		
		//call dfs 
		dfs(randomNode , this.g);
		
		//reverse the graph
		directed_weighted_graph new_graph = new DWGraph_DS();
		for(node_data n : this.g.getV()) {					//add all the nodes
			node_data new_node = new Node(n.getKey());
			new_node.setTag(0);
			new_node.setInfo(Colors.WHITE.toString());
			new_graph.addNode(new_node);
		}
		for(node_data n : this.g.getV()) {					//connect all edges reversed
			for(edge_data e : this.g.getE(n.getKey())) {
				new_graph.connect(e.getDest(), e.getSrc(), e.getWeight());
			}
		}
		
		//call dfs on new_garph
		dfs(new_graph.getNode(randomNode.getKey()) , new_graph);
		
		
		//check if all nodes are connected to the random node in both directions
		for(node_data n : this.g.getV()) {
			if(n.getTag() != 1 || new_graph.getNode(n.getKey()).getTag() != 1) {return false;}
		}
		
		return true;
	}
	
	/**
	 * @return the minimum path from node to node
	 * if havn't path return -1
	 */
	@Override
	public double shortestPathDist(int src, int dest) {
		
		if(dijkstra(src , dest) == null)
			return -1;
		
		return this.g.getNode(dest).getWeight();
	}
	
	/**
	 * @return the shortest path from src to dest
	 * if hasn't path it's return null
	 */
	@Override
	public List<node_data> shortestPath(int src, int dest) {
		HashMap<Integer, node_data> map = dijkstra(src , dest);
		
		if(map == null)				//check if map is null (e.g. src not exist or doesn't isn't exist) 
			return null;
		LinkedList<node_data> path = new LinkedList<node_data>();
		
		if(this.g.getNode(dest).getInfo().equals(Colors.WHITE.toString()))		//if the color is white so the node can't reachable from src
			return null;
		
		node_data temp = this.g.getNode(dest);
		
		while(temp != this.g.getNode(src)) {
			
			path.addFirst(temp);							//go from the dest to the src
			temp = map.get(temp.getKey());
		}
		
		path.addFirst(this.g.getNode(src));			//insert the src to the list
		return path;
	}
	
	/**
	 * save the input of the current graph in json file
	 */
	@Override
	public boolean save(String file) {
		
		if(this.g == null)			//check if it's null graph
			return false; 
		
		/*
		 * //if empty graph if(this.g.nodeSize() == 0) { try {
		 * 
		 * PrintWriter pw = new PrintWriter(new File(file));
		 * pw.write("{\"Nods\":[],\"Edges\":[]}"); pw.close();
		 * 
		 * } catch(Exception e) { return false; //e.printStackTrace(); } return true; }
		 */
		
		
		
		
		StringBuilder jb = new StringBuilder("{\"Edges\":[");
		
		//add all the edges
		for(node_data node : this.g.getV()) {
			for(edge_data edge : this.g.getE(node.getKey())) {
				jb.append("{\"src\":"); jb.append(edge.getSrc()); 
				jb.append(",\"w\":"); jb.append(edge.getWeight());
				jb.append(",\"dest\":"); jb.append(edge.getDest()); jb.append("},");
			}
		}
		
		if(this.g.edgeSize() != 0)
			jb.deleteCharAt(jb.length() -1); //remove the last comma
		
		//add all the nodes
		jb.append("],\"Nodes\":[");
		for(node_data node : this.g.getV()) {
			jb.append("{\"pos\":\""); jb.append(node.getLocation().toString()); jb.append("\"");
			jb.append(",\"id\":"); jb.append(node.getKey()); jb.append("},");
		}
		
		if(this.g.nodeSize() != 0)
			jb.deleteCharAt(jb.length() -1); //remove the last comma
		
		jb.append("]}");
		
		
		
		//write json to file
		
		try {
		
			PrintWriter pw = new PrintWriter(new File(file));
			pw.write(jb.toString());
			pw.close();
		
		}
		catch(Exception e) {
			return false;
			//e.printStackTrace();
		}
		
		return true;
	}
	
/**
 * this method use for read graph from json
 * 
 */
	@Override
	public DWGraph_DS deserialize(JsonElement json, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {				
		
		DWGraph_DS graph = new  DWGraph_DS();		//create new graph to put json graph in
		
		JsonObject jsonObject = json.getAsJsonObject();
		
		JsonArray NodeArray = jsonObject.get("Nodes").getAsJsonArray();	//get the list of nodes		
		JsonArray EdgeArray = jsonObject.get("Edges").getAsJsonArray(); //get the list of edges
	
		//add all the nodes
		for(JsonElement n : NodeArray) {	
			
			JsonObject no = n.getAsJsonObject();
			
			//create the node
			node_data node = new Node(no.get("id").getAsInt() , 0 , "" , new Point3D(no.get("pos").getAsString()));
			
			graph.addNode(node);	//add the current node to the graph	
		}
		
		//add all the edges
		for(JsonElement e : EdgeArray) {
			
			JsonObject eo = e.getAsJsonObject();
			
			//connect the given edge
			graph.connect(eo.get("src").getAsInt(), eo.get("dest").getAsInt(), eo.get("w").getAsDouble());
		}
		
		return graph;
	}
	
	/**
	 * load the current class with graph from Json
	 */
	@Override
	public boolean load(String file) {
	
		GsonBuilder builder = new GsonBuilder();		//create jason object
		builder.registerTypeAdapter(DWGraph_DS.class, new DWGraph_Algo());		//adapt between the object class to the way to read json that create from this class
		Gson gson = builder.create();
		
		try {
			 
			// read from json
			
			FileReader read = new FileReader(file);
			this.g = gson.fromJson(read,DWGraph_DS.class);		//update the current graph as json graph
			
			if(this.g == null) //if it's empty file return empty graph
				this.g = new DWGraph_DS();
			
		} catch (FileNotFoundException e) {
			
			//e.printStackTrace();
			return false;		//didn't succeed to read from file
		
		}
		
		
		return true;
	}

	// enum used by algorithms
	private enum Colors {BLACK , WHITE , GREY;}
	
	
	
		public static void main(String [] args) {
			
			DWGraph_DS graph = new DWGraph_DS();
			
			 node_data a = new Node(1 ,1.3);
			 node_data b = new Node(2 ,1.5);
			 node_data c = new Node(3 ,1.6);
			 node_data d = new Node(4 ,1.7);
			 node_data e = new Node(5 ,1.7);
			 
			 graph.addNode(a);
			 graph.addNode(b);
			 graph.addNode(c);
			 graph.addNode(d);
			 
			 graph.connect(4, 1, 3);
			 graph.connect(4, 3, 5);
			 graph.connect(3, 2, 1);
			 graph.connect(2, 3, 1);
			 graph.connect(1, 2, 1);
			 graph.connect(2, 5, 22);
			 graph.connect(3, 5, 2);
			 
			 //graph.connect(3, 4, 2.7);
			 
		//	 System.out.println(graph);
			 
			DWGraph_Algo alg = new DWGraph_Algo();
			alg.init(graph);
			
			//System.out.println(alg.shortestPathDist(1, 2));
			//System.out.println(alg.shortestPath(1, 2));
//			System.out.println(alg.isConnected());
			//alg.save("graph.txt");
			//System.out.println(alg.load("empty.txt"));
			//System.out.println(alg.getGraph());
//			System.out.println(alg.save("empty.txt"));
//			System.out.println(alg.shortestPathDist(4,1));
			
//			LinkedList<node_data> list = new LinkedList<node_data>(graph.getV());
			
//			System.out.println(list);
//			System.out.println(graph.getV());
			
			game_service game = Game_Server_Ex2.getServer(3); // you have [0,23] games
			DWGraph_Algo algor = new DWGraph_Algo();
			System.out.println(algor.load(game.getGraph()));
			System.out.println(game.getGraph());
		}
	
}
