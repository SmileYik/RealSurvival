package com.outlook.schooluniformsama.data.player;

public class Weight {
	private double weight;
	private double weightMax;
	private double other;
	
	public Weight(double weight, double weightMax) {
		super();
		this.weight = weight;
		this.weightMax = weightMax;
	}
	
	public double getWeight(){
		return weight;
	}
	
	public String isOverWeight(){
		if(weight>getWeightMax())
			return "Weight";
		return null;
	}
	
	public boolean isOver(){
		return weight>getWeightMax();
	}

	public String setWeight(double weight) {
		if(this.weight<weight&&weight>getWeightMax()){
			this.weight = weight;
			return "weight-over";
		}
		this.weight = weight;
		return null;
	}
	
	public double getWeightMax(){
		return weightMax+other;
	}
}
