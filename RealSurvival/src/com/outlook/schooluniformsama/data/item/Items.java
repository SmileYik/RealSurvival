package com.outlook.schooluniformsama.data.item;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.util.HashMap;
import com.outlook.schooluniformsama.util.Msg;

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
		water.set(name, is.clone());
		water.save(new File(Data.DATAFOLDER+"/items/waters.yml"));
		water = YamlConfiguration.loadConfiguration(new File(Data.DATAFOLDER+"/items/waters.yml"));
	}
	
	public boolean createWater(){
		File waterFile=new File(Data.DATAFOLDER+"/items/waters.yml");
		if(waterFile.exists())return true;
		YamlConfiguration data = YamlConfiguration.loadConfiguration(waterFile);
		
		ItemStack is=new ItemStack(Material.POTION);
		ItemMeta im=is.getItemMeta();
		
		im.setDisplayName(Msg.getMsg("Seawater", false));
		is.setItemMeta(im);
		new NBTItemData(-20, ItemLoreData.badCode(), ItemLoreData.badCode(), 2, new HashMap<>(), new LinkedList<>(), 
				ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode()).save("Seawater");
		data.set("Seawater",Data.nbtitem.addNBT(is.clone(), "RealSurvival", "Seawater") );
		
		im.setDisplayName(Msg.getMsg("IceSeawater", false));
		is.setItemMeta(im);
		new NBTItemData(-20, ItemLoreData.badCode(), ItemLoreData.badCode(), 2, new HashMap<>(), new LinkedList<>(), 
				ItemLoreData.badCode(), ItemLoreData.badCode(), -0.5, ItemLoreData.badCode()).save("IceSeawater");
		data.set("IceSeawater",Data.nbtitem.addNBT(is.clone(), "RealSurvival", "IceSeawater") );
		
		im.setDisplayName(Msg.getMsg("SwampWater", false));
		is.setItemMeta(im);
		new NBTItemData(5, ItemLoreData.badCode(), ItemLoreData.badCode(), 2, new HashMap<>(), new LinkedList<>(), 
				ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode()).save("SwampWater");
		data.set("SwampWater",Data.nbtitem.addNBT(is.clone(), "RealSurvival", "SwampWater") );
		
		im.setDisplayName(Msg.getMsg("LakeWater", false));
		is.setItemMeta(im);
		new NBTItemData(15, ItemLoreData.badCode(), ItemLoreData.badCode(), 2, new HashMap<>(), new LinkedList<>(), 
				ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode()).save("LakeWater");
		data.set("LakeWater",Data.nbtitem.addNBT(is.clone(), "RealSurvival", "LakeWater") );
		
		im.setDisplayName(Msg.getMsg("IceLakeWater", false));
		is.setItemMeta(im);
		new NBTItemData(15, ItemLoreData.badCode(), ItemLoreData.badCode(), 2, new HashMap<>(), new LinkedList<>(), 
				ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode()).save("IceLakeWater");
		data.set("IceLakeWater",Data.nbtitem.addNBT(is.clone(), "RealSurvival", "IceLakeWater") );
		
		im.setDisplayName(Msg.getMsg("HotLakeWater", false));
		is.setItemMeta(im);
		new NBTItemData(15, ItemLoreData.badCode(), ItemLoreData.badCode(), 2, new HashMap<>(), new LinkedList<>(), 
				ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode()).save("HotLakeWater");
		data.set("HotLakeWater",Data.nbtitem.addNBT(is.clone(), "RealSurvival", "HotLakeWater") );
		
		im.setDisplayName(Msg.getMsg("Freshwater", false));
		is.setItemMeta(im);
		new NBTItemData(30, ItemLoreData.badCode(), ItemLoreData.badCode(), 2, new HashMap<>(), new LinkedList<>(), 
				ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode()).save("Freshwater");
		data.set("Freshwater",Data.nbtitem.addNBT(is.clone(), "RealSurvival", "Freshwater") );
		
		im.setDisplayName(Msg.getMsg("HotWater", false));
		is.setItemMeta(im);
		new NBTItemData(30, ItemLoreData.badCode(), ItemLoreData.badCode(), 2, new HashMap<>(), new LinkedList<>(), 
				ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode()).save("HotWater");
		data.set("HotWater",Data.nbtitem.addNBT(is.clone(), "RealSurvival", "HotWater") );
		
		im.setDisplayName(Msg.getMsg("IceWater", false));
		is.setItemMeta(im);
		new NBTItemData(30, ItemLoreData.badCode(), ItemLoreData.badCode(), 2, new HashMap<>(), new LinkedList<>(), 
				ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode()).save("IceWater");
		data.set("IceWater",Data.nbtitem.addNBT(is.clone(), "RealSurvival", "IceWater") );
		
		im.setDisplayName(Msg.getMsg("Rainwater", false));
		is.setItemMeta(im);
		new NBTItemData(23, ItemLoreData.badCode(), ItemLoreData.badCode(), 2, new HashMap<>(), new LinkedList<>(), 
				ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode()).save("Rainwater");
		data.set("Rainwater",Data.nbtitem.addNBT(is.clone(), "RealSurvival", "Rainwater") );
		
		try {
			data.save(waterFile);
		} catch (IOException e) {
			return false;
		}
		return true;
	}
}
