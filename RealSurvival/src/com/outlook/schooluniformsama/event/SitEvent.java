package com.outlook.schooluniformsama.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.cnaude.chairs.api.PlayerChairSitEvent;
import com.cnaude.chairs.api.PlayerChairUnsitEvent;
import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.player.PlayerData;

public class SitEvent implements Listener{

	@EventHandler
	public void sit(PlayerChairSitEvent e){
		if(!Data.playerData.containsKey(e.getPlayer().getUniqueId()))return;
		PlayerData pd = Data.playerData.get(e.getPlayer().getUniqueId());
		pd.getSleep().setSleepBuff(pd.getSleep().getSleepBuff()*0.3);
		pd.getSleep().setHasSleep(true);
		e.setCancelled(false);
		return;
	}
	
	@EventHandler
	public void unsit(PlayerChairUnsitEvent e){
		if(!Data.playerData.containsKey(e.getPlayer().getUniqueId()))return;
		PlayerData pd = Data.playerData.get(e.getPlayer().getUniqueId());
		pd.getSleep().setSleepBuff(pd.getSleep().getSleepBuff()/0.3);
		pd.getSleep().setHasSleep(false);
		return;
	}
}
