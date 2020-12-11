package gameClient;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.OptionalLong;
import java.util.Map.Entry;
import java.util.Queue;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.internal.builders.NullBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Server.Game_Server_Ex2;
import api.DWGraph_Algo;
import api.DWGraph_DS;
import api.Point3D;
import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import api.edge_data;
import api.game_service;
import api.node_data;
import kotlin.OptIn;


public class Ex2_Client{

	private MyFrame frame;
	private Arena arena;
	private int numberOfagents=0;
	private int numebrOfPokmens = 0;
	private game_service game;
	private double timerRel=1 ;		//chek every move if the agent go to it pokeman and affect on the Threed.sleep time (the goal is not to wate move call)
	private HashSet<CL_Pokemon> to_be_picked = new HashSet<CL_Pokemon>();

	/**
	 * @param game at specific level
	 */
	public Ex2_Client(game_service game) {

		this.game = game;
		this.numberOfagents = numberOfagents(game); //get the number of agents
		initAgents();	//locate all the agent at specific places
		this.arena = new Arena(this.game.getGraph() , this.game.getPokemons() , this.game.getAgents());	//update the arena field
		this.numebrOfPokmens = numberOfPokmens(game);

		//get level

		this.frame = new MyFrame("Level " + getLevel(game), this.arena);
		frame.setVisible(true);

		//start the game

		game.startGame();
		while(game.isRunning()) {	//stop the game when the game will finish
			moveAgents();		//move the agents according to the map
			this.arena.setAgents(this.game.getAgents(), this.game.getPokemons());
			try {
				frame.repaint();
				Thread.sleep((int)(100/this.timerRel));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		this.frame.dispose();	//close the frame
	}

	public static void main(String []args) {

		//for (1 -> 23) move on all the games

		game_service game = Game_Server_Ex2.getServer(11);
		Ex2_Client client = new Ex2_Client(game);
		String res = game.toString();
		System.out.println(res);

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

	/**
	 * @param game game with specific level
	 * @return the number of agents in this level
	 */
	public int numberOfPokmens(game_service game) {
		int num=0;
		try {
			JSONObject json = new JSONObject(game.toString());			//create json object from json string
			JSONObject gameParameters = json.getJSONObject("GameServer");
			num = gameParameters.getInt("pokemons");
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
	 * locate all the agent at strategic locations
	 */

	public void initAgents() {
		
		LinkedList<CL_Pokemon> pokList = shortestPath();
		
		int pokemans = numberOfPokmens(this.game);
		int agents = this.numberOfagents;

		for(int i = 0 ; i < agents ; i++) {
			CL_Pokemon pok = pokList.get((int)((double)pokemans/agents)*(i));
			int num = pok.getEdge().getSrc();
			this.game.addAgent(num);
		
		}	
	}
	
	/**
	 * find the pokman that have the very big potential to start with him
	 * @return
	 */
	public LinkedList<CL_Pokemon> shortestPath() {
		
		Arena arena = new Arena(this.game.getGraph() ,this.game.getPokemons());
		double distanceOutter= Double.MAX_VALUE;		//dis for the outer loop
		double distanceIn= 0;		//dis for the second for (e.g. for all the path for spcific pokeman)	
		DWGraph_Algo alg = new DWGraph_Algo();
		alg.init(arena.getGraph());
		
		//move on all the pokmens in the graph

		LinkedList<CL_Pokemon> list = new LinkedList<CL_Pokemon>();
		LinkedList<CL_Pokemon> optList = new LinkedList<CL_Pokemon>();

		for(int i= 0 ; i < arena.getPokemons().size() ; i++) {
			
			list=  shortestPathFrom(arena.getPokemons().get(i));
			ListIterator<CL_Pokemon> iterList = list.listIterator();
			CL_Pokemon a = iterList.next();			
			while(iterList.hasNext()) {
				CL_Pokemon b = iterList.next();
				distanceIn += alg.shortestPathDist(a.getEdge().getSrc(), b.getEdge().getDest());
				a=b;
			}
				
			if(distanceIn < distanceOutter) {
				distanceOutter = distanceIn;
				optList = list;
		
			}
		}
		
		return optList;
	}
	
	/**
	 * @param sophPok get pokeman
	 * @return shortest path path through all the pokeman
	 */
	public LinkedList<CL_Pokemon> shortestPathFrom(CL_Pokemon sophPok){
		
		Arena arena = new Arena(this.game.getGraph() , this.game.getPokemons());
		LinkedList<CL_Pokemon> bestPath = new LinkedList<CL_Pokemon>();
		LinkedList<CL_Pokemon> noOrder = new LinkedList<CL_Pokemon>(arena.getPokemons());

		noOrder.remove(sophPok);
		
		ListIterator<CL_Pokemon> iterNoOrder = noOrder.listIterator();
		DWGraph_Algo alg = new DWGraph_Algo();
		alg.init(arena.getGraph());
		CL_Pokemon pokInn = sophPok;
		double innerDis= Double.MAX_VALUE;
		bestPath.add(sophPok);
		CL_Pokemon optTemp= pokInn;
		int size = noOrder.size();
		
		for(int i =0  ; i< size ; i++) {
								
			while(iterNoOrder.hasNext()) {
				
				CL_Pokemon tempPok = iterNoOrder.next();
				if(alg.shortestPathDist(pokInn.getEdge().getDest(), tempPok.getEdge().getSrc())< innerDis) {
					innerDis = 	alg.shortestPathDist(pokInn.getEdge().getDest(), tempPok.getEdge().getSrc());
					optTemp = tempPok;
				}
			
			}
			innerDis = Double.MAX_VALUE;
			pokInn = optTemp;
			bestPath.add(pokInn);
			noOrder.remove(pokInn);
			iterNoOrder = noOrder.listIterator();	
		}
	
		return bestPath;
	}

	public void moveAgents() {

		boolean is_agent_eat = false;

		DWGraph_Algo ga = new DWGraph_Algo();
		ga.init(this.arena.getGraph());

		//iterate through the agents
		for(CL_Agent agent : this.arena.getAgents()) {

			//check if the agent is to eat pokemon soon
			if(!is_agent_eat) {
				for(CL_Pokemon pok : this.arena.getPokemons()) {
					if(pok.getEdge().getSrc() == agent.getSrc()) {is_agent_eat = true; break;}
				}
			}

			//if the agent has no destination
			if(agent.getDest() == -1){

					CL_Pokemon min_pok = this.arena.getPokemons().get(0);
					double min_dis = ga.shortestPathDist(agent.getSrc(), min_pok.getEdge().getSrc()) + min_pok.getEdge().getWeight();

					//check each pokemon
					for(CL_Pokemon pok : this.arena.getPokemons()) {
						if(!this.to_be_picked.contains(pok)) {					//make sure no other agent is on it's way to pick this pokemon
							double dis = ga.shortestPathDist(agent.getSrc(), pok.getEdge().getSrc()) + pok.getEdge().getWeight();	//calculate the distance to the pokemon
							if(dis < min_dis && dis != -1) {			//if lower then the min, set as the min
								min_pok = pok;
								min_dis = dis;
							}
						}
					}

					//set the new pokemon as the destination for the agent
					this.to_be_picked.remove(agent.current_pok);	//replace the pokemon to be picked
					agent.current_pok = min_pok;
					this.to_be_picked.add(min_pok);
					if(agent.getSrc() == min_pok.getEdge().getSrc()) {			//if on the pokemon source, set to the destination
						game.chooseNextEdge(agent.getId(), min_pok.getEdge().getDest());
						}
					else {	//if not, set to the source
						node_data next = ga.shortestPath(agent.getSrc(), min_pok.getEdge().getSrc()).get(1);
						game.chooseNextEdge(agent.getId(), next.getKey());
					}	
				}		
					
			}

		if(!is_agent_eat){this.timerRel = 4;}	//check if no one need to eat soon
		else {this.timerRel = 1.5;}
		this.game.move();

		}

}