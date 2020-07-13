package miskyle.realsurvival.data.config;

import java.util.ArrayList;
import java.util.HashMap;

import miskyle.realsurvival.data.effect.EffectData;
import miskyle.realsurvival.util.RSEntry;

public class SleepConfig {
	private boolean enable;
	private boolean sleepInDay;
	private double  maxValue;
	private double  decreaseValue;
	private double  increaseValue;
	
	private HashMap<RSEntry<Double, Double>, ArrayList<EffectData>> effectData;
	
	public SleepConfig() {
		
	}
	
	public SleepConfig(boolean enable, boolean sleepInDay, double maxValue, double decreaseValue, double increaseValue,
			HashMap<String, String> effectData) {
		super();
		this.enable = enable;
		this.sleepInDay = sleepInDay;
		this.maxValue = maxValue;
		this.decreaseValue = decreaseValue;
		this.increaseValue = increaseValue;
		this.effectData = new HashMap<RSEntry<Double,Double>, ArrayList<EffectData>>();
		effectData.forEach((s1,s2)->{
			String[] temp = s1.split("-");
			String[] temp2 = s2.split(";");
			ArrayList<EffectData> list = new ArrayList<>();
			for(String s:temp2)
				list.add(EffectData.loadFromString(s));
			this.effectData.put(
					new RSEntry<Double, Double>(
							Double.parseDouble(temp[0]), Double.parseDouble(temp[1])), list);
		});
	}
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public boolean isSleepInDay() {
		return sleepInDay;
	}
	public void setSleepInDay(boolean sleepInDay) {
		this.sleepInDay = sleepInDay;
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
	public double getIncreaseValue() {
		return increaseValue;
	}
	public void setIncreaseValue(double increaseValue) {
		this.increaseValue = increaseValue;
	}
	public HashMap<RSEntry<Double, Double>, ArrayList<EffectData>> getEffectData() {
		return effectData;
	}

	public void setEffectData(HashMap<String, String> effectData) {
		this.effectData = new HashMap<RSEntry<Double,Double>, ArrayList<EffectData>>();
		effectData.forEach((s1,s2)->{
			String[] temp = s1.split("-");
			String[] temp2 = s2.split(";");
			ArrayList<EffectData> list = new ArrayList<>();
			for(String s:temp2)
				list.add(EffectData.loadFromString(s));
			this.effectData.put(
					new RSEntry<Double, Double>(
							Double.parseDouble(temp[0]), Double.parseDouble(temp[1])), list);
		});
	}
	
	
	
}
