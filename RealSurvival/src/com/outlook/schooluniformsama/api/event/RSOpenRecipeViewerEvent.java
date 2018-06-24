package com.outlook.schooluniformsama.api.event;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.outlook.schooluniformsama.data.recipes.Recipe;
import com.outlook.schooluniformsama.data.recipes.WorkbenchType;

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

	public Recipe getRecipe() {
		return recipe;
	}

	public Inventory getRecipeViewer() {
		return recipeViewer;
	}

	public WorkbenchType getWorkbenchType() {
		return workbenchType;
	}
}
