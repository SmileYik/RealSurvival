package com.outlook.schooluniformsama.gui;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.outlook.schooluniformsama.I18n;
import com.outlook.schooluniformsama.api.event.RSOpenRecipeViewerEvent;
import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.TempData;
import com.outlook.schooluniformsama.data.WorkbenchShape;
import com.outlook.schooluniformsama.data.item.Items;
import com.outlook.schooluniformsama.data.recipe.FurnaceRecipe;
import com.outlook.schooluniformsama.data.recipe.Recipe;
import com.outlook.schooluniformsama.data.recipe.WorkbenchRecipe;
import com.outlook.schooluniformsama.data.recipe.WorkbenchType;
import com.outlook.schooluniformsama.data.recipe.timer.RainwaterCollectorTimer;

public class FeatureGUI{
	public static List<Integer> waterList=Arrays.asList(26,20,23,19,24,25,18,22,21,15,17,14,10,9,11,13,12,16,5,4,8,0,1,3,2,6,7);
	public static List<Integer> workbenchSlot=Arrays.asList(10,12,14,16,28,30,32,34);
	
	private static Material getThreeViewDrawingMaterial(String item){
		Material material = Material.matchMaterial(item+"_ITEM");
		if(material!=null)return material;
		return Material.matchMaterial(item);
	}
	
	public static Inventory threeViewDrawing(String type){
		if(!Data.workbenchs.containsKey(type))return null;
		WorkbenchShape ws = Data.workbenchs.get(type);
		Inventory inv = Bukkit.createInventory(null, 54,I18n.tr("recipe1"));
		//main item
		if(ws.getFront()!=null)
			inv.setItem(11, new ItemStack(getThreeViewDrawingMaterial(ws.getFront())));
		else
			inv.setItem(11, new ItemStack(getThreeViewDrawingMaterial(ws.getMain())));
		if(ws.getLeft()!=null)
			inv.setItem(15, new ItemStack(getThreeViewDrawingMaterial(ws.getLeft())));
		else
			inv.setItem(15, new ItemStack(getThreeViewDrawingMaterial(ws.getMain())));
		if(ws.getUp()!=null)
			inv.setItem(38, new ItemStack(getThreeViewDrawingMaterial(ws.getUp())));
		else
			inv.setItem(38, new ItemStack(getThreeViewDrawingMaterial(ws.getMain())));
		
		if(ws.getUp()!=null){
			inv.setItem(6, new ItemStack(getThreeViewDrawingMaterial(ws.getUp())));
			inv.setItem(2, new ItemStack(getThreeViewDrawingMaterial(ws.getUp())));
		}
		if(ws.getDown()!=null){
			inv.setItem(24, new ItemStack(getThreeViewDrawingMaterial(ws.getDown())));
			inv.setItem(20, new ItemStack(getThreeViewDrawingMaterial(ws.getDown())));
		}
		if(ws.getLeft()!=null){
			inv.setItem(37, new ItemStack(getThreeViewDrawingMaterial(ws.getLeft())));
			inv.setItem(10, new ItemStack(getThreeViewDrawingMaterial(ws.getLeft())));
		}
		if(ws.getRight()!=null){
			inv.setItem(39, new ItemStack(getThreeViewDrawingMaterial(ws.getRight())));
			inv.setItem(12, new ItemStack(getThreeViewDrawingMaterial(ws.getRight())));
		}
		if(ws.getBehind()!=null){
			inv.setItem(29, new ItemStack(getThreeViewDrawingMaterial(ws.getBehind())));
			inv.setItem(14, new ItemStack(getThreeViewDrawingMaterial(ws.getBehind())));
		}
		if(ws.getFront()!=null){
			inv.setItem(47, new ItemStack(getThreeViewDrawingMaterial(ws.getFront())));
			inv.setItem(16, new ItemStack(getThreeViewDrawingMaterial(ws.getFront())));
		}
		inv.setItem(49, Items.createPItem((short) 14, I18n.tr("recipeWorkbenchViewClose")));
		
/*		//main
		if(ws.getFront()!=null)
			inv.setItem(11, new ItemStack(Material.valueOf(ws.getFront().toUpperCase())));
		else
			inv.setItem(11, new ItemStack(Material.valueOf(ws.getMain().toUpperCase())));
		if(ws.getUp()!=null)
			inv.setItem(2, new ItemStack(Material.valueOf(ws.getUp().toUpperCase())));
		if(ws.getDown()!=null)
			inv.setItem(20, new ItemStack(Material.valueOf(ws.getDown().toUpperCase())));
		if(ws.getLeft()!=null)
			inv.setItem(10, new ItemStack(Material.valueOf(ws.getLeft().toUpperCase())));
		if(ws.getRight()!=null)
			inv.setItem(12, new ItemStack(Material.valueOf(ws.getRight().toUpperCase())));
		
		//Left
		if(ws.getLeft()!=null)
			inv.setItem(15, new ItemStack(Material.valueOf(ws.getLeft().toUpperCase())));
		else
			inv.setItem(15, new ItemStack(Material.valueOf(ws.getMain().toUpperCase())));
		if(ws.getBehind()!=null)
			inv.setItem(14, new ItemStack(Material.valueOf(ws.getBehind().toUpperCase())));
		if(ws.getFront()!=null)
			inv.setItem(16, new ItemStack(Material.valueOf(ws.getFront().toUpperCase())));
		if(ws.getUp()!=null)
			inv.setItem(6, new ItemStack(Material.valueOf(ws.getUp().toUpperCase())));
		if(ws.getDown()!=null)
			inv.setItem(24, new ItemStack(Material.valueOf(ws.getDown().toUpperCase())));
		
		//Up
		if(ws.getUp()!=null)
			inv.setItem(38, new ItemStack(Material.valueOf(ws.getUp().toUpperCase())));
		else
			inv.setItem(38, new ItemStack(Material.valueOf(ws.getMain().toUpperCase())));
		if(ws.getLeft()!=null)
			inv.setItem(37, new ItemStack(Material.valueOf(ws.getLeft().toUpperCase())));
		if(ws.getRight()!=null)
			inv.setItem(39, new ItemStack(Material.valueOf(ws.getRight().toUpperCase())));
		if(ws.getBehind()!=null)
			inv.setItem(29, new ItemStack(Material.valueOf(ws.getBehind().toUpperCase())));
		if(ws.getFront()!=null)
			inv.setItem(47, new ItemStack(Material.valueOf(ws.getFront().toUpperCase())));*/
		
		return inv;
	}
	
	public static Inventory openRainwaterCollector(RainwaterCollectorTimer rct){
		Inventory inv = Bukkit.createInventory(null, 27,I18n.tr("rainwater1"));
		
		for(int i=0;i<27;i++)
				inv.setItem(i, Items.createPItem((short)0, " "));

		int amount=rct.getSize();
		for(int i:waterList)
			if(amount-->0)
				inv.setItem(i, Items.createPItem((short)14, I18n.tr("water10")));
		return inv;
	}
	
	public static boolean openRecipeViewer(Player p,String type,String recipeName){
		WorkbenchType type2;
		try {
			type2 = WorkbenchType.valueOf(type);
		} catch (Exception e) {
			if(!Data.workbenchs.containsKey(type))return false;
			type2 = Data.workbenchs.get(type).getType();
		}
		Inventory inv = null;
		Recipe recipe = null;
		switch (type2) {
		case FURNACE:
			recipe = FurnaceRecipe.load(recipeName);
			if(recipe == null)return false;
			inv =  Furnace.createRecipeViewerGUI((FurnaceRecipe)recipe);
			break;
		case WORKBENCH:
			recipe = WorkbenchRecipe.load(recipeName);
			if(recipe == null)return false;
			inv =  Workbench.createRecipeViewerGUI((WorkbenchRecipe)recipe);
			break;
		default:
			break;
		}
		
		//Event RSOpenRecipeViewer
		RSOpenRecipeViewerEvent event = new RSOpenRecipeViewerEvent(p, recipe, inv,type2);
		Bukkit.getServer().getPluginManager().callEvent(event);
		if (!event.isCancelled()) {
			TempData.openRecipeViewer.put(p.getName(), type);
		    p.openInventory(inv);
		}
		return true;
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
