package com.outlook.schooluniformsama.data.player;

import com.outlook.schooluniformsama.data.Data;

public class Weight {
	private double weight;
	private int weightLevel=0;
	private double addWeight;
	private float effect=0;
	
	public Weight(double weight, int weightLevel, double addWeight) {
		super();
		this.weight = weight;
		this.weightLevel = weightLevel;
		this.addWeight = addWeight;
	}
	
	public void changeEffect(float f){
		effect+=f;
	}
	
	public double getMaxWeight(){
		return Data.weight+addWeight;
	}
	
	public double getWeight(){
		return weight;
	}
	
	public String isOverWeight(){
		if(weight>getMaxWeight())
			return "Weight";
		return null;
	}
	
	public void levelUp(int i){
		weightLevel+=i;
		double sum = 0;
		for(int j=1;j<=weightLevel;j++){
			sum+=Math.log(j);
		}
		addWeight=Math.sqrt(sum);
	}
	
	private void reCheckLevel(){
		double sum = 0;
		for(int j=1;j<=weightLevel;j++){
			sum+=Math.log(j);
		}
		addWeight=Math.sqrt(sum);
	}

	public int getWeightLevel() {
		return weightLevel;
	}

	public void setWeightLevel(int weightLevel) {
		this.weightLevel = weightLevel;
		reCheckLevel();
	}

	public double getAddWeight() {
		return addWeight;
	}

	public void setAddWeight(double addWeight) {
		this.addWeight = addWeight;
	}

	public String setWeight(double weight) {
		weight+=weight*effect;
		if(this.weight<weight&&weight>getMaxWeight()){
			this.weight = weight;
			return "weight-over";
		}
		this.weight = weight;
		return null;
	}
}
