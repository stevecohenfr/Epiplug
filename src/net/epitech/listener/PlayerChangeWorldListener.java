package net.epitech.listener;

import java.util.List;

import net.epitech.other.DataBase;
import net.epitech.vose.EpiPlug;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerChangeWorldListener implements Listener {
	
	private EpiPlug plugin;
	
	public PlayerChangeWorldListener(EpiPlug epiplug) {
		this.plugin = epiplug;
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
		if (!event.getPlayer().getName().startsWith(plugin.getUserPrefix()))
			return ;
		Player player = event.getPlayer();
		World from = event.getFrom();
		World to = player.getWorld();
		List<String> worlds = plugin.getConfig().getStringList("Queue.Worlds");

		/* Gestion crade tuto */
		if (from.getName().equals("QG") && to.getName().equals("Montpellier")) {
			DataBase.setHasConnectedAtLeastOnce(DataBase.getIdByName(player.getName()), 2);
		}
		
		if (worlds.contains(from.getName())) { //S'il viens d'un monde avec une queue
			if (plugin.getPlayerQueue().get(from).contains(player))
				plugin.getPlayerQueue().get(from).remove(player);
			for (Player p : plugin.getPlayerQueue().get(from))
				p.sendMessage(ChatColor.BLUE + "Vous êtes maintenant à la position " + ChatColor.GREEN + (plugin.getPlayerQueue().get(from).indexOf(p) + 1) + ChatColor.BLUE + " dans la file d'attente.");
		}
		if (worlds.contains(to.getName())) { //Si le monde ou il va a une queue
			plugin.getPlayerQueue().get(to).add(player);
			player.sendMessage(ChatColor.BLUE + "Un représentant de l'école va bientôt vous prendre en charge.");
			player.sendMessage(ChatColor.BLUE + "Vous êtes à la position " + ChatColor.GREEN + (plugin.getPlayerQueue().get(to).indexOf(player) + 1) + ChatColor.BLUE + '.');
		}
	}
}
