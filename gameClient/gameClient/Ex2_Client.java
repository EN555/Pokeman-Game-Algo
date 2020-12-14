package gameClient;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import org.json.JSONException;
import org.json.JSONObject;

import api.DWGraph_Algo;
import api.edge_data;
import api.game_service;
import api.node_data;

public class Ex2_Client {

	private MyFrame frame;
	private Arena arena;
	private int numberOfagents=0;
	private int numebrOfPokmens = 0;
	private game_service game;
	private double timerRel = 0; // chek every move if the agent go to it pokeman and affect on the Threed.sleep
									// time (the goal is not to wate move call)
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

		// get level

		this.frame = new MyFrame("Level " + getLevel(game), this.arena, this.game);
		frame.setVisible(true);

		// start the game
		game.startGame();
		while(game.isRunning()) {	//stop the game when the game will finish
			this.arena.setAgents(this.game.getAgents(), this.game.getPokemons());
			moveAgents();		//move the agents according to the map
			
			try {
				frame.repaint();
			
				Thread.sleep((int)(this.timerRel+10));
				this.game.move();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			

		}
		frame.dispose();

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
			JSONObject json = new JSONObject(game.toString()); // create json object from json string
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

	public void moveAgents() {

		//boolean is_agent_eat = false;

		DWGraph_Algo ga = new DWGraph_Algo();
		ga.init(this.arena.getGraph());

		// iterate through the agents
		for (CL_Agent agent : this.arena.getAgents()) {
			

			// if the agent has no destination
			if (agent.getDest() == -1) {
				
				this.to_be_picked.remove(agent.current_pok);
				CL_Pokemon min_pok = this.arena.getPokemons().get(0);
				double min_dis = ga.shortestPathDist(agent.getSrc(), min_pok.getEdge().getSrc()) + min_pok.getEdge().getWeight();

				// check each pokemon
				for (CL_Pokemon pok : this.arena.getPokemons()) {
					if (!this.to_be_picked.contains(pok)) { // make sure no other agent is on it's way to pick this pokemon
						double dis = ga.shortestPathDist(agent.getSrc(), pok.getEdge().getSrc()) + pok.getEdge().getWeight(); // calculate the distance to the pokemon
						if (dis < min_dis && dis != -1) { // if lower then the min, set as the min
							min_pok = pok;
							min_dis = dis;
						}
					}
				}

				// set the new pokemon as the destination for the agent
				agent.current_pok = min_pok;	 // replace the pokemon to be picked
				this.to_be_picked.add(min_pok);
				if (agent.getSrc() == min_pok.getEdge().getSrc()) { // if on the pokemon source, set to the destination
					game.chooseNextEdge(agent.getId(), min_pok.getEdge().getDest());
				} else { // if not, set to the source
					node_data next = ga.shortestPath(agent.getSrc(), min_pok.getEdge().getSrc()).get(1);
					game.chooseNextEdge(agent.getId(), next.getKey());
				}
			
			}
			
		}

//		if(!is_agent_eat){this.timerRel = 1;}	//check if no one need to eat soon
//		else {this.timerRel = 4;}
		
		this.arena.setAgents(this.game.getAgents(), this.game.getPokemons());
		
		double min_time = Double.MAX_VALUE;
			for(CL_Agent agent : this.arena.getAgents()) {			
	
			edge_data edge = ga.getGraph().getEdge(agent.getSrc(), agent.getDest());
			double speed= agent.getSpeed();
			double weight = edge.getWeight();			
			double TimeAllPath = (weight/speed);
			
			//case1: hasn't pokeman on the edge
		
			double RelativePath = ga.getGraph().getNode(agent.getDest()).getLocation().distance(agent.getPos())/ga.getGraph().getNode(agent.getDest()).getLocation().distance(ga.getGraph().getNode(agent.getSrc()).getLocation()); 
			
			//case2: have pokeman on the edge
			
			HashSet<CL_Pokemon> pokAtSameEdge = new HashSet<CL_Pokemon>();	//insert all the pokemans that on the same edge
			for(CL_Pokemon pok : this.arena.getPokemons()) {
				edge_data pokEdge = pok.getEdge();
				if(pokEdge.getDest() == agent.getDest())
							pokAtSameEdge.add(pok);
			}
			
			
			if(!pokAtSameEdge.isEmpty()) {		//check if have pokemon on on the same edge
				Iterator<CL_Pokemon> iter = pokAtSameEdge.iterator();
				while(iter.hasNext()) {
					CL_Pokemon pok = iter.next();
					double dis = pok.getPos().distance(agent.getPos())/ga.getGraph().getNode(agent.getDest()).getLocation().distance(ga.getGraph().getNode(agent.getSrc()).getLocation()); 
					if(dis < RelativePath) {
					RelativePath= dis;
					}
				}
			}
			
			double TimeStay = TimeAllPath*RelativePath;

			if(TimeStay < min_time)
				min_time = TimeStay;
			
			}
	
			this.timerRel = min_time*1000;
	
			}
	}