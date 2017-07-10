package com.outlook.z815007320.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.cnaude.chairs.api.PlayerChairSitEvent;
import com.cnaude.chairs.api.PlayerChairUnsitEvent;
import com.outlook.z815007320.data.PlayerData;
import com.outlook.z815007320.data.PluginRS;

public class SitEvent extends PluginRS implements Listener{

	@EventHandler
	public void sit(PlayerChairSitEvent e){
		if(rs.getPlayerData(e.getPlayer())==null)return;
		PlayerData pd=rs.getPlayerData(e.getPlayer());
		pd.setSleep(true,0.3);
		pd.setBed(e.getSitLocation().getBlock());
		e.setCancelled(false);
		return;
	}
	
	@EventHandler
	public void unsit(PlayerChairUnsitEvent e){
		if(rs.getPlayerData(e.getPlayer())==null)return;
		PlayerData pd=rs.getPlayerData(e.getPlayer());
		pd.setSleep(false,0.3);
		return;
	}
}
