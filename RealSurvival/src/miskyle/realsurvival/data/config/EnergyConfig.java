package miskyle.realsurvival.data.config;

import java.util.ArrayList;
import java.util.HashMap;

import miskyle.realsurvival.data.effect.EffectData;
import miskyle.realsurvival.util.RSEntry;

public class EnergyConfig {
	private boolean enable;
	private double 	maxValue;
	private double 	increaseValue;
	private double 	decreaseSneaking;
	private double 	decreaseSprinting;
	private double 	decreaseJumping;
	private double 	decreaseSwimming;
	
	private HashMap<EnergyBreakBlockData,Double> 			actionDecrease;
	private HashMap<RSEntry<Double, Double>, ArrayList<EffectData>>		 effectData;
	
	public EnergyConfig() {
		
	}
	
	public EnergyConfig(boolean enable, double maxValue, double increaseValue, double decreaseSneaking,
			double decreaseSprinting, double decreaseJumping, double decreaseSwimming,
			HashMap<String, String> effectData, HashMap<EnergyBreakBlockData, Double> actionDecrease) {
		super();
		this.enable = enable;
		this.maxValue = maxValue;
		this.increaseValue = increaseValue;
		this.decreaseSneaking = decreaseSneaking;
		this.decreaseSprinting = decreaseSprinting;
		this.decreaseJumping = decreaseJumping;
		this.decreaseSwimming = decreaseSwimming;
		this.actionDecrease = actionDecrease;
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

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public double getIncreaseValue() {
		return increaseValue;
	}

	public void setIncreaseValue(double increaseValue) {
		this.increaseValue = increaseValue;
	}

	public double getDecreaseSneaking() {
		return decreaseSneaking;
	}

	public void setDecreaseSneaking(double decreaseSneaking) {
		this.decreaseSneaking = decreaseSneaking;
	}

	public double getDecreaseSprinting() {
		return decreaseSprinting;
	}

	public void setDecreaseSprinting(double decreaseSprinting) {
		this.decreaseSprinting = decreaseSprinting;
	}

	public double getDecreaseJumping() {
		return decreaseJumping;
	}

	public void setDecreaseJumping(double decreaseJumping) {
		this.decreaseJumping = decreaseJumping;
	}

	public double getDecreaseSwimming() {
		return decreaseSwimming;
	}

	public void setDecreaseSwimming(double decreaseSwimming) {
		this.decreaseSwimming = decreaseSwimming;
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

	public HashMap<EnergyBreakBlockData, Double> getActionDecrease() {
		return actionDecrease;
	}

	public void setActionDecrease(HashMap<EnergyBreakBlockData, Double> actionDecrease) {
		this.actionDecrease = actionDecrease;
	}
	
	
	
}
