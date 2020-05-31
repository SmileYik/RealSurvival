package com.outlook.schooluniformsama.api.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.outlook.schooluniformsama.data.recipe.FurnaceRecipe;

/**
 * When player start make a item by furnace <p>
 * \u5f53\u73a9\u5bb6\u7528RealSurvival\u4e2d\u7684Furnace\u5236\u4f5c\u7269\u54c1\u65f6\u89e6\u53d1\u6b64\u4e8b\u4ef6
 * @author School_Uniform
 *
 */
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
	
	/**
	 * Get the recipe <p>
	 * \u83b7\u53d6\u8be5\u73a9\u5bb6\u51c6\u5907\u5236\u4f5c\u7684\u914d\u65b9
	 * @return recipe
	 */
	public FurnaceRecipe getRecipe() {
		return recipe;
	}

	/**
	 * get the workbench's location <p>
	 * \u83b7\u53d6\u5de5\u4f5c\u53f0\u7684\u4f4d\u7f6e
	 * @return A Location about the workbench's Location
	 */
	public Location getLocation() {
		return location;
	}

	
	/**
	 * get the workbench's type<p>
	 * \u83b7\u53d6\u5de5\u4f5c\u53f0\u7684\u79cd\u7c7b
	 * @return A String about the type of workbench
	 */
	public String getWorkbenchName() {
		return workbenchName;
	}
	
	
}
