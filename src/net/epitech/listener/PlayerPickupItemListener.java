package net.epitech.listener;

import net.epitech.other.DataBase;
import net.epitech.tasks.AutoPopRam;
import net.epitech.vose.EpiPlug;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerPickupItemListener implements Listener {
	private EpiPlug plugin;
	
	public PlayerPickupItemListener(EpiPlug epiplug) {
		plugin = epiplug;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		System.out.println("RegisterEventPickup");
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerPickupItem(PlayerPickupItemEvent e) {
		String taken = e.getItem().getItemStack().getType().toString();
		String ram = plugin.getConfig().getString("Application.Ram.Name");
		if (taken.equals(ram)) {
			AutoPopRam.needItem = true;
			if (e.getPlayer().getName().startsWith(plugin.getUserPrefix()))
				DataBase.validateCondition(4, e.getPlayer().getName());
			PlayerPickupItemEvent.getHandlerList().unregister(this);
		}
	}
}
