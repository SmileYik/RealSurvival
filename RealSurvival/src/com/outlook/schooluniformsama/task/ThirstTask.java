package com.outlook.schooluniformsama.task;

import org.bukkit.entity.Player;

import com.outlook.schooluniformsama.RealSurvival;
import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.player.EffectType;
import com.outlook.schooluniformsama.data.player.PlayerData;

public class ThirstTask implements Runnable{
	@Override
	public void run() {
		for(PlayerData pd:Data.playerData.values()){
			Player p = RealSurvival.getPlayer(pd.getUuid());
			if(p==null || pd==null || p.isDead())return;
			pd.change(EffectType.THIRST, -Data.thirst[3]);
		}
	}
	
}
