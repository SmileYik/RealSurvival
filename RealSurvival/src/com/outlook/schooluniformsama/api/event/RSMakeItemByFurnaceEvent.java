package com.outlook.schooluniformsama.api.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.outlook.schooluniformsama.data.recipes.FurnaceRecipe;

public class RSMakeItemByFurnaceEvent extends RealSurvivalEvent{
	private FurnaceRecipe recipe;
	private String workbenchName;
	private Location location;
	
	public RSMakeItemByFurnaceEvent(Player player,FurnaceRecipe recipe,Location location,String workbenchName) {
		super(player);
		this.recipe = recipe;
		this.location = location;
		this.workbenchName = workbenchName;
	}

	public FurnaceRecipe getRecipe() {
		return recipe;
	}

	public Location getLocation() {
		return location;
	}

	public String getWorkbenchName() {
		return workbenchName;
	}
	
	
}
