### Ex2 Project 

*I will brief the project every part of the brief will get most explanation in the wiki pages.*

The assignment divide to two parts:

#### The first part:

![image](https://user-images.githubusercontent.com/61500507/102008619-a0329880-3d3a-11eb-9805-56823c361469.png)

This illustration exhibit all the classes and their dependencies.

Tthe first two classes is the **Edge** and **Node**.

The **Node** represent single node in graph, for every node have special key, and all her neighbors.

The **Edge** represent directe weight edge from src location to another destination, the weight must be positive and you can determine him.

The **Point3D** class represent a point in the 3D space, and basic operation between two points and etc.

The **Graph** class complicated from Node and Edge, in the class the node of the graph and the edges keep in two different hashmap. 
You can operate sum operation on teh graph, you can removeNode, you can AddNode , you can conncet between two nodes and put weight for the edge.

To read more on graph operation [Press Here!](https://github.com/EN555/ex2/wiki/Graph)

The **Graph Algo** class composed of graph class, you need to initial the class with graph, and you can operate on her some operation.

You can copy the graph in deep copy, you can find the shortestpath from src to other destination, you can check if the graph is connectively,
you can export Json file of the graph that contain all the edges and the nodes of the graph. you can too read Json file and load the class with him.

To read more on graph operation [Press Here!](https://github.com/EN555/ex2/wiki/Algorithms)

#### The second part:

![image](https://user-images.githubusercontent.com/61500507/102008991-26e87500-3d3d-11eb-8a5f-b58049a2dcef.png)

The second part complicated from eight classes, and the image try to illusrate the dependencied between them.

The first two basic classes is pokeman and teh agent.

The **agent** class represent the player that want to eat the pokemans and for every agent you cand determine src and destination that he move on in the game.

The **Pokemans** class represent the player that the server given to you and you need to eat atmost that you can, for every pokeman have value, 
and the end of the game you wull get the grade of sum of the ate value. 

The **arena** class take responsibility on the read the Json from the server, this class read 3 different things, read the pokemand Json, read the Graph
Json, And read the number f agents.

The **My Panel** class depend on the frame class, this class paint on the panel and not on the frame class, she create all the visualization of the pokemons, agents
and the graph.

To see more visualization you can look here [Press Here!](https://github.com/EN555/ex2/wiki/Game-Proccess)















