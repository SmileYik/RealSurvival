package com.outlook.schooluniformsama.api.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import com.outlook.schooluniformsama.data.recipes.WorkbenchRecipe;

public class RSMakeItemDoneByWorkbenchEvent extends RSMakeItemByWorkbenchEvent{
	public RSMakeItemDoneByWorkbenchEvent(Player player,WorkbenchRecipe recipe,Location location,String workbenchName) {
		super(player,recipe,location,workbenchName);
	}
}
