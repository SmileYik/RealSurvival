package com.outlook.schooluniformsama.task;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerBedEnterEvent;

import com.outlook.schooluniformsama.I18n;
import com.outlook.schooluniformsama.RealSurvival;
import com.outlook.schooluniformsama.api.data.EffectType;
import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.player.PlayerData;
import com.outlook.schooluniformsama.event.SleepEvent;
import com.outlook.schooluniformsama.util.Util;

public class SleepTask implements Runnable{
//l.getBlock().setType(Material.BED_BLOCK);
	@Override
	public void run() {
		for(PlayerData pd:Data.playerData.values()){
			Player p = RealSurvival.getPlayer(pd.getUuid());
			
			if(p==null || pd==null || p.isDead())return;
			
			if(pd.getSleep().isHasSleep()){
				byte lightLevel = p.getWorld().getBlockAt(p.getLocation()).getLightLevel();
				if(lightLevel>10)
					pd.change(EffectType.SLEEP, Util.randomNum(Data.sleep[4]*0.1, Data.sleep[4]*0.25));
				else if(lightLevel>5)
					pd.change(EffectType.SLEEP, Util.randomNum(Data.sleep[4]*0.35, Data.sleep[4]*0.55));
				else
					pd.change(EffectType.SLEEP, Util.randomNum(Data.sleep[4]*0.8, Data.sleep[4]));
			}else{
				pd.change(EffectType.SLEEP, -Util.randomNum(Data.sleep[3]*0.9, Data.sleep[3]*1.1));	
				
				if(SleepEvent.sleepDuringDay && pd.getSleep().getSleep()<=1){
					Location l = p.getLocation();
					Data.bed.sleep(p, l);
					p.sendMessage(I18n.tr("sleep1"));
					Bukkit.getPluginManager().callEvent(new PlayerBedEnterEvent(p, l.getBlock()));
				}
			}
		}
	}
}
