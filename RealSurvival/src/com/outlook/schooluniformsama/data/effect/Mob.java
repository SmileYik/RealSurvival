package com.outlook.schooluniformsama.data.effect;

import java.util.HashMap;

public class Mob {
	private String name;
	private HashMap<String, Double> illness = new HashMap<>();
	public Mob(String name, HashMap<String, Double> illness) {
		super();
		this.name = name;
		this.illness = illness;
	}
	public Mob(String name, String illnesses) {
		super();
		this.name = name;
		for(String str:illnesses.split(";"))
			illness.put(str.split(",")[0], Double.parseDouble(str.split(",")[1].replaceAll("%", "")));
	}
	public String getName() {
		return name;
	}
	public HashMap<String, Double> getIllness() {
		return illness;
	}
	
}
