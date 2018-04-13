package com.outlook.schooluniformsama.event;

import java.util.Map;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.player.PlayerData;
import com.outlook.schooluniformsama.util.Msg;

public class DamageEvent implements Listener{
	@EventHandler
	public void PlayerIsHurt(EntityDamageByEntityEvent e){
		if(!(e.getEntity() instanceof Player))return;
		if(!Data.playerData.containsKey(e.getEntity().getUniqueId()))return;
		PlayerData pd = Data.playerData.get(e.getEntity().getUniqueId());
		
		if(e.getDamager() instanceof LivingEntity && Data.mobEffect.containsKey(e.getDamager().getType().name())){
			for(Map.Entry<String, Double> entity:Data.mobEffect.get(e.getDamager().getType().name()).getIllness().entrySet()){
				if(pd.getIllness().addIllness(entity.getKey(),entity.getValue(),null))
					Msg.sendRandomTitleToPlayer((Player)e.getEntity(),"HitByMob", new String[]{"%sick%"}, new String[]{entity.getKey()}, true);
			}
		}
		
	}
}
