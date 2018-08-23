package net.epitech.listener;

import java.util.logging.Level;

import net.epitech.vose.EpiPlug;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;

public class ItemDespawnListener implements Listener {
	
	private EpiPlug plugin;
	
	public ItemDespawnListener(EpiPlug epiplug) {
		this.plugin = epiplug;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onItemDespawn(ItemDespawnEvent event) {
		String item = event.getEntity().getItemStack().getType().toString();
		String ram = plugin.getConfig().getString("Application.Ram.Name");
		
		if (item.equals(ram)) {
			plugin.getLogger().log(Level.WARNING, "RAM Item despawning!");
			event.setCancelled(true);
		}
	}
}
