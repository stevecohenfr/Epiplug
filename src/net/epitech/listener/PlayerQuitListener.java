package net.epitech.listener;

import net.epitech.other.DataBase;
import net.epitech.vose.EpiPlug;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

	private String	PseudoPrefix = "Guest";
	private final EpiPlug plugin;
	
	public PlayerQuitListener(EpiPlug plug) {
		this.plugin = plug;
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		if (event.getPlayer().getName().startsWith(PseudoPrefix)) {
			File BaseFolder = new File(Bukkit.getServer().getWorld("Epitech Montpellier").getWorldFolder(), "players");
			File playerFile = new File(BaseFolder, event.getPlayer().getName() + ".dat");
			playerFile.delete(); //Delete player datas
			plugin.getLogger().log(Level.INFO, event.getPlayer().getName() + ".dat deleted!");
			DataBase.resetSpecificUser(event.getPlayer().getName());
			if (plugin.getAccounts().containsKey(event.getPlayer().getName())) {
				plugin.getAccounts().get(event.getPlayer().getName()).cancel();
				plugin.getAccounts().remove(event.getPlayer().getName());
			}
		}
	}
}
