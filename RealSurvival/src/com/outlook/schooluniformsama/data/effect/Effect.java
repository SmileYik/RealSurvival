package com.outlook.schooluniformsama.data.effect;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Effect {
	private PotionEffectType type1;
	private EffectType type2;
	private int duration;
	private int amplifier;
	public Effect(String type, int duration, int amplifier) {
		super();
		type1=PotionEffectType.getByName(type);
		try{
			type2=EffectType.valueOf(type);
		}catch (Exception e) {
			type2=null;
		}
		this.duration = duration;
		this.amplifier = amplifier;
	}
	
	public boolean isPotion(){
		return type1!=null;
	}
	
	public PotionEffect getEffect(){
		return new PotionEffect(type1, duration, amplifier);
	}

	public EffectType getType2() {
		return type2;
	}

	public int getDuration() {
		return duration;
	}

	public float getAmplifier() {
		if(type1==null)
			return amplifier/100.0F;
		return amplifier;
	}
	
	
}
