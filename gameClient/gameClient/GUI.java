package gameClient;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * the start menu for the game 
 * @author nir son
 *
 */
public class GUI extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private volatile int level;
	private volatile String ID;
	private volatile boolean isRunning = true;
	
	//constructor
	public GUI() {
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//set the dimensions of the frame
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize((int) (screenSize.width / 2), (int) (screenSize.height / 2)); // the size of the frame will be half of the screen
		this.setLocation((int) (screenSize.width / 4), (int) (screenSize.height / 4)); // update it to the middle of the screen
		
		//add the panel
		this.add(new panel());
		this.setVisible(true);
	}
	
	//getters and setters
	public int getLevel() {return level;}
	public void setLevel(int level) {this.level = level;}
	public String getID() {return ID;}
	public void setID(String iD) {ID = iD;}
	public boolean isRunning() {return isRunning;}
	public void setRunning(boolean isRunning) {this.isRunning = isRunning;}

	/**
	 * the panel of the start menu
	 * @author nir son
	 *
	 */
	private class panel extends JPanel{
		
		private static final long serialVersionUID = 1L;

		public panel() {
		
		//enter the ID
		JLabel labelID = new JLabel("Enter ID:");
		labelID.setSize(100, 20);
		JTextField ID_enter = new JTextField(9);
		this.add(labelID);
		this.add(ID_enter);
		
		//choose a level
		JComboBox<Integer> level_choose = new JComboBox<Integer>();
		for(int i = 0; i <= 23; i++) {level_choose.addItem(i);}
		this.add(level_choose);
		
		//start game button
		JButton start = new JButton("start game");
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				level = level_choose.getSelectedIndex();
				ID = ID_enter.getText();
				isRunning = false;
			}
		});
		this.add(start);
		}	
	}
	
}

