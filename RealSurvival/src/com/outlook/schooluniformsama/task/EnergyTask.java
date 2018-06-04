package com.outlook.schooluniformsama.task;

import org.bukkit.entity.Player;

import com.outlook.schooluniformsama.RealSurvival;
import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.player.EffectType;
import com.outlook.schooluniformsama.data.player.PlayerData;

public class EnergyTask implements Runnable{
	@Override
	public void run() {
		for(PlayerData pd:Data.playerData.values()){
			Player p = RealSurvival.getPlayer(pd.getUuid());
			if(p==null || pd==null || p.isDead())return;
			if(p.isSneaking()){
				if(pd.getEnergy().getEnergy()<Data.energy[3])
					p.setSneaking(false);
				else
					pd.change(EffectType.ENERGY, -Data.energy[3]);
			}else if(p.isSprinting()){
				if(pd.getEnergy().getEnergy()<Data.energy[4])
					p.setSprinting(false);
				else
					pd.change(EffectType.ENERGY, -Data.energy[4]);
			}else{
				pd.change(EffectType.ENERGY, Data.energy[2]);
			}
		}
	}
}
