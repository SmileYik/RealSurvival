package com.outlook.schooluniformsama.data.item;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.outlook.schooluniformsama.I18n;
import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.nms.NBTItem;
import com.outlook.schooluniformsama.util.HashMap;

public class Items{
	private static YamlConfiguration water ;
	
	public static ItemStack createPItem(short damage,String name){
		ItemStack is=new ItemStack(Material.STAINED_GLASS_PANE,1,damage);
		ItemMeta im=is.getItemMeta();
		im.setDisplayName(name);
		is.setItemMeta(im);
		return is;
	}
	
	public static ItemStack getWater(String name){
		if(water==null)water = YamlConfiguration.loadConfiguration(new File(Data.DATAFOLDER+"/items/waters.yml"));
		return water.getItemStack(name);
	}
	
	public static void setWater(String name,ItemStack is) throws IOException{
		if(water==null)water = YamlConfiguration.loadConfiguration(new File(Data.DATAFOLDER+"/items/waters.yml"));
		water.set(name, new RSItem(is.clone()).getSaveItem());
		water.save(new File(Data.DATAFOLDER+"/items/waters.yml"));
		water = YamlConfiguration.loadConfiguration(new File(Data.DATAFOLDER+"/items/waters.yml"));
	}
	
	public boolean createWater(){
		File waterFile=new File(Data.DATAFOLDER+"/items/waters.yml");
		if(waterFile.exists())return true;
		YamlConfiguration data = YamlConfiguration.loadConfiguration(waterFile);
		
		ItemStack is=new ItemStack(Material.POTION);
		ItemMeta im=is.getItemMeta();
		
		im.setDisplayName(I18n.tr("water1"));
		is.setItemMeta(im);
		new NBTItemData(-20, ItemLoreData.badCode(), ItemLoreData.badCode(), 2, new HashMap<>(), new LinkedList<>(), 
				ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode()).save("Seawater");
		data.set("Seawater",NBTItem.setNBT(is.clone(), "RealSurvival", "Seawater") );
		
		im.setDisplayName(I18n.tr("water2"));
		is.setItemMeta(im);
		new NBTItemData(-20, ItemLoreData.badCode(), ItemLoreData.badCode(), 2, new HashMap<>(), new LinkedList<>(), 
				ItemLoreData.badCode(), ItemLoreData.badCode(), -0.5, ItemLoreData.badCode()).save("IceSeawater");
		data.set("IceSeawater",NBTItem.setNBT(is.clone(), "RealSurvival", "IceSeawater") );
		
		im.setDisplayName(I18n.tr("water3"));
		is.setItemMeta(im);
		new NBTItemData(5, ItemLoreData.badCode(), ItemLoreData.badCode(), 2, new HashMap<>(), new LinkedList<>(), 
				ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode()).save("SwampWater");
		data.set("SwampWater",NBTItem.setNBT(is.clone(), "RealSurvival", "SwampWater") );
		
		im.setDisplayName(I18n.tr("water4"));
		is.setItemMeta(im);
		new NBTItemData(15, ItemLoreData.badCode(), ItemLoreData.badCode(), 2, new HashMap<>(), new LinkedList<>(), 
				ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode()).save("LakeWater");
		data.set("LakeWater",NBTItem.setNBT(is.clone(), "RealSurvival", "LakeWater") );
		
		im.setDisplayName(I18n.tr("water5"));
		is.setItemMeta(im);
		new NBTItemData(15, ItemLoreData.badCode(), ItemLoreData.badCode(), 2, new HashMap<>(), new LinkedList<>(), 
				ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode()).save("IceLakeWater");
		data.set("IceLakeWater",NBTItem.setNBT(is.clone(), "RealSurvival", "IceLakeWater") );
		
		im.setDisplayName(I18n.tr("water6"));
		is.setItemMeta(im);
		new NBTItemData(15, ItemLoreData.badCode(), ItemLoreData.badCode(), 2, new HashMap<>(), new LinkedList<>(), 
				ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode()).save("HotLakeWater");
		data.set("HotLakeWater",NBTItem.setNBT(is.clone(), "RealSurvival", "HotLakeWater") );
		
		im.setDisplayName(I18n.tr("water7"));
		is.setItemMeta(im);
		new NBTItemData(30, ItemLoreData.badCode(), ItemLoreData.badCode(), 2, new HashMap<>(), new LinkedList<>(), 
				ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode()).save("Freshwater");
		data.set("Freshwater",NBTItem.setNBT(is.clone(), "RealSurvival", "Freshwater") );
		
		im.setDisplayName(I18n.tr("water8"));
		is.setItemMeta(im);
		new NBTItemData(30, ItemLoreData.badCode(), ItemLoreData.badCode(), 2, new HashMap<>(), new LinkedList<>(), 
				ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode()).save("HotWater");
		data.set("HotWater",NBTItem.setNBT(is.clone(), "RealSurvival", "HotWater") );
		
		im.setDisplayName(I18n.tr("water9"));
		is.setItemMeta(im);
		new NBTItemData(30, ItemLoreData.badCode(), ItemLoreData.badCode(), 2, new HashMap<>(), new LinkedList<>(), 
				ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode()).save("IceWater");
		data.set("IceWater",NBTItem.setNBT(is.clone(), "RealSurvival", "IceWater") );
		
		im.setDisplayName(I18n.tr("water10"));
		is.setItemMeta(im);
		new NBTItemData(23, ItemLoreData.badCode(), ItemLoreData.badCode(), 2, new HashMap<>(), new LinkedList<>(), 
				ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode()).save("Rainwater");
		data.set("Rainwater",NBTItem.setNBT(is.clone(), "RealSurvival", "Rainwater") );
		
		try {
			data.save(waterFile);
		} catch (IOException e) {
			return false;
		}
		return true;
	}
}
