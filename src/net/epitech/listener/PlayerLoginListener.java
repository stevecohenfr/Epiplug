package net.epitech.listener;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import net.epitech.other.DataBase;
import net.epitech.vose.EpiPlug;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class PlayerLoginListener implements Listener {
	private final EpiPlug plugin;
	private int loop = 0;
	private BukkitTask task = null;

	public PlayerLoginListener(EpiPlug plug) {
		this.plugin = plug;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerLogin(PlayerLoginEvent event) {
		final Player player = event.getPlayer();
		/**
		 * ALL
		 * Check using launcher
		 */
		if (EpiPlug.securityEnabled == false)
			return ;
		if (plugin.getSessions().getSessions().contains(player.getName())) {
			String ip = event.getAddress().getHostAddress();;
			if (event.getAddress().getHostAddress().startsWith("163.5.")) {
				System.out.println("Hmm... New player is connecting from EPITECH :)");
				ip = "163.5.*.*";
			}
			if (plugin.getSessions().isSessionRunnin(player, ip)) {
				plugin.getSessions().removePlayerSession(player);
				plugin.getLogger().log(Level.INFO, player.getName() + " has arrived!");
				// TODO WIP : If Admin : add him in the admins map
				/*
				if (!player.getName().startsWith(plugin.getUserPrefix())) {
					if (plugin.getAdmins().get(player.getName()) != null)
				}
				 */
			} else {
				event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Vous tentez d'usurper l'identitée de " + player.getName() + ".\nLe FBI arrive chez vous !");
				plugin.getLogger().log(Level.INFO, player.getName() + " try to loggin with wrong ip (Connect => " + ip + " | " + plugin.getSessions().getSessions().getString(player.getName()) + " <= Waited");
				return;
			}
		} else {
			event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Votre session a expirée\nou\nVous n'utilisez pas une version valide de notre launcher.\nVeuillez télécharger la dernière version sur:\nhttp://jpo-virtuelle-epitech.eu");
			plugin.getLogger().log(Level.INFO, player.getName() + " try to loggin but is not in waiting list...");
			return;
		}

		/**
		 * GUEST
		 * ID collision
		 */
		if (player.getName().startsWith(plugin.getUserPrefix())) {
			boolean res = DataBase.compareIds(player.getName());
			if (DataBase.readUserList(player.getName()) == 2) {
				event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Erreur lors de la connexion au server. Veuillez réssayer.");
			} else if (res == true)
				DataBase.modifyUserList(player.getName(), 2);
			else if (res == false) {
				DataBase.modifyUserList(player.getName(), 2);
				DataBase.modifyIds(player.getName());
			}
			DataBase.setfb_username(player.getName());

			//AutoKickTask t = new AutoKickTask(plugin, player);
			//BukkitTask t2 = plugin.getServer().getScheduler().runTaskTimer(plugin, t, 0, 20);
			//plugin.getAccounts().put(player.getName(), new Pair<BukkitTask, AutoKickTask>(t2, t));
		}

		/**
		 * GUEST
		 * Player needs tuto ?
		 */

		if (player.getName().startsWith(EpiPlug.userPrefix)) {
			boolean needTuto = DataBase.tutoPlayerDone(DataBase.getIdByName(player.getName()));
			if (needTuto == false) {
				System.out.println(player.getName() + " NEED TUTO ? " + needTuto);
				BukkitRunnable br = new BukkitRunnable() {
					@Override
					public void run() {
						if (loop >= 1) {
							player.teleport(new Location(plugin.getServer().getWorld("QG"), 272, 51, -1154));
							loop = 0;
							if (task != null)
								plugin.getServer().getScheduler().cancelTask(task.getTaskId());
						}
						loop++;
					}
				};
				task = plugin.getServer().getScheduler().runTaskTimer(plugin, br, 0, 20);
			}
		}

		/**
		 * GUEST
		 * Send real time to clients
		 */
		if (player.getName().startsWith(plugin.getUserPrefix())) {
			String infos = "NewPlayer-";
			Map<String, String> map = DataBase.getUserInfo(player.getName());
			//map.put("Remaining", Integer.toString(plugin.getAccounts().get(player.getName()).getSecond().getRemainingTime()));
			map.put("Remaining", "N.C.");
			Set<String> cles = map.keySet();
			Iterator<String> it = cles.iterator();
			while (it.hasNext()){
				String cle = it.next();
				String value = map.get(cle);
				infos += cle + ':' + value;
				if (!(cles.toArray()[cles.toArray().length - 1].toString() == cle))
					infos += "&";
			}
			plugin.getLauncherConnector().sendToAllRealTime(infos);
		}
	}
}