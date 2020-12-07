package gameClient;

import java.awt.Point;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;

import api.directed_weighted_graph;
import api.edge_data;
import api.node_data;
import api.Point3D;

public class Arena{
private directed_weighted_graph graph;
private List<CL_Agent> agents;
private List<CL_Pokemon> pokemons;		//create list of pokemon object from Json

public Arena() {
	
	
}

public void generatePokemonFromJson() {
	
	
	
	
}


public void generateagentsFromJson() {
	
	
	
	
}

/**
 * 
 * @param jsonPokemon - get pokemon json string from the server and need to convert it to object of pokemon
 */
	public void generatePokemonFromJson(String jsonPokemon) {	//{"Pokemons":[{"Pokemon":{"value":5.0,"type":-1,"pos":"35.20273974670703,32.10439601193746,0.0"}}]}
	
		try {
			JSONObject allPokemons= new JSONObject(jsonPokemon);	//get json of all the pokemons	
		
			ArrayList<CL_Pokemon> jsonArray = new ArrayList<CL_Pokemon>();	//create array of pokemons to insert them
			
			JSONArray array = allPokemons.getJSONArray("Pokemons");	//every pokemon contain in the array
			
			for(int i= 0; i<array.length() ; i++) {	//move on all the array and extract the parameters of the pokemon
				
				JSONObject pokemon  = array.getJSONObject(i);
				JSONObject pokemonParameters = pokemon.getJSONObject("Pokemon");
			
				//create all the pokemons
			
				int type = pokemonParameters.getInt("type");
				double value = pokemonParameters.getDouble("value");
				String position = pokemonParameters.getString("pos");
						
				//find the edge of the current pokemon according to the positon and type
				
				edge_data edge = getEdgePokemon(new Point3D(position) , type); 
					
				CL_Pokemon pok = new CL_Pokemon(new Point3D(position),value ,type , null);
		}
	
	}
	
	catch (JSONException e) {
		e.printStackTrace();
	}
	
}
	
public edge_data getEdgePokemon(Point3D point , int type) {
	
	edge_data pokemonEdge = null;
	
	Iterator<node_data> nodesIterator = this.graph.getV().iterator(); 
	
	while(nodesIterator.hasNext()) {    //move on all the nodes of the graph and look for the edge that pokemon sit on her
		
		Iterator<edge_data> edgesIterator = this.graph.getE(nodesIterator.next().getKey()).iterator();	//move on all the edges in the graph	
		
		edge_data temp = edgesIterator.next();
		
		if(temp.getSrc() < point.x() && temp.getDest() < point.y()) {	//check if the pokemon sit between them
		
			if(temp.getDest() - temp.getSrc() >0 && type ==1) {  //it the (dest - src) > 0 so the edge in positive slope 
				pokemonEdge = temp;
			}
			if(temp.getDest() - temp.getSrc() < 0 && type == -1) { //it the (dest - src) > 0 so the edge in negative slope
				pokemonEdge = temp;
			}
			
		}
	}
	
	
	return pokemonEdge;
}

}