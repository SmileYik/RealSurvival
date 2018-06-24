package com.outlook.schooluniformsama.data.item;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.util.Util;

public class RSItem {
	private ItemStack item;
	public RSItem(ItemStack item){
		this.item=item;
	}
	
	/*
	 * 
	- "Thirst:Thirst"
    - "Sleep:Sleep"
    - "Energy:Energy"
    - "Weight:Weight"
    - "Illness:IllnessName"
    - "Treatable:Treatable"
    - "DrugEffect:Drug Effect"
    - "Temperature:Temperature"
    - "IllnessProbability:Illness Probability"
    - "MedicineDuration:Medicine Duration"
    - "Hungery:Hungry"
	 * 
	 * 
	 */
	
	public static RSItem loadItem(String name){
		ItemStack is;
		if(name.contains("/"))
			is = YamlConfiguration.loadConfiguration(new File(Data.DATAFOLDER+"/items/"+name+".yml")).getItemStack(name.substring(name.lastIndexOf("/")+1),null);
		else if(name.contains("\\"))
			is = YamlConfiguration.loadConfiguration(new File(Data.DATAFOLDER+"/items/"+name+".yml")).getItemStack(name.substring(name.lastIndexOf("\\")+1),null);
		else 
			is = YamlConfiguration.loadConfiguration(new File(Data.DATAFOLDER+"/items/"+name+".yml")).getItemStack(name,null);
		if(is==null)return null;
		if(!is.hasItemMeta() || !is.getItemMeta().hasLore())return new RSItem(is);
		ItemMeta im=is.getItemMeta();
		List<String> temp = im.getLore();
		List<String>lore = new LinkedList<>();
		for(String line:temp){
			for(String str:Data.label.keySet())
				line=line.replaceAll("%"+str+"%", Data.label.get(str));
			line=line.replaceAll("%split%", Data.split);
			lore.add(line);
		}
		im.setLore(lore);
		is.setItemMeta(im);
		return new RSItem(is);
	}
	
	public static RSItem loadItem(String path,String name){
		ItemStack is;
		if(name.contains("/"))
			is = YamlConfiguration.loadConfiguration(new File(path+name+".yml")).getItemStack(name.substring(name.lastIndexOf("/")+1),null);
		else if(name.contains("\\"))
			is = YamlConfiguration.loadConfiguration(new File(path+name+".yml")).getItemStack(name.substring(name.lastIndexOf("\\")+1),null);
		else 
			is = YamlConfiguration.loadConfiguration(new File(path+name+".yml")).getItemStack(name,null);
		if(is==null)return null;
		if(!is.hasItemMeta() || !is.getItemMeta().hasLore())return new RSItem(is);
		ItemMeta im=is.getItemMeta();
		List<String> temp = im.getLore();
		List<String>lore = new LinkedList<>();
		for(String line:temp){
			for(String str:Data.label.keySet())
				line=line.replaceAll("%"+str+"%", Data.label.get(str));
			line=line.replaceAll("%split%", Data.split);
			lore.add(line);
		}
		im.setLore(lore);
		is.setItemMeta(im);
		return new RSItem(is);
	}
	
	public static RSItem loadItem(ItemStack is){
		if(is==null)return null;
		if(!is.hasItemMeta() || !is.getItemMeta().hasLore())return new RSItem(is);
		ItemMeta im=is.getItemMeta();
		List<String> temp = im.getLore();
		List<String>lore = new LinkedList<>();
		for(String line:temp){
			for(String str:Data.label.keySet())
				line=line.replaceAll("%"+str+"%", Data.label.get(str));
			line=line.replaceAll("%split%", Data.split);
			lore.add(line);
		}
		im.setLore(lore);
		is.setItemMeta(im);
		return new RSItem(is);
	}
	
	
	/*
	 * -1 Save Failed
	 * 0 The File was exists
	 * 1 Successed
	 */
	public int save(){
		if(new File(Data.DATAFOLDER+"/items/"+getName()+".yml").exists())
			return 0;
		YamlConfiguration itemData=YamlConfiguration.loadConfiguration(new File(Data.DATAFOLDER+"/items/"+getName()+".yml"));
		ItemStack item=this.item.clone();
		ItemMeta im=item.getItemMeta();
		List<String> temp = im.hasLore()?im.getLore():new LinkedList<>();
		List<String>lore = new LinkedList<>();
		for(String line:temp){
			for(Entry<String, String> str:Data.label.entrySet())
				line=line.replaceAll(str.getValue(),"%"+str.getKey()+"%" );
			line=line.replaceAll(Data.split,"%split%");
			lore.add(line);
		}
		im.setLore(lore);
		item.setItemMeta(im);

		itemData.set(getName(), item);
		try {
			itemData.save(new File(Data.DATAFOLDER+"/items/"+getName()+".yml"));
		} catch (IOException e) {
			return -1;
		}
		return 1;
	}
	
