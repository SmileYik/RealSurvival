package com.outlook.z815007320.data;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.outlook.z815007320.RealSurvival;
import com.outlook.z815007320.gui.CraftItemGUI;

public class WorkbenchRecipe {
	private long builtTime;
	private String name;
	private RealSurvival plug;
	private HashMap<Integer,ItemStack> material;
	private HashMap<Integer,ItemStack> product;
	
	public void save(){
		YamlConfiguration DIYSF;
		 File f=new File(plug.getDataFolder()+File.separator+"SyntheticFormula"+File.separator+name+".yml");
			if(!f.exists())
				try {f.createNewFile();} catch (IOException e1) {}
		 DIYSF=YamlConfiguration.loadConfiguration(f);
		 DIYSF.set(name+".name",name );
		 DIYSF.set(name+".builtTime",builtTime );

		 for(int key:CraftItemGUI.materials)
			DIYSF.set(name+".material."+key, material.get(key));
		 
		 for(int key:CraftItemGUI.products)
			DIYSF.set(name+".product."+key, product.get(key));
		 
		try {
			DIYSF.save(f);
		} catch (IOException e) {}
	}
	
	public Inventory setInv(Inventory inv){
		for(Entry<Integer, ItemStack> entity:material.entrySet())
			inv.setItem(entity.getKey(), entity.getValue());
		for(Entry<Integer, ItemStack> entity:product.entrySet())
			inv.setItem(entity.getKey(), entity.getValue());
		return inv;
	}
	
	public WorkbenchRecipe(String name, RealSurvival plug, HashMap<Integer, ItemStack> material,
			HashMap<Integer, ItemStack> product) {
		super();
		this.name = name;
		this.plug = plug;
		this.material = material;
		this.product = product;
	}

	public WorkbenchRecipe(String name, RealSurvival plug, HashMap<Integer, ItemStack> material,
			HashMap<Integer, ItemStack> product,long builtTime) {
		super();
		this.name = name;
		this.plug = plug;
		this.material = material;
		this.product = product;
		this.builtTime=builtTime;
	}
	
	public long getBuiltTime() {
		return builtTime;
	}

	public void setBuiltTime(long builtTime) {
		this.builtTime = builtTime;
	}

	public HashMap<Integer, ItemStack> getMaterial() {
		return material;
	}

	public void setMaterial(HashMap<Integer, ItemStack> material) {
		this.material = material;
	}

	public HashMap<Integer, ItemStack> getProduct() {
		return product;
	}

	public void setProduct(HashMap<Integer, ItemStack> product) {
		this.product = product;
	}
	
	public boolean compare(HashMap<Integer,ItemStack> material){
		return this.material.equals(material);
	}

	public String getName() {
		return name;
	}
	
}
