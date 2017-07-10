package com.outlook.z815007320.task;

import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.outlook.z815007320.commands.TestCMD;
import com.outlook.z815007320.data.PlayerData;
import com.outlook.z815007320.data.PluginRS;
import com.outlook.z815007320.utils.Utils;
/*
 * #SPEED(速度提升),SLOW(缓慢),FAST_DIGGING（急迫）,SLOW_DIGGING(挖掘疲劳),INCREASE_DAMAGE(伤害提升)
# HEAL(瞬间治疗),HARM(瞬间伤害),JUMP(跳跃提升),CONFUSION(反胃),REGENERATION（生命恢复）
# DAMAGE_RESISTANCE（抗性）,FIRE_RESISTANCE(防火),WATER_BREATHING（水下呼吸）,INVISIBILITY（隐身）
# BLINDNESS(失明)，NIGHT_VISION（夜视）,HUNGER（饥饿），WEAKNESS（虚弱）,POISON（中毒）
# WITHER（凋零）,HEALTH_BOOST（生命提升）,ABSORPTION(伤害吸收),SATURATION(饱和)
 */
public class EffectTask extends PluginRS implements Runnable{
	private double sleep;
	private double thirst;
	private double temperature;
	@Override
	public void run() {
		if(!rs.isOver())return;
		rs.setOver(false);
		Set<UUID> players=rs.getPlayerDatas().keySet();
		for(UUID ps:players){
			Player p = rs.getServer().getPlayer(ps);
			if(p.isDead())continue;
		      if ((p.hasPermission("RealSurvival.unlimited")||p.hasPermission("RealSurvival.Admin"))&&!TestCMD.contains(p)) 
		    	  continue;
		      PlayerData pd=rs.getPlayerData(p);
		      //Sleep
		      sleep=pd.getSleep();
		      if(sleep>=rs.getSleepMax()*0.8)
		    	  Utils.addPotionEffect(p, rs.getEffects("SleepMax"));
		      else if(sleep<=rs.getSleepMin())
		    	  Utils.addPotionEffect(p,rs.getEffects("SleepMin"));
		      
		      //Thirst
		      thirst=pd.getThirst();
		      if(thirst>=rs.getThirstMax()*0.8)
		    	  Utils.addPotionEffect(p, rs.getEffects("ThirstMax"));
		      else if(thirst<=rs.getThirstMin())
		    	  Utils.addPotionEffect(p, rs.getEffects("ThirstMin"));
		      
		      //Temperature
		      temperature=pd.getTemperature();
		      if(temperature>38)
		    	  Utils.addPotionEffect(p, rs.getEffects("Fever"));
		      else if(temperature<36)
		    	  Utils.addPotionEffect(p, rs.getEffects("Cold"));
		      
		      //PhysicalStrength
		      if(pd.getPhysical_strength()==0)
		    	  Utils.addPotionEffect(p, rs.getEffects("PhysicalStrengthMin"));
		      
		      //Sick
		      if(pd.isSick())
		    	  for(String sick:pd.getSickKindList())
		    		  Utils.addPotionEffect(p, rs.getEffects(sick));
		      
		      
		      //weight
		      if(pd.getWeight()>rs.getWeight()){
		    	  Utils.addPotionEffect(p, rs.getEffects("Weight"));
		    	  p.setSprinting(false);
		      }
		}
		rs.setOver(true);
	}
	
}
