package net.epitech.listener;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import net.epitech.other.DataBase;
import net.epitech.vose.EpiPlug;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerQuitListener implements Listener {
	private final EpiPlug plugin;

	public PlayerQuitListener(EpiPlug plug) {
		this.plugin = plug;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerQuit(final PlayerQuitEvent event) {
		String PseudoPrefix = plugin.getUserPrefix();
		Player player = event.getPlayer();
		
		//if Guest
		if (player.getName().startsWith(PseudoPrefix)) {
			
			 //Delete player essentials data
			File essentialsPlayerBase = new File(Bukkit.getPluginManager().getPlugin("Essentials").getDataFolder(), "userdata");
			File essentialsdata = new File(essentialsPlayerBase, player.getName().toLowerCase() + ".yml");
			if (essentialsdata.delete())
				plugin.getLogger().log(Level.INFO, essentialsdata.getPath() + " deleted!");
			else
				plugin.getLogger().log(Level.WARNING, "Unable to delete " + essentialsdata.getPath());
			
			 //Delete player server datas
			plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new BukkitRunnable() {
				@Override
				public void run() {
					final File PlayerBase = new File(plugin.getServer().getWorlds().get(0).getWorldFolder(), "players");
					final File datas = new File(PlayerBase, event.getPlayer().getName() + ".dat");
					try {
						Thread.sleep(3000);
						if (datas.delete())
							plugin.getLogger().log(Level.INFO, datas.getPath() + " deleted!");
						else
							plugin.getLogger().log(Level.WARNING, "Unable to delete " + datas.getPath());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});

			// Delete player from any queue
			List<String> worlds = plugin.getConfig().getStringList("Queue.Worlds");
			World from = player.getWorld();
			if (worlds.contains(from.getName())) {
				if (plugin.getPlayerQueue().get(from).contains(player))
					plugin.getPlayerQueue().get(from).remove(player);
				for (Player p : plugin.getPlayerQueue().get(from))
					p.sendMessage(ChatColor.BLUE + "Vous êtes maintenant à la position " + ChatColor.GREEN + (plugin.getPlayerQueue().get(from).indexOf(p) + 1) + ChatColor.BLUE + " dans la file d'attente.");
			}

			// Delete player from the privateCom Map
			Map<Player, Player> privateCom = plugin.getPrivateCom();
			Set<Player> keys = privateCom.keySet();
			Iterator<Player> i = keys.iterator();
			while (i.hasNext()) {
				Player admin = i.next();
				Player peon = privateCom.get(admin);

				if (admin == player || peon == player) {
					privateCom.remove(admin);
				}
			}

			//Reset Guest infos
			DataBase.resetSpecificUser(player.getName());
			
			//Stop autokick task
			if (plugin.getAccounts().containsKey(player.getName())) {
				//DataBase.sendPlayedTime(DataBase.getIdByName(player.getName()), plugin.getAccounts().get(player.getName()).getSecond().getTimePlayed());
				plugin.getAccounts().get(player.getName()).getFirst().cancel();
				plugin.getAccounts().remove(player.getName());
			}
			
			//Remove player from LibraryCondition Checker
			plugin.getLibraryEvent().getQuest().remove(player);
		}
		
		//Run session
//		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new BukkitRunnable() {
//			@Override
//			public void run() {
//				System.out.println(event.getPlayer().getName() + " quit, waiting for reconnection from " + event.getPlayer().getAddress().getAddress().getHostAddress());
//				plugin.getWillConnect().put(event.getPlayer().getName(), event.getPlayer().getAddress().getAddress().getHostAddress());
//				try {
//					Thread.sleep(plugin.getConfig().getLong("Sessions.Time") * 1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				if (plugin.getWillConnect().containsKey(event.getPlayer().getName())) {
//					plugin.getWillConnect().remove(event.getPlayer().getName());
//					plugin.getServer().getLogger().log(Level.INFO, "La session de " + event.getPlayer().getName() + '(' + event.getPlayer().getAddress().getAddress().getHostAddress() + ") a expiré.");
//				}
//			}
//		});
		plugin.getSessions().savePlayerSession(player);
		plugin.getSessions().runPlayerSession(player);
		/* Dispatch player quit event to all real time clients */
		plugin.getLauncherConnector().sendToAllRealTime("QuitPlayer-" + player.getName() + "-quit");
	}
}
