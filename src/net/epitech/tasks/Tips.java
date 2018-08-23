package net.epitech.tasks;

import java.util.List;

import net.epitech.other.DataBase;
import net.epitech.vose.EpiPlug;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Tips extends BukkitRunnable {
private 				EpiPlug plugin;
private List<String> 	tips;

	public Tips (EpiPlug plugin) {
		this.plugin = plugin;
		this.tips = this.plugin.getTips().getStringList("Tips");
	}
	
	@Override
	public void run() {
		int index = (int)(Math.random() * (tips.size() -1));
		for (Player p : this.plugin.getServer().getOnlinePlayers()) {
			if (!DataBase.getUserRank(p.getName()).equals("Dev"))
				p.sendMessage(ChatColor.ITALIC + "" + ChatColor.AQUA + "TIPS: " + tips.get(index));
		}
		//plugin.getLogger().log(Level.INFO, "TIPS: " + tips.get(index));
		tips.remove(index);
		if (tips.isEmpty()) {
			this.plugin.reloadTips();
			this.tips = this.plugin.getTips().getStringList("Tips");
		}
	}
}
