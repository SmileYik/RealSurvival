package miskyle.realsurvival.data.config;

import java.util.HashMap;

import miskyle.realsurvival.util.RSEntry;

public class ThirstConfig {
	private boolean enable;
	private double  maxValue;
	private double  decreaseValue;
	
	private HashMap<RSEntry<Double, Double>, String> effectData;
	
	public ThirstConfig() {
		
	}
	
	public ThirstConfig(boolean enable, double maxValue, double decreaseValue, HashMap<String, String> effectData) {
		super();
		this.enable = enable;
		this.maxValue = maxValue;
		this.decreaseValue = decreaseValue;
		this.effectData = new HashMap<RSEntry<Double,Double>, String>();
		effectData.forEach((s1,s2)->{
			String[] temp = s1.split("-");
			this.effectData.put(
					new RSEntry<Double, Double>(
							Double.parseDouble(temp[0]), Double.parseDouble(temp[1])), s2);
		});
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

	public double getDecreaseValue() {
		return decreaseValue;
	}

	public void setDecreaseValue(double decreaseValue) {
		this.decreaseValue = decreaseValue;
	}

	public HashMap<RSEntry<Double, Double>, String> getEffectData() {
		return effectData;
	}

	public void setEffectData(HashMap<String, String> effectData) {
		this.effectData = new HashMap<RSEntry<Double,Double>, String>();
		effectData.forEach((s1,s2)->{
			String[] temp = s1.split("-");
			this.effectData.put(
					new RSEntry<Double, Double>(
							Double.parseDouble(temp[0]), Double.parseDouble(temp[1])), s2);
		});
	}
	
	
}
