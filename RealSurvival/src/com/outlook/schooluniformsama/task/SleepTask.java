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
					Msg.sendTitleToPlayer(p, pd.getSleep().change(Data.sleep[4]*0.2),Data.enablePrefixInTitle);
				else if(lightLevel>5)
					Msg.sendTitleToPlayer(p, pd.getSleep().change(Data.sleep[4]*0.5),Data.enablePrefixInTitle);
				else
					Msg.sendTitleToPlayer(p, pd.getSleep().change(Data.sleep[4]),Data.enablePrefixInTitle);
			}else if(!Msg.sendTitleToPlayer(p, pd.getSleep().change(-Data.sleep[3]),Data.enablePrefixInTitle)){
				//TODO When Player was very tired
				
				/*Location loc = p.getLocation();
				loc.setY(loc.getY()+1);
				Material temp=loc.getBlock().getType();
				loc.getBlock().setType(Material.BED_BLOCK);
				Data.bnms.sleep(p, loc.getBlock().getLocation());
				Bukkit.getPluginManager().callEvent(new PlayerBedEnterEvent(p, null));
				loc.getBlock().setType(temp);*/
			}
		}
	}
}
