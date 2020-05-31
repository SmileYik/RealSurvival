package com.outlook.schooluniformsama.api.event;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.outlook.schooluniformsama.data.recipe.Recipe;
import com.outlook.schooluniformsama.data.recipe.WorkbenchType;

/**
 * When player open a Recipe Viewer <p>
 * \u5f53\u73a9\u5bb6\u6253\u5f00\u4e00\u4e2a\u914d\u65b9\u67e5\u770b\u5668\u7684\u65f6\u5019\u89e6\u53d1
 * @author School_Uniform
 */
public class RSOpenRecipeViewerEvent extends RealSurvivalEvent{
    private Recipe recipe;
    private Inventory recipeViewer;
    private WorkbenchType workbenchType;

	public RSOpenRecipeViewerEvent(Player player, Recipe recipe, Inventory recipeViewer,WorkbenchType workbenchType) {
		super(player);
		this.recipe = recipe;
		this.recipeViewer = recipeViewer;
		this.workbenchType=workbenchType;
	}
	/**
	 * Get the recipe <p>
	 * \u83b7\u53d6\u8be5\u73a9\u5bb6\u51c6\u5907\u5236\u4f5c\u7684\u914d\u65b9
	 * @return recipe
	 */
	public Recipe getRecipe() {
		return recipe;
	}
	/**
	 * get the Inventory what is the player view.
	 * \u83b7\u53d6\u73a9\u5bb6\u6240\u770b\u7684\u5e93\u5b58\u9875\u9762
	 * @return
	 */
	public Inventory getRecipeViewer() {
		return recipeViewer;
	}

	/**
	 * get the workbench's type<p>
	 * \u83b7\u53d6\u5de5\u4f5c\u53f0\u7684\u79cd\u7c7b
	 * @return A String about the type of workbench
	 */
	public WorkbenchType getWorkbenchType() {
		return workbenchType;
	}
}
