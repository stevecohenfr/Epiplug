package net.epitech.other;

import org.bukkit.entity.Player;

enum State {
	Queue,
	Online,
	Offline,
	Away
}

//private Map<String, Player> admins = new HashMap<String, Player>();

public class JpoAdmin {
	private Player adm;
	private String name;
	private String rank;
	private String city;
	private State state;

	public JpoAdmin(Player player) {
		this.adm = player;
		this.name = player.getName();
		this.rank = DataBase.getUserRank(this.name);
		this.city = DataBase.getUserCity(this.name);
		this.state = State.Online;
	}
	
	public JpoAdmin(String name, String rank, String city) {
		this.adm = null;
		this.name = name;
		this.rank = rank;
		this.city = city;
		this.state = State.Offline;
	}

	public Player getAdm() {
		return adm;
	}

	public String getName() {
		return name;
	}

	public String getRank() {
		return rank;
	}

	public String getCity() {
		return city;
	}

	public State getState() {
		return state;
	}

	public void setState(State newState) {
		this.state = newState;
	}
}
