package gameClient;

import Server.Game_Server_Ex2;
import api.game_service;

public class Ex2 {

	public static void main(String[] args) {
		
		game_service game;
		
		if(args.length == 0) {
			GUI gui = new GUI();
			while(gui.isRunning()) {}
			gui.dispose();
			game = Game_Server_Ex2.getServer(gui.getLevel());
			game.login(Long.valueOf(gui.getID()).longValue());
		}
		else {
			game = Game_Server_Ex2.getServer(Integer.parseInt(args[1]));
			game.login(Long.valueOf(args[0]).longValue());
		}
		
		Ex2_Client client = new Ex2_Client(game);
		System.out.println(game.toString());
	}

}
//public static void main(String[] args) {
//	
//	// for (1 -> 23) move on all the games
//
//	game_service game = Game_Server_Ex2.getServer(11);
//	Ex2_Client client = new Ex2_Client(game);
//	String res = game.toString();
//	System.out.println(res);
//	}
//}