package com.outlook.z815007320.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.outlook.z815007320.data.PlayerData;
import com.outlook.z815007320.data.PluginRS;
import com.outlook.z815007320.utils.Utils;

public class FractureEvent extends PluginRS implements Listener{
	@EventHandler
	public void fall(EntityDamageEvent e){
		if(!(e.getEntity() instanceof Player))return;
		Player p=(Player) e.getEntity();
		if(rs.getPlayerData(p)==null)return;
		if(p.isDead())return;
		if(e.getCause()==DamageCause.FALL){
			int fall=(int) e.getDamage();
			//ÑÏÖØ¹ÇÕÛ
			if(fall>=rs.getFracture()[2]){
				if(Math.random()*100<=rs.getFracture()[3]){
					PlayerData pd=rs.getPlayerData(p);
					pd.setSick(true);
					if(pd.getSickKindList().contains("¹ÇÕÛ"))
						pd.removeSickKind("¹ÇÕÛ");
					pd.addSickKind("ÑÏÖØ¹ÇÕÛ");
					Utils.sendMsgToPlayer(pd, "Fracture");
					return;
				}
			}
			//ÆÕÍ¨¹ÇÕÛ
			if(fall>=rs.getFracture()[0]){
				if(Math.random()*100<=rs.getFracture()[1]){
					PlayerData pd=rs.getPlayerData(p);
					pd.setSick(true);
					if(!pd.getSickKindList().contains("ÑÏÖØ¹ÇÕÛ"))
						pd.addSickKind("¹ÇÕÛ");
					Utils.sendMsgToPlayer(pd, "Fracture");
					return;
				}
			}
		}else if(e.getCause()==DamageCause.FALLING_BLOCK&&e.getDamage()>=rs.getFracture()[6]){
			if(Math.random()*100<=rs.getFracture()[4]){
				PlayerData pd=rs.getPlayerData(p);
				pd.setSick(true);
				if(Math.random()*100<=rs.getFracture()[5]){
					if(pd.getSickKindList().contains("¹ÇÕÛ"))
						pd.removeSickKind("¹ÇÕÛ");
					pd.addSickKind("ÑÏÖØ¹ÇÕÛ");
				}
				else
					if(!pd.getSickKindList().contains("ÑÏÖØ¹ÇÕÛ"))
						pd.addSickKind("¹ÇÕÛ");
				Utils.sendMsgToPlayer(pd, "HitByFallingBlock");
				return;
			}
		}
		
	}
}
