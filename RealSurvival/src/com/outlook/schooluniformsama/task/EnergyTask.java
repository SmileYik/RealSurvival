package com.outlook.schooluniformsama.task;

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
				if(!Msg.sendTitleToPlayer(p, pd.getEnergy().change(-Data.energy[3]),Data.enablePrefixInTitle)){
					p.setSneaking(false);
				}
			}else if(p.isSprinting()){
				if(!Msg.sendTitleToPlayer(p, pd.getEnergy().change(-Data.energy[4]),Data.enablePrefixInTitle))
					p.setSprinting(false);
			}else{
				Msg.sendTitleToPlayer(p, pd.getEnergy().change(Data.energy[2]),Data.enablePrefixInTitle);
			}
		}
	}
}
