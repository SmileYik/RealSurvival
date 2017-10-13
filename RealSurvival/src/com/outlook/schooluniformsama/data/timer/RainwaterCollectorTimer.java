package com.outlook.schooluniformsama.data.timer;

import com.outlook.schooluniformsama.data.recipe.WorkbenchType;

public class RainwaterCollectorTimer extends Timer{
	public RainwaterCollectorTimer(String playerName, String worldName, int x, int y, int z) {
		super("RainwaterCollector",playerName, WorkbenchType.RAINWATER_COLLECTOR, worldName, x, y, z);
	}
	
	public void start(){
		this.needTime=60;
	}
	
	@Override
	public void subTime() {
		if(time<1680)
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
