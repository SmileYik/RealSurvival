package com.outlook.schooluniformsama.event.basic;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;

import com.outlook.schooluniformsama.api.data.EffectType;
import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.player.PlayerData;

public class BasicEvent implements Listener{
	private Plugin plugin = Bukkit.getPluginManager().getPlugin("RealSurvival");
	//Player Join
	
	@EventHandler
	public void join(PlayerJoinEvent e){
		Data.addPlayer(e.getPlayer());
		return;
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent e){
		if(!Data.enableInPlayer(e.getPlayer().getUniqueId()))return;
		Data.removePlayer(e.getPlayer().getUniqueId());
		return;
	}
	
	@EventHandler
	public void teleport(PlayerTeleportEvent e){
		Player p = e.getPlayer();
		
		if(Data.enableInPlayer(e.getPlayer().getUniqueId())){
			if(!Data.enableInWorld(e.getTo().getWorld().getName())){
				Data.removePlayer(e.getPlayer().getUniqueId());
			}else Data.getPlayerData(p.getUniqueId()).setWorld();
		}else{
			if(Data.enableInWorld(e.getTo().getWorld().getName())){
				plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, ()->Data.addPlayer(p), 1);
			}
		}
	}
	//Player Join End
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e){
		Player p = e.getPlayer();
		if(!Data.enableInPlayer(p.getUniqueId()))return;
		PlayerData pd = Data.getPlayerData(p.getUniqueId());
		pd.setWorld();
		if(Data.switchs[1]){
			pd.change(EffectType.SLEEP, Data.deathData[0]);
			pd.change(EffectType.THIRST, Data.deathData[1]);
			pd.change(EffectType.ENERGY, Data.deathData[2]);
			if(Data.deathData[3]==1)
				pd.getIllness().clear();
		}
		if(!p.isSleeping())pd.getSleep().setHasSleep(false);
		plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, ()->{
			if(!Data.enableInWorld(e.getRespawnLocation().getWorld().getName())){
				Data.removePlayer(pd.getUuid());
				return;
			}			
		}, 1);
		
		
	}
}
