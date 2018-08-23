package net.epitech.vose;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.epitech.commands.CandidateComputerCommand;
import net.epitech.commands.ForceValidateCandidate;
import net.epitech.commands.ListUsersInQueue;
import net.epitech.commands.NextCommand;
import net.epitech.commands.Pabsent;
import net.epitech.commands.PaddTime;
import net.epitech.commands.Pinfo;
import net.epitech.commands.Preset;
import net.epitech.commands.Pwhere;
import net.epitech.commands.SelectLibraryCommand;
import net.epitech.commands.SetSecurity;
import net.epitech.commands.TutoSesame;
import net.epitech.commands.testCommand;
import net.epitech.connector.LauncherConnector;
import net.epitech.listener.*;
import net.epitech.other.DataBase;
import net.epitech.other.JpoAdmin;
import net.epitech.other.Pair;
import net.epitech.tasks.AutoKickTask;
import net.epitech.tasks.Session;
import net.epitech.tasks.Tips;
import net.epitech.tasks.AutoPopRam;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class EpiPlug extends JavaPlugin {

	public static boolean								running;
	public static boolean								securityEnabled = true;
	public Logger										logger;

	private FileConfiguration 							Config = null;
	private File 										ConfigFile = null;
	private FileConfiguration 							Tips = null;
	private File 										TipsFile = null;
	public static String								userPrefix;
	private Map<String, Pair<BukkitTask, AutoKickTask>>	accounts = new HashMap<String, Pair<BukkitTask, AutoKickTask>>();
	private Map<World, ArrayList<Player>>				playerQueue = new HashMap<World, ArrayList<Player>>();
	private Map<String, String>							pdfMap = new HashMap<String, String>();
	private Map<String, JpoAdmin>						admins = new HashMap<String, JpoAdmin>();
	private Map<Player, Player>							privateCom = new HashMap<Player, Player>();
	private LauncherConnector 							launcherConnector;
	private Tips										tipsTask;

	/* Listeners */
	private PlayerQuitListener 							playerquitevent = new PlayerQuitListener(this);
	private PlayerKickListener 							playerkickevent = new PlayerKickListener(this);
	private PlayerLoginListener							playerpreloginevent = new PlayerLoginListener(this);
	private CandidateComputerListener					candidateComputerEvent = new CandidateComputerListener(this);
	private LibraryListener 							libraryEvent = new LibraryListener(this);
	private PlayerChangeWorldListener 					changeWorldEvent = new PlayerChangeWorldListener(this);
	private PlayerDropItemListener						dropItemEvent = new PlayerDropItemListener(this);
	private PlayerChatListener							chatEvent = new PlayerChatListener(this);
	private ItemDespawnListener							itemdespawnevent = new ItemDespawnListener(this);

	/* Commands */
	private CandidateComputerCommand 					candidateComputerCommand = new CandidateComputerCommand(this);
	private ForceValidateCandidate						forcevalidatecondition = new ForceValidateCandidate(this);
	private SelectLibraryCommand 						libraryCommand = new SelectLibraryCommand(this);
	private NextCommand 								nextCommand = new NextCommand(this);
	private Pinfo 										pinfo = new Pinfo(this);
	private Preset 										preset = new Preset(this);
	private PaddTime									paddtime = new PaddTime(this);
	private Pwhere										pwhere = new Pwhere(this);
	private Pabsent										pabsent = new Pabsent(this);
	private ListUsersInQueue							listusersinqueue = new ListUsersInQueue(this);
	private TutoSesame									tutosesame	= new TutoSesame(this);
	private testCommand									testcmd = new testCommand(this);
	private SetSecurity									setsecurity = new SetSecurity(this);
	
	/* Logging security */
	//private Map<String, String>						willConnect = new HashMap<String, String>();
	private Session										sessions;

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
	
	/*
	** Tips file
	*/
	public void reloadTips() {
		if (TipsFile == null) {
			TipsFile = new File(getDataFolder(), "tips.yml");
		}
		this.Tips = YamlConfiguration.loadConfiguration(TipsFile);
		InputStream defTipsStream = this.getResource("tips.yml");
		if (defTipsStream != null) {
			YamlConfiguration defTips = YamlConfiguration.loadConfiguration(defTipsStream);
			Tips.setDefaults(defTips);
		}
	}

	public FileConfiguration getTips() {
		if (Tips == null) {
			this.reloadTips();
		}
		return Tips;
	}

	public void saveTips() {
		if (Tips == null || TipsFile == null) {
			return;
		}
		try {
			getTips().save(TipsFile);
		} catch (IOException ex) {
			this.getLogger().log(Level.SEVERE, "Could not save config to " + TipsFile, ex);
		}
	}

	public void saveDefaultTips(boolean replace) {
		if (TipsFile == null) {
			TipsFile = new File(getDataFolder(), "tips.yml");
		}
		if (!TipsFile.exists()) {            
			this.saveResource("tips.yml", replace);
			this.getLogger().log(Level.INFO, "Default tips.yml saved.");
		}
	}
	
	@Override
	public void onEnable()
	{
		this.logger =  Logger.getLogger("Minecraft");
		
		EpiPlug.running = true;

		sessions = new Session(this);
		
		/* Config Files */
		this.saveDefaultConfig(false);
		this.reloadConfig();
		this.saveDefaultTips(false);
		this.reloadTips();
		sessions.saveDefaultSessions(false);
		sessions.reloadSessions();

		/* Register events */
		this.getServer().getPluginManager().registerEvents(playerquitevent, this);
		this.getServer().getPluginManager().registerEvents(playerkickevent, this);
		this.getServer().getPluginManager().registerEvents(playerpreloginevent, this);
		this.getServer().getPluginManager().registerEvents(candidateComputerEvent, this);
		this.getServer().getPluginManager().registerEvents(libraryEvent, this);
		this.getServer().getPluginManager().registerEvents(changeWorldEvent, this);
		this.getServer().getPluginManager().registerEvents(dropItemEvent, this);
		this.getServer().getPluginManager().registerEvents(chatEvent, this);
		this.getServer().getPluginManager().registerEvents(itemdespawnevent, this);

		/* DataBase connection */
		boolean dataBaseIsConnected = DataBase.DataBaseConnect(this.getConfig());
		if (dataBaseIsConnected)
			getLogger().log(Level.INFO, "DataBase connected successfuly!");
		else {
			getLogger().log(Level.SEVERE, "Fail to connect to MySQL!");
			this.setEnabled(false);
		}
		/* reset database */
		DataBase.resetUserList();
		/* Get the user prefix */
		userPrefix = DataBase.getUserPrefix();

		/* reset all players */
		File BaseFolder = new File(Bukkit.getServer().getWorlds().get(0).getWorldFolder(), "players");
		File essentialsPlayerBase = new File(Bukkit.getPluginManager().getPlugin("Essentials").getDataFolder(), "userdata");

		for(OfflinePlayer p : Bukkit.getOfflinePlayers())
		{
			if (!p.getName().startsWith("Guest"))
				continue;
			File playerFile = new File(BaseFolder, p.getName() + ".dat");
			File essentialsdata = new File(essentialsPlayerBase, p.getName().toLowerCase() + ".yml");
			playerFile.delete();
			essentialsdata.delete();
			this.getLogger().log(Level.INFO, playerFile.getPath() + " deleted!");
			this.getLogger().log(Level.INFO, essentialsdata.getPath() + " deleted!");
		}

		/* Commands */
		getCommand("nextGuest").setExecutor((CommandExecutor)nextCommand);
		getCommand("setCandidateComputer").setExecutor((CommandExecutor)candidateComputerCommand);
		getCommand("setLibraryPDF").setExecutor((CommandExecutor)libraryCommand);
		getCommand("pinfo").setExecutor((CommandExecutor)pinfo);
		getCommand("where").setExecutor((CommandExecutor)pwhere);
		getCommand("absent").setExecutor((CommandExecutor)pabsent);
		getCommand("preset").setExecutor((CommandExecutor)preset);
		getCommand("paddtime").setExecutor((CommandExecutor)paddtime);
		getCommand("forceValidateCandidate").setExecutor((CommandExecutor)forcevalidatecondition);
		getCommand("listUsersInQueue").setExecutor((CommandExecutor)listusersinqueue);
		getCommand("sesame").setExecutor((CommandExecutor)tutosesame);
		getCommand("test").setExecutor((CommandExecutor)testcmd);
		getCommand("setSecurity").setExecutor((CommandExecutor)setsecurity);

		/* Set la LibraryMap (pdfmap) */
		this.setPdfMap(DataBase.getSavedLibrary());
		
		/* Set la AdminMap (admins) */
		// TODO WIP continuer ça
		/*this.setAdminsMap(DataBase.getAdmins());
		Set<String> keys = admins.keySet();
		Iterator<String> i = keys.iterator();
		while (i.hasNext()) {
			String key = i.next();
			JpoAdmin admin = admins.get(key);
			
			System.out.println(key + ": name = " + admin.getName() + ", rank = " + admin.getRank() + ", city = " + admin.getCity());
		}
		*/

		/* Launcher Listener */ /* Tasks */
		launcherConnector = new LauncherConnector(this, this.getConfig().getInt("Query.Port"));
		this.getServer().getScheduler().runTaskAsynchronously(this, launcherConnector);
		tipsTask = new Tips(this);
		if (this.getConfig().getBoolean("Tips.Enabled"))
			this.getServer().getScheduler().runTaskTimerAsynchronously(this, tipsTask , this.getConfig().getInt("Tips.Delay") * 20, this.getConfig().getInt("Tips.Period") * 20);
		
		/* ChangeWorld */
		// fill hashmap with world,
		Server server = this.getServer();
		for (String s : this.getConfig().getStringList("Queue.Worlds")) {
			playerQueue.put(server.getWorld(s), new ArrayList<Player>());
		}
		this.getServer().getScheduler().runTaskTimer(this, new AutoPopRam(this), 20, 400);
	}

	@Override
	public void onDisable()
	{
		EpiPlug.running = false;
		if (DataBase.closeSession())
			getLogger().log(Level.INFO, "DataBase closed!");
		else
			getLogger().log(Level.SEVERE, "DataBase can not be closed!");
		for (Player p : this.getServer().getOnlinePlayers())
			this.getSessions().savePlayerSession(p);
	}

	public boolean compareBlocks(Block block1, Block block2) {
		if (block1 == null || block2 == null) {
			Bukkit.getLogger().log(Level.SEVERE, "[EpiPlug] WARNING Comparing null in CompareBlock !");
			return false;
		}
		if (block1.getX() == block2.getX() &&
				block1.getY() == block2.getY() &&
				block1.getZ() == block2.getZ() &&
				block1.getWorld() == block2.getWorld())
			return true;
		return false;
	}

	public Map<String, Pair<BukkitTask, AutoKickTask>> getAccounts() {
		return accounts;
	}

	public void setAccounts(Map<String, Pair<BukkitTask, AutoKickTask>> accounts) {
		this.accounts = accounts;
	}

	public Map<World, ArrayList<Player>> getPlayerQueue() {
		return playerQueue;
	}

	public void setPlayerQueue(Map<World, ArrayList<Player>> playerQueue) {
		this.playerQueue = playerQueue;
	}
	
	public Map<String, String> getPdfMap() {
		return pdfMap;
	}

	public void setPdfMap(Map<String, String> pdfMap) {
		this.pdfMap = pdfMap;
	}

	public LibraryListener getLibraryEvent() {
		return libraryEvent;
	}

	public Map<String, JpoAdmin> getAdmins() {
		return this.admins;
	}

	public void setAdminsMap(Map<String, JpoAdmin> adminsMap) {
		this.admins = adminsMap;
	}
	
	public Map<Player, Player> getPrivateCom() {
		return privateCom;
	}
	
	public String getUserPrefix() {
		return userPrefix;
	}

//	public Map<String, String> getWillConnect() {
//		return willConnect;
//	}
//
//	public void setWillConnect(Map<String, String> willConnect) {
//		this.willConnect = willConnect;
//	}
	
	public LauncherConnector getLauncherConnector() {
		return launcherConnector;
	}

	public Session getSessions() {
		return sessions;
	}

	public void setSessions(Session sessions) {
		this.sessions = sessions;
	}

}
