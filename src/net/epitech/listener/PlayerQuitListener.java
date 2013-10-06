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
			DataBase.resetSpecificUser(event.getPlayer().getName());
			if (plugin.getAccounts().containsKey(event.getPlayer().getName())) {
				plugin.getAccounts().get(event.getPlayer().getName()).cancel();
				plugin.getAccounts().remove(event.getPlayer().getName());
			}
		}
	}
}
