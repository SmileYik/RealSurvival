package com.outlook.z815007320.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.outlook.z815007320.gui.FireCraftTableGUI;


public class FireCraftTableRecipe extends PluginRS{
	private int time;
	private double temperature;
	private int maxTime;
	private String name;
	private List<String> shape=new ArrayList<>();
	private HashMap<Character,ItemStack> materials=new HashMap<>();
	private ItemStack[] product=new ItemStack[3];
	
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

	public boolean save(){
		YamlConfiguration recipe;
		 File f=new File(rs.getDataFolder()+File.separator+"SyntheticFormula"+File.separator+"FireCraftTable"+File.separator+name+".yml");
		if(!f.exists())try {f.createNewFile();} catch (IOException e1) {return false;}
		
		recipe=YamlConfiguration.loadConfiguration(f);
		recipe.set(name+".name", name);
		recipe.set(name+".time", time);
		recipe.set(name+".temperature", temperature);
		recipe.set(name+".maxTime", maxTime);
		recipe.set(name+".shape", shape);
		for(Character c:materials.keySet())
			recipe.set(name+".material."+c,materials.get(c) );
		for(int i=0;i<3;i++)
			recipe.set(name+".product."+i, product[i]);
		try {recipe.save(f);} catch (IOException e) {return false;}
		return true;
	}
	
	public static FireCraftTableRecipe load(String name){
		 File f=new File(rs.getDataFolder()+File.separator+"SyntheticFormula"+File.separator+"FireCraftTable"+File.separator+name+".yml");
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
	
	public static boolean createFireCraftTableRecipe(Inventory inv){
		short index=0;
		char charArray[]="ABCDEFGHIJKLMNOP".toCharArray();
		int time=Integer.parseInt(inv.getItem(0).getItemMeta().getDisplayName().split(":")[1]);
		String s="";
		HashMap<ItemStack,Character> m=new HashMap<>();
		ItemStack is=inv.getItem(FireCraftTableGUI.mSlot.get(0));
		String name=inv.getItem(0).getItemMeta().getDisplayName().split(":")[0];
		String value=inv.getItem(0).getItemMeta().getDisplayName();//name;time;maxtem;tem
		if(new File(rs.getDataFolder()+File.separator+"SyntheticFormula"+File.separator+"FireCraftTable"+File.separator+name+".yml").exists())
			return false;
		
		if(is!=null)
			m.put(is,charArray[index]);

		for(int i:FireCraftTableGUI.mSlot){
			ItemStack temp=inv.getItem(i);
			if(temp==null){
				s+=" ";
				continue;
			}
			if(!temp.equals(is)){
				if(m.containsKey(temp))
					s+=m.get(temp);
				else{
					is=temp;
					index++;
					s+=charArray[index];
					m.put(is,charArray[index]);
				}
			}else{
				s+=charArray[index];
			}
		}
		
		HashMap<Character,ItemStack> m2=new HashMap<>();
		for(Entry<ItemStack, Character> entity:m.entrySet())
			m2.put(entity.getValue(), entity.getKey());
		
		FireCraftTableRecipe wr=new FireCraftTableRecipe(time,Double.parseDouble(value.split(":")[3]),Integer.parseInt(value.split(":")[2])
				,name, Arrays.asList(s.substring(0,3),s.substring(3, 6)),m2, new ItemStack[]{inv.getItem(FireCraftTableGUI.pSlot.get(0)),
						inv.getItem(FireCraftTableGUI.pSlot.get(1)),inv.getItem(FireCraftTableGUI.pSlot.get(2))});
		return wr.save();
		
	}
	
	public FireCraftTableRecipe containsShape(Inventory inv){
		short index=0;
		char charArray[]="ABCDEFGHIJKLMNOP".toCharArray();
		String s="";
		HashMap<ItemStack,Character> m=new HashMap<>();
		ItemStack is=inv.getItem(FireCraftTableGUI.mSlot.get(0));
		if(is!=null)
			m.put(is,charArray[index]);

		for(int i:FireCraftTableGUI.mSlot){
			ItemStack temp=inv.getItem(i);
			if(temp==null){
				s+=" ";
				continue;
			}
			if(!temp.equals(is)){
				if(m.containsKey(temp))
					s+=m.get(temp);
				else{
					is=temp;
					index++;
					s+=charArray[index];
					m.put(is,charArray[index]);
				}
			}else{
				s+=charArray[index];
			}
		}
		
		if(!shape.equals(Arrays.asList(s.substring(0,3),s.substring(3, 6))))return null;
		if(materials.size()!=m.size())return null;
		HashMap<Character,ItemStack> m2=new HashMap<>();
		for(Entry<ItemStack, Character> entity:m.entrySet())
			m2.put(entity.getValue(), entity.getKey());
		if(!materials.equals(m2))return null;
		return this;
	}
	
	public boolean isTrue(Inventory inv){
		short index=0;
		for(String s:shape)
			for(char c:s.toCharArray()){
				if(c==' ')
					if(inv.getItem(FireCraftTableGUI.mSlot.get(index++))==null)
						continue;
					else 
						return false;
				else{
					ItemStack is=inv.getItem(FireCraftTableGUI.mSlot.get(index++));
					if(is==null)return false;
					if(is.equals(materials.get(c)))
						continue;
					else 
						return false;
				}
			}
		return true;
	}
	
	public Inventory setInv(Inventory inv){
		short index=0;
		for(String s:shape)
			for(char c:s.toCharArray()){
				if(c==' '){
					index++;
					continue;
				}
				inv.setItem(FireCraftTableGUI.mSlot.get(index++), materials.get(c));
			}
		index=0;
		while(index!=3)
			inv.setItem(FireCraftTableGUI.pSlot.get(index), product[index++]);
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
