package net.epitech.vose;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.epitech.listener.PlayerPreLoginListener;
import net.epitech.listener.PlayerQuitListener;
import net.epitech.other.DataBase;
import net.epitech.tasks.AutoKickTask;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class EpiPlug extends JavaPlugin {
	
	private FileConfiguration 				Config = null;
    private File 							ConfigFile = null;
    private Map<String, BukkitTask>			accounts = new HashMap<String, BukkitTask>();
    
    /* Listeners */
    public PlayerQuitListener 				playerquitevent = new PlayerQuitListener(this);
    public PlayerPreLoginListener			playerpreloginevent = new PlayerPreLoginListener(this);
	
	/*
    ** Override configFile
    */
    public void reloadConfig() {
		 if (ConfigFile == null) {
			 ConfigFile = new File(getDataFolder(), "config.yml");
	        }
	        this.Config = YamlConfiguration.loadConfiguration(ConfigFile);
	        InputStream defConfigStream = this.getResource("config.yml");
	        if (defConfigStream != null) {
	            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	            Config.setDefaults(defConfig);
	        }
	    }
    
    	public FileConfiguration getConfig() {
	        if (Config == null) {
	            this.reloadConfig();
	        }
	        return Config;
	    }
    
	    public void saveConfig() {
	        if (Config == null || ConfigFile == null) {
	        return;
	        }
	        try {
	            getConfig().save(ConfigFile);
	        } catch (IOException ex) {
	            this.getLogger().log(Level.SEVERE, "Could not save config to " + ConfigFile, ex);
	        }
	    }
	    
	    public void saveDefaultConfig(boolean replace) {
	        if (ConfigFile == null) {
	        	ConfigFile = new File(getDataFolder(), "config.yml");
	        }
	        if (!ConfigFile.exists()) {            
	        	this.saveResource("config.yml", replace);
	    	    this.getLogger().log(Level.INFO, "Default config.yml saved.");
	         }
	    }

	@Override
	public void onEnable()
	{
		Logger.getLogger("Minecraft");
		
		this.saveDefaultConfig(false);
		this.reloadConfig();
		
		/* Register events */
		this.getServer().getPluginManager().registerEvents(playerquitevent, this);
		this.getServer().getPluginManager().registerEvents(playerpreloginevent, this);
		
		/* DataBase connection */
		boolean dataBaseIsConnected = DataBase.DataBaseConnect(Config);
		if (dataBaseIsConnected)
			getLogger().log(Level.INFO, "DataBase connected successfuly!");
		else {
			getLogger().log(Level.SEVERE, "Fail to connect to MySQL!");
			this.setEnabled(false);
		}
		/* reset database */
		DataBase.resetUserList();
		
		/* reset all players */
		File BaseFolder = new File(Bukkit.getServer().getWorld("Epitech Montpellier").getWorldFolder(), "players");
	       
        for(OfflinePlayer p : Bukkit.getOfflinePlayers())
        {
            File playerFile = new File(BaseFolder, p.getName() + ".dat");
            playerFile.delete();
            this.getLogger().log(Level.INFO, p.getName() + ".dat deleted!");
        }
	}
	
	@Override
	public void onDisable()
	{
		if (DataBase.closeSession())
			getLogger().log(Level.INFO, "DataBase closed!");
		else
			getLogger().log(Level.SEVERE, "DataBase can not be closed!");
	}
	
	public Map<String, BukkitTask> getAccounts() {
		return accounts;
	}
	
	public void setAccounts(Map<String, BukkitTask> accounts) {
		this.accounts = accounts;
	}
}
