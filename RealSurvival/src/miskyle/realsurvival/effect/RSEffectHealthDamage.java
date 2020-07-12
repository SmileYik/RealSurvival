package miskyle.realsurvival.effect;

import java.util.HashMap;

import org.bukkit.entity.Player;

import miskyle.realsurvival.data.effect.RSEffect;

public class RSEffectHealthDamage extends RSEffect{
	
	private HashMap<Integer, Double> healthValue;
	
	public RSEffectHealthDamage() {
		healthValue = new HashMap<Integer, Double>();
		for(int i=0;i<10;i++)
			healthValue.put(i, (double)i);
	}
	
	@Override
	public void effect(Player player) {
		player.damage(healthValue.get(super.getEffectLevel()), player);
	}
	
}
