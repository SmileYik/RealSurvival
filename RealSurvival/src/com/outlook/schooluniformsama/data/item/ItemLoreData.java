package com.outlook.schooluniformsama.data.item;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.util.Util;

public class ItemLoreData {
	private double thirst;
	private double sleep;
	private double energy;
	//private double weight;
	private String[] illnessNames;
	private double illnessProbability;
	private String[] treatable;
	private double drugEffect;
	private double medicineDuration;
	private double hungery;
	private String workbenchType;
	private String recipeName;
	private String effectName;
	
	private ItemLoreData(double thirst, double sleep, double energy, String[] illnessNames,
			double illnessProbability, String[] treatable, double drugEffect, double medicineDuration,double hungery,String workbenchType,String recipeName,String effectName) {
		super();
		this.thirst = thirst;
		this.sleep = sleep;
		this.energy = energy;
		//this.weight = weight;
		this.illnessNames = illnessNames;
		this.illnessProbability = illnessProbability;
		this.treatable = treatable;
		this.drugEffect = drugEffect;
		this.medicineDuration = medicineDuration;
		this.hungery=hungery;
		this.workbenchType=workbenchType;
		this.recipeName = recipeName;
		this.effectName = effectName;
	}
	
	public static ItemLoreData getItemLoreData(ItemStack is){
		if(is==null || !is.hasItemMeta() || !is.getItemMeta().hasLore())return null;
		List<String> lore=is.clone().getItemMeta().getLore();
		String illness = getLoreString("illness", lore);
		String treatable = getLoreString("treatable", lore);
		return new ItemLoreData(	getLore("thirst", lore), 
														getLore("sleep", lore), 
														getLore("energy", lore), 
														//getLore("weight", lore),
														(illness == null)?null:illness.split(";"), 
														getLore("illnessprobability", lore), 
														(treatable==null)?null:treatable.split(";"), 
														getLore("drugeffect", lore),
														getLore("medicineduration", lore), 
														//getLore("temperature", lore),
														getLore("hungr", lore),
														getLoreString("workbenchtype", lore),
														getLoreString("recipename", lore),
														getLoreString("effectname",lore));
	}
	
	public static String getLoreString(String tabel,List<String> lore){
		tabel=Data.label.get(tabel.toLowerCase());
		for(String line:lore){
			line=Util.removeColor(line);
			if(line.contains(tabel))
				return Util.removeColor(line.split(Data.split)[1].replace(" ", ""));
		}
		return null;
	}
	
	public static double getLore(String key,List<String> lore){
		String tabel=Data.label.get(key.toLowerCase());
		
		for(String line:lore){
			line=Util.removeColor(line);
			if(line.contains(tabel)){
				String temp=line.replaceAll("[^0-9.+-]", "");
				if(line.contains("%")){
					if(line.contains("--")){
						//min -- max
						if(key.equalsIgnoreCase("sleep"))
							return Util.randomNum(Double.parseDouble(temp.split("--")[0])*Data.sleep[0], Double.parseDouble(temp.split("--")[1])*Data.sleep[0])/100D;
						else if(key.equalsIgnoreCase("thirst"))
							return Util.randomNum(Double.parseDouble(temp.split("--")[0])*Data.thirst[0], Double.parseDouble(temp.split("--")[1])*Data.thirst[0])/100D;
						else if(key.equalsIgnoreCase("energy"))
							return Util.randomNum(Double.parseDouble(temp.split("--")[0])*Data.energy[0], Double.parseDouble(temp.split("--")[1])*Data.energy[0])/100D;
						else if(key.equalsIgnoreCase("drugeffect"))
							return Util.randomNum(Double.parseDouble(temp.split("--")[0]), Double.parseDouble(temp.split("--")[1]));
						else
							return Util.randomNum(Double.parseDouble(temp.split("--")[0]), Double.parseDouble(temp.split("--")[1]))/100D;
					}else{
						if(key.equalsIgnoreCase("sleep"))
							return Double.parseDouble(temp)*Data.sleep[0]/100D;
						else if(key.equalsIgnoreCase("thirst"))
							return Double.parseDouble(temp)*Data.thirst[0]/100D;
						else if(key.equalsIgnoreCase("energy"))
							return Double.parseDouble(temp)*Data.energy[0]/100D;
						else if(key.equalsIgnoreCase("drugeffect"))
							return Double.parseDouble(temp);
						else
							return Double.parseDouble(temp)/100D;
					}
				}else{
					if(line.contains("--")){
						//min -- max
						if(key.equalsIgnoreCase("sleep"))
							return Util.randomNum(Double.parseDouble(temp.split("--")[0]), Double.parseDouble(temp.split("--")[1]));
						else if(key.equalsIgnoreCase("thirst"))
							return Util.randomNum(Double.parseDouble(temp.split("--")[0]), Double.parseDouble(temp.split("--")[1]));
						else if(key.equalsIgnoreCase("energy"))
							return Util.randomNum(Double.parseDouble(temp.split("--")[0]), Double.parseDouble(temp.split("--")[1]));
						else
							return Util.randomNum(Double.parseDouble(temp.split("--")[0]), Double.parseDouble(temp.split("--")[1]));
					}else{
						return Double.parseDouble(temp);
					}
				}
			}
		}
		return badCode();
	}
	
	public static double badCode(){
		return -1.1111111;
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

	public double getHungery() {
		return hungery;
	}

	public String getWorkbenchType() {
		return workbenchType;
	}
	
	public String getRecipeName() {
		return recipeName;
	}
	
	public String getEffectName(){
		return effectName;
	}
}
