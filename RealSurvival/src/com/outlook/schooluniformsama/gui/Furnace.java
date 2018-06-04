package com.outlook.schooluniformsama.gui;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.outlook.schooluniformsama.data.item.Items;
import com.outlook.schooluniformsama.data.recipes.FurnaceRecipe;
import com.outlook.schooluniformsama.data.timer.FurnaceTimer;
import com.outlook.schooluniformsama.task.TemperatureTask;
import com.outlook.schooluniformsama.util.Msg;
import com.outlook.schooluniformsama.util.Util; 

public class Furnace{
	public static final List<Integer> mSlot=Arrays.asList(12,13,14,21,22,23);
	public static final List<Integer> pSlot=Arrays.asList(39,40,41);
	public static final List<Integer> tSlot=Arrays.asList(16,25,34,43);
	public static final List<Integer> passSlot=Arrays.asList(10,19,28,37);
	
	//创建默认的界面
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
	
	public static Inventory createFurnaceGUI(String title){
		Inventory inv=createDefGUI(title+" §f- F");
		inv.setItem(49, Items.createPItem((short) 14, Msg.getMsg("FurnaceStart", false)));
		return inv;
	}
	
	public static Inventory openFurnaceGUI(FurnaceTimer ft){
		Inventory inv=createDefGUI(ft.getWorkbenchShape().getTitle()+" §f- F"+"§3§l*");
		return checkHeatSource(FurnaceRecipe.load(ft.getRecipeName()).setInv(inv),ft);
	}
	
	public static Inventory furnaceMakerGUI(String title){
		Inventory inv=createDefGUI(title+" §f- F"+"*");
		inv.setItem(49, Items.createPItem((short) 14, Msg.getMsg("SaveRecipe", false)));
		return inv;
	}
	
	public static Inventory checkHeatSource(Inventory inv,FurnaceTimer ft){
		ItemStack temp;
		if(ft.isBad()){
			for(int i:passSlot)
				inv.setItem(i,  Items.createPItem((short)12, Msg.getMsg("FurnaceFailed", false)));
			inv.setItem(49,   Items.createPItem((short)5, Msg.getMsg("WorkbenchProgress2", false)));
			return inv;
		}
		
		double nowTemperature =TemperatureTask.getBaseTemperature(ft.getLocation(),true);
		double pass=(ft.getExtraTemperature()+nowTemperature)/ft.getMinTemperature();
		if(ft.getMinTemperature()>0){
			temp= Items.createPItem((short)1, Msg.getMsg("FurnaceWorkProgress+", new String[]{"%temperature%","%minTemperature%"},
					new String[]{Util.RDP(nowTemperature,2),Util.RDP( ft.getMinTemperature(),2)},false));
			if(pass>0)
				inv.setItem(43,temp );
			if(pass>0.25)
				inv.setItem(34,temp);
			if(pass>0.5)
				inv.setItem(25,temp);
			if(pass>0.75)
				inv.setItem(16,temp);
		}else{
			temp= Items.createPItem((short)3, Msg.getMsg("FurnaceWorkProgress-", new String[]{"%temperature%","%minTemperature%"},
					new String[]{Util.RDP(nowTemperature,2),Util.RDP( ft.getMinTemperature(),2)},false));
			if(pass>0)
				inv.setItem(43,temp );
			if(pass>0.25)
				inv.setItem(34,temp);
			if(pass>0.5)
				inv.setItem(25,temp);
			if(pass>0.75)
				inv.setItem(16,temp);
		}
		//Time
		pass=ft.getTime()/(double)ft.getNeedTime();
		temp=Items.createPItem((short)5, Msg.getMsg("FurnaceTimeProgress", new String[]{"%timeProgress%","%time%"},
				new String[]{Util.RDP(pass*100,2),(ft.getNeedTime()-ft.getTime())+""},false));
		if(pass>=0)
			inv.setItem(37, temp );
		if(pass>=0.5)
			inv.setItem(28,  temp);
		if(pass>=0.75)
			inv.setItem(19,  temp);
		if(pass>=1){
			inv.setItem(10,   temp);
			inv.setItem(49,   Items.createPItem((short)5, Msg.getMsg("WorkbenchProgress2", false)));
		}
		//SaveTime
		if(ft.getTime()>ft.getNeedTime()){
			pass=ft.getTime()/(double)(ft.getNeedTime()+ft.getSaveTime());
			temp=Items.createPItem((short)12, Msg.getMsg("FuranceSaveProgress", new String[]{"%time%"},
					new String[]{(ft.getNeedTime()+ft.getSaveTime()-ft.getTime())+""},false));
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
