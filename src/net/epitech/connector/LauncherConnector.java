package net.epitech.connector;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import net.epitech.vose.EpiPlug;

import org.bukkit.scheduler.BukkitRunnable;

public class LauncherConnector extends BukkitRunnable {

	private ServerSocket	server;
	private EpiPlug			plugin;
	private List<Client>	clients = new ArrayList<Client>();

	public List<Client> getClients() {
		return clients;
	}

	public LauncherConnector(EpiPlug epiplug, int port) {
		this.plugin = epiplug;
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while(EpiPlug.running) {
			try {
				final Socket sock = server.accept();
				plugin.getLogger().log(Level.INFO, "[Connector] New Connexion from " + sock.getInetAddress());
				/* New client */
				Client client = new Client(plugin, this, sock);
				client.listen();
				clients.add(client);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sendToAllRealTime(String str) {
		for (Client c : clients) {
			if (c.isInRealTime())
				c.sendPacket(str);
		}
	}
}