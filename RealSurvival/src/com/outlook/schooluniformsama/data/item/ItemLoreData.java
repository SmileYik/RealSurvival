package com.outlook.schooluniformsama.data.item;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.util.Util;

public class ItemLoreData {
	private double thirst;
	private double sleep;
	private double energy;
	private double weight;
	private String[] illnessNames;
	private double illnessProbability;
	private String[] treatable;
	private double drugEffect;
	private double medicineDuration;
	private double temperature;
	private double hungery;
	
	private ItemLoreData(double thirst, double sleep, double energy, double weight, String[] illnessNames,
			double illnessProbability, String[] treatable, double drugEffect, double medicineDuration,
			double temperature,double hungery) {
		super();
		this.thirst = thirst;
		this.sleep = sleep;
		this.energy = energy;
		this.weight = weight;
		this.illnessNames = illnessNames;
		this.illnessProbability = illnessProbability;
		this.treatable = treatable;
		this.drugEffect = drugEffect;
		this.medicineDuration = medicineDuration;
		this.temperature = temperature;
		this.hungery=hungery;
	}
	
	
	
	public static ItemLoreData getItemLoreData(ItemStack is){
		if(is==null || !is.hasItemMeta() || !is.getItemMeta().hasLore())return null;
		List<String> lore=is.clone().getItemMeta().getLore();
		return new ItemLoreData(getLore("Thirst", lore, false), getLore("Sleep", lore, false), getLore("Energy", lore, false), getLore("Weight", lore, true),
					getLoreString("Illness", lore)==null?null:getLoreString("Illness", lore).split("|"), getLore("IllnessProbability", lore, true), getLoreString("Treatable", lore)==null?null:
					getLoreString("Treatable", lore).split(";"), getLore("DrugEffect", lore, true),getLore("MedicineDuration", lore, true), getLore("Temperature", lore, true),getLore("Hungery", lore, false));
	}
	
	private static String getLoreString(String tabel,List<String> lore){
		tabel=Data.label.get(tabel);
		for(String line:lore){
			line=Util.removeColor(line);
			if(line.contains(tabel))
				return Util.removeColor(line.split(Data.split)[1].replace(" ", ""));
		}
		return null;
	}
	
	public static double getLore(String tabel,List<String> lore,boolean isChance){
		tabel=Data.label.get(tabel);
		for(String line:lore){
			line=Util.removeColor(line);
			if(line.contains(tabel)){
				String temp=remove(line.split(Data.split)[1].replaceAll(" ", ""));
				if(isChance){
					temp=temp.replaceAll("%", "");
					if(temp.contains("--"))
						return Util.randomNum(Double.parseDouble(temp.split("--")[0]), Double.parseDouble(temp.split("--")[1]));
					return Double.parseDouble(temp);
				}else{
					if(temp.contains("%")){
						temp=temp.replaceAll("%", "");
						if(temp.contains("--"))
							if(tabel=="Sleep")
								return Util.randomNum(Double.parseDouble(temp.split("--")[0]), Double.parseDouble(temp.split("--")[1]))*Data.sleep[0];
							else if(tabel=="Thirst")
								return Util.randomNum(Double.parseDouble(temp.split("--")[0]), Double.parseDouble(temp.split("--")[1]))*Data.thirst[0];
							else 
								return Util.randomNum(Double.parseDouble(temp.split("--")[0]), Double.parseDouble(temp.split("--")[1]))*Data.energy[0];
					}else if(temp.contains("--"))
						return Util.randomNum(Double.parseDouble(temp.split("--")[0]), Double.parseDouble(temp.split("--")[1]));
					else
						return Double.parseDouble(temp);
				}
			}
		}
		return badCode();
	}
	
	public static double badCode(){
		return -1.1111111;
	}
	
	private static String remove(String input){
		for(String word:Data.removeChars)
			input=input.replaceAll(word, "");
		return input;
	}
	
	public double getThirst() {
		return thirst;
	}

	public double getSleep() {
		return sleep;
	}

	public double getEnergy() {
		return energy;
	}

	public double getWeight() {
		return weight;
	}

	public String[] getIllnessNames() {
		return illnessNames;
	}

	public double getIllnessProbability() {
		return illnessProbability;
	}

	public String[] getTreatable() {
		return treatable;
	}

	public double getDrugEffect() {
		return drugEffect;
	}

	public double getMedicineDuration() {
		return medicineDuration;
	}

	public double getTemperature() {
		return temperature/100D;
	}

	public double getHungery() {
		return hungery;
	}
	
	
}
