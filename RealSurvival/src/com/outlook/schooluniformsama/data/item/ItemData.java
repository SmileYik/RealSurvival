package com.outlook.schooluniformsama.data.item;

public class ItemData {
	private double weight=0;
	private double heat=0;

	private ItemData(double weight, double heat) {
		this.weight = weight;
		this.heat = heat;
	}
	
	public static ItemData createData(double weight, double heat){
		return new ItemData(weight, heat);
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getHeat() {
		return heat;
	}

	public void setHeat(double heat) {
		this.heat = heat;
	}
}
