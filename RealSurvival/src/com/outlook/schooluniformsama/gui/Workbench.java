package com.outlook.schooluniformsama.gui;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import com.outlook.schooluniformsama.data.item.Items;
import com.outlook.schooluniformsama.data.recipes.WorkbenchRecipe;
import com.outlook.schooluniformsama.data.timer.WorkbenchTimer;
import com.outlook.schooluniformsama.util.Msg;

public class Workbench {
	public static final  List<Integer> materials = Arrays.asList(10,11,12,13,19,20,21,22,28,29,30,31,37,38,39,40);
	public static final  List<Integer> products = Arrays.asList(24,25,33,34);
	private static Inventory createDefaultGUI(String title){
		Inventory inv=Bukkit.createInventory(null, 54,title);
		for(int i=0;i<54;i++)
			if(!(materials.contains(i)||products.contains(i)))
				inv.setItem(i, Items.createPItem((short)15, " "));
		return inv;
	}
	
	public static Inventory createWorkbenchGUI(String title){
		Inventory inv=createDefaultGUI(title+" §f- W");
		inv.setItem(49, Items.createPItem((short) 14, Msg.getMsg("WorkbenchStart", false)));
		return inv;
	}
	
	public static Inventory createWorkbenchRecipeGUI(String title){
		Inventory inv=createDefaultGUI(title+" §f- W"+"*");
		inv.setItem(16, Items.createPItem((short) 14, Msg.getMsg("SaveRecipe", false)));
		return inv;
	}

	public static Inventory openWorkbenchGUI(WorkbenchTimer wt){
		Inventory inv=createDefaultGUI(wt.getWorkbenchShape().getTitle()+" §f- W"+"§3§l*");
		WorkbenchRecipe wr=WorkbenchRecipe.load(wt.getRecipeName());
		return checkPass(wr.setInv(inv),wt);
	}
	
	public static  Inventory checkPass(Inventory inv,WorkbenchTimer wt){
		if(wt.isOver())
			inv.setItem(49, Items.createPItem((short)14, Msg.getMsg("WorkbenchProgress2", false)));
		else
			inv.setItem(49, Items.createPItem((short)5, Msg.getMsg("WorkbenchProgress1", new String[]{"%pass%"}, 
					new String[]{((wt.getTime()/(double)wt.getNeedTime())*100)+""}, false)));
		
		return inv;
	}
}
