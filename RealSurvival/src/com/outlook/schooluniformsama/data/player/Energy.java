package com.outlook.schooluniformsama.data.player;

import com.outlook.schooluniformsama.data.Data;

public class Energy {
	private double energy;
	private double energyMax;
	private double other;
	private double oldEnergy;
	
	public Energy(double energy , double energyMax) {
		super();
		this.energy = energy;
		this.energyMax = energyMax;
	}
	
	public double getEnergyMax(){
		return energyMax+other;
	}
	
	public String getState(){
		if(energy<getEnergyMax()*Data.energy[1])
			return "EnergyMin";
		return null;
	}
	
	public String getInfo(){
		if(energy <= getEnergyMax()*Data.energy[1] && oldEnergy> getEnergyMax()*Data.energy[1]) return "no-energy";
		if(oldEnergy < getEnergyMax()*0.8 && energy > getEnergyMax()*0.8) return "energy-fullly";
		return null;
	}
	
	public void change(double num){
		oldEnergy = energy;
		energy+=num;
		if(energy<0)
			energy=0;
		else if(energy>getEnergyMax()) 
			energy=getEnergyMax();
	}

	public void setEnergy(double energy) {
		this.energy = energy;
	}
	
	public double getEnergy(){
		return energy;
	}
}
