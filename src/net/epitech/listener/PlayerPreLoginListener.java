package net.epitech.listener;

import net.epitech.other.DataBase;
import net.epitech.tasks.AutoKickTask;
import net.epitech.vose.EpiPlug;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scheduler.BukkitTask;

public class PlayerPreLoginListener implements Listener {
	private final EpiPlug plugin;
	
	public PlayerPreLoginListener(EpiPlug plug) {
		this.plugin = plug;
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerLoginEvent(PlayerLoginEvent event)
	{
		if (!event.getPlayer().getName().startsWith("Guest"))
			return ;
		if (DataBase.readUserList(event.getPlayer().getName()) == 2) {
			event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Erreur lors de la connexion au server. Veuillez réssayer.");
		}else {
			DataBase.modifyUserList(event.getPlayer().getName(), 2);
			AutoKickTask t = new AutoKickTask(plugin, event.getPlayer().getName());
			BukkitTask t2 = plugin.getServer().getScheduler().runTaskTimer(plugin, t, 0, 20);
			plugin.getAccounts().put(event.getPlayer().getName(), t2);
		}
	}
}
