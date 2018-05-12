package com.outlook.schooluniformsama.lowversion.converter;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class WorkbenchRecipe {
	private long builtTime;
	private String name;
	private HashMap<Integer,ItemStack> material;
	private HashMap<Integer,ItemStack> product;
	
	public WorkbenchRecipe(String name,  HashMap<Integer, ItemStack> material,
			HashMap<Integer, ItemStack> product) {
		super();
		this.name = name;
		this.material = material;
		this.product = product;
	}

	public WorkbenchRecipe(String name,HashMap<Integer, ItemStack> material,
			HashMap<Integer, ItemStack> product,long builtTime) {
		super();
		this.name = name;
		this.material = material;
		this.product = product;
		this.builtTime=builtTime;
	}
	
	public Inventory setInv(Inventory inv){
		for(Entry<Integer, ItemStack> entity:material.entrySet())
			inv.setItem(entity.getKey(), entity.getValue());
		for(Entry<Integer, ItemStack> entity:product.entrySet())
			inv.setItem(entity.getKey(), entity.getValue());
		return inv;
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
