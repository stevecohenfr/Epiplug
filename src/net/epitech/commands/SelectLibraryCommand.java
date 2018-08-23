package net.epitech.commands;


import net.epitech.listener.SelectEvent;
import net.epitech.listener.SelectEvent.SelectionType;
import net.epitech.vose.EpiPlug;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SelectLibraryCommand implements CommandExecutor {
	private EpiPlug plugin;

	public SelectLibraryCommand(EpiPlug epiPlug) {
		this.plugin = epiPlug;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "You can't use this command on console.");
			return false;
		}
		Player adm = (Player) sender;

		if (args.length != 1)
			return false;
		adm.sendMessage(ChatColor.BLUE + "Veuillez sélectionner une bibliotheque.");
		new SelectEvent(plugin, adm, SelectionType.Library, args[0]);
		return true;
	}
}
