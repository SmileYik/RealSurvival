package com.outlook.z815007320.data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

import io.puharesource.mc.titlemanager.api.v2.TitleManagerAPI;

public class MainDatas extends JavaPlugin{
	//Max Mid Min Sub Add
	protected double[] sleepDatas=new double[5];
	//Max Mid Min Sub
	protected double[] thirstDatas=new double[4];
	//Max Min Add Sprinting Sneaking
	protected double[] psDatas=new double[5];
	//s-h sl-c se-h se-c slc sec damage
	protected double[] fracture=new double[7];
	protected double[] onDeath=new double[5];
	protected String[] workbenchItems=new String[8];
	protected String[] fireCraftTableItems=new String[8];
	
	protected double weight;
	protected double heatSourceFix;
	protected boolean isOver=true;
	protected String defSick;
	protected TitleManagerAPI tmapi;
	protected List<String> worlds=new LinkedList<>();
	protected HashMap<String,Double> heatSource = new HashMap<String, Double>();
	protected HashMap<UUID,PlayerData> playersDatas=new HashMap<UUID,PlayerData>();
	protected LinkedList<WorkbenchRecipe> SF=new LinkedList<WorkbenchRecipe>();
	protected LinkedList<FireCraftTableRecipe> FCTR=new LinkedList<>();
	protected HashMap<String,List<String>> effects = new HashMap<String,List<String>>();
	protected HashMap<String,String> loreTabel = new HashMap<String,String>();
	protected HashMap<String,String> messages = new HashMap<String,String>();
	protected HashMap<String,Double> itemWeight = new HashMap<String,Double>();
	protected HashMap<String,String> mob = new HashMap<String,String>();
	protected HashMap<String,String> eatFood = new HashMap<String,String>();
	protected HashMap<String,String>foods = new HashMap<String,String>();
	protected HashMap<String,String> workbench = new HashMap<String,String>();
	protected HashMap<String,String> rainwaterCollector = new HashMap<String,String>();
	protected HashMap<String,String> fireCraftTable = new HashMap<String,String>();
	
	public double getSleepMax() {
		return sleepDatas[0];
	}

	public double getSleepMin() {
		return sleepDatas[2];
	}

	public double getSleepMid() {
		return sleepDatas[1];
	}

	public double getSleepSub() {
		return sleepDatas[3];
	}

	public double getSleepAdd() {
		return sleepDatas[4];
	}

	public double getThirstMax() {
		return thirstDatas[0];
	}

	public double getThirstMin() {
		return thirstDatas[2];
	}

	public double getThirstMid() {
		return thirstDatas[1];
	}

	public double getThirstSub() {
		return thirstDatas[3];
	}

	public double getPhysical_strength() {
		return psDatas[0];
	}

	public double getPhysical_strength_add() {
		return psDatas[2];
	}

	public double getWeight() {
		return weight;
	}

	public HashMap<String, Double> getHeatSource() {
		return heatSource;
	}

	public double getSneaking() {
		return psDatas[4];
	}

	public double getSprinting() {
		return psDatas[3];
	}

	public LinkedList<WorkbenchRecipe> getSF() {
		return SF;
	}

	public double getPhysical_strength_min() {
		return psDatas[1];
	}

	public boolean isOver() {
		return isOver;
	}

	public void setOver(boolean isOver) {
		this.isOver = isOver;
	}

	public String[] getWorkbenchItems() {
		return workbenchItems;
	}
	
	public String[] getFireCraftTableItems() {
		return fireCraftTableItems;
	}

	public TitleManagerAPI getTitleManager() {
		return tmapi;
	}
	
	/**
	 * 0为睡眠 1为口渴 2为体力 3为温度 4为生病
	 * 温度不是直接加 生病也不是
	 * @return
	 */
	public double[] getOnDeath() {
		return onDeath;
	}
	
	public String getPrefix(){
		return messages.get("prefix");
	}

	public double[] getFracture() {
		return fracture;
	}
	
	public boolean worldExists(String worldName){
		return worlds.contains(worldName);
	}

	public LinkedList<FireCraftTableRecipe> getFCTR() {
		return FCTR;
	}

	public double getHeatSourceFix() {
		return heatSourceFix;
	}
	
	public double getDistanceEffect(){
		return heatSource.get("DistanceEffect");
	}
}
