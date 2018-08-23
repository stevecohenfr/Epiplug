package net.epitech.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import net.epitech.vose.EpiPlug;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class LibraryListener implements Listener {
	private EpiPlug plugin;
	private Map<Player, Integer> quest;
	
	public Map<Player, Integer> getQuest() {
		return quest;
	}

	public LibraryListener(EpiPlug epiplug) {
		this.plugin = epiplug;
		quest = new HashMap<Player, Integer>();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(final PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getClickedBlock() == null || event.getClickedBlock().getTypeId() != 47)
			return;

		Player player = event.getPlayer();
		String location = event.getClickedBlock().getWorld().getName() + "-" + event.getClickedBlock().getLocation().getX() + "-" + event.getClickedBlock().getLocation().getY() + "-" + event.getClickedBlock().getLocation().getZ();		if (plugin.getPdfMap().containsKey(location)) {			if (plugin.getPdfMap().get(location).equals("null") == false) {
				player.sendMessage(ChatColor.GREEN + "Cliquez ici pour la page de documentation : " + ChatColor.BLUE + ChatColor.UNDERLINE + plugin.getPdfMap().get(location));			} else {				player.sendMessage(ChatColor.RED + "Cette bibliotheque n'est pas utilisable maintenant.");				plugin.getLogger().log(Level.WARNING, "URL not define on library : " + location);			}		}		else			plugin.getLogger().log(Level.WARNING, "Not define library block in : " + location);	}
}