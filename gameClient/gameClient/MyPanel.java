package gameClient;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import api.node_data;
import api.directed_weighted_graph;
import api.edge_data;
import api.game_service;

public class MyPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private int frame_width;
	private int frame_height;

	private Arena arena;
	private game_service game;

	private Color back_color = Color.WHITE;
	private Color node_color = Color.BLUE;
	private Color edge_color = Color.BLACK;
	private Color pokemon_color = Color.YELLOW;
	private Color agent_color = Color.RED;

	private double factorX;
	private double factorY;
	private double minX;
	private double minY;
	private int offSetX;
	private int offSetY;
	private int radios;

	public MyPanel(int frame_width, int frame_height, Arena arena, game_service game) {
		this.frame_width = frame_width;
		this.frame_height = frame_height;
		this.arena = arena;
		this.game = game;

		this.radios = (frame_width + frame_height) / 70;
		this.offSetX = frame_width / 10;
		this.offSetY = frame_height / 10;

		this.setBackground(this.back_color);

		calculate_factor();
	}

	/**
	 * calculate the factor to multiply the positions in order the fit the frame
	 */
	private void calculate_factor() {
		double Xmax = 0, Xmin = Integer.MAX_VALUE, Ymax = 0, Ymin = Integer.MAX_VALUE;

		// find min and max X and y
		for (node_data node : this.arena.getGraph().getV()) {
			if (node.getLocation().x() < Xmin) {
				Xmin = node.getLocation().x();
			}
			if (node.getLocation().x() > Xmax) {
				Xmax = node.getLocation().x();
			}
			if (node.getLocation().y() < Ymin) {
				Ymin = node.getLocation().y();
			}
			if (node.getLocation().y() > Ymax) {
				Ymax = node.getLocation().y();
			}
		}

		this.minX = Xmin;
		this.minY = Ymin;
		this.factorX = (this.frame_width - 2 * offSetX) / (Xmax - Xmin);
		this.factorY = (this.frame_height - 2 * offSetY) / (Xmax - Xmin);
	}

	// colors setters and getters
	public Color getBack_color() {
		return back_color;
	}

	public void setBack_color(Color back_color) {
		this.back_color = back_color;
		this.setBackground(this.back_color);
	}

	public Color getNode_color() {
		return node_color;
	}

	public void setNode_color(Color node_color) {
		this.node_color = node_color;
	}

	public Color getEdge_color() {
		return edge_color;
	}

	public void setEdge_color(Color edge_color) {
		this.edge_color = edge_color;
	}

	public Color getPokemon_color() {
		return pokemon_color;
	}

	public void setPokemon_color(Color pokemon_color) {
		this.pokemon_color = pokemon_color;
	}

	public Color getAgent_color() {
		return agent_color;
	}

	public void setAgent_color(Color agent_color) {
		this.agent_color = agent_color;
	}

	@Override
	public void paint(Graphics g) {
		paint_nodes(g);
		paint_edges(g);
		paints_pokemons(g);
		paints_agents(g);
		paint_clock(g);
		paint_agents_score(g);
	}

	/**
	 * paints all the nodes
	 * 
	 * @param g
	 */
	private void paint_nodes(Graphics g) {
		g.setColor(this.node_color);
		for (node_data node : this.arena.getGraph().getV()) {
			g.fillOval((int) ((node.getLocation().x() - this.minX) * this.factorX) + offSetX,
					(int) ((node.getLocation().y() - this.minY) * this.factorY) + offSetY, this.radios, this.radios);
		}
	}

	/**
	 * paints all the edges
	 * 
	 * @param g
	 */
	private void paint_edges(Graphics g) {
		g.setColor(this.edge_color);

		directed_weighted_graph graph = this.arena.getGraph();

		for (node_data node : graph.getV()) {
			for (edge_data edge : graph.getE(node.getKey())) {

				int x1 = (int) (((graph.getNode(edge.getSrc()).getLocation().x() - this.minX) * this.factorX)
						+ (this.radios / 2) + offSetX);
				int y1 = (int) (((graph.getNode(edge.getSrc()).getLocation().y() - this.minY) * this.factorY)
						+ (this.radios / 2) + offSetY);
				int x2 = (int) (((graph.getNode(edge.getDest()).getLocation().x() - this.minX) * this.factorX)
						+ (this.radios / 2) + offSetX);
				int y2 = (int) (((graph.getNode(edge.getDest()).getLocation().y() - this.minY) * this.factorY)
						+ (this.radios / 2) + offSetY);

				g.drawLine(x1, y1, x2, y2);
			}
		}
	}

	/**
	 * paints all the pokemons
	 * 
	 * @param g
	 */
	private void paints_pokemons(Graphics g) {
		g.setColor(this.pokemon_color);

		for (CL_Pokemon pokemon : this.arena.getPokemons()) {
			g.fillOval((int) ((pokemon.getPos().x() - this.minX) * this.factorX) + offSetX,
					(int) ((pokemon.getPos().y() - this.minY) * this.factorY) + offSetY, this.radios, this.radios);
		}
	}

	/**
	 * paints all the agents
	 * 
	 * @param g
	 */
	private void paints_agents(Graphics g) {
		g.setColor(this.agent_color);

		for (CL_Agent agent : this.arena.getAgents()) {
			g.fillOval((int) ((agent.getPos().x() - this.minX) * this.factorX) + offSetX,
					(int) ((agent.getPos().y() - this.minY) * this.factorY) + offSetY, this.radios, this.radios);
		}
	}

	/**
	 * paints the time left for the game
	 * 
	 * @param g
	 */
	private void paint_clock(Graphics g) {
		g.setColor(Color.BLACK);
		g.setFont(new Font("ariel", Font.PLAIN, 15));
		g.drawString("number of sconds left for the game: " + game.timeToEnd() / 1000 + "." + game.timeToEnd() % 1000,
				this.offSetX, (int) (this.frame_height - 2 * this.offSetY));
	}

	private void paint_agents_score(Graphics g) {
		g.setColor(Color.BLACK);
		g.setFont(new Font("ariel", Font.PLAIN, 15));

		StringBuilder sb = new StringBuilder("agents scores: \n");
		for (CL_Agent agent : this.arena.getAgents()) {
			sb.append(" id: ");
			sb.append(agent.getId());
			sb.append(" score: ");
			sb.append(agent.getValue());
			sb.append(" |");
		}

		g.drawString(sb.toString(), this.offSetX, (int) (this.frame_height - 3 * this.offSetY));
	}

}
