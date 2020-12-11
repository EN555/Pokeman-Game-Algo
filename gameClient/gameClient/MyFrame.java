package gameClient;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Server.Game_Server_Ex2;
import api.game_service;
import api.node_data;

public class MyFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private game_service game;

	public MyFrame(String name, Arena arena, game_service game) {
		super(name);
		this.game = game;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize((int) (screenSize.width / 2), (int) (screenSize.height / 2)); // the size of the frame will be half
																					// of the screen
		this.setLocation((int) (screenSize.width / 4), (int) (screenSize.height / 4)); // update it to the middle of the
																						// screen

		MyPanel panel = new MyPanel(this.getWidth(), this.getHeight(), arena, game);
		this.add(panel);
	}

	public game_service getGame() {
		return game;
	}

	public void setGame(game_service game) {
		this.game = game;
	}

}