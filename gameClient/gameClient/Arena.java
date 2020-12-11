package gameClient;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import api.DWGraph_Algo;
import api.DWGraph_DS;

public class Arena {
	private directed_weighted_graph graph;
	private List<CL_Agent> agents;
	private List<CL_Pokemon> pokemons;

	// ***** constructors *****

	public Arena() {
	}

	public Arena(String graphJson, String pokemonJson, String agentJson) {
		read_graph(graphJson);
		read_pokemons(pokemonJson);
		read_agents(agentJson);
	}

	public Arena(String graphJson, String pokemonJson) {
		read_graph(graphJson);
		read_pokemons(pokemonJson);
	}

	// ***** methods *****

	// getters
	public directed_weighted_graph getGraph() {
		return graph;
	}

	public List<CL_Agent> getAgents() {
		return agents;
	}

	public List<CL_Pokemon> getPokemons() {
		return pokemons;
	}

	/**
	 * set the graph from a json string
	 * 
	 * @param json
	 */
	public void read_graph(String json) {
		GsonBuilder builder = new GsonBuilder(); // create json object
		builder.registerTypeAdapter(DWGraph_DS.class, new DWGraph_Algo()); // adapt between the object class to the way
																			// to read json that create from this class
		Gson gson = builder.create();
		this.graph = gson.fromJson(json, DWGraph_DS.class);
	}

	/**
	 * read the pokemons list from json String
	 * 
	 * @param json
	 */
	public void read_pokemons(String json) {
		List<CL_Pokemon> pokemon_list = new LinkedList<CL_Pokemon>();

		try {
			JSONObject data = new JSONObject(json);
			JSONArray pokemons = data.getJSONArray("Pokemons");

			// read all the pokemons
			for (int i = 0; i < pokemons.length(); i++) {
				String pok = pokemons.getJSONObject(i).getJSONObject("Pokemon").toString();
				CL_Pokemon pokemon = CL_Pokemon.generate_from_json(pok); // generate the pokemon
				pokemon.setEdge(getPokemonEdge(pokemon)); // set it's edge
				pokemon_list.add(pokemon); // add it to the list
			}

			this.pokemons = pokemon_list; // set the list of pokemons
		}

		catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * get the edge in the graph that the pokemon is on
	 * 
	 * @param pokemon
	 * @return
	 */
	private edge_data getPokemonEdge(CL_Pokemon pokemon) {

		// iterate through all the edges
		for (node_data node : this.graph.getV()) {
			for (edge_data edge : this.graph.getE(node.getKey())) {
				// if found the right edge, return it
				if (isOnEdge(pokemon.getPos(), edge)) {
					edge_data revers_edge = this.graph.getEdge(edge.getDest(), edge.getSrc());
					if (revers_edge == null) // no reversed edge - return
						return edge;
					else { // reversed edge exist
						int min_node = Math.min(edge.getDest(), edge.getSrc());
						int max_node = Math.max(edge.getDest(), edge.getSrc());
						if (pokemon.getType() == 1) // check the type
							return this.graph.getEdge(min_node, max_node);
						else
							return this.graph.getEdge(max_node, min_node);
					}

				}
			}
		}
		return null;
	}

	/**
	 * check if a given point is on a given edge in the graph
	 * 
	 * @param p
	 * @param edge
	 * @return
	 */
	private boolean isOnEdge(geo_location p, edge_data edge) {
		final double EPS = 0.0001;

		// get the src and dest locations
		geo_location srcP = this.graph.getNode(edge.getSrc()).getLocation();
		geo_location destP = this.graph.getNode(edge.getDest()).getLocation();

		// calculate the distance from src to dest
		double dis = srcP.distance(destP);
		// calculate the distance from src to dest, through p
		double dis_p = p.distance(srcP) + p.distance(destP);

		// check if the distances are the same (with tiny error)
		return (Math.abs(dis - dis_p) < EPS);
	}

	/**
	 * @param json string of all the agents update the field agent of the class
	 */
	public void setAgents(String json, String json2) {
		read_agents(json);
		read_pokemons(json2);
	}

	/**
	 * read the agents list from json String
	 * 
	 * @param json
	 */
	public void read_agents(String json) {
		List<CL_Agent> agents_list = new LinkedList<CL_Agent>();

		try {
			JSONObject data = new JSONObject(json);
			JSONArray Agents = data.getJSONArray("Agents");

			// read all the agents
			for (int i = 0; i < Agents.length(); i++) {
				String agen = Agents.getJSONObject(i).getJSONObject("Agent").toString();
				CL_Agent agent = CL_Agent.generate_from_json(agen); // generate the agent
				agents_list.add(agent); // add it to the list
			}

			this.agents = agents_list; // set the list of pokemons
		}

		catch (JSONException e) {
			e.printStackTrace();
		}
	}

}