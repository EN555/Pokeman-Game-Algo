package gameClient;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.swing.JPanel;

import Server.Game_Server_Ex2;
import api.node_data;
import api.Node;
import api.directed_weighted_graph;
import api.game_service;
import api.geo_location;

public class MyPanel extends JPanel{
	
	private directed_weighted_graph graph;	
	
	private int Xdistance = 0;		//the distance between the edges of the x position 
	private int Ydistance = 0;		//the distance between the edges of the y position 
	private int screen_width = 0;	//the width of the screen
	private int screen_height = 0;	//the height of the screen
	
	public MyPanel() {
		
		//helper to check this graphic interface
		game_service game = Game_Server_Ex2.getServer(3); // you have [0,23] games
		Arena arena = new Arena(game.getGraph() , game.getPokemons() , game.getAgents());
		this.graph = arena.getGraph();
		
		//update the size of the screen
		
//		this.screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
//		this.screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
//		
//		this.setBackground(Color.gray);
//		
//		initDistances();
//		repaint();
		
	}
	
	@Override
	public void paint(Graphics g) {

		System.out.println(this.graph.getV());
		
	}
	
	/**
	 * @param num insert 0 mean the maximum distance in x coordinate
	 * 1 mean the maximum distance in y coordinate
	 * @return the maximum distance 
	 */
	public void initDistances() {
		
		int Xmin = 0;
		int Xmax = 0;
		int Ymin = 0;
		int Ymax = 0;
		
		LinkedList<node_data> nodes = (LinkedList<node_data>) this.graph.getV();
		ListIterator<node_data> iter = nodes.listIterator();
		
		//initial the first locatin
		
		if(nodes.size() != 0)
		{
			node_data firstNode = iter.next();
			Xmin = (int)firstNode.getLocation().x();
			Xmax = (int)firstNode.getLocation().x();
			Ymin = (int)firstNode.getLocation().y();
			Ymax = (int)firstNode.getLocation().y();	
		}
		
		while(iter.hasNext()) {
			node_data node = iter.next();
			geo_location location = node.getLocation();
			
			if((int)location.x() > Xmax) {
				Xmax = (int)location.x();
			}
			
			if((int)location.x()< Xmin) {
				Xmin = (int)location.x();
			}
				
			if((int)location.y()< Ymin) {
				Ymin = (int)location.y();
			}
			
			if((int)location.y()< Ymin) {
				Ymin = (int)location.y();
			}
		}
		
		
		this.Xdistance = Xmax -Xmin;		//update the methodes
		this.Ydistance = Ymax - Ymin;
		
	}
	
	
	public void paintNodes(Graphics g) {
		
		Iterator<node_data> iter = this.graph.getV().iterator();

		
		while(iter.hasNext()) {
			
			node_data loc_node = iter.next();
		
			g.setColor(Color.YELLOW);
		
			int x = (int)loc_node.getLocation().x();
			int y = (int)loc_node.getLocation().y();
		
			g.fillOval(x*(((this.screen_width/2)-10)/this.Xdistance)+10,y*(((this.screen_height/2)-10)/this.Ydistance)+10, 10, 10);
		}
		
	}
	public static void main(String [] args) {
	//	MyPanel a =  new MyPanel();
		game_service game = Game_Server_Ex2.getServer(3); // you have [0,23] games
		
		System.out.println(game.getGraph());
		//System.out.println(a.graph);
	}
	
}
