package net.epitech.commands;

import net.epitech.vose.EpiPlug;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PaddTime implements CommandExecutor {

	private EpiPlug plugin;
	//private int time = 0;

	public PaddTime(EpiPlug epiplug) {
		plugin = epiplug;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!sender.hasPermission("epiplug.addtime")) {
			sender.sendMessage(ChatColor.RED + "Vous n'avez pas l'autorisation d'utiliser cette commande !");
			return true;
		}
		if (args.length != 2)
			return false;

		Player target = plugin.getServer().getPlayer(args[0]);
		if (target == null) {
			sender.sendMessage(ChatColor.RED + "Joueur non trouvé !");
			return true;
		}
		
		//if (args[1].matches("(-[1-9][0-9]*)|([1-9][0-9]*)"))
		//	if (args[1].length() <= 4)
		//		time = Integer.parseInt(args[1]);
		//else {
		//	sender.sendMessage(ChatColor.RED + "Seul les nombres et le signe '-' sont autorisés !");
		//	return true;			
		//}
		//plugin.getAccounts().get(target.getName()).getSecond().setRemainingTime(time * 60);
		//int timeLeft = plugin.getAccounts().get(target.getName()).getSecond().getRemainingTime();
		//sender.sendMessage(ChatColor.BLUE + "Le joueur " + args[0] + " a maintenant " + ChatColor.GREEN + (timeLeft / 60) + " minutes (" + timeLeft + " sec)" + ChatColor.BLUE + " avant de quitter.");
		//target.sendMessage(ChatColor.BLUE + "Vous avez maintenant " + ChatColor.GREEN + (timeLeft / 60) + " minutes" + ChatColor.BLUE + " avant de quitter.");
		return true;
	}
}
