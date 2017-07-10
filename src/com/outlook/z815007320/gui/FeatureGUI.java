package com.outlook.z815007320.gui;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.outlook.z815007320.data.Items;
import com.outlook.z815007320.data.PluginRS;

public class FeatureGUI extends PluginRS{
	public static List<Integer> waterList=Arrays.asList(24,21,20,25,23,22,19,11,12,10,3,2,1);
	
	
	//雨水收集器 Start
	public static Inventory openRwC(ItemStack itemKey){
		Inventory inv=Bukkit.createInventory(null, 27,"§3§l雨水收集器");
		for(int i=0;i<27;i++)
				inv.setItem(i, Items.createPItem((short)15, "§7§l讨厌啦~不要总是看着人家啦~"));
		inv.setItem(26, null);
		inv.setItem(0, itemKey);
		
		int amount=Integer.parseInt(rs.getRwC(itemKey.getItemMeta().getDisplayName()).split(",")[2]);
		for(int i:waterList)
			if(amount>0){
				inv.setItem(i, Items.getPlaceholder13());
				amount--;
			}
		
		return inv;
	}
	//雨水收集器 end
	
	//j净化装置 Start
	public static Inventory openPD(){
		Inventory inv=Bukkit.createInventory(null, 18,"§2§l净化装置");
		for(int i=0;i<18;i++)
			inv.setItem(i, Items.createPItem((short)15, "§7§l讨厌啦~不要总是看着人家啦~"));
		inv.setItem(1,null);
		inv.setItem(4, null);
		inv.setItem(7, null);
		inv.setItem(13, Items.createPItem((short)5, "§2§l开始净化"));
		return inv;
	}
	
	//净化装置 End
}
