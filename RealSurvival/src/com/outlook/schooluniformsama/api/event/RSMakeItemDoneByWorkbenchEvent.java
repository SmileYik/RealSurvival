package com.outlook.schooluniformsama.api.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import com.outlook.schooluniformsama.data.recipes.WorkbenchRecipe;

/**
 * when player make a item done by a workbench <p>
 * \u5f53\u73a9\u5bb6\u7528RealSurvival\u4e2d\u7684Workbench\u5236\u4f5c\u5b8c\u4e00\u4e2a\u7269\u54c1\u7684\u65f6\u5019\u89e6\u53d1
 * @author School_Uniform
 */
public class RSMakeItemDoneByWorkbenchEvent extends RSMakeItemByWorkbenchEvent{
	
	public RSMakeItemDoneByWorkbenchEvent(Player player,WorkbenchRecipe recipe,Location location,String workbenchName) {
		super(player,recipe,location,workbenchName);
	}
}
