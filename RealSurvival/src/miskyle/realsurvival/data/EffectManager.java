package miskyle.realsurvival.data;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import com.github.miskyle.mcpt.MCPT;

import miskyle.realsurvival.data.effect.EffectData;
import miskyle.realsurvival.data.effect.MinecraftPotionEffect;
import miskyle.realsurvival.data.effect.RSEffect;
import miskyle.realsurvival.util.RSClassLoader;

public class EffectManager {
	private static EffectManager em;
	
	private HashMap<String, Class<?>> clazzes;
	
	public EffectManager() {
		RSClassLoader loader = new RSClassLoader(
				MCPT.plugin.getDataFolder()+"/effect/");
		clazzes = loader.loadAllClass(RSEffect.class);
	}
	
	
	
	public static void effectPlayer(Player player,
			String effectName,int duration,int effectLevel,boolean immediate) {
		if(em.clazzes.containsKey(effectName)) {
			try {
				Class<?> clazz = em.clazzes.get(effectName);
				Object o = clazz.getConstructor(int.class,int.class,boolean.class)
						.newInstance(duration,effectLevel,immediate);
				clazz.getMethod("effect", Player.class).invoke(o,player);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException 
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			} 
			
		}else if(PotionEffectType.getByName(effectName)!=null) {
			new MinecraftPotionEffect(effectName, duration, effectLevel).effect(player);
		}
	}
	
	public static void effectPlayer(Player player,EffectData effect) {
		effectPlayer(player, effect.getName(), effect.getDuration(), effect.getEffecLevel(), effect.isImmediate());
	}
}
