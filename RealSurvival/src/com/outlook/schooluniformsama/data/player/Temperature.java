package com.outlook.schooluniformsama.data.player;

import com.outlook.schooluniformsama.data.Data;

public class Temperature {
	private double temperature;
	private double oldTemperature;
	
	public Temperature(double temperature) {
		super();
		this.temperature = temperature;
	}
	
	public double getTemperature(){
		return temperature;
	}
	
	public String change(double num){
		oldTemperature = temperature;
		temperature = num;
		if(temperature<0)
			temperature=0;
		
		if(oldTemperature>=(Data.temperature[5])&&temperature<(Data.temperature[5]))
			return "cold";
		else if(oldTemperature<=(Data.temperature[6])&&temperature>(Data.temperature[6]))
			return  "hot";
		return null;
	}
	
	public String errorTemperature(){
		if(temperature>Data.temperature[6])
			return "Fever";
		else if(temperature<Data.temperature[5])
			return "Cold";
		return null;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public double getOldTemperature() {
		return oldTemperature;
	}

	public void setOldTemperature(double oldTemperature) {
		this.oldTemperature = oldTemperature;
	}
}
