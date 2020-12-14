package gameClient;

import java.util.AbstractMap;

import org.json.JSONException;
import org.json.JSONObject;

import api.Point3D;
import api.geo_location;

public class CL_Agent {

	private final int id;
	private double value;
	private int src;
	private int dest;
	private final double speed;
	private geo_location pos;
	CL_Pokemon current_pok;
	AbstractMap.SimpleEntry<Integer, Integer> pre_edge;
	AbstractMap.SimpleEntry<Integer, Integer> pre_pre_edge;

	// ***** constructors ******

	public CL_Agent(int id, double value, int src, int dest, double speed, geo_location pos) {
		this.id = id;
		this.value = value;
		this.src = src;
		this.dest = dest;
		this.speed = speed;
		this.pos = pos;
	}

	// ***** methods ******

	// getters and setters
	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public int getSrc() {
		return src;
	}

	public void setSrc(int src) {
		this.src = src;
	}

	public int getDest() {
		return dest;
	}

	public void setDest(int dest) {
		this.dest = dest;
	}

	public geo_location getPos() {
		return pos;
	}

	public void setPos(geo_location pos) {
		this.pos = pos;
	}

	public int getId() {
		return id;
	}

	public double getSpeed() {
		return speed;
	}

	/**
	 * generate an Agent from a json string
	 * 
	 * @param json
	 * @return
	 */
	public static CL_Agent generate_from_json(String json) {
		try {
			JSONObject data = new JSONObject(json);

			// get all the fileds as their types
			int id = data.getInt("id"), src = data.getInt("src"), dest = data.getInt("dest");
			double value = data.getDouble("value"), speed = data.getDouble("speed");
			Point3D pos = new Point3D(data.getString("pos"));

			return new CL_Agent(id, value, src, dest, speed, pos); // return the agent object
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

	}

}