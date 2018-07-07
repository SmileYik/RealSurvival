package com.outlook.schooluniformsama.event.basic;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.player.EffectType;
import com.outlook.schooluniformsama.data.player.PlayerData;

public class BasicEvent implements Listener{
	//Player Join
	
	@EventHandler
	public void join(PlayerJoinEvent e){
		Data.addPlayer(e.getPlayer());
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
		if(!Data.playerData.containsKey(e.getPlayer().getUniqueId()))
			Data.addPlayer(e.getPlayer());
	}
	//Player Join End
	
	//Player Death
	@EventHandler
	public void onDeath(org.bukkit.event.entity.PlayerDeathEvent e){
		Player p = e.getEntity();
		if(!Data.playerData.containsKey(p.getUniqueId()))return;
		PlayerData pd = Data.playerData.get(p.getUniqueId());
		if(Data.switchs[1]){
			pd.change(EffectType.SLEEP, Data.deathData[0]);
			pd.change(EffectType.THIRST, Data.deathData[1]);
			pd.change(EffectType.ENERGY, Data.deathData[2]);
			pd.change(EffectType.TEMPERATURE, Data.deathData[3]);
			if(Data.deathData[4]==1)
				pd.getIllness().clear();
		}
	}
	//Player Death End
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e){
		if(!Data.worlds.contains(e.getPlayer().getWorld().getName())){
			PlayerData pd = Data.playerData.get(e.getPlayer().getUniqueId());
			if(pd==null)return;
			pd.save();
			Data.playerData.remove(pd.getUuid());
			return;
		}else Data.addPlayer(e.getPlayer());
	}
}
