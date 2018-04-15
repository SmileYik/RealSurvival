package com.outlook.schooluniformsama.gui;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import com.outlook.schooluniformsama.data.item.Items;
import com.outlook.schooluniformsama.data.timer.RainwaterCollectorTimer;
import com.outlook.schooluniformsama.util.Msg;

public class FeatureGUI{
	public static List<Integer> waterList=Arrays.asList(26,20,23,19,24,25,18,22,21,15,17,14,10,9,11,13,12,16,5,4,8,0,1,3,2,6,7);
	public static Inventory openRainwaterCollector(RainwaterCollectorTimer rct){
		Inventory inv = Bukkit.createInventory(null, 27,Msg.getMsg("RainwaterCollectorTitle", false));
		
		for(int i=0;i<27;i++)
				inv.setItem(i, Items.createPItem((short)0, " "));

		int amount=rct.getSize();
		for(int i:waterList)
			if(amount-->0)
				inv.setItem(i, Items.createPItem((short)14, Msg.getMsg("Rainwater", false)));
		return inv;
	}
}
