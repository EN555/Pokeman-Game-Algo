package gameClient;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Server.Game_Server_Ex2;
import api.DWGraph_Algo;
import api.DWGraph_DS;
import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import api.edge_data;
import api.game_service;
import api.node_data;


public class Ex2_Client{
	
	private MyFrame frame;
	private Arena arena;
	private int numberOfagents=0;
	private game_service game;
	private double timerRel=1 ;		//chek every move if the agent go to it pokeman and affect on the Threed.sleep time (the goal is not to wate move call)
	private HashMap<Integer, LinkedList<CL_Pokemon>> map ;  //Integer represent node and CL_pokman represent all the pokemons that closet to him 

	/**
	 * @param game at specific level
	 */
	public Ex2_Client(game_service game) {

		this.game = game;
		this.numberOfagents = numberOfagents(game); //get the number of agents
		this.map = new HashMap<Integer, LinkedList<CL_Pokemon>>();		//Integer - node id , linked list represent all the pokeman at ascending distance order
		updateMap();	//create the hashmap
		initAgents();	//locate all the agent at specific places
		this.arena = new Arena(this.game.getGraph() , this.game.getPokemons() , this.game.getAgents());	//update the arena field

		//get level
		
		MyFrame frame = new MyFrame("Level " + getLevel(game), this.arena);
		frame.setVisible(true);
		
		//start the game
		
		game.startGame();
		while(game.isRunning()) {	//stop the game when the game will finish
			
			updateMap();		//every move need to update the map according to the new pokemans
			moveAgents();		//move the agents according to the map
			this.arena.setAgents(this.game.getAgents(), this.game.getPokemons());
			try {
				frame.repaint();
				Thread.sleep((int)(200/this.timerRel));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public static void main(String []args) {
	
		//for (1 -> 23) move on all the games
		
		game_service game = Game_Server_Ex2.getServer(11);
		Ex2_Client client = new Ex2_Client(game);
		String res = game.toString();
		System.out.println(res);
	
	}
	
	/**
	 * this function take the graph with all the current pokemons and calculate the closet pokemns to every node
	 */
	public void updateMap() {
		
		//get the nodes
		Arena arenaHelp = new Arena(this.game.getGraph() , this.game.getPokemons());
		LinkedList<node_data> nodes = new LinkedList<node_data>(arenaHelp.getGraph().getV());
		ListIterator<node_data> iterNodes = nodes.listIterator();
		
		//get the pokemons
		LinkedList<CL_Pokemon> pokemons = new LinkedList<CL_Pokemon>(arenaHelp.getPokemons());
		ListIterator<CL_Pokemon> iterPok = pokemons.listIterator();
		
		//get all the function on this graph
		DWGraph_Algo algo = new DWGraph_Algo();
		algo.init(arenaHelp.getGraph());
		
		while(iterNodes.hasNext()) {		//move on all the nodes
			node_data node = iterNodes.next();
			LinkedList<CL_Pokemon> listPok = new LinkedList<CL_Pokemon>();
			HashMap<Double, CL_Pokemon> helper = new HashMap<Double ,  CL_Pokemon>(); // double represent the distance from the current node
			
			//create linked list for all the pokemons in the current node 
			
			while(iterPok.hasNext()) {
				CL_Pokemon pok = iterPok.next();
				int nodePok = pok.getEdge().getDest();	//the way to reach the pokeman				
				double dis = algo.shortestPathDist(node.getKey(), nodePok); 
				
				if(dis != -1) {		//check if have path like that 
					helper.put(dis , pok);		//if two have same distance it'snt change because that he will reach to this destination
				}				
			}
			
			Object arr [] = new Object[helper.size()];
			arr = helper.keySet().toArray();			
			Arrays.sort(arr);
			
			for(int i= 0 ; i < arr.length ; i++) {
				listPok.add(helper.get((double)arr[i]));
			}
			
			this.map.put(node.getKey() , listPok);
			iterPok = pokemons.listIterator();
		}
		
	}
	
		
	/**
	 * @param game game with specific level
	 * @return the number of agents in this level
	 */
	public int numberOfagents(game_service game) {
		int num=0;
		try {
			JSONObject json = new JSONObject(game.toString());			//create json object from json string
			JSONObject gameParameters = json.getJSONObject("GameServer");
			num = gameParameters.getInt("agents");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return num;	
	}
	
	public int getLevel(game_service game) {
		int level = 0;
		try {
			JSONObject json = new JSONObject(game.toString());			//create json object from json string
			JSONObject gameParameters = json.getJSONObject("GameServer");
			level = gameParameters.getInt("game_level");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return level;
	}
	
	/**
	 * locate all the agent at simple strategic locations
	 */
	public void initAgents(){		//simple implementation
		
		Arena arenaHelp = new Arena(this.game.getGraph() , this.game.getPokemons());
		LinkedList<node_data> list = new LinkedList<node_data>(arenaHelp.getGraph().getV());
		ListIterator<node_data> iterList = list.listIterator();		
		
		for(int i=0 ; i <this.numberOfagents ; i++) {
			if(iterList.hasNext()) {
				this.game.addAgent(i);		//iterList.next().getKey());
			}
			else {
				
				iterList = list.listIterator();	
				i--;
			}
		}
		
	}
	
	public void moveAgents() {
		
		int counter = 0 ; //counter the number of agents that need to it in this move
		
		LinkedList<CL_Agent> agents = new LinkedList<CL_Agent>(this.arena.getAgents());
		ListIterator<CL_Agent> iterAgents = agents.listIterator();
		DWGraph_Algo algo = new DWGraph_Algo();
		algo.init(this.arena.getGraph());
		
			while(iterAgents.hasNext()) {		//move on all the agents and locate them
				CL_Agent agent = iterAgents.next();
				int src = agent.getSrc();				
				int pokSrc = this.map.get(src).getFirst().getEdge().getDest();			
				if(src != pokSrc) {		//look for the shortest path to the closet pokeman
					ListIterator<node_data> iterAg = (ListIterator<node_data>)algo.shortestPath(src, pokSrc).listIterator();
					iterAg.next();		//the first is the vertex himself and neet to remove him
					node_data dest= iterAg.next();
					this.game.chooseNextEdge(agent.getId() ,dest.getKey());			// <---*----(*)	, (*) - agent, * - pokeman
					System.out.println("Agent: "+agent.getId()+", val: " +agent.getValue()+"   turned to node: "+dest.getKey());
					
					//check if this agent and pokeman founded on the same edge
					if(dest.getKey() - this.map.get(src).getFirst().getEdge().getDest() == 0)	// look like that -1-<---*---(*)-0-
							counter++;
				}
				else {
					ListIterator<node_data> iterAg = (ListIterator<node_data>)algo.shortestPath(src, this.map.get(src).getFirst().getEdge().getSrc()).listIterator();
					iterAg.next();		//the first is the vertex himself and neet to remove him
					node_data dest= iterAg.next();
					this.game.chooseNextEdge(agent.getId(), dest.getKey());	// ---*--->(*) , (*) - agent, * - pokeman
					System.out.println("Agent: "+agent.getId()+", val: "+agent.getValue()+"   turned to node: "+pokSrc);
				}
				
			}
			
			if(counter == 0)	//check if no one need to it at this move
				this.timerRel = 0.65;
			this.game.move();
		
	}
	
	
}