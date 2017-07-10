package com.outlook.z815007320.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import com.outlook.z815007320.data.PlayerData;
import com.outlook.z815007320.data.PluginRS;

public class SleepEvent extends PluginRS implements Listener{
	@EventHandler
	public void Sleep(PlayerBedEnterEvent e){
		if(rs.getPlayerData(e.getPlayer())==null)return;
		PlayerData pd=rs.getPlayerData(e.getPlayer());
		pd.setSleep(true,1);
		pd.setBed(e.getBed());
		e.setCancelled(false);
		return;
	}
	
	@EventHandler
	public void leaveSleep(PlayerBedLeaveEvent e){
		if(rs.getPlayerData(e.getPlayer())==null)return;
		PlayerData pd=rs.getPlayerData(e.getPlayer());
		pd.setSleep(false,1);
		return;
	}
	
	/*@EventHandler
	public void PlayerSleepAtDay(PlayerInteractEvent event){
		
	}*/
}
