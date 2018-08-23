package net.epitech.tasks;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import net.epitech.vose.EpiPlug;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Session {
	private EpiPlug										plugin;
	private FileConfiguration 							Sessions = null;
	private File 										SessionsFile = null;

	public Session(EpiPlug epiplug) {
		this.plugin = epiplug;
	}
	
	public void reloadSessions() {
		if (SessionsFile == null) {
			SessionsFile = new File(plugin.getDataFolder(), "sessions.yml");
		}
		this.Sessions = YamlConfiguration.loadConfiguration(SessionsFile);
		InputStream defTipsStream = plugin.getResource("sessions.yml");
		if (defTipsStream != null) {
			YamlConfiguration defSessions = YamlConfiguration.loadConfiguration(defTipsStream);
			Sessions.setDefaults(defSessions);
		}
	}

	public FileConfiguration getSessions() {
		if (Sessions == null) {
			this.reloadSessions();
		}
		return Sessions;
	}

	public void saveSessions() {
		if (Sessions == null || SessionsFile == null) {
			return;
		}
		try {
			getSessions().save(SessionsFile);
		} catch (IOException ex) {
			plugin.getLogger().log(Level.SEVERE, "Could not save sessions to " + SessionsFile, ex);
		}
	}
	
	public void saveAndReloadSessions() {
		this.saveSessions();
		this.reloadSessions();
	}
	
	public void saveDefaultSessions(boolean replace) {
		if (SessionsFile == null) {
			SessionsFile = new File(plugin.getDataFolder(), "sessions.yml");
		}
		if (!SessionsFile.exists()) {            
			plugin.saveResource("sessions.yml", replace);
			plugin.getLogger().log(Level.INFO, "Default sessions.yml saved.");
		}
	}
	
	public void savePlayerSession(Player p) {
		String ip = p.getAddress().getAddress().getHostAddress();
		if (ip.startsWith("163.5."))
			ip = "163.5.*.*";
		plugin.getLogger().log(Level.INFO, "Saving " + p.getName() + "'s session with IP: " + ip);
		getSessions().set(p.getName(), ip);
		this.saveAndReloadSessions();
	}
	
	public void savePlayerSession(String username, String ip) {
		if (ip.startsWith("163.5."))
			ip = "163.5.*.*";
		plugin.getLogger().log(Level.INFO, "Saving " + username + "'s session with IP: " + ip);
		getSessions().set(username, ip);
		this.saveAndReloadSessions();
	}
	
	public boolean removePlayerSession(Player p) {
		if (getSessions().contains(p.getName())) {
			plugin.getLogger().log(Level.INFO, p.getName() + "'s session has been removed.");
			getSessions().set(p.getName(), null);
			this.saveAndReloadSessions();
			return true;
		}
		return false;
	}
	
	public boolean removePlayerSession(String username, String ip) {
		if (getSessions().contains(username)) {
			plugin.getLogger().log(Level.INFO, username + "'s session has been removed.");
			getSessions().set(username, null);
			this.saveAndReloadSessions();
			return true;
		}
		return false;
	}
	
	public boolean isSessionRunnin(Player p, String ip) {
		if (getSessions().contains(p.getName()) && getSessions().getString(p.getName()).equals(ip)) {
			return true;
		}
		return false;
	}
	
	public void runPlayerSession(final Player p) {
		new Thread() {
			public void run() {
				try {
					Thread.sleep(plugin.getConfig().getInt("Sessions.Time") * 1000);
					removePlayerSession(p);
					saveAndReloadSessions();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

}
