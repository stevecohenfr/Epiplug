package net.epitech.other;

import org.bukkit.entity.Player;

public class MyConversation {
	private Player													player1;
	private Player													player2;
	
	public MyConversation() {
		
	}
	
	public MyConversation (Player player1, Player player2) {
		this.setPlayer1(player1);
		this.setPlayer2(player2);
	}
	
	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}

	public void put(Player player1, Player player2) {
		this.setPlayer1(player1);
		this.setPlayer2(player2);
	}
}
