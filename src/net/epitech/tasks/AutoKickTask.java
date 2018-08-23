package net.epitech.tasks;

import org.bukkit.scheduler.BukkitRunnable;

public class AutoKickTask extends BukkitRunnable {

	@Override
	public void run() {
		
	}
	 
    /*private final EpiPlug plugin;
    private final Player player;
    private int time = 0;
    private int kickTime;
    
    public AutoKickTask(EpiPlug plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        kickTime = plugin.getConfig().getInt("Auto-kick.Time");
    }
    public void run() {
    	if ((kickTime - time) >= 600) {
    		if (time % 600 == 0) //every 10 minutes
    			player.sendMessage(ChatColor.BLUE + "Il vous reste " + ((kickTime - time) / 60) + " minutes");
    	}
    	else if ((kickTime - time) < 600) {
    		if (time % 60 == 0) // when 10 minutes last : every minutes
    			player.sendMessage(ChatColor.RED + "Il vous reste " + ((kickTime - time) / 60) + " minute(s)");
    	}
    	if (time >= kickTime) {
    		player.kickPlayer("Vous avez dépassé le temps autorisé sur le serveur de " + kickTime / 60 + " minutes");
    		if (plugin.getAccounts().containsKey(player.getName())) {
    			plugin.getAccounts().get(player.getName()).getFirst().cancel();
    			plugin.getAccounts().remove(player.getName());
    		}
    	}
    	time++;
    }

    public int getTimePlayed() {
    	return time;
	}
    
    public int getRemainingTime() {
    	return kickTime - time;
    }
    
    public void setRemainingTime(int time) {
    	kickTime += time;
    }*/
}
