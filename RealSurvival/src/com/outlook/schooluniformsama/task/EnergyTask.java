package com.outlook.schooluniformsama.task;


import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.outlook.schooluniformsama.RealSurvival;
import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.player.PlayerData;
import com.outlook.schooluniformsama.util.Msg;

public class EnergyTask implements Runnable{
	@Override
	public void run() {
		for(PlayerData pd:Data.playerData.values()){
			Player p = RealSurvival.getPlayer(pd.getUuid());
			if(p==null || pd==null || p.isDead())return;
			if(p.isSneaking()){
				Msg.sendTitleToPlayer(p, pd.getEnergy().change(-Data.energy[3]),true);
			}else if(p.isSprinting()){
				Msg.sendTitleToPlayer(p, pd.getEnergy().change(-Data.energy[4]),true);
			}else if(p.getWorld().getBlockAt(p.getLocation()).getType()==Material.WATER || p.getWorld().getBlockAt(p.getLocation()).getType()==Material.WATER_LILY){
				//TODO swimming
			}else{
				Msg.sendTitleToPlayer(p, pd.getEnergy().change(Data.energy[2]),true);
			}
		}
	}
}
