package com.outlook.schooluniformsama.data.effect;

public class EffectData{
	private boolean percentage;
	private double amplifier;
	private int replaceLevel;
	public EffectData(boolean percentage, double amplifier,int replaceLevel) {
		this.percentage = percentage;
		this.amplifier = amplifier;
		this.replaceLevel=replaceLevel;
	}
	public boolean isPercentage() {
		return percentage;
	}
	public double getAmplifier() {
		return amplifier;
	}
	public int getReplaceLevel() {
		return replaceLevel;
	}
	
}
