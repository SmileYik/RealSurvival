package miskyle.realsurvival.data.config.status;

import java.util.ArrayList;
import java.util.HashMap;

import miskyle.realsurvival.data.effect.EffectData;

public class WeightConfig {
	private boolean enable;
	private double  maxValue;
	private ArrayList<EffectData>  effects;
	
	private HashMap<String, Double> itemWeight;
	
	public WeightConfig() {
		
	}
	
	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public ArrayList<EffectData> getEffects() {
		return effects;
	}

	public void setEffects(String effectString) {
		effects = new ArrayList<EffectData>();
		if(effectString.equalsIgnoreCase("null"))return;
		for(String s:effectString.split(";"))
			effects.add(EffectData.loadFromString(s));
	}

	public HashMap<String, Double> getItemWeight() {
		return itemWeight;
	}

	public void setItemWeight(HashMap<String, Double> itemWeight) {
		this.itemWeight = itemWeight;
	}
	
	
}
