package net.epitech.listener;

import java.io.IOException;
import java.util.LinkedHashMap;

import net.epitech.other.DataBase;
import net.epitech.other.HttpRequest;
import net.epitech.vose.EpiPlug;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class CandidateComputerListener implements Listener {
	private EpiPlug plugin;

	public CandidateComputerListener(EpiPlug epiplug) {
		this.plugin = epiplug;
	}

	public String candidate(String mail, String id) {
		LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
		parameters.put("login", mail);
		parameters.put("pass", id);
		parameters.put("source", "1");
		try {
			String result = HttpRequest.sendPost("http://candidature.epitech.net/web/adm_add_candidature.php", parameters);
			//String result = HttpRequest.sendPost("http://jpo-virtuelle-epitech.eu/adm/test_post_request.php", parameters);
			if (result.contains("User created"))
				return "OK";
			else if (result.contains("User already exists"))
				return "EXISTS";
			else if (result.contains("Missing infos"))
				return ("KO");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "KO";
	}

	private boolean ConditionsComplete(Player player) {
		PlayerInventory inventory = player.getInventory();
		ItemStack item1 = plugin.getConfig().getItemStack("Application.Keyboard.Object");
		ItemStack item2 = plugin.getConfig().getItemStack("Application.Mouse.Object");
		ItemStack item3 = plugin.getConfig().getItemStack("Application.Ethernet.Object");
		ItemStack item4 = plugin.getConfig().getItemStack("Application.Ram.Object");
		ItemStack item5 = plugin.getConfig().getItemStack("Application.GCard.Object");
		System.out.println(item1.getType() + "\n" + item2.getType() + "\n" + item3.getType() + "\n" + item4.getType() + "\n" + item5.getType() + "\n");
		if (inventory.contains(item1, 1) && inventory.contains(item2, 1) && inventory.contains(item3, 1) && inventory.contains(item4, 1) && inventory.contains(item5, 1))
			return true;
		return false;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(final PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getClickedBlock() == null || event.getClickedBlock().getTypeId() != 531)
			return;
		event.setCancelled(true);
		if (DataBase.getUserRank(player.getName()).equals("Dev"))
			return;
		String res = DataBase.IsComputerSaved(event.getClickedBlock().getLocation());
		if (res.equals("malformed_res")) {
			player.sendMessage(ChatColor.YELLOW + "[INFO] Aucun ordinateur n'est sélectionné pour les candidatures.");
			return;
		}
		if (player.getName().startsWith(plugin.getUserPrefix())) {
			String email = DataBase.getUserInfo(player.getName()).get("email");
			if (ConditionsComplete(player)) {
				if (res.equals("ok")) {
					String id = DataBase.getIdByName(player.getName());
					if (id != null) {
						if (email != null) {
							String result = candidate(email, id);
							String censuredID = "";
							for (int i = 0; i < id.length(); ++i) { if (i < 5) censuredID += id.charAt(i); else censuredID += "*"; }
							if (DataBase.hasAlreadyCandidate(id) == false && result.equals("OK")) {
								// Success
								DataBase.CandidateToEpitech(id);
								DataBase.incrementStat("Candidature");
								player.sendMessage(ChatColor.GREEN + "Votre candidature à bien été enregistré. Rendez-vous sur : \n" + ChatColor.BLUE + ChatColor.UNDERLINE + "http://candidature.epitech.net/connection.php");
								player.sendMessage(ChatColor.GREEN + "Votre login est votre email (" + email + ")\nVotre mot de passe est votre code de connexion (" + censuredID + ")");
								//player.sendMessage(ChatColor.GREEN + "Votre candidature à bien été enregistré. Nous vous tiendrons informé de l'avancement en vous joignant à l'adresse suivante : " + email);
							} else {
								player.sendMessage(ChatColor.GREEN + "Rendez-vous sur : \n" + ChatColor.BLUE + ChatColor.UNDERLINE + "http://candidature.epitech.net/connection.php");
								player.sendMessage(ChatColor.GREEN + "Votre login est votre email (" + email + ")\nVotre mot de passe est votre code de connexion (" + censuredID + ")");
								//player.sendMessage("Votre candidature à déja été enregistré. Nous vous tiendrons informé de l'avancement en vous joignant à l'adresse suivante : " + email);
							}
						}
						else
							player.sendMessage(ChatColor.RED + "Impossible de trouver vos informations. Merci de relancer le jeu et de réessayer.");
					}
					else
						player.sendMessage(ChatColor.RED + "Un problème est survenue lors de l'envoie du formulaire. Veuillez vérifier votre code de connexion.");
				}
			}
			else
				player.sendMessage(ChatColor.BLUE + "Vous n'avez pas remplis les conditions pour déposer une candidature.");
		}
		else
			player.sendMessage(ChatColor.YELLOW + "[INFO] Seulement les personnes visitant l'école sont autorisées à déposer une candidature.");
	}
}