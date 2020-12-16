package gameClient;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;

import org.json.JSONException;
import org.json.JSONObject;

import api.DWGraph_Algo;
import api.dw_graph_algorithms;
import api.edge_data;
import api.game_service;
import api.node_data;

public class Ex2_Client {

	private MyFrame frame;
	private Arena arena;
	private int numberOfagents=0;
	private game_service game;
	private double timerRel = 0; // chek every move if the agent go to it pokeman and affect on the Threed.sleep time (the goal is not to wate move call)
	private boolean isStack = false;

	private HashSet<edge_data> to_be_picked = new HashSet<edge_data>();

	/**
	 * @param game at specific level
	 */
	public Ex2_Client(game_service game) {

		this.game = game;
		this.numberOfagents = numberOfagents(game); //get the number of agents
		initAgents();	//locate all the agent at specific places
		this.arena = new Arena(this.game.getGraph() , this.game.getPokemons() , this.game.getAgents());	//update the arena fields
		this.frame = new MyFrame("Level " + getLevel(game), this.arena, this.game);		//make the frame
	}

	public void startGame() {
		
		frame.setVisible(true);	// start the frame for the game
		game.startGame();		// start the game

		//debug
		int counter_if = 0 , counter_else = 0;
		//

		while(game.isRunning()) {	//stop the game when the game will finish
			
			this.arena.setAgents(this.game.getAgents(), this.game.getPokemons());	//update the arena
			moveAgents();															//set the agents targets
			this.timerRel = timeToNextPick() * 1000;								//
			frame.repaint();														//repaint the farme

			try {
				if(this.isStack) {	//if an agent is stack on one edge, sleep less to allow him to pick the pokemon

					//debug
					counter_if++;
					//

					Thread.sleep((int)(this.timerRel));
				}
				else {	//if an agent is not stack on one edge, sleep 1/10 of a second, or time needed for the closest agent to pick his pokemon

					//debug
					counter_else++;
					System.out.println(this.to_be_picked);
					//

					Thread.sleep(Math.max((int)(this.timerRel) , 100));
				}
			} catch (InterruptedException e) {e.printStackTrace();}

			//move the agents
			this.game.move();
		}

		//the game finished - remove the frame
		frame.dispose();
		
		//debug
		System.out.println(counter_if+"    "+ counter_else);
		//

	}


