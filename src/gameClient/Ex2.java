package gameClient;

import Server.Game_Server_Ex2;
import api.game_service;
import gameClient.Ex2_Client;

/**
 * the main to manage the game
 * @author nir son
 *
 */
public class Ex2 {

	public static void main(String[] args) {
		
		game_service game;
		
		//check if user entered argoumants. if not:
		if(args.length == 0) {
			GUI gui = new GUI();								//pop up the start menue 
			while(gui.isRunning()) {}							//wait for the user to enter the game he wants
			gui.dispose();										//remove the start menue
			game = Game_Server_Ex2.getServer(gui.getLevel());	//set the game
			game.login(Long.valueOf(gui.getID()).longValue());
		}
		else {	//if yes:
			game = Game_Server_Ex2.getServer(Integer.parseInt(args[1]));	//set the game
			game.login(Long.valueOf(args[0]).longValue());
		}
		
		//stert the game
		Ex2_Client client = new Ex2_Client(game);
		client.startGame();
		System.out.println(game.toString());
	}

}