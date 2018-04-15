package com.outlook.schooluniformsama.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

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
				if(pd.getIllness().addIllness("Severe",Data.fracture[3],Msg.getMsg("Slight", false))){
					Msg.sendRandomTitleToPlayer(p, "Fracture", new String[]{"%sick%"}, new String[]{Msg.getMsg("Severe", false)}, Data.enablePrefixInTitle);
					return;
				}
			}
			//Slight
			if(fall>=Data.fracture[0]){
				if(pd.getIllness().getIllness().containsKey(Msg.getMsg("Severe", false))){
					if(pd.getIllness().addIllness("Severe",Data.fracture[1],Msg.getMsg("Slight", false))){
						Msg.sendRandomTitleToPlayer(p, "Fracture", new String[]{"%sick%"}, new String[]{Msg.getMsg("Severe", false)}, Data.enablePrefixInTitle);
						return;
					}
				}else if(pd.getIllness().addIllness("Slight",Data.fracture[1],null)){
						Msg.sendRandomTitleToPlayer(p, "Fracture", new String[]{"%sick%"}, new String[]{Msg.getMsg("Slight", false)}, Data.enablePrefixInTitle);
						return;
				}
			}
		}else if(e.getCause()==DamageCause.FALLING_BLOCK&&e.getDamage()>=Data.fracture[6]){
			
			if(pd.getIllness().getIllness().containsKey(Msg.getMsg("Severe", false))){
				if(pd.getIllness().addIllness("Severe",Data.fracture[4],Msg.getMsg("Slight", false))){
					Msg.sendRandomTitleToPlayer(p, "Fracture", new String[]{"%sick%"}, new String[]{Msg.getMsg("Severe", false)}, Data.enablePrefixInTitle);
					return;
				}
			}else if(pd.getIllness().addIllness(Msg.getMsg("Slight", false),Data.fracture[4],null)){
				if(pd.getIllness().addIllness("Severe",Data.fracture[5],Msg.getMsg("Slight", false))){
					Msg.sendRandomTitleToPlayer(p, "Fracture", new String[]{"%sick%"}, new String[]{Msg.getMsg("Severe", false)}, Data.enablePrefixInTitle);
					return;
				}
				Msg.sendRandomTitleToPlayer(p, "Fracture", new String[]{"%sick%"}, new String[]{Msg.getMsg("Slight", false)}, Data.enablePrefixInTitle);
				return;
			}
		}
	}
}
