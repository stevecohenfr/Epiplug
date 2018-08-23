package net.epitech.commands;

import net.epitech.vose.EpiPlug;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Pabsent implements CommandExecutor {
	private EpiPlug plugin;
	
	public Pabsent(EpiPlug epiPlug) {
		this.plugin = epiPlug;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "You can't use this command on console.");
			return false;
		}
		Player adm = (Player) sender;
		if (!adm.hasPermission("epiplug.absent")) {
			adm.sendMessage(ChatColor.RED + "Vous n'avez pas l'autorisation d'utiliser cette commande !");
			return true;
		}

		if (plugin.getPrivateCom().containsKey(adm))
			plugin.getPrivateCom().remove(adm); 
		adm.sendMessage(ChatColor.BLUE + "Vous êtes maintenant en mode absent.");
		return true;
	}
}
