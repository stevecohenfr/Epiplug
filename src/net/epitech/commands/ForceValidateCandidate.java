package net.epitech.commands;

import net.epitech.other.DataBase;
import net.epitech.vose.EpiPlug;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ForceValidateCandidate implements CommandExecutor {
	private EpiPlug plugin;

	public ForceValidateCandidate(EpiPlug epiplug) {
		plugin = epiplug;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "You can't use this command on console.");
			return false;
		}

		Player adm = (Player) sender;
		if (!adm.hasPermission("epiplug.forcevalidatecandidate")) {
			sender.sendMessage(ChatColor.RED + "Vous n'avez pas l'autorisation d'utiliser cette commande !");
			return true;
		}
		if (args.length != 1)
			return false;

		Player target = plugin.getServer().getPlayer(args[0]);
		if (target == null) {
			adm.sendMessage(ChatColor.RED + "Joueur non trouvé !");
			return true;
		}

		DataBase.validateAllConditions(args[0]);
		target.sendMessage(ChatColor.BLUE + "Vos " + ChatColor.LIGHT_PURPLE + 3 + ChatColor.BLUE + " conditions ont bien été " + ChatColor.GREEN + "validés" + ChatColor.BLUE + ".");
		adm.sendMessage(ChatColor.GREEN + "Vous avez validé les 3 conditions de " + ChatColor.YELLOW + args[0] + ChatColor.GREEN + ".");
		/* Give all items to candidate */
		ItemStack mouse = plugin.getConfig().getItemStack("Application.Items.Mice");
		ItemStack keyboard = plugin.getConfig().getItemStack("Application.Items.Keyboard");
		ItemStack ethernet = plugin.getConfig().getItemStack("Application.Items.Ethernet");
		target.getInventory().addItem(mouse);
		target.getInventory().addItem(keyboard);
		target.getInventory().addItem(ethernet);
		target.sendMessage(ChatColor.BLUE + "Maintenant grace à la " + ChatColor.LIGHT_PURPLE + "souris" + ChatColor.BLUE + 
				", au " + ChatColor.LIGHT_PURPLE + "clavier " + ChatColor.BLUE + "et au " + ChatColor.LIGHT_PURPLE + "cable ethernet " + ChatColor.BLUE + 
				", vous allez pouvoir déposer une candidature à l'aide de l'ordinateur situé dans le bureau du " + ChatColor.LIGHT_PURPLE + " Directeur de Developpement Regional.");
		return true;
	}
}
