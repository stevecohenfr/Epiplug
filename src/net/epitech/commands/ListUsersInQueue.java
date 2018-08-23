package net.epitech.commands;

import java.util.ArrayList;

import net.epitech.vose.EpiPlug;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListUsersInQueue implements CommandExecutor {
	private EpiPlug plugin;

	public ListUsersInQueue(EpiPlug epiPlug) {
		this.plugin = epiPlug;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "You can't use this command on console.");
			return false;
		}
		Player adm = (Player) sender;
		if (!adm.hasPermission("epiplug.listusersinqueue")) {
			adm.sendMessage(ChatColor.RED + "Vous n'avez pas l'autorisation d'utiliser cette commande !");
			return true;
		}

		ArrayList<Player> queue = plugin.getPlayerQueue().get(adm.getWorld());
		
		if (queue == null) {
			adm.sendMessage(ChatColor.YELLOW + "Il n'y a pas de queue dans ce monde.");
			return true;
		}
		if (queue.size() > 0) {
			int i = 1;
			adm.sendMessage(ChatColor.YELLOW + "Liste des joueurs dans la queue de " + ChatColor.LIGHT_PURPLE + adm.getWorld().getName() + ChatColor.YELLOW + " :");
			for (Player p : queue) {
				adm.sendMessage(ChatColor.YELLOW + "  - " + i + ". " + p.getName());
				i++;
			}
		}
		else
			adm.sendMessage(ChatColor.YELLOW + "La liste de " + ChatColor.LIGHT_PURPLE + adm.getWorld().getName() + ChatColor.YELLOW + " est vide.");
		return true;
	}
}
