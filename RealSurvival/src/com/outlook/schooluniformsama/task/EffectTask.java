package com.outlook.schooluniformsama.task;

import org.bukkit.entity.Player;

import com.outlook.schooluniformsama.RealSurvival;
import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.effect.Effect;
import com.outlook.schooluniformsama.data.player.PlayerData;
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
	private RealSurvival plugin;
	
	public EffectTask(RealSurvival p){
		this.plugin=p;
	}
	
	@Override
	public void run() {
		for(PlayerData pd:Data.playerData.values()){
			Player p = RealSurvival.getPlayer(pd.getUuid());
			if(p==null || pd==null || p.isDead())return;
			addEffect(pd.getSleep().errorSleep(),p,pd);
			addEffect(pd.getTemperature().errorTemperature(), p,pd);
			addEffect(pd.getEnergy().errorEnergy(), p,pd);
			addEffect(pd.getThirst().errorThirst(), p,pd);
			addEffect(pd.getWeight().isOverWeight(), p,pd);
			for(String illness:pd.getIllness().getIllness().keySet())
				for(Effect effect:Data.illnessEffects.get(illness))
					if(effect.isPotion()){
						p.addPotionEffect(effect.getEffect());
						continue;
					}else{
						pd.changeEffect(effect.getType2(), effect.getAmplifier());
						plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
							@Override
							public void run() {
								
							}
						}, effect.getDuration()/20);
					}
		}
	}
	
	private void addEffect(String name,Player p,PlayerData pd){
		if(name==null)return;
		for(Effect effect:Data.illnessEffects.get(name)){
			if(effect.isPotion()){
				p.addPotionEffect(effect.getEffect());
				continue;
			}else{
				pd.changeEffect(effect.getType2(), effect.getAmplifier());
				plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
					@Override
					public void run() {
						
					}
				}, effect.getDuration()/20);
			}
		}
	}
}