	/**
	 * @param game game with specific level
	 * @return the number of agents in this level
	 */
	public int numberOfagents(game_service game) {
		int num = 0;
		try {
			JSONObject json = new JSONObject(game.toString()); // create json object from json string
			JSONObject gameParameters = json.getJSONObject("GameServer");
			num = gameParameters.getInt("agents");				//get number of agents
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return num;
	}

	/**
	 * @param game game with specific level
	 * @return the number of pokemons at a specific time
	 */
	private int numberOfPokmens(game_service game) {
		int num=0;
		try {
			JSONObject json = new JSONObject(game.toString());			//create json object from json string
			JSONObject gameParameters = json.getJSONObject("GameServer");
			num = gameParameters.getInt("pokemons");					//get number of pokemons
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return num;	
	}

	/**
	 * 
	 * @param game 
	 * @return the level of the game
	 */
	public int getLevel(game_service game) {
		int level = 0;
		try {
			JSONObject json = new JSONObject(game.toString()); // create json object from json string
			JSONObject gameParameters = json.getJSONObject("GameServer");
			level = gameParameters.getInt("game_level");		//get the level
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
	 * @return shortest path through all the pokeman
	 */
	public LinkedList<CL_Pokemon> shortestPathFrom(CL_Pokemon sophPok){

		Arena arena = new Arena(this.game.getGraph() , this.game.getPokemons());
		LinkedList<CL_Pokemon> bestPath = new LinkedList<CL_Pokemon>();
		LinkedList<CL_Pokemon> noOrder = new LinkedList<CL_Pokemon>(arena.getPokemons());	//get all the pokemans 

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

	/**
	 * set the agents destinations , and calculate the sleeping time needed
	 */
	public void moveAgents() {

		this.isStack = false;

		DWGraph_Algo ga = new DWGraph_Algo();
		ga.init(this.arena.getGraph());

		// iterate through the agents
		for (CL_Agent agent : this.arena.getAgents()) {	
			
			//debug
			//System.out.println(agent.pre_edge);
			//
			
			if (agent.getDest() == -1) {	// if the agent has no destination

				if(agent.current_pok != null) {	//if the agent is on his way to a pokemon,
					this.to_be_picked.remove(agent.current_pok.getEdge());	//delete his destination edge from the list, so he can re-pick it if needed
				}
				else {	//if the agent just eaten it's pokemon, 
					this.to_be_picked.remove(agent.pre_edge);	//delete the edge he just finished
				}

				setAgentDest(agent , ga);	//set the agent's destination

				//update the arena
				this.arena.setAgents(this.game.getAgents(), this.game.getPokemons());
				
				//check if the agent is stack 
				this.isStack |= isStack(agent);
			}

		}

	}

	/**
	 * 
	 * @param agent
	 * @return the next pokemon the agent will try to pick. work with greedy algorithm
	 */
	private CL_Pokemon next_pok (CL_Agent agent , dw_graph_algorithms ga) {

		CL_Pokemon min_pok = this.arena.getPokemons().get(0);		//get random pokemon
		double min_dis = ga.shortestPathDist(agent.getSrc(), min_pok.getEdge().getSrc()) + min_pok.getEdge().getWeight();

		// check each pokemon
		for (CL_Pokemon pok : this.arena.getPokemons()) {
			if (!this.to_be_picked.contains(pok.getEdge())) { // make sure no other agent is on it's way to pick this pokemon
				double dis = ga.shortestPathDist(agent.getSrc(), pok.getEdge().getSrc()) + pok.getEdge().getWeight(); // calculate the distance to the pokemon
				if (dis < min_dis && dis >= pok.getEdge().getWeight()) { // if lower then the min, set as the min
					min_pok = pok;
					min_dis = dis;
				}
			}
		}

		return min_pok;		//return the closest pokemon
	}

	/**
	 * set the agent destination pokemon
	 * @param agent
	 */
	private void setAgentDest(CL_Agent agent , dw_graph_algorithms ga){

		CL_Pokemon min_pok = next_pok(agent , ga);	//find the next destination

		// set the new pokemon as the destination for the agent

		agent.current_pok = min_pok;	 // replace the pokemon to be picked

		this.to_be_picked.add(min_pok.getEdge());	//add it to the pick list

		if (agent.getSrc() == min_pok.getEdge().getSrc()) { // if on the pokemon source, set to the destination
			game.chooseNextEdge(agent.getId(),min_pok.getEdge().getDest());
		}
		else { // if not, set to the source
			node_data next = ga.shortestPath(agent.getSrc(), min_pok.getEdge().getSrc()).get(1);
			game.chooseNextEdge(agent.getId(), next.getKey());
		}
	
		//debug
		this.arena.setAgents(game.getAgents(), game.getPokemons());
		System.out.println("setAgentDest : " + agent.getDest());
		//
	}

	/**
	 * check if the agent is stack on one edge , and update the records for previous edges
	 * @param agent
	 * @return
	 */
	private boolean isStack(CL_Agent agent) {

		boolean stack;

		edge_data current_edge = this.arena.getGraph().getEdge(agent.getSrc(), agent.getDest());	//get the agent's edge

		if(agent.pre_pre_edge != null && agent.pre_pre_edge.equals(current_edge)) {stack = true;}	//if the same edge as before , set as stack
		else {stack = false;}

		agent.pre_pre_edge = agent.pre_edge;	//update the previous edges
		agent.pre_edge = current_edge;
		
		return stack;
	}

	/**
	 * return the lowest number of seconds needed for an agent to get to his next destination
	 * @return
	 */
	private double timeToNextPick() {

		double min_time = Double.MAX_VALUE;

		//iterate though the agents
		for(CL_Agent agent : this.arena.getAgents()) {			

			//get some data
			edge_data edge = this.arena.getGraph().getEdge(agent.getSrc(), agent.getDest()); 	//the agent's edge
			double edge_dis = this.arena.getGraph().getNode(edge.getDest()).getLocation().distance(
					this.arena.getGraph().getNode(edge.getSrc()).getLocation());		//the edge distance
			double speed= agent.getSpeed();														//the agent's speed
			double weight = edge.getWeight();													//the agent's edge weight
			double TimeAllPath = (weight/speed);												//the time the agent will take to move the entire edge

			//calculate the relative part of the edge the agent have left
			double RelativePath = this.arena.getGraph().getNode(agent.getDest()).getLocation().distance(agent.getPos()) / edge_dis;

			//find all the pokemons on the agent's edge
			HashSet<CL_Pokemon> pokAtSameEdge = new HashSet<CL_Pokemon>();
			for(CL_Pokemon pok : this.arena.getPokemons()) {				//check each pokemon
				if(pok.getEdge().equals(edge)) {pokAtSameEdge.add(pok);}	//if on the agent's edge, add it to the list
			}

			//check each pokemon on the agen's edge
			for(CL_Pokemon pok : pokAtSameEdge) {
				double dis = pok.getPos().distance(agent.getPos()) / edge_dis; //find the relative distance from the agent
				if(dis < RelativePath) {										//if lower the the current, replace
					RelativePath= dis;
				}
			}

			double TimeStay = TimeAllPath*RelativePath;	//calculate the time needed for the agent to get to the closest destination

			if(TimeStay < min_time)		//if lower then the min , replace
				min_time = TimeStay;

		}
		return min_time;	//return the shortest time needed for an agent to get to his next destination
	}
}