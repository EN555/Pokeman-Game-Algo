package gameClient;

import api.edge_data;

import org.json.JSONException;
import org.json.JSONObject;

import api.Point3D;
public class CL_Pokemon{
	
	private double value;	
	private int type;		// 1/-1 , 1 = if the pokemon on (3,2) edge, 1- represent that he stand on the edge from the low place to the high 
	private Point3D pos;	//the location of the pokemon
	private edge_data edge;	//edge that the pokemon founded there
	
	public CL_Pokemon(Point3D position , double value , int type , edge_data edge) {
		this.value = value;
		this.type= type;
		this.pos = position;
		this.edge =edge;
				
	}

	//setters and getters
	
	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Point3D getPos() {
		return pos;
	}

	public void setPos(Point3D pos) {
		this.pos = pos;
	}

	public edge_data getEdge() {
		return edge;
	}

	public void setEdge(edge_data edge) {
		this.edge = edge;
	}
	
	/**
	 * generate a pokemon from a json string
	 * @param json
	 * @return
	 */
	public static CL_Pokemon generate_from_json(String json) {
		try {
			JSONObject data = new JSONObject(json);
			
			//get all the fields as their types
			double value = data.getDouble("calue");
			int type = data.getInt("type");
			Point3D pos = new Point3D(data.getString("pos"));
			
			//return the pokemon object
			return new CL_Pokemon(pos , value , type , null);
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
}