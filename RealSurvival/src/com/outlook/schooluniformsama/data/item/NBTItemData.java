package com.outlook.schooluniformsama.data.item;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.util.HashMap;

public class NBTItemData {
	private double thirst;
	private double sleep;
	private double energy;
	private double weight;
	private HashMap<String, Double> illness = new HashMap<>();
	private List<String> treatable;
	private double drugEffect;
	private double medicineDuration;
	private double temperature;
	private double hungery;
	public NBTItemData(double thirst, double sleep, double energy, double weight, HashMap<String, Double> illness,
			List<String> treatable, double drugEffect, double medicineDuration, double temperature, double hungery) {
		super();
		this.thirst = thirst;
		this.sleep = sleep;
		this.energy = energy;
		this.weight = weight;
		this.illness = illness;
		this.treatable = treatable;
		this.drugEffect = drugEffect;
		this.medicineDuration = medicineDuration;
		this.temperature = temperature;
		this.hungery = hungery;
	}
	
	public static String getNBTItemPath(String name){
		return Data.DATAFOLDER+"/nbtitem/"+name+".yml";
	}
	
	public static boolean isNBTItem(ItemStack is){
		if(Data.nbtitem.getNBTValue(is, "RealSurvival")==null)return false;
		return true;
	}
	
	public static NBTItemData load(ItemStack is){
		String fileName = Data.nbtitem.getNBTValue(is, "RealSurvival");
		if(fileName==null)return null;
		return load(fileName);
	}
	
	public static NBTItemData load(String fileName){
		YamlConfiguration nbtItem = YamlConfiguration.loadConfiguration(new File(getNBTItemPath(fileName)));
		HashMap<String, Double> illness = new HashMap<>();
		for(String str:nbtItem.getStringList("illness.list"))
			illness.put(str, nbtItem.getDouble("illness.illness."+str));
		return new NBTItemData(nbtItem.getDouble("thirst",ItemLoreData.badCode()), nbtItem.getDouble("sleep",ItemLoreData.badCode()),
				nbtItem.getDouble("energy",ItemLoreData.badCode()), nbtItem.getDouble("weight",ItemLoreData.badCode()), illness, nbtItem.getStringList("treatable"), 
				nbtItem.getDouble("de",ItemLoreData.badCode()), nbtItem.getDouble("md",ItemLoreData.badCode()), nbtItem.getDouble("temperature",ItemLoreData.badCode()), nbtItem.getDouble("hungery",ItemLoreData.badCode()));
	}
	
	public boolean save(String fileName){
		YamlConfiguration nbtItem = YamlConfiguration.loadConfiguration(new File(getNBTItemPath(fileName)));
		List<String> illnessList = new LinkedList<>();
		for(Entry<String,Double> entity:illness.entrySet()){
			illnessList.add(entity.getKey());
			nbtItem.set("illness.illness."+entity.getKey(), entity.getValue());
		}
		nbtItem.set("illness.list", illnessList);
		nbtItem.set("thirst", thirst);
		nbtItem.set("sleep", sleep);
		nbtItem.set("weight", weight);
		nbtItem.set("energy", energy);
		nbtItem.set("treatable", treatable);
		nbtItem.set("de", drugEffect);
		nbtItem.set("md", medicineDuration);
		nbtItem.set("temperature", temperature);
		nbtItem.set("hungery", hungery);
		try {
			nbtItem.save(new File(getNBTItemPath(fileName)));
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	
	public double getThirst() {
		return thirst;
	}
	public void setThirst(double thirst) {
		this.thirst = thirst;
	}
	public double getSleep() {
		return sleep;
	}
	public void setSleep(double sleep) {
		this.sleep = sleep;
	}
	public double getEnergy() {
		return energy;
	}
	public void setEnergy(double energy) {
		this.energy = energy;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public HashMap<String, Double> getIllness() {
		return illness;
	}
	public void setIllness(HashMap<String, Double> illness) {
		this.illness = illness;
	}
	public List<String> getTreatable() {
		return treatable;
	}
	public void setTreatable(LinkedList<String> treatable) {
		this.treatable = treatable;
	}
	public double getDrugEffect() {
		return drugEffect;
	}
	public void setDrugEffect(double drugEffect) {
		this.drugEffect = drugEffect;
	}
	public double getMedicineDuration() {
		return medicineDuration;
	}
	public void setMedicineDuration(double medicineDuration) {
		this.medicineDuration = medicineDuration;
	}
	public double getTemperature() {
		return temperature;
	}
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	public double getHungery() {
		return hungery;
	}
	public void setHungery(int hungery) {
		this.hungery = hungery;
	}
	
	
}
