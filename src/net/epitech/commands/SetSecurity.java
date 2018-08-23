package net.epitech.commands;

import net.epitech.vose.EpiPlug;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSecurity implements CommandExecutor {
	private EpiPlug plugin;
	
	public SetSecurity(EpiPlug epiPlug) {
		this.plugin = epiPlug;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if ((sender instanceof Player)){
			((Player)sender).sendMessage(ChatColor.RED + "You can't use this command as a player");
			return true;
		}
		if (args.length < 1 || (!args[0].equalsIgnoreCase("true") && !args[0].equalsIgnoreCase("false")))
			return false;
		EpiPlug.securityEnabled = Boolean.parseBoolean(args[0]);
		System.out.println("Serveur security " + (EpiPlug.securityEnabled ? "enabled" : "disabled") + " !");
		return true;
	}
}
