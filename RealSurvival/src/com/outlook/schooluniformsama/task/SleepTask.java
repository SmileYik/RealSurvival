package com.outlook.schooluniformsama.task;

import org.bukkit.entity.Player;

import com.outlook.schooluniformsama.RealSurvival;
import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.player.PlayerData;
import com.outlook.schooluniformsama.util.Msg;

public class SleepTask implements Runnable{

	@Override
	public void run() {
		for(PlayerData pd:Data.playerData.values()){
			Player p = RealSurvival.getPlayer(pd.getUuid());
			
			if(p==null || pd==null || p.isDead())return;
			
			if(pd.getSleep().isHasSleep()){
				byte lightLevel = p.getWorld().getBlockAt(p.getLocation()).getLightLevel();
				if(lightLevel>10)
					Msg.sendTitleToPlayer(p, pd.getSleep().change(Data.sleep[4]*0.2),true);
				else if(lightLevel>5)
					Msg.sendTitleToPlayer(p, pd.getSleep().change(Data.sleep[4]*0.5),true);
				else
					Msg.sendTitleToPlayer(p, pd.getSleep().change(Data.sleep[4]),true);
			}else
				Msg.sendTitleToPlayer(p, pd.getSleep().change(-Data.sleep[3]),true);
		}
	}
}
