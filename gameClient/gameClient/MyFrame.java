package gameClient;

import java.awt.Dimension;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Toolkit;

import javax.swing.JFrame;


public class MyFrame extends JFrame{
	
	
	public MyFrame() {
		initFrame();
		initPanel();
	}
	
	public void initPanel() {
		MyPanel myPanel = new MyPanel();
		this.add(myPanel);
	}
	
	
	private void initFrame() 
	{
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		//update the frame according to the screen 
		
	    this.setSize(screenSize.width/2, screenSize.height/2);		//the size of the frame will be half of the screen
	    
	    this.setLocation(screenSize.width/4, screenSize.height/4);		//update it to the middle of the screen
	}	

	
	
	
public static void main(String [] args) {
	
		MyFrame g = new MyFrame();
		g.setVisible(true);
}
	
	
	
}