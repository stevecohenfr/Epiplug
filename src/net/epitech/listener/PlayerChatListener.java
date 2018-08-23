package net.epitech.listener;

import java.util.Iterator;
import java.util.Map;

import net.epitech.connector.Client;
import net.epitech.vose.EpiPlug;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

	private EpiPlug plugin;
	private Player listenedPlayer;
	private Client client;

	public PlayerChatListener(EpiPlug epiplug) {
		this.listenedPlayer = null;
		this.plugin = epiplug;
		this.client = null;
	}

	public PlayerChatListener(EpiPlug epiplug, Client client) {
		this.listenedPlayer = null;
		this.plugin = epiplug;
		this.client = client;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public PlayerChatListener(EpiPlug epiplug, Client client, Player p) {
		this.listenedPlayer = p;
		this.plugin = epiplug;
		this.client = client;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void PlayerCommand(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		/* Private Chat listener */
		if (client == null) {
			Map<Player, Player> privateCom = plugin.getPrivateCom();

			if (privateCom.size() > 0) {
				Iterator<Player> i = privateCom.keySet().iterator();
				while (i.hasNext()) {
					Player admin = i.next();
					if (admin == player || privateCom.get(admin) == player) {
						event.getRecipients().clear();
						event.getRecipients().add(admin);
						event.getRecipients().add(privateCom.get(admin));
					}
				}
			}
		/* CLientAPP Listener*/
		} else {
			if (listenedPlayer == null || listenedPlayer == player)
				client.sendPacket("PlayerChat-" + player.getName() + "-" + event.getMessage());
		}
	}

	public void stopListener() {
		AsyncPlayerChatEvent.getHandlerList().unregister(this);
	}
}
