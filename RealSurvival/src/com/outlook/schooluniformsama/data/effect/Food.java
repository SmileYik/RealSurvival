package com.outlook.schooluniformsama.data.effect;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;

import com.outlook.schooluniformsama.data.Data;

public class Food {
	@Override
	public String toString() {
		return "Food [type=" + type + ", sleep=" + sleep + ", thirst=" + thirst + ", energy=" + energy
				+ ", temperature=" + temperature + ", illness=" + illness + ", hasIllness=" + hasIllness + "]";
	}

	private Material type;
	private double sleep;
	private double thirst;
	private double energy;
	private double temperature;
	private HashMap<String, Double> illness = new HashMap<>();
	private boolean hasIllness=false;
	
	public Food(Material type, String sleep, String thirst, String energy, double temperature,
			List<String> illness, boolean hasIllness) {
		super();
		this.type = type;
		
		if(sleep.contains("%"))
			this.sleep=Double.parseDouble(sleep.replaceAll("%", ""))/100.0*Data.sleep[0];
		else
			this.sleep=Double.parseDouble(sleep);
		
		if(thirst.contains("%"))
			this.thirst=Double.parseDouble(thirst.replaceAll("%", ""))/100.0*Data.thirst[0];
		else
			this.thirst=Double.parseDouble(thirst);
		
		if(energy.contains("%"))
			this.energy=Double.parseDouble(energy.replaceAll("%", ""))/100.0*Data.energy[0];
		else
			this.energy=Double.parseDouble(energy);
		
		this.temperature = temperature;
		if(hasIllness)
			for(String name:illness)
				this.illness.put(name.split(":")[0], Double.parseDouble(name.split(":")[1].replaceAll("%", "")));
		this.hasIllness = hasIllness;
	}

	public Material getType() {
		return type;
	}

	public void setType(Material type) {
		this.type = type;
	}

	public double getSleep() {
		return sleep;
	}

	public void setSleep(double sleep) {
		this.sleep = sleep;
	}

	public double getThirst() {
		return thirst;
	}

	public void setThirst(double thirst) {
		this.thirst = thirst;
	}

	public double getEnergy() {
		return energy;
	}

	public void setEnergy(double energy) {
		this.energy = energy;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public HashMap<String, Double> getIllness() {
		return illness;
	}

	public void setIllness(HashMap<String, Double> illness) {
		this.illness = illness;
	}

	public boolean isHasIllness() {
		return hasIllness;
	}

	public void setHasIllness(boolean hasIllness) {
		this.hasIllness = hasIllness;
	}
	
	
	
	
}
