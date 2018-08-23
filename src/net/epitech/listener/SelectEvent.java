package net.epitech.listener;

import net.epitech.other.DataBase;
import net.epitech.vose.EpiPlug;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SelectEvent implements Listener {
	private Player 			player;
	private EpiPlug 		plugin;
	private SelectionType 	selectionType;
	private String			arg;

	public static enum SelectionType {
		Computer,
		Library
	}

	public SelectEvent(EpiPlug epiplug, Player player, SelectionType st, String arg) {
		this.player = player;
		this.plugin = epiplug;
		this.selectionType = st;
		this.arg = arg;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getPlayer() == player) {
			if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getClickedBlock() == null)
				return ;
			if (selectionType == SelectionType.Computer) {
				if (event.getClickedBlock().getTypeId() == 531) {
					event.setCancelled(true);
					Location location = event.getClickedBlock().getLocation();
					String world = location.getWorld().getName();
					double x = location.getX();
					double y = location.getY();
					double z = location.getZ();

					String value = world + "-" + x + "-" + y + "-" + z;
					if (DataBase.saveCandidateComputer(value))
						player.sendMessage(ChatColor.GREEN + "L'ordinateur pour les candidatures à été correctement enregistré.");
					else
						player.sendMessage(ChatColor.RED + "L'ordinateur n'a pas été enregistré correctement.");
					PlayerInteractEvent.getHandlerList().unregister(this);
				}
			} else if (selectionType == SelectionType.Library) {
				if (event.getClickedBlock().getTypeId() == 47) {
					Location location = event.getClickedBlock().getLocation();
					String world = location.getWorld().getName();
					double x = location.getX();
					double y = location.getY();
					double z = location.getZ();

					String value = world + "-" + x + "-" + y + "-" + z;
					if (DataBase.saveLibrary(value, arg)) {
						plugin.getPdfMap().put(value, arg);
						player.sendMessage(ChatColor.GREEN + "La bibliotheque pour les pdf à été correctement enregistrée.");
					} else
						player.sendMessage(ChatColor.RED + "La bibliotheque n'a pas été enregistré correctement.");
					PlayerInteractEvent.getHandlerList().unregister(this);
				}
			}
		}
	}
}
