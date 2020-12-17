package gameClient;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

import api.game_service;
import gameClient.Arena;

/**
 * the main game frame
 * @author nir son
 *
 */
public class MyFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public MyFrame(String name, Arena arena, game_service game) {
		
		super(name);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//set the frame dimensions
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize((int) (screenSize.width / 2), (int) (screenSize.height / 2)); // the size of the frame will be half																		// of the screen
		this.setLocation((int) (screenSize.width / 4), (int) (screenSize.height / 4)); // update it to the middle of the

		//add the panel for all the drawing
		MyPanel panel = new MyPanel(this, arena, game);
		this.add(panel);
		
		//make the frame resizeable
		this.addComponentListener(new ComponentAdapter() {
		    public void componentResized(ComponentEvent componentEvent) {
		        panel.update_data();	//update the dimensions needed for drawing
		        repaint();
		    }
		});
	}

}