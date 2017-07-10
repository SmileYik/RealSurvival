package com.outlook.z815007320.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;

import com.outlook.z815007320.data.PluginRS;

public class PhysicalStrengthEvent extends PluginRS implements Listener {
	
	@EventHandler
	public void sneak(PlayerToggleSneakEvent e){
		if(rs.getPlayerData(e.getPlayer())==null)return;
		if(rs.getPlayerData(e.getPlayer()).getPhysical_strength()<rs.getSneaking()){
			e.setCancelled(true);
			e.getPlayer().setSneaking(false);
		}
	}
	
	@EventHandler
	public void sprint(PlayerToggleSprintEvent e){
		if(rs.getPlayerData(e.getPlayer())==null)return;
		if(rs.getPlayerData(e.getPlayer()).getPhysical_strength()>=rs.getSprinting()||
				rs.getPlayerData(e.getPlayer()).getWeight()<=rs.getWeight())return;
			e.setCancelled(true);
			e.getPlayer().setSprinting(false);
	}
}
