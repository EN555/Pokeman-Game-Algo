package ex2;

import Server.Game_Server_Ex2;
import api.DWGraph_Algo;
import api.game_service;

public class test1 {
	public static void main(String[] arg) {

		game_service game = Game_Server_Ex2.getServer(20);

		//

		DWGraph_Algo ga = new DWGraph_Algo();

		ga.load("data/A5");

		// System.out.println(ga.isConnected());

//		int[] arr = {1,2,3};
//		
//		int num_of_agents = 3;
//
//		for(int i=0 ; i<num_of_agents ; i++) {
//			game.addAgent(arr[i]);
//		}
//		System.out.println(game.getAgents());
//		
//		game.chooseNextEdge(0, 0);
//		System.out.println(game.getAgents());

//		game.addAgent(0);
//		
//		String s = game.getPokemons();
//		
//		System.out.println(game);

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				break;

			}
			System.out.println(i);
		}

	}

}
