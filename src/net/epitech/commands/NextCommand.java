package net.epitech.commands;


import java.util.ArrayList;

import net.epitech.vose.EpiPlug;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class NextCommand implements CommandExecutor {
	private EpiPlug plugin;

	public NextCommand(EpiPlug epiPlug) {
		this.plugin = epiPlug;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "You can't use this command on console.");
			return false;
		}
		final Player adm = (Player) sender;
		if (!adm.hasPermission("epiplug.next")) {
			adm.sendMessage(ChatColor.RED + "Vous n'avez pas l'autorisation d'utiliser cette commande !");
			return true;
		}
		
		ArrayList<Player> playersList = plugin.getPlayerQueue().get(adm.getWorld());
		if (playersList == null) {
			adm.sendMessage(ChatColor.BLUE + "Désolé, mais la fonctionnalitée 'file d'attente' n'est pasaccessible dans ce monde");
			return true;
		}
		if (playersList.size() != 0) {
			final Player next = playersList.get(0);
			final Location location = next.getLocation();
			
			if (location == null)
				return true;
			location.setYaw(location.getYaw());
			adm.performCommand("pinfo " + next.getName());
			playersList.remove(0);
			plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new BukkitRunnable() {				
				@Override
				public void run() {
					try {
						adm.sendMessage(ChatColor.BLUE + "Vous allez maintenant être téléporté vers la prochaine personne dans la file d'attente");
						adm.sendMessage(ChatColor.BLUE + "dans 3 sec...");
						Thread.sleep(1000);
						adm.sendMessage(ChatColor.BLUE + "2 sec...");
						Thread.sleep(1000);
						adm.sendMessage(ChatColor.BLUE + "1 sec...");
						Thread.sleep(1000);
						if (next.isOnline()) {
							adm.teleport(location); // exception mystere : synchronized code got accessed from an other thread
							adm.getLocation().setYaw(location.getYaw() + 2);
							adm.getLocation().setPitch(location.getPitch() + 180);
							next.sendMessage(ChatColor.BLUE + "Le membre du staff " + ChatColor.LIGHT_PURPLE + adm.getName() + ChatColor.BLUE + " s'occupe à present de vous.");
							
							plugin.getPrivateCom().put(adm, next);
						} else
							adm.sendMessage(ChatColor.BLUE + "Le joueur vient de partir, vous pouvez refaire /next");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			for (Player p : playersList)
				p.sendMessage(ChatColor.BLUE + "Vous êtes maintenant à la position " + ChatColor.GREEN + (playersList.indexOf(p) + 1) + ChatColor.BLUE + " dans la file d'attente.");
		} else {
			plugin.getPrivateCom().remove(adm);
			adm.sendMessage("La file d'attente est vide.");
		}
		return true;
	}
}
