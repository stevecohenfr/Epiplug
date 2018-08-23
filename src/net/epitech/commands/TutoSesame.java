package net.epitech.commands;

import net.epitech.vose.EpiPlug;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TutoSesame implements CommandExecutor {
	private EpiPlug plugin;
	
	public TutoSesame(EpiPlug epiPlug) {
		this.plugin = epiPlug;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			player.teleport(new Location(plugin.getServer().getWorld("QG"), 267, 59, -1124));
		}else {
			sender.sendMessage(ChatColor.RED + "Cette commande n'est utilisable que par les joueurs dans le tutorial");
		}
		return true;
	}
}
