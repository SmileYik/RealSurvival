package com.outlook.schooluniformsama.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.player.PlayerData;

public class EnergyEvent implements Listener {
	
	@EventHandler
	public void sneak(PlayerToggleSneakEvent e){
		if(!Data.playerData.containsKey(e.getPlayer().getUniqueId()))return;
		PlayerData pd = Data.playerData.get(e.getPlayer().getUniqueId());
		if(pd.getEnergy().getEnergy()<Data.energy[3]){
			e.setCancelled(true);
			e.getPlayer().setSneaking(false);
		}
	}
	
	@EventHandler
	public void sprint(PlayerToggleSprintEvent e){
		if(!Data.playerData.containsKey(e.getPlayer().getUniqueId()))return;
		PlayerData pd = Data.playerData.get(e.getPlayer().getUniqueId());
		if(pd.getEnergy().getEnergy()<Data.energy[4]){
			e.setCancelled(true);
			e.getPlayer().setSprinting(false);
			return;
		}
		if(pd.getWeight().getWeight()>pd.getWeight().getMaxWeight()){
			e.setCancelled(true);
			e.getPlayer().setSprinting(false);
			return;
		}
			
	}
}
