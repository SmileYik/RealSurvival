package miskyle.realsurvival.data.config;

import java.util.HashMap;

public class WeightConfig {
	private boolean enable;
	private double  maxValue;
	private String  effectString;
	
	private HashMap<String, Double> itemWeight;
	
	public WeightConfig() {
		
	}

	public WeightConfig(boolean enable, double maxValue, String effectString, HashMap<String, Double> itemWeight) {
		super();
		this.enable = enable;
		this.maxValue = maxValue;
		this.effectString = effectString;
		this.itemWeight = itemWeight;
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

	public String getEffectString() {
		return effectString;
	}

	public void setEffectString(String effectString) {
		this.effectString = effectString;
	}

	public HashMap<String, Double> getItemWeight() {
		return itemWeight;
	}

	public void setItemWeight(HashMap<String, Double> itemWeight) {
		this.itemWeight = itemWeight;
	}
	
	
}
