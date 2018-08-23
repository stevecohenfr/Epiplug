package net.epitech.commands;

import java.util.Map;

import net.epitech.other.DataBase;
import net.epitech.vose.EpiPlug;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Pinfo implements CommandExecutor {
	private EpiPlug plugin;
	
	public Pinfo(EpiPlug epiPlug) {
		this.plugin = epiPlug;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		CommandSender adm = sender;
		if (!adm.hasPermission("epiplug.pinfo")) {
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
		
		Map<String, String> infos = DataBase.getUserInfo(info.getName());
		adm.sendMessage(ChatColor.YELLOW + "Prenom : " + infos.get("first_name"));
		adm.sendMessage(ChatColor.YELLOW + "Nom : " + infos.get("last_name"));
		adm.sendMessage(ChatColor.YELLOW + "Email : " + infos.get("email"));
		adm.sendMessage(ChatColor.YELLOW + "Date de naissance : " + infos.get("birthday"));
		adm.sendMessage(ChatColor.YELLOW + "Ville : " + infos.get("location"));
		adm.sendMessage(ChatColor.YELLOW + "Education : " + infos.get("education"));
		return true;
	}
}
