package com.outlook.schooluniformsama.api.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.outlook.schooluniformsama.data.recipes.FurnaceRecipe;
/**
* start when player made over a item. <p>
* \u5f53\u73a9\u5bb6\u6536\u8d27\u7269\u54c1\u7684\u65f6\u5019\u89e6\u53d1\u8be5\u4e8b\u4ef6
 * @author School_Uniform
 *
 */
public class RSMakeItemDoneByFurnaceEvent extends RSMakeItemByFurnaceEvent{
	private boolean bad;
	
	public RSMakeItemDoneByFurnaceEvent(Player player,FurnaceRecipe recipe,Location location,String workbenchName,boolean bad) {
		super(player,recipe,location,workbenchName);
		this.bad = bad;
	}

	/**
	 * get the item is made bad<p>
	 * \u68c0\u67e5\u7269\u54c1\u662f\u5426\u5236\u4f5c\u5931\u8d25
	 * @return
	 */
	public boolean isBad() {
		return bad;
	}
	
	
}
