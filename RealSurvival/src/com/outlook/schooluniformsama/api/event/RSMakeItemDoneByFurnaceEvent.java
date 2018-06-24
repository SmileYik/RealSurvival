package com.outlook.schooluniformsama.api.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.outlook.schooluniformsama.data.recipes.FurnaceRecipe;

public class RSMakeItemDoneByFurnaceEvent extends RSMakeItemByFurnaceEvent{
	private boolean bad;
	
	public RSMakeItemDoneByFurnaceEvent(Player player,FurnaceRecipe recipe,Location location,String workbenchName,boolean bad) {
		super(player,recipe,location,workbenchName);
		this.bad = bad;
	}

	public boolean isBad() {
		return bad;
	}
	
	
}
