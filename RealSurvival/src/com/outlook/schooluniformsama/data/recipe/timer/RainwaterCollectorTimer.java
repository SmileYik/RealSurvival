package com.outlook.schooluniformsama.data.recipe.timer;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.recipe.WorkbenchType;
import com.outlook.schooluniformsama.util.Util;

public class RainwaterCollectorTimer extends Timer{
	public RainwaterCollectorTimer(String playerName, String worldName, int x, int y, int z) {
		super("RainwaterCollector",playerName, WorkbenchType.RAINWATER_COLLECTOR, worldName, x, y, z);
	}
	
	public void start(int time){
		this.needTime=60;
		this.time = time;
	}
	public void start(){
		this.needTime=60;
	}
	
	@Override
	public void subTime() {
		if(!Bukkit.getWorld(worldName).hasStorm()) return;
		if (Bukkit.getWorld(worldName).getBlockAt(x, y, z).getType() != Material.CAULDRON){
			Data.timer.remove(Util.getWorkbenchID((Timer)this));
			return;
		}
		if(time<27*60)
			time++;
	}
	
	public void removeWater(int amount){
		time-=needTime*amount;
	}
	
	public int getSize(){
		return (int)(time/needTime);
	}
	
	public String hasWater(){
		if(time>=needTime)
			return null;
		return "NoRainwater";
	}
	
	public void getWater(){
		time-=needTime;
	}
}
