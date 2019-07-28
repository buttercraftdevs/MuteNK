package me.xxsirgamesxx.mute;

import cn.nukkit.Player;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.level.Level;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.level.Location;

import cn.nukkit.plugin.Plugin;
import cn.nukkit.event.player.PlayerChatEvent;

import cn.nukkit.command.*;

import cn.nukkit.command.data.*;

import java.util.Map;
import java.io.File;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Main extends PluginBase implements Listener {
	
	private List<Player> mutedPlayers = new ArrayList<>();

    private static final int configVersion = 3;
    private Config config;
    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {

        this.getServer().getPluginManager().registerEvents(this, this);
				
    }
	
    public boolean isMuted(Player player) {

        return mutedPlayers.contains(player);

    }	

    public boolean switchMuted(Player player) {

        boolean muted = this.isMuted(player);

        if (muted) {
			
            mutedPlayers.remove(player);

        } else {

            mutedPlayers.add(player);

        }

        return !muted;

    }
	public void mute(Player player){
		mutedPlayers.add(player);
		
	}
	
	public void unmute(Player player){
		mutedPlayers.remove(player);
	}		

	
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		//Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("mute")) {
			Player player = (Player) sender;
			if (!(sender instanceof Player)) {
				sender.sendMessage("Run command as player.");
				return true;
				//Not sure why its not working ill have t dig deeper.
			}							
			if (args.length == 0) {
				sender.sendMessage("§7Usage: 6/mute <player>");
				return false;
			}

			if(sender.hasPermission("cmd.mute")){
				if(this.getServer().getPlayer(args[0]) == null){
					player.sendMessage("§cPlayer Not Found!");
					return false;
				} else{
					Player t = this.getServer().getPlayer(args[0]);
					mute(t);
					t.sendMessage("§6You have been muted!");
					player.sendMessage("§6" + t.getName() + " §7has been muted!");
					return true;
				}
				//return true;
				
			} else {
				sender.sendMessage("§cYou do not have permission!");
			}
			return true;
				
			} else if (cmd.getName().equalsIgnoreCase("unmute")) {
				Player player = (Player) sender;
				if (args.length > 1) {
					sender.sendMessage("§7Usage: §6/unmute <player>");
					return false;
				}
				if(player.hasPermission("cmd.mute")){
					if(this.getServer().getPlayer(args[0]) == null){
						player.sendMessage("§cPlayer Not Found!");
						return false;
						} else{
							Player ts = this.getServer().getPlayer(args[0]);
							if(isMuted(ts)) {
								this.unmute(ts);
								ts.sendMessage("§aYou have been unmuted!");
								player.sendMessage("§6" + ts.getName() + "7 has been unmuted!");
								} else {
									player.sendMessage("§6" + ts.getName() + " §7is already unmuted!");
								}
								return true;
						}
						//return true;
				} else {
				sender.sendMessage("§cYou do not have permission!");
				}
					
				return true;
			
			} 		
			return true;		
    }

  @EventHandler
  public void onJoin(PlayerChatEvent e) {
	  if(isMuted(e.getPlayer())){
		  e.setCancelled(true);
		  e.getPlayer().sendTip("§cYou are muted!");
	  }
  }	  
}
