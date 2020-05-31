package com.outlook.schooluniformsama.api.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.outlook.schooluniformsama.data.recipe.WorkbenchRecipe;

/**
 * When player start make a item by workbench <p>
 *  \u5f53\u73a9\u5bb6\u7528RealSurvival\u4e2d\u7684Workbench\u5236\u4f5c\u7269\u54c1\u7684\u65f6\u5019\u89e6\u53d1
 *  @author School_Uniform
 */
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

	/**
	 * Get the recipe <p>
	 * \u83b7\u53d6\u8be5\u73a9\u5bb6\u51c6\u5907\u5236\u4f5c\u7684\u914d\u65b9
	 * @return recipe
	 */
	public WorkbenchRecipe getRecipe() {
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
