package net.epitech.connector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import net.epitech.listener.PlayerChatListener;
import net.epitech.other.DataBase;
import net.epitech.other.HttpRequest;
import net.epitech.vose.EpiPlug;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Client {

	private Socket				sock;
	private BufferedReader		in;
	private PrintWriter 		out;

	private boolean				inRealTime = false;

	private PlayerChatListener chatListener;
	
	public boolean isInRealTime() {
		return inRealTime;
	}

	private EpiPlug				plugin;
	private LauncherConnector 	connector;

	int							ne = 1, n = 1;

	public Client(EpiPlug epiplug, LauncherConnector connector, Socket sock) throws IOException {
		this.plugin = epiplug;
		this.sock = sock;
		this.connector = connector;
		out = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
	}

	public void looseConnection() {
		try {
			sock.close();
			AsyncPlayerChatEvent.getHandlerList().unregister(chatListener);
			plugin.getLogger().log(Level.INFO, "[Connector] " + sock.getInetAddress().toString().substring(1) + " disconnected.");
		} catch (IOException e1) {
		}
	}

	public void listen() {
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new BukkitRunnable() {
			@Override
			public void run() {
				while (!sock.isClosed()) {
					try {
						String buffer = in.readLine();
						if (buffer != null && !buffer.isEmpty()) {
							plugin.getLogger().log(Level.INFO, "[Connector] Paquet recu n°" + n + ": [" + buffer + ']');
							n++;
							//if (buffer.startsWith("logging") && buffer.contains("-SEPARATOR-tek"))
								execCmd(buffer.split("-SEPARATOR-"));
							//else
								//execCmd(buffer.split("-"));
						}else {
							looseConnection();
						}
					} catch (IOException e) {
						looseConnection();
					}
				}
				connector.getClients().remove(this);
			}
		});
	}

	@SuppressWarnings("rawtypes")
	private void execCmd(String[] args) {
		Method method;
		try {
			List<String> cleanArgs = new LinkedList<String>(Arrays.asList(args));
			cleanArgs.remove(0);
			Class[] cArg = new Class[1];
			cArg[0] = List.class;
			method = this.getClass().getMethod(args[0], cArg);
			method.invoke(this, cleanArgs);
		} catch (NoSuchMethodException e) {
			plugin.getLogger().log(Level.WARNING, sock.getInetAddress() + " ask for unknown command : " + args[0]);
		} catch (SecurityException e) {
		} catch (IllegalAccessException e) {
		} catch (IllegalArgumentException e) {
		} catch (InvocationTargetException e) {
		}
	}
	
	public void CheckLogin(List<String> args) {
		if (args.size() == 2) {
			sendPacket("CheckLogin-" + DataBase.checkUserInDb(args.get(0), args.get(1)).toUpperCase());
		}
	}

	public void enterInChat(List<String> args) {
		chatListener = new PlayerChatListener(plugin, this);
	}
	
	public void sendMessage(List<String> args) {
		if (args.size() >= 2) {
			String sender = args.get(0);
			System.out.println(ChatColor.DARK_GREEN + "<" + sender + "> " + ChatColor.RESET + args.get(1));
			for (Player p : plugin.getServer().getOnlinePlayers()) {
				if (p.isOnline())
					p.sendMessage(ChatColor.DARK_GREEN + "<" + sender + "> " + ChatColor.RESET + args.get(1));
			}
			for (Client c : connector.getClients()) {
				if (c != this)
					c.sendPacket("PlayerChat-" + sender + "-" + args.get(1));
			}
		}
	}
	
	public void sendPlayerMessage(List<String> args) {
		if (args.size() >= 3) {
			String sender = args.get(0);
			String receiver = args.get(1);
			Player p = plugin.getServer().getPlayer(receiver);
			if (p != null && p.isOnline())
				p.sendMessage(ChatColor.DARK_GREEN + "<" + sender + "> " + ChatColor.RESET + args.get(1));
			else
				sendPacket("Error-Sorry the player is offline.");
		}
	}

	public void sendConsoleCmd(List<String> args) {
		if (args.size() >= 1) {
			plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), args.get(0));
		}
	}

	public void realTime(List<String> args) {
		this.inRealTime = args.get(0).startsWith("true");
		plugin.getLogger().log(Level.INFO, "[Connector] " + sock.getInetAddress() + " set real-time to " + String.valueOf(this.inRealTime));
	}


	public void getServInfos(List<String> args) {
		try {
			Server server = plugin.getServer();

			String ip = HttpRequest.getInetIp();
			String port = Integer.toString(server.getPort());
			String name = server.getName();
			String motd = server.getMotd();
			String nbrp = Integer.toString(server.getOnlinePlayers().length);
			String maxp = Integer.toString(server.getMaxPlayers());

			String infos = "ServInfos-";
			infos += "ip:" + ip + "-";
			infos += "port:" + port + "-";
			infos += "name:" + name + "-";
			infos += "motd:" + motd + "-";
			infos += "nbrp:" + nbrp + "-";
			infos += "maxp:" + maxp;

			sendPacket(infos);	
		} catch (IOException e) {
			e.printStackTrace();
		}}

	public void getPlayers(List<String> args) {
		ArrayList<Player> players = new ArrayList<Player>(Arrays.asList(plugin.getServer().getOnlinePlayers()));
		String rep = "Players-";
		for (Player p : players) {
			System.out.println("PlayerLoop1");
			Map<String, String> map = DataBase.getUserInfo(p.getName());
			System.out.println("PlayerLoop2");
			//Pair<BukkitTask, AutoKickTask> account = plugin.getAccounts().get(p.getName());
			//if (account != null)
			//	map.put("Remaining", Integer.toString(account.getSecond().getRemainingTime()));
			//else
				map.put("Remaining", "NC");
			System.out.println("PlayerLoop3");
			Set<String> cles = map.keySet();
			System.out.println("PlayerLoop4");
			Iterator<String> it = cles.iterator();
			System.out.println("PlayerLoop5");
			while (it.hasNext()){
				System.out.println("InfoLoop");
				String cle = it.next();
				String value = map.get(cle);
				rep += cle + ':' + value;
				if (!(cles.toArray()[cles.toArray().length-1].toString() == cle))
					rep += "&";
			}
			System.out.println("PlayerLoop6");
			if (players.indexOf(p) < players.size() - 1)
				rep += "-";
			System.out.println("PlayerLoop7");
		}
		System.out.println("EndOfLoop");
		sendPacket(rep);
	}

	public void getWorlds(List<String> args) {
		List<World> worlds = plugin.getServer().getWorlds();
		String names = "Worlds-";
		for (World w : worlds) {
			names += w.getName();
			if (worlds.indexOf(w) < worlds.size() - 1)
				names += "-";
		}
		sendPacket(names);
	}

	public void ping(List<String> args) {
		sendPacket("pong\n");
	}

	public void createPerm(List<String> args) throws IOException {
		/* Si reçu on parse le paquet ainsi : createPerm-adminLogin-admPassMd5-newusername-newrank-city */
		/* Si le paquet ne contient pas assez d'elements, on alerte et on sort*/
		if (args.size() < 5) {
			plugin.getLogger().log(Level.SEVERE, "[Connector] Receive wrong packet on LauncherPermListener:");
			sendPacket("KO\n");
			return;
		}
		String checkrank = DataBase.getUserRank(args.get(0));
		/* Check si l'envoyeur est bien identifié et son rank on ajoute la ligne avec le rank dans les permissions */
		if (DataBase.checkUserInDb(args.get(0), args.get(1)).equals("ok") && (checkrank.equals("Dev") || checkrank.equals("Adm"))) {
			//plugin.getLogger().log(Level.INFO, "[Connector] " + "=============>>> Check in db OK!");
			String login = args.get(2);
			String rank = args.get(3);
			String newUserPerm = "";

			/* Génération du bloque de permission */
			newUserPerm += "  "+ login + ":" + "\n";
			newUserPerm += "    permissions: []" + "\n";
			newUserPerm += "    groups:" + "\n";
			newUserPerm += "    - " + rank + "\n";

			/* Récupération du fichier de permissions utilisateurs sur le serveur minecraft */
			File bPermissionBase = new File(Bukkit.getPluginManager().getPlugin("bPermissions").getDataFolder(), "epitech " + args.get(4));
			File usersdata = new File(bPermissionBase, "users.yml");

			/* Ecriture du bloque à la fin du fichier */
			FileWriter fw = new FileWriter(usersdata, true);
			PrintWriter output = new PrintWriter(fw);
			output.write(newUserPerm);
			output.flush();
			output.close();

			//plugin.getLogger().log(Level.INFO, "[Connector] " + "=============>>> Block added:");
			//plugin.getLogger().log(Level.INFO, "[Connector] " + newUserPerm);

			/* Réponse au launcher que tout c'est bien passé */
			sendPacket("OK\n");
		} else {
			sendPacket("KO\n");
		}
	}

	public void logging(List<String> args) {
		if (args.size() < 4)
			return;
		String login = args.get(0);
		String password = args.get(1);
		String rank = args.get(2);
		String ip = (args.get(3).startsWith("163.5") ? "163.5.*.*" : args.get(3));

		plugin.getLogger().log(Level.INFO, "[Connector] " + "Login: " + login);
		plugin.getLogger().log(Level.INFO, "[Connector] " + "Pass: " + password);
		plugin.getLogger().log(Level.INFO, "[Connector] " + "Rank: " + rank);
		plugin.getLogger().log(Level.INFO, "[Connector] " + "IP: " + ip);
		if (rank.equals("tek")) {
			try {
				boolean auth = HttpRequest.sendPostCheckEpitechLogin("JSON", login, password);
				if (auth) {
					plugin.getSessions().savePlayerSession(login, ip);
					plugin.getLogger().log(Level.INFO, "[Tek] " + login + " will connect soon.");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (rank.equals("guest")) {
			plugin.getSessions().savePlayerSession(login, ip);
			plugin.getLogger().log(Level.INFO, "[" + DataBase.getUserRank(login) + "] " + login + " will connect soon.");
		} else {
			if (DataBase.checkUserInDb(login, password).equals("ok")) {
				plugin.getSessions().savePlayerSession(login, ip);
				plugin.getLogger().log(Level.INFO, "[" + DataBase.getUserRank(login) + "] " + login + " will connect soon.");
			}
		}
	}

	public void cancellogging(List<String> args) {
		if (args.size() < 4)
			return;
		String login = args.get(0);
		String rank = args.get(2);
		String ip = (args.get(3).startsWith("163.5") ? "163.5.*.*" : args.get(3));

		plugin.getSessions().removePlayerSession(login, ip);
		DataBase.resetSpecificUser(login);
		plugin.getLogger().log(Level.INFO, '[' + rank + ']' + login + " from " + ip + ", has canceled his connection request.");
	}

	public boolean sendPacket(String msg) {
		out.print(msg);
		boolean ret = out.checkError();
		if (ret = true)
			plugin.getLogger().log(Level.INFO, "[Connector] " + "Paquet envoyé n°" + ne + ": " + msg);
		else
			plugin.getLogger().log(Level.SEVERE, "[Connector] " + "Fail to send packet n°" + ne + ": " + msg);
		ne++;
		return ret;
	}
}
