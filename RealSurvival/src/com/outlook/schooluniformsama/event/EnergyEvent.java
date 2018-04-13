package com.outlook.schooluniformsama.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.player.PlayerData;
import com.outlook.schooluniformsama.util.Msg;

public class EnergyEvent implements Listener {
	
	@EventHandler
	public void jump(PlayerMoveEvent e){
		if(!Data.playerData.containsKey(e.getPlayer().getUniqueId()))return;
		PlayerData pd = Data.playerData.get(e.getPlayer().getUniqueId());
		if(!e.getFrom().getBlock().getType().name().contains("WATER")){
			
			if(e.getTo().getY()-e.getFrom().getY()>0.5 && 
					(!Msg.sendTitleToPlayer(e.getPlayer(), pd.getEnergy().change(-Data.energy[5]), true) ||
					pd.getWeight().isOverWeight()!=null))//weight
					e.setCancelled(true);
			
		}else if(e.getFrom().getBlockX()!=e.getTo().getBlockX() || 
				e.getFrom().getBlockZ()!=e.getTo().getBlockZ() ||
				e.getFrom().getBlockY()<e.getTo().getBlockY()) {
			if(!Msg.sendTitleToPlayer(e.getPlayer(), pd.getEnergy().change(-Data.energy[6]), true))
				e.setCancelled(true);
		}
		
		
	}
}
