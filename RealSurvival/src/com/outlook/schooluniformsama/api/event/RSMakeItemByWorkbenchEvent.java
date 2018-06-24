package com.outlook.schooluniformsama.api.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import com.outlook.schooluniformsama.data.recipes.WorkbenchRecipe;

public class RSMakeItemByWorkbenchEvent extends RealSurvivalEvent{
	private WorkbenchRecipe recipe;
	private String workbenchName;
	private Location location;
	
	public RSMakeItemByWorkbenchEvent(Player player,WorkbenchRecipe recipe,Location location,String workbenchName) {
		super(player);
		this.recipe = recipe;
		this.location = location;
		this.workbenchName = workbenchName;
	}

	public WorkbenchRecipe getRecipe() {
		return recipe;
	}

	public Location getLocation() {
		return location;
	}

	public String getWorkbenchName() {
		return workbenchName;
	}
	
	
}
