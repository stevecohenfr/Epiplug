package net.epitech.commands;


import java.io.IOException;
import java.util.LinkedHashMap;

import net.epitech.other.HttpRequest;
import net.epitech.vose.EpiPlug;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class testCommand implements CommandExecutor {
	private EpiPlug plugin;

	public testCommand(EpiPlug epiPlug) {
		this.plugin = epiPlug;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "You can't use this command on console.");
			return false;
		}
		final Player adm = (Player) sender;
		if (!adm.hasPermission("epiplug.test")) {
			adm.sendMessage(ChatColor.RED + "Vous n'avez pas l'autorisation d'utiliser cette commande !");
			return true;
		}
		//TEST HERE
		LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
		parameters.put("login", args[0]);
		parameters.put("pass", args[1]);
		parameters.put("source", "1");
		
		try {
			HttpRequest.sendPost("http://candidature.epitech.net/web/adm_add_candidature.php", parameters);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//END TEST HERE
		return true;
	}
}
