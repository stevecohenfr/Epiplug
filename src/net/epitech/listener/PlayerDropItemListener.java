package net.epitech.listener;

import net.epitech.vose.EpiPlug;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItemListener implements Listener {
	private EpiPlug plugin;
	
	public PlayerDropItemListener(EpiPlug epiplug) {
		this.plugin = epiplug;
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		String PseudoPrefix = plugin.getUserPrefix();
		Player player = event.getPlayer();
		if (player.getName().startsWith(PseudoPrefix)) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.BLUE + "Il faut respecter l'environnement et ne pas jeter n'importe quoi par terre. Les objets vous seront utiles.");
		}
	}
}