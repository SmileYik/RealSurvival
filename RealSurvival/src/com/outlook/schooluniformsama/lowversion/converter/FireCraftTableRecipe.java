package com.outlook.schooluniformsama.lowversion.converter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.outlook.schooluniformsama.data.Data;


public class FireCraftTableRecipe{
	private int time;
	private double temperature;
	private int maxTime;
	private String name;
	private List<String> shape=new ArrayList<>();
	private HashMap<Character,ItemStack> materials=new HashMap<>();
	private ItemStack[] product=new ItemStack[3];
	
	public  final List<Integer> mSlot=Arrays.asList(12,13,14,21,22,23);
	public  final List<Integer> pSlot=Arrays.asList(39,40,41);
	public  final List<Integer> tSlot=Arrays.asList(16,25,34,43);
	public  final List<Integer> passSlot=Arrays.asList(10,19,28,37);
	
	public FireCraftTableRecipe(int time, double temperature, int maxTime, String name, List<String> shape,
			HashMap<Character, ItemStack> materials, ItemStack[] product) {
		super();
		this.time = time;
		this.temperature = temperature;
		this.maxTime = maxTime;
		this.name = name;
		this.shape = shape;
		this.materials = materials;
		this.product = product;
	}
	
	public static FireCraftTableRecipe load(String name){
		 File f=new File(Data.DATAFOLDER+File.separator+"SyntheticFormula"+File.separator+"FireCraftTable"+File.separator+name+".yml");
		 if(!f.exists())return null;
		 YamlConfiguration recipe=YamlConfiguration.loadConfiguration(f);
		 HashMap<Character,ItemStack> materials=new HashMap<>();
		 char c=' ';
		 for(String s:recipe.getStringList(name+".shape"))
			 for(char temp:s.toCharArray())
				 if(temp!=' '&&temp!=c){
					 c=temp;
					 materials.put(c, recipe.getItemStack(name+".material."+c));
				 }
		 ItemStack[] product=new ItemStack[4];
		 for(int i=0;i<3;i++)
			 product[i]=recipe.getItemStack(name+".product."+i);
		 
		 return new FireCraftTableRecipe(recipe.getInt(name+".time"),recipe.getDouble(name+".temperature"),recipe.getInt(name+".maxTime"), name,
				 recipe.getStringList(name+".shape"), materials, product);
	}
	
	public Inventory setInv(Inventory inv){
		short index=0;
		for(String s:shape)
			for(char c:s.toCharArray()){
				if(c==' '){
					index++;
					continue;
				}
				inv.setItem(mSlot.get(index++), materials.get(c));
			}
		index=0;
		while(index!=3)
			inv.setItem(pSlot.get(index), product[index++]);
		return inv;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public int getmaxTime() {
		return maxTime;
	}

	public void setmaxTime(int maxTime) {
		this.maxTime = maxTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getShape() {
		return shape;
	}

	public void setShape(List<String> shape) {
		this.shape = shape;
	}

	public HashMap<Character, ItemStack> getMaterials() {
		return materials;
	}

	public void setMaterials(HashMap<Character, ItemStack> materials) {
		this.materials = materials;
	}

	public ItemStack[] getProduct() {
		return product;
	}

	public void setProduct(ItemStack[] product) {
		this.product = product;
	}
}
