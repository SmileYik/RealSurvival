package com.outlook.z815007320.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.outlook.z815007320.data.PlayerData;
import com.outlook.z815007320.data.PluginRS;
import com.outlook.z815007320.utils.Utils;

public class DamageEvent extends PluginRS implements Listener{
	
	@EventHandler
	public void PlayerIsHurt(EntityDamageByEntityEvent e){
		if(!(e.getEntity() instanceof Player))return;
		if(rs.getPlayerData((Player)e.getEntity())==null)return;
		if(rs.mobContains(e.getDamager().getName())){
			for(Object[] obj:rs.getMob(e.getDamager().getName())){
				PlayerData pd=rs.getPlayerData((Player)e.getEntity());
				if(obj==null||pd.isSick())return;
				if(Math.random()*100<=(Double)obj[1]){
					pd.setSick(true);
					pd.addSickKind((String)obj[0]);
					Utils.sendMsgToPlayer(pd, "HitByMob");
				}
			}
		}else if(rs.mobContains(e.getDamager().getType().name())){
			for(Object[] obj:rs.getMob(e.getDamager().getType().name())){
				PlayerData pd=rs.getPlayerData((Player)e.getEntity());
				if(obj==null||pd.isSick())return;
				if(Math.random()*100<=(Double)obj[1]){
					pd.setSick(true);
					pd.addSickKind((String)obj[0]);
					Utils.sendMsgToPlayer(pd, "HitByMob");
				}
			}
		}
		
	}

}
