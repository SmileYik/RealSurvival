package com.outlook.schooluniformsama.data.effect;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Effect {
	private PotionEffectType type1;
	private EffectType type2;
	private int duration;
	private double amplifier;
	private boolean percentage =false;
	private int replaceLevel;
	
	public Effect(String effectString){
		String data[] = effectString.split(",");
		type1 = PotionEffectType.getByName(data[0]);
		type2 = EffectType.getByName(data[0]);
		duration = Integer.parseInt(data[1]);
		if(type2!=null){
			type1=null;
			if(data[2].contains("%")){
				amplifier = Double.parseDouble(data[2].replaceAll("[^0-9.+-]", ""))/100D;
				percentage = true;
			}else{
				amplifier = Double.parseDouble(data[2]);
				percentage = false;
			}
			replaceLevel = Integer.parseInt(data[3]);
		}else{
			type2=null;
			amplifier = Integer.parseInt(data[2]);
		}
	}
	
	public boolean isPotion(){
		return type1!=null;
	}
	
	public PotionEffect getEffect(){
		return new PotionEffect(type1, duration, (int)amplifier);
	}

	public EffectType getType2() {
		return type2;
	}

	public int getDuration() {
		return duration;
	}

	public double getAmplifier() {
		return amplifier;
	}
	
	public boolean isPercentage(){
		return percentage;
	}

	public int getReplaceLevel() {
		return replaceLevel;
	}
	
	
}
