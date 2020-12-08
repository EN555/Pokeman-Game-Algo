package ex2;

import Server.Game_Server_Ex2;
import api.game_service;

public class test1 {
	public static void main(String[] arg) {
	
		game_service game = Game_Server_Ex2.getServer(2);
		
		game.addAgent(0);
		
		String s = game.getPokemons();
		
		System.out.println(game);
		
		
	}
	
}
