package net.epitech.tasks;

import net.epitech.listener.PlayerPickupItemListener;
import net.epitech.vose.EpiPlug;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoPopRam extends BukkitRunnable {

	private EpiPlug plugin;
	private Location loc;
	private ItemStack item;
	public static boolean needItem = true;

	public AutoPopRam(EpiPlug epiplug) {
		plugin = epiplug;
		loc = new Location(plugin.getServer().getWorld("Montpellier"), 777, 64, 746);
		item = plugin.getConfig().getItemStack("Application.Ram.Object");
	}

	@Override
	public void run() {
		if (AutoPopRam.needItem) {
			Item dropped = plugin.getServer().getWorld("Montpellier").dropItemNaturally(loc, item);
			dropped.setTicksLived(47000);
			AutoPopRam.needItem = false;
			new PlayerPickupItemListener(plugin);
		}
	}
}
