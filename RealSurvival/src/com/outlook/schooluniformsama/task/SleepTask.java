package com.outlook.schooluniformsama.task;

import org.bukkit.entity.Player;

import com.outlook.schooluniformsama.RealSurvival;
import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.player.EffectType;
import com.outlook.schooluniformsama.data.player.PlayerData;

public class SleepTask implements Runnable{

	@Override
	public void run() {
		for(PlayerData pd:Data.playerData.values()){
			Player p = RealSurvival.getPlayer(pd.getUuid());
			
			if(p==null || pd==null || p.isDead())return;
			
			if(pd.getSleep().isHasSleep()){
				byte lightLevel = p.getWorld().getBlockAt(p.getLocation()).getLightLevel();
				if(lightLevel>10)
					pd.change(EffectType.SLEEP, Data.sleep[4]*0.2);
				else if(lightLevel>5)
					pd.change(EffectType.SLEEP, Data.sleep[4]*0.5);
				else
					pd.change(EffectType.SLEEP, Data.sleep[4]);
			}else{
				pd.change(EffectType.SLEEP, -Data.sleep[3]);
				//TODO 
/*				if(pd.getSleep().getInfo().equals("very-tired")){
					
				}*/
			}
		}
	}
}
