package com.outlook.schooluniformsama.task;

import java.util.*;

import org.bukkit.entity.Player;

import com.outlook.schooluniformsama.RealSurvival;
import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.effect.*;
import com.outlook.schooluniformsama.data.player.Illness;
import com.outlook.schooluniformsama.data.player.PlayerData;

/*
 * TPYE = SLEEP,THIRST,TEMPERATURE,ENERGY,WEIGHT,IMMUNE
 * TYPE,(-)LEVEL,TIME
 */
public class EffectTask implements Runnable{
	
	private static RealSurvival plugin;
	private static HashMap<String,HashMap<EffectType,EffectData>> playerEffect = new HashMap<>();
	private static HashMap<EffectData,Integer> manager = new HashMap<>();
	
	
	public EffectTask(RealSurvival p){
		plugin=p;
	}
	
	@Override
	public void run() {
		for(PlayerData pd:Data.playerData.values()){
			Player p = RealSurvival.getPlayer(pd.getUuid());
			if(p==null || pd==null || p.isDead())return;
			
			if(Data.switchs[2])addEffect(pd.getSleep().getState(),p);
			if(Data.switchs[3])addEffect(pd.getThirst().getState(), p);
			if(Data.switchs[4])addEffect(pd.getEnergy().getState(), p);
			if(Data.switchs[7])addEffect(pd.getWeight().isOverWeight(), p);
			if(Data.switchs[8])addEffect(pd.getTemperature().errorTemperature(), p);
			if(Data.switchs[6])
				for(Map.Entry<String, Illness> entry : pd.getIllness().entrySet())
					if(entry.getValue().isAddBuff()) addEffect(entry.getKey(), p);
		}
	}
	
	public static void addEffect(Player p,Effect effect){
		if(effect.isPotion()){ p.addPotionEffect(effect.getEffect(),true); return; }
		if(playerEffect.containsKey(p.getName())){
			HashMap<EffectType, EffectData> effects = playerEffect.get(p.getName());
			if(effects.containsKey(effect.getType2())&&effect.getReplaceLevel()>effects.get(effect.getType2()).getReplaceLevel()){
				cancelEffect(effects.get(effect.getType2()));
				EffectData ed = new EffectData(effect.isPercentage(), effect.getAmplifier(), effect.getReplaceLevel());
				effects.put(effect.getType2(), ed);
				registeEffect(p.getName(), ed, effect.getType2(), effect.getDuration());
			}else{
				EffectData ed = new EffectData(effect.isPercentage(), effect.getAmplifier(), effect.getReplaceLevel());
				effects.put(effect.getType2(), ed);
				registeEffect(p.getName(), ed, effect.getType2(), effect.getDuration());
			}
		}else{
			HashMap<EffectType,EffectData> effects = new HashMap<>();
			EffectData ed = new EffectData(effect.isPercentage(), effect.getAmplifier(), effect.getReplaceLevel());
			effects.put(effect.getType2(), ed);
			playerEffect.put(p.getName(), effects);
		}
	}
	
	private static void registeEffect(String playerName,EffectData ed,EffectType et,int duration){
		manager.put(ed, plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				if(playerEffect.containsKey(playerName) && playerEffect.get(playerName).containsKey(et))
					playerEffect.get(playerName).remove(et);
				if(playerEffect.containsKey(playerName) && playerEffect.get(playerName).isEmpty()) playerEffect.remove(playerName);
			}
		}, duration).getTaskId());
	}
	
	private static void cancelEffect(EffectData ed){
		if(manager.containsKey(ed))plugin.getServer().getScheduler().cancelTask(manager.get(ed));
	}
	
	public static EffectData getEffect(Player p,EffectType type){
		if(playerEffect.containsKey(p.getName()) && playerEffect.get(p.getName()).containsKey(type))
			return playerEffect.get(p.getName()).get(type);
		return null;
	}
	
	private void addEffect(String name,Player p){
		if(name==null || !Data.illnessEffects.containsKey(name))return;
		for(Effect effect:Data.illnessEffects.get(name))
			addEffect(p, effect);
	}
}
