package com.outlook.z815007320.gui;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.outlook.z815007320.data.Items;
import com.outlook.z815007320.data.PluginRS;
import com.outlook.z815007320.data.WorkbenchRecipe;

public class CraftItemGUI extends PluginRS{
	public static final  List<Integer> materials = Arrays.asList(10,11,12,13,19,20,21,22,28,29,30,31,37,38,39,40);
	public static final  List<Integer> products = Arrays.asList(24,25,33,34);
	
	//24 25 33 34
	//10 11 12 13 19 20 21 22 28 29 30 31 37 38 39 40
	
	private static Inventory createDefaultGUI(String title){
		Inventory inv=Bukkit.createInventory(null, 54,title);
		for(int i=0;i<54;i++)
			if(!(materials.contains(i)||products.contains(i)))
				inv.setItem(i, Items.createPItem((short)15, "§7§l讨厌啦~不要总是看着人家啦~"));
		return inv;
	}
	
	public static Inventory createWorkbenchGUI(ItemStack itemKey){
		Inventory inv=createDefaultGUI(rs.getWorkbenchItems()[7]);
		inv.setItem(0, itemKey);
		inv.setItem(49, Items.createPItem((short) 14, "§b§l==>§c§l开始制造§b§l<=="));
		return inv;
	}
	
	public static Inventory createWorkbenchRecipeGUI(ItemStack itemKey){
		Inventory inv=createDefaultGUI(rs.getWorkbenchItems()[7]+"*");
		inv.setItem(0, itemKey);
		inv.setItem(49, Items.createPItem((short) 14, "§b§l==>§c§l保存§b§l<=="));
		return inv;
	}
	
	public static Inventory resetSF(ItemStack itemKey){
		WorkbenchRecipe sf=rs.getSF(itemKey.getItemMeta().getDisplayName().split(":")[0]);
		Inventory inv=createDefaultGUI(rs.getWorkbenchItems()[7]+"*");
		inv.setItem(0, itemKey);
		inv.setItem(49, Items.createPItem((short) 14, "§b§l==>§c§l保存§b§l<=="));
		return sf.setInv(inv);
	}

	public static Inventory openWorkbenchGUI(ItemStack itemKey){
		Inventory inv=createDefaultGUI(rs.getWorkbenchItems()[7]+"§3§l*");
		WorkbenchRecipe wr=rs.getSF(rs.getWorkbench(itemKey.getItemMeta().getDisplayName(), 1));
		inv.setItem(0, itemKey);
		return checkPass(wr.setInv(inv),rs.getWorkbench(itemKey.getItemMeta().getDisplayName(), 2),wr);
	}
	
	public static  Inventory checkPass(Inventory inv,String str,WorkbenchRecipe wr){
		double x=0;
		if(wr.getBuiltTime()==0L)
			x=1;
		else
			x=Double.parseDouble(str)/(double)wr.getBuiltTime();
		
		if(x>=1)
			inv.setItem(49, Items.getPlaceholder10((short)5,  100));
		else
			inv.setItem(49, Items.getPlaceholder10((short)14, (int) (x*100)));
	
		return inv;
	}
	
	public Inventory sFWatcher(WorkbenchRecipe sF){
		Inventory inv =createDefaultGUI( "§9§l配方 - "+sF.getName());
		inv=sF.setInv(inv);
		inv.setItem(45, Items.getPlaceholder6());
		return inv;
	}
	
	public  Inventory sFList(int start,Player p){
		Inventory inv = Bukkit.createInventory(null,54 , "§9§l 配方列表");
		for(WorkbenchRecipe sf:rs.getSF()){
			if(!(p.hasPermission("RealSurvival.Items."+sf.getName())||(
					p.hasPermission("RealSurvival.Items.*")&&p.hasPermission("RealSurvival.Items."+sf.getName())))) continue;
			if(sf==null||sf.getProduct().get(24)==null)
				inv.addItem(Items.getPlaceholder9(sf.getName(),new ItemStack(Material.BOOK).clone(),sf.getBuiltTime()));
			else
				inv.addItem(Items.getPlaceholder9(sf.getName(),sf.getProduct().get(24).clone(),sf.getBuiltTime()));
		}
		if(rs.getSF().size()>54)
			inv.setItem(53, Items.getPlaceholder7());
		return inv;
	}
}
