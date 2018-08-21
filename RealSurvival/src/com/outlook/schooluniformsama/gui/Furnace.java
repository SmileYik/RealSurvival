package com.outlook.schooluniformsama.gui;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.outlook.schooluniformsama.I18n;
import com.outlook.schooluniformsama.data.item.Items;
import com.outlook.schooluniformsama.data.recipes.FurnaceRecipe;
import com.outlook.schooluniformsama.data.timer.FurnaceTimer;
import com.outlook.schooluniformsama.task.TemperatureTask;
import com.outlook.schooluniformsama.util.Util; 

public class Furnace{
	public static final List<Integer> mSlot=Arrays.asList(12,13,14,21,22,23);
	public static final List<Integer> pSlot=Arrays.asList(39,40,41);
	public static final List<Integer> tSlot=Arrays.asList(16,25,34,43);
	public static final List<Integer> passSlot=Arrays.asList(10,19,28,37);
	
	private static Inventory createDefGUI(String title){
		Inventory inv = Bukkit.createInventory(null,54 , title);
		for(int i=0;i<54;i++)
			if(!mSlot.contains(i)&&!pSlot.contains(i)&&!tSlot.contains(i)&&!passSlot.contains(i))
				inv.setItem(i, Items.createPItem((short)15, " "));
		for(int i:passSlot)
			inv.setItem(i, Items.createPItem((short)0, " "));
		for(int i:tSlot)
			inv.setItem(i, Items.createPItem((short)0, " "));
		return inv;
	}
	
	public static Inventory createRecipeViewerGUI(FurnaceRecipe recipe){
		if(recipe == null) return null;
		Inventory inv = Bukkit.createInventory(null,54 , I18n.tr("recipe1"));
		for(int i=0;i<54;i++)
			if(!mSlot.contains(i)&&!pSlot.contains(i)&&!tSlot.contains(i)&&!passSlot.contains(i))
				inv.setItem(i, Items.createPItem((short)15, " "));
		for(int i:passSlot)
			inv.setItem(i, Items.createPItem((short)0, I18n.tr("recipe3", Util.RDP(recipe.getNeedTime(), 2),Util.RDP(recipe.getSaveTime(), 2))));
		for(int i:tSlot)
			inv.setItem(i, Items.createPItem((short)0, I18n.tr("recipe4",Util.RDP(recipe.getMinTemperature(), 2),Util.RDP(recipe.getMaxTemperature(), 2))));
		inv.setItem(49, Items.createPItem((short) 14, I18n.tr("recipe6")));
		return recipe.setInv(inv);
	}
	
	public static Inventory createFurnaceGUI(String title){
		Inventory inv=createDefGUI(title+" §f- F");
		inv.setItem(49, Items.createPItem((short) 14, I18n.tr("furnace1")));
		return inv;
	}
	
	public static Inventory openFurnaceGUI(FurnaceTimer ft){
		Inventory inv=createDefGUI(ft.getWorkbenchShape().getTitle()+" §f- F"+"§3§l*");
		return checkHeatSource(FurnaceRecipe.load(ft.getRecipeName()).setInv(inv),ft);
	}
	
	public static Inventory furnaceMakerGUI(String title){
		Inventory inv=createDefGUI(title+" §f- F"+"*");
		inv.setItem(49, Items.createPItem((short) 14, I18n.tr("furnace11")));
		return inv;
	}
	
	public static Inventory checkHeatSource(Inventory inv,FurnaceTimer ft){
		ItemStack temp;
		if(ft.isBad()){
			for(int i:passSlot)
				inv.setItem(i,  Items.createPItem((short)12, I18n.tr("furnace6")));
			inv.setItem(49,   Items.createPItem((short)5, I18n.tr("furnace12")));
			return inv;
		}
		
		double nowTemperature =TemperatureTask.getBaseTemperature(ft.getLocation(),true)+ft.getExtraTemperature();
		double pass=(ft.getExtraTemperature()+nowTemperature)/ft.getMinTemperature();
		if(ft.getMinTemperature()>0){
			temp= Items.createPItem((short)1, I18n.tr("furnace7",Util.RDP(nowTemperature,2),Util.RDP( ft.getMinTemperature(),2)));
			inv.setItem(43,temp);
			if(pass>0.25)
				inv.setItem(34,temp);
			if(pass>0.5)
				inv.setItem(25,temp);
			if(pass>0.75)
				inv.setItem(16,temp);
		}else{
			temp= Items.createPItem((short)3, I18n.tr("furnace8",Util.RDP(nowTemperature,2),Util.RDP( ft.getMinTemperature(),2)));
			inv.setItem(43,temp );
			if(pass>=0.25)
				inv.setItem(34,temp);
			if(pass>0.5)
				inv.setItem(25,temp);
			if(pass>0.75)
				inv.setItem(16,temp);
		}
		//Time
		pass=ft.getTime()/(double)ft.getNeedTime();
		temp=Items.createPItem((short)5, I18n.tr("furnace9",Util.RDP(pass*100,2),(ft.getNeedTime()-ft.getTime())));
		if(pass>=0)
			inv.setItem(37, temp );
		if(pass>=0.5)
			inv.setItem(28,  temp);
		if(pass>=0.75)
			inv.setItem(19,  temp);
		if(pass>=1){
			inv.setItem(10,   temp);
			inv.setItem(49,   Items.createPItem((short)5, I18n.tr("furnace12")));
		}
		//SaveTime
		if(ft.getTime()>ft.getNeedTime()){
			pass=ft.getTime()/(double)(ft.getNeedTime()+ft.getSaveTime());
			temp=Items.createPItem((short)12, I18n.tr("furnace10",ft.getNeedTime()+ft.getSaveTime()-ft.getTime()));
			if(pass>=0)
				inv.setItem(37,  temp);
			if(pass>=0.5)
				inv.setItem(28,  temp);
			if(pass>=0.75)
				inv.setItem(19,  temp);
			if(pass>=1)
				inv.setItem(10,  temp);
		}
		return inv;
	}
	
}
