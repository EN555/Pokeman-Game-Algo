package gameClient;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Server.Game_Server_Ex2;
import api.game_service;
import api.node_data;


public class MyFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	private game_service game;

	public MyFrame(String name , Arena arena) {

		super(name);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		this.setSize((int)(screenSize.width/2), (int)(screenSize.height/2));		//the size of the frame will be half of the screen
		this.setLocation((int)(screenSize.width/4), (int)(screenSize.height/4));		//update it to the middle of the screen

		
		MyPanel panel = new MyPanel(this.getWidth(), this.getHeight() , arena);
		this.add(panel);
		//this.add(new clock(this.getHeight(),  this.getWidth()));
	}


	public game_service getGame() {return game;}
	public void setGame(game_service game) {this.game = game;}

//
//	private class clock extends JPanel{
//		private static final long serialVersionUID = 1L;
//		private double time = 0;
//		private int frame_h;
//		private int frame_w;
//		public clock(int h, int w) {this.frame_h = h; this.frame_w = w;}
//		
//		@Override
//		public void paint(Graphics g) {
//			this.time = game.timeToEnd();
//			g.drawString("sconds left for the game:\n"+this.time/1000, 0, 0);//this.frame_w - this.frame_w/10,  this.frame_h - this.frame_h/10);
//		}	
//	}

	public static void main(String [] args) {

		game_service game = Game_Server_Ex2.getServer(12);
		game.addAgent(0);
		Arena arena = new Arena(game.getGraph() , game.getPokemons() , game.getAgents());
		MyFrame g = new MyFrame("level 12" , arena);
		
		//g.repaint();
		
		
		//System.out.println(game.getGraph());
		
//		for(node_data node : arena.getGraph().getV()) {
//			System.out.println(node.getLocation());
//		}
		
		g.setVisible(true);
	}



}