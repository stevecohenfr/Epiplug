package net.epitech.tasks;

import net.epitech.vose.EpiPlug;

import org.bukkit.scheduler.BukkitRunnable;

public class AutoKickTask extends BukkitRunnable {
	 
    private final EpiPlug plugin;
    private final String name;
    private int time = 0;
    private  int kickTime;;
    
    public AutoKickTask(EpiPlug plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        kickTime = plugin.getConfig().getInt("Auto-kick.Time");
    }
    public void run() {
    	
    	if (time % 600 == 0)
    		plugin.getServer().getPlayer(name).sendMessage("Il vous reste " + ((kickTime - time) / 60) + " minute(s)");
    	if (time >= kickTime) {
    		plugin.getServer().getPlayer(name).kickPlayer("Vous avez dépassé le temps autorisé sur le serveur de " + kickTime/60 + " minutes");
    		if (plugin.getAccounts().containsKey(name)) {
    			plugin.getAccounts().get(name).cancel();
    			plugin.getAccounts().remove(name);
    		}
    	}
    	time++;
    }
}
