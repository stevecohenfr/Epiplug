package net.epitech.commands;

import net.epitech.vose.EpiPlug;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Pwhere implements CommandExecutor {
	private EpiPlug plugin;
	
	public Pwhere(EpiPlug epiPlug) {
		this.plugin = epiPlug;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		CommandSender adm = sender;
		if (!adm.hasPermission("epiplug.where")) {
			adm.sendMessage(ChatColor.RED + "Vous n'avez pas l'autorisation d'utiliser cette commande !");
			return true;
		}
		if (args.length != 1)
			return false;
		
		Player info = plugin.getServer().getPlayer(args[0]);
		if (info == null) {
			adm.sendMessage(ChatColor.RED + "Joueur non trouvé !");
			return true;
		}
		
		adm.sendMessage(ChatColor.BLUE + "Le joueur " + ChatColor.GREEN + args[0] + ChatColor.BLUE + " est dans le monde " + ChatColor.GREEN + info.getWorld().getName() + ChatColor.BLUE + ".");
		return true;
	}
}