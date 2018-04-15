package com.outlook.schooluniformsama.data.timer;

import org.bukkit.Bukkit;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.recipes.WorkbenchRecipe;
import com.outlook.schooluniformsama.data.recipes.WorkbenchType;
import com.outlook.schooluniformsama.util.Util;

public class WorkbenchTimer extends Timer{
	
	public WorkbenchTimer(String workbenchName,String playerName,String worldName,int x,int y,int z){
		super(workbenchName,playerName,WorkbenchType.WORKBENCH,worldName,x,y,z);
	}
	
	@Override
	public void subTime() {
		if(!Bukkit.getWorld(worldName).getBlockAt(x, y, z).getType().name().equalsIgnoreCase(Data.workbenchs.get(workbenchName).getMain())){
			Data.timer.remove(Util.getWorkbenchID((Timer)this));
			return;
		}
		super.subTime();
	}
	
	public WorkbenchRecipe getRecipe(){
		return WorkbenchRecipe.load(recipeName);
	}
	
	public void start(WorkbenchRecipe recipe){
		this.time=0;
		this.needTime=recipe.getNeedTime();
		this.recipeName=recipe.getName();
	}
	
	public void continueStart(WorkbenchRecipe recipe,int time){
		this.time=time;
		this.recipeName=recipe.getName();
		this.needTime=recipe.getNeedTime();
	}
	
}
