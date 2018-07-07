package com.outlook.schooluniformsama.task;

import org.bukkit.entity.Player;

import com.outlook.schooluniformsama.RealSurvival;
import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.effect.*;
import com.outlook.schooluniformsama.data.player.PlayerData;
import com.outlook.schooluniformsama.util.HashMap;
/*
 * #SPEED(速度提升),SLOW(缓慢),FAST_DIGGING（急迫）,SLOW_DIGGING(挖掘疲劳),INCREASE_DAMAGE(伤害提升)
# HEAL(瞬间治疗),HARM(瞬间伤害),JUMP(跳跃提升),CONFUSION(反胃),REGENERATION（生命恢复）
# DAMAGE_RESISTANCE（抗性）,FIRE_RESISTANCE(防火),WATER_BREATHING（水下呼吸）,INVISIBILITY（隐身）
# BLINDNESS(失明)，NIGHT_VISION（夜视）,HUNGER（饥饿），WEAKNESS（虚弱）,POISON（中毒）
# WITHER（凋零）,HEALTH_BOOST（生命提升）,ABSORPTION(伤害吸收),SATURATION(饱和)
 */
/*
 * TPYE = SLEEP,THIRST,TEMPERATURE,ENERGY,WEIGHT,IMMUNE
 * TYPE,(-)LEVEL,TIME
 */
public class EffectTask implements Runnable{
	
	public class EffectData{
		private boolean percentage;
		private double amplifier;
		private int replaceLevel;
		private EffectData(boolean percentage, double amplifier,int replaceLevel) {
			this.percentage = percentage;
			this.amplifier = amplifier;
			this.replaceLevel=replaceLevel;
		}
		public boolean isPercentage() {
			return percentage;
		}
		public double getAmplifier() {
			return amplifier;
		}
		
	}
	
	private RealSurvival plugin;
	private static HashMap<String,HashMap<EffectType,EffectData>> playerEffect = new HashMap<>();
	private static HashMap<EffectData,Integer> manager = new HashMap<>();
	
	
	public EffectTask(RealSurvival p){
		this.plugin=p;
	}
	
	@Override
	public void run() {
		for(PlayerData pd:Data.playerData.values()){
			Player p = RealSurvival.getPlayer(pd.getUuid());
			if(p==null || pd==null || p.isDead())return;
			
			addEffect(pd.getSleep().getState(),p,pd);
			addEffect(pd.getTemperature().errorTemperature(), p,pd);
			addEffect(pd.getEnergy().getState(), p,pd);
			addEffect(pd.getThirst().getState(), p,pd);
			addEffect(pd.getWeight().isOverWeight(), p,pd);
			
			for(String illness:pd.getIllness().keySet())
				addEffect(illness, p, pd);
		}
	}
	
	private void addEffect(Player p,PlayerData pd,Effect effect){
		if(effect.isPotion()){ p.addPotionEffect(effect.getEffect(),true); return; }
		if(playerEffect.containsKey(p.getName())){
			HashMap<EffectType, EffectData> effects = playerEffect.get(p.getName());
			if(effects.containsKey(effect.getType2())&&effect.getReplaceLevel()>effects.get(effect.getType2()).replaceLevel){
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
	
	private void registeEffect(String playerName,EffectData ed,EffectType et,int duration){
		manager.put(ed, plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				if(playerEffect.containsKey(playerName) && playerEffect.get(playerName).containsKey(et))
					playerEffect.get(playerName).remove(et);
				if(playerEffect.containsKey(playerName) && playerEffect.get(playerName).isEmpty()) playerEffect.remove(playerName);
			}
		}, duration).getTaskId());
	}
	
	private void cancelEffect(EffectData ed){
		if(manager.containsKey(ed))plugin.getServer().getScheduler().cancelTask(manager.get(ed));
	}
	
	public static EffectData getEffect(Player p,EffectType type){
		if(playerEffect.containsKey(p.getName()) && playerEffect.get(p.getName()).containsKey(type))
			return playerEffect.get(p.getName()).get(type);
		return null;
	}
	
	private void addEffect(String name,Player p,PlayerData pd){
		if(name==null || !Data.illnessEffects.containsKey(name))return;
		for(Effect effect:Data.illnessEffects.get(name))
			addEffect(p, pd, effect);
	}
}
