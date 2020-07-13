package miskyle.realsurvival.data.effect;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MinecraftPotionEffect extends RSEffect{
	private PotionEffectType effectType;
	
	public MinecraftPotionEffect(PotionEffectType type,int duration,int amplifier) {
		super(duration,amplifier);
		this.effectType = type;
	}
	
	public MinecraftPotionEffect(String type,int duration,int amplifier) {
		super(duration,amplifier);
		this.effectType = PotionEffectType.getByName(type.toUpperCase());
	}
	
	@Override
	public void effect(Player player) {
		player.addPotionEffect(new PotionEffect(effectType, getDuration(), getEffectLevel()));
	}

}
