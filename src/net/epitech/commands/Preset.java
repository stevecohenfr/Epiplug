package net.epitech.commands;

import net.epitech.other.DataBase;
import net.epitech.vose.EpiPlug;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Preset implements CommandExecutor {

	private EpiPlug plugin;

	public Preset(EpiPlug epiplug) {
		plugin = epiplug;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!sender.hasPermission("epiplug.resetuser")) {
			sender.sendMessage(ChatColor.RED + "Vous n'avez pas l'autorisation d'utiliser cette commande !");
			return true;
		}
		if (args.length != 1)
			return false;

		Player info = plugin.getServer().getPlayer(args[0]);
		if (info == null) {
			sender.sendMessage(ChatColor.RED + "Joueur non trouvé !");
			return true;
		}
		DataBase.resetSpecificUser(args[0]);
		sender.sendMessage(ChatColor.GREEN + "Le joueur " + ChatColor.YELLOW + args[0] + ChatColor.GREEN + " a été reset correctement !");
		return true;
	}
}
