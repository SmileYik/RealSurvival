package com.outlook.schooluniformsama.data.player;

import com.outlook.schooluniformsama.data.Data;

public class Energy {
	private double energy;
	private double energyBuff=1;
	private int energyLevel=0;
	private double addEnergy;
	private float effect;
	
	public Energy(double energy, double energyBuff, int energyLevel, double addEnergy) {
		super();
		this.energy = energy;
		this.energyBuff = energyBuff;
		this.energyLevel = energyLevel;
		this.addEnergy = addEnergy;
	}
	
	public void changeEffect(float f){
		effect+=f;
	}
	
	public double getMaxEnergy(){
		return Data.energy[0]+addEnergy;
	}
	
	public String errorEnergy(){
		if(energy<Data.energy[1])
			return "EnergyMin";
		return null;
	}
	
	public void levelUp(int i){
		energyLevel+=i;
		double sum = 0;
		for(int j=1;j<=energyLevel;j++){
			sum+=Math.log(j);
		}
		addEnergy=Math.sqrt(sum);
	}
	
	public String change(double num){
		num*=energyBuff;
		num+=num*effect;
		energy+=num;
		if(energy<0)
			energy=0;
		else if(energy>getMaxEnergy())
			energy=getMaxEnergy();
		
		if(energy<=Data.energy[1]&&(energy-num)>Data.energy[1])
			return "no-energy";
		else if(energy>=getMaxEnergy()*0.9&&(energy-num)<getMaxEnergy()*0.9)
			return "energt-fullly";
		return null;
	}
	
	private void reCheckLevel(){
		double sum = 0;
		for(int j=1;j<=energyLevel;j++){
			sum+=Math.log(j);
		}
		addEnergy=Math.sqrt(sum);
	}

	public double getEnergyBuff() {
		return energyBuff;
	}

	public void setEnergyBuff(double energyBuff) {
		this.energyBuff = energyBuff;
	}

	public int getEnergyLevel() {
		return energyLevel;
	}

	public void setEnergyLevel(int energyLevel) {
		this.energyLevel = energyLevel;
		reCheckLevel();
	}

	public double getAddEnergy() {
		return addEnergy;
	}

	public void setAddEnergy(double addEnergy) {
		this.addEnergy = addEnergy;
	}

	public void setEnergy(double energy) {
		this.energy = energy;
	}
	
	public double getEnergy(){
		return energy;
	}
}
