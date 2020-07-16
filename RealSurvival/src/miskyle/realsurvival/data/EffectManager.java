package miskyle.realsurvival.data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import com.github.miskyle.mcpt.MCPT;

import miskyle.realsurvival.api.effect.RSEffect;
import miskyle.realsurvival.data.effect.EffectData;
import miskyle.realsurvival.data.effect.MinecraftPotionEffect;
import miskyle.realsurvival.util.RSClassLoader;

public class EffectManager {
	private static EffectManager em;
	private Method methondEffect; 
	
	private HashMap<String, Object> effects;
	
	public EffectManager() {
		em = this;
		setupRSEffect();
	}
	
	public void setupRSEffect() {
		effects = new HashMap<String, Object>();
		try {
			methondEffect = RSEffect.class.getMethod("effect", Player.class,int.class,int.class);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		new RSClassLoader(MCPT.plugin.getDataFolder()+"/effect/")
		.loadAllClass(RSEffect.class).forEach((k,v)->{
			try {
				Object effect = v.getConstructor(String.class).newInstance(k);
				effects.put(k, effect);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		MCPT.plugin.getLogger().info("Load RSEffect: "+effects.size());
	}
	
	
	
	public static void effectPlayer(Player player,String effectName,int duration,int effectLevel) {
			if(em.effects.containsKey(effectName)) {
				try {
					em.methondEffect.invoke(em.effects.get(effectName), player,effectLevel,duration);
				} catch (IllegalAccessException | IllegalArgumentException 
						| InvocationTargetException | SecurityException e) {
					e.printStackTrace();
				} 
				
			}else if(PotionEffectType.getByName(effectName)!=null) {
				MCPT.plugin.getServer().getScheduler().runTask(MCPT.plugin, ()->{
					new MinecraftPotionEffect(effectName).effect(player,duration, effectLevel);
				});
			}			
	}
	
	public static void effectPlayer(Player player,EffectData effect) {
		effectPlayer(player, effect.getName(), effect.getDuration(), effect.getEffecLevel());
	}
}
