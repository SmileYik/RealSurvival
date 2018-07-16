package com.outlook.schooluniformsama.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.outlook.schooluniformsama.I18n;
import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.player.PlayerData;
import com.outlook.schooluniformsama.util.Msg;

public class FractureEvent implements Listener{
	@EventHandler
	public void fall(EntityDamageEvent e){
		if(!(e.getEntity() instanceof Player))return;
		Player p=(Player) e.getEntity();
		if(!Data.playerData.containsKey(p.getUniqueId()))return;
		if(p.isDead())return;
		PlayerData pd = Data.playerData.get(p.getUniqueId());
		
		if(e.getCause()==DamageCause.FALL){
			int fall=(int) e.getDamage();
			//Severe
			if(fall>=Data.fracture[2]){
				if(pd.addIllness("Severe",Data.fracture[3],"Slight")){
					Msg.send(p, "messages.illness.fracture",I18n.tr("state9"));
					return;
				}
			}
			//Slight
			if(fall>=Data.fracture[0]){
				if(pd.getIllness().containsKey(I18n.tr("state9"))){
					if(pd.addIllness("Severe",Data.fracture[1],"Slight")){
						Msg.send(p, "messages.illness.fracture",I18n.tr("state9"));
						return;
					}
				}else if(pd.addIllness("Slight",Data.fracture[1],null)){
					Msg.send(p, "messages.illness.fracture",I18n.tr("state10"));
						return;
				}
			}
		}else if(e.getCause()==DamageCause.FALLING_BLOCK&&e.getDamage()>=Data.fracture[6]){
			
			if(pd.getIllness().containsKey(I18n.tr("state9"))){
				if(pd.addIllness("Severe",Data.fracture[4],"Slight")){
					Msg.send(p, "messages.illness.fracture",I18n.tr("state9"));
					return;
				}
			}else if(pd.addIllness(I18n.tr("state10"),Data.fracture[4],null)){
				if(pd.addIllness("Severe",Data.fracture[5],"Slight")){
					Msg.send(p, "messages.illness.fracture",I18n.tr("state9"));
					return;
				}
				Msg.send(p, "messages.illness.fracture",I18n.tr("state10"));
				return;
			}
		}
	}
}
