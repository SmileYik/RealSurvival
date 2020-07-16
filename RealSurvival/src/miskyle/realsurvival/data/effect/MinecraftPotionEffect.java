package miskyle.realsurvival.data.effect;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import miskyle.realsurvival.api.effect.RSEffect;

public class MinecraftPotionEffect extends RSEffect{
	private PotionEffectType effectType;
	
	public MinecraftPotionEffect(PotionEffectType type) {
		super("MinecraftPotionEffect");
		this.effectType = type;
	}
	
	public MinecraftPotionEffect(String type) {
		super("MinecraftPotionEffect");
		this.effectType = PotionEffectType.getByName(type.toUpperCase());
	}
	
	@Override
	public void effect(Player player,int amplifier,int duration) {
		player.addPotionEffect(new PotionEffect(effectType, amplifier, duration));
	}

}