	public static String getItemPath(String name){
		return Data.DATAFOLDER+"/items/"+name+".yml";
	}
	
	public int save(String name){
		YamlConfiguration itemData=YamlConfiguration.loadConfiguration(new File(Data.DATAFOLDER+"/items/"+name+".yml"));
		ItemStack item=this.item.clone();
		ItemMeta im=item.getItemMeta();
		List<String> temp = im.hasLore()?im.getLore():new LinkedList<>();
		List<String>lore = new LinkedList<>();
		for(String line:temp){
			for(Entry<String, String> str:Data.label.entrySet())
				line=line.replaceAll(str.getValue(),"%"+str.getKey()+"%" );
			line=line.replaceAll(Data.split,"%split%");
			lore.add(line);
		}
		im.setLore(lore);
		item.setItemMeta(im);
		if(name.contains("/"))
			itemData.set(name.substring(name.lastIndexOf("/")+1), item);
		else if(name.contains("\\"))
			itemData.set(name.substring(name.lastIndexOf("\\")+1), item);
		else
			itemData.set(name, item);
		try {
			itemData.save(new File(Data.DATAFOLDER+"/items/"+name+".yml"));
		} catch (IOException e) {
			return -1;
		}
		return 1;
	}
	
	public int save(String path,String name){
		YamlConfiguration itemData=YamlConfiguration.loadConfiguration(new File(path+name+".yml"));
		ItemStack item=this.item.clone();
		ItemMeta im=item.getItemMeta();
		List<String> temp = im.hasLore()?im.getLore():new LinkedList<>();
		List<String>lore = new LinkedList<>();
		for(String line:temp){
			for(Entry<String, String> str:Data.label.entrySet())
				line=line.replaceAll(str.getValue(),"%"+str.getKey()+"%" );
			line=line.replaceAll(Data.split,"%split%");
			lore.add(line);
		}
		im.setLore(lore);
		item.setItemMeta(im);
		if(name.contains("/"))
			itemData.set(name.substring(name.lastIndexOf("/")+1), item);
		else if(name.contains("\\"))
			itemData.set(name.substring(name.lastIndexOf("\\")+1), item);
		else
			itemData.set(name, item);
		try {
			itemData.save(new File(path+name+".yml"));
		} catch (IOException e) {
			return -1;
		}
		return 1;
	}
	
	public String getName(){
		String name;
		if(!item.hasItemMeta() || !item.getItemMeta().hasDisplayName())
			name=item.getType().name();
		else name=item.getItemMeta().getDisplayName();
		return Util.removeColor(name);
	}

	public ItemStack getItem() {
		return item;
	}

	public ItemStack getSaveItem() {
		ItemStack item=this.item.clone();
		ItemMeta im=item.getItemMeta();
		List<String> temp = im.hasLore()?im.getLore():new LinkedList<>();
		List<String>lore = new LinkedList<>();
		for(String line:temp){
			for(Entry<String, String> str:Data.label.entrySet())
				line=line.replaceAll(str.getValue(),"%"+str.getKey()+"%" );
			line=line.replaceAll(Data.split,"%split%");
			lore.add(line);
		}
		im.setLore(lore);
		item.setItemMeta(im);
		return item;
	}
	
	public void setItem(ItemStack item) {
		this.item = item;
	}
	
	
}
