package com.outlook.schooluniformsama.gui;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import com.outlook.schooluniformsama.data.item.Items;
import com.outlook.schooluniformsama.data.timer.RainwaterCollectorTimer;
import com.outlook.schooluniformsama.util.Msg;

public class FeatureGUI{
	public static List<Integer> waterList=Arrays.asList(40,33,32,31,30,29,25,24,23,22,21,20,19,18,15,14,13,12,11,10,9,7,6,5,4,3,2,1);
	
	public static Inventory openRainwaterCollector(RainwaterCollectorTimer rct){
		Inventory inv = Bukkit.createInventory(null, 54,Msg.getMsg("RainwaterCollectorTitle", false));
		
		for(int i=0;i<54;i++)
			if(waterList.contains(i))
				inv.setItem(i, Items.createPItem((short)0, " "));
			else
				inv.setItem(i, Items.createPItem((short)15, " "));
		inv.setItem(49, null);
		int amount=rct.getSize();
		for(int i:waterList)
			if(amount-->0)
				inv.setItem(i, Items.createPItem((short)14, Msg.getMsg("Rainwater", false)));
		return inv;
	}
	
	//j净化装置 Start
	public static Inventory openPurificationDevice(){
		Inventory inv=Bukkit.createInventory(null, 27, Msg.getMsg("PurificationDeviceTitle", false));
		for(int i=0;i<27;i++)
			inv.setItem(i, Items.createPItem((short)15, " "));
		inv.setItem(12,null);
		inv.setItem(9, null);
		inv.setItem(6, null);
		inv.setItem(21, Items.createPItem((short)8," "));
		inv.setItem(23, Items.createPItem((short)8," "));
		inv.setItem(22, Items.createPItem((short)15,Msg.getMsg("PurificationDeviceOK", false)));
		return inv;
	}
	
	//净化装置 End
}
