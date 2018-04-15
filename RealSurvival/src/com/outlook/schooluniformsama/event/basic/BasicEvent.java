package com.outlook.schooluniformsama.event.basic;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.player.PlayerData;
import com.outlook.schooluniformsama.util.HashMap;

public class BasicEvent implements Listener{
	//Player Join
	
	@EventHandler
	public void join(PlayerJoinEvent e){
		Player p=e.getPlayer();
		if(p.hasMetadata("NPC"))return;
		if(!Data.worlds.contains(p.getWorld().getName()))return;
		Data.playerData.put(p.getUniqueId(), PlayerData.load(p.getUniqueId()));
		return;
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent e){
		if(e.getPlayer().hasMetadata("NPC"))return;
		if(!Data.playerData.containsKey(e.getPlayer().getUniqueId()))return;
		PlayerData pd = Data.playerData.get(e.getPlayer().getUniqueId());
		pd.save();
		Data.playerData.remove(pd.getUuid());
		return;
	}
	
	@EventHandler
	public void teleport(PlayerTeleportEvent e){
		if(e.getPlayer().hasMetadata("NPC"))return;
		if(!Data.worlds.contains(e.getTo().getWorld().getName())){
			PlayerData pd = Data.playerData.get(e.getPlayer().getUniqueId());
			if(pd==null)return;
			pd.save();
			Data.playerData.remove(pd.getUuid());
			return;
		}
		Data.playerData.put(e.getPlayer().getUniqueId(), PlayerData.load(e.getPlayer().getUniqueId()));
	}
	//Player Join End
	
	//Player Death
	@EventHandler
	public void death(org.bukkit.event.entity.PlayerDeathEvent e){
		Player p = e.getEntity();
		if(!Data.playerData.containsKey(p.getUniqueId()))return;
		PlayerData pd = Data.playerData.get(p.getUniqueId());
		
		pd.getSleep().change(Data.deathData[0]);
		pd.getThirst().change(Data.deathData[1]);
		pd.getEnergy().change(Data.deathData[2]);
		pd.getTemperature().setTemperature(Data.deathData[3]);
		if(Data.deathData[4]==1)
			pd.getIllness().setIllness(new HashMap<>());
	}
	//Player Death End
}
