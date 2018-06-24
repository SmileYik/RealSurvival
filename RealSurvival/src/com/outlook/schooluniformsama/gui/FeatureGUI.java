package com.outlook.schooluniformsama.gui;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.outlook.schooluniformsama.api.event.RSOpenRecipeViewerEvent;
import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.item.Items;
import com.outlook.schooluniformsama.data.recipes.FurnaceRecipe;
import com.outlook.schooluniformsama.data.recipes.Recipe;
import com.outlook.schooluniformsama.data.recipes.WorkbenchRecipe;
import com.outlook.schooluniformsama.data.recipes.WorkbenchType;
import com.outlook.schooluniformsama.data.timer.RainwaterCollectorTimer;
import com.outlook.schooluniformsama.util.Msg;

public class FeatureGUI{
	public static List<Integer> waterList=Arrays.asList(26,20,23,19,24,25,18,22,21,15,17,14,10,9,11,13,12,16,5,4,8,0,1,3,2,6,7);
	public static List<Integer> workbenchSlot=Arrays.asList(10,12,14,16,28,30,32,34);
	
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
	
	public static void openRecipeViewer(Player p,String type,String recipeName){
		//String nextSlot = Msg.getMsg("recipe-viewer.next-slot", false);
		//String previousSlot = Msg.getMsg("recipe-viewer.previous-slot", false); 
		if(!Data.workbenchs.containsKey(type))return;
		Inventory inv = null;
		Recipe recipe = null;
		WorkbenchType type2 = Data.workbenchs.get(type).getType();
		switch (type2) {
		case FURNACE:
			recipe = FurnaceRecipe.load(recipeName);
			inv =  Furnace.createRecipeViewerGUI((FurnaceRecipe)recipe);
			break;
		case WORKBENCH:
			recipe = WorkbenchRecipe.load(recipeName);
			inv =  Workbench.createRecipeViewerGUI((WorkbenchRecipe)recipe);
			break;
		default:
			break;
		}
		
		//Event RSOpenRecipeViewer
		RSOpenRecipeViewerEvent event = new RSOpenRecipeViewerEvent(p, recipe, inv,type2);
		Bukkit.getServer().getPluginManager().callEvent(event);
		if (!event.isCancelled()) {
		    p.openInventory(inv);
		}
	}
	
	public static Inventory openRecipeViewer(String type,String recipeName){
		//String nextSlot = Msg.getMsg("recipe-viewer.next-slot", false);
		//String previousSlot = Msg.getMsg("recipe-viewer.previous-slot", false); 
		if(!Data.workbenchs.containsKey(type))return null;
		Inventory inv = null;
		switch (Data.workbenchs.get(type).getType()) {
		case FURNACE:
			inv =  Furnace.createRecipeViewerGUI(FurnaceRecipe.load(recipeName));
			break;
		case WORKBENCH:
			inv =  Workbench.createRecipeViewerGUI(WorkbenchRecipe.load(recipeName));
			break;
		default:
			break;
		}
		
		return inv;
	}
}
