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
	private HashSet<CL_Pokemon> to_be_picked = new HashSet<CL_Pokemon>();

	/**
	 * @param game at specific level
	 */
	public Ex2_Client(game_service game) {

		this.game = game;
		this.numberOfagents = numberOfagents(game); //get the number of agents
		initAgents();	//locate all the agent at specific places
		this.arena = new Arena(this.game.getGraph() , this.game.getPokemons() , this.game.getAgents());	//update the arena field

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

				//check if has queued destinations
				if(!agent.way.isEmpty()) {
					game.chooseNextEdge(agent.getId(), agent.way.poll().getKey());
				}

				//if not, find a new destination
				else {
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
					List<node_data> path = ga.shortestPath(agent.getSrc(), min_pok.getEdge().getSrc());		//calculate the way
					path.add(this.arena.getGraph().getNode(min_pok.getEdge().getDest()));					//add the dest
					path.remove(0);																			//remove the source node
					for(node_data n : path) {agent.way.add(n);}												//add to the agent queue
					game.chooseNextEdge(agent.getId(), agent.way.poll().getKey());							//set the next destination
					this.to_be_picked.add(min_pok);															//mark
				}	
			}
		}

		if(!is_agent_eat){this.timerRel = 0.65;}	//check if no one need to eat soon
		else {this.timerRel = 1;}
		this.game.move();

	}


}