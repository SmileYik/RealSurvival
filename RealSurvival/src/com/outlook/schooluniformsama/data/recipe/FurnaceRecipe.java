package com.outlook.schooluniformsama.data.recipe;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.Items;
import com.outlook.schooluniformsama.data.recipe.Recipe;
import com.outlook.schooluniformsama.data.recipe.WorkbenchType;
import com.outlook.schooluniformsama.gui.FurnaceGUI;

public class FurnaceRecipe extends Recipe{
	private int saveTime;
	private double minTemperature;
	private double maxTemperature;
	private HashMap<Character,ItemStack> materials;
	private ItemStack[] product;
	
	public FurnaceRecipe(String name,int needTime,int saveTime,double minTemperature,
			double maxTemperature,List<String> shape, HashMap<Character, ItemStack> materials,ItemStack[] product){
		super(name,  WorkbenchType.FURNACE,shape,needTime);
		this.materials = materials;
		this.product = product;
		this.saveTime=saveTime;
		this.minTemperature=minTemperature;
		this.maxTemperature=maxTemperature;
		
	}
	
	public FurnaceRecipe(String name,int needTime,int saveTime,double minTemperature,double maxTemperature){
		super(name, WorkbenchType.FURNACE,needTime);
		this.saveTime=saveTime;
		this.minTemperature=minTemperature;
		this.maxTemperature=maxTemperature;
		
	}
	
	public boolean save(){
		YamlConfiguration recipe;
		 File f=new File(Data.DATAFOLDER+File.separator+"recipe"+File.separator+"furnace"+File.separator+name+".yml");
		if(!f.exists())try {f.createNewFile();} catch (IOException e1) {return false;}
		
		recipe=YamlConfiguration.loadConfiguration(f);
		recipe.set(name+".type", type.name());
		recipe.set(name+".name", name);
		recipe.set(name+".needTime", needTime);
		recipe.set(name+".saveTime", saveTime);
		recipe.set(name+".minTemperature", minTemperature);
		recipe.set(name+".maxTemperature", maxTemperature);
		recipe.set(name+".shape", shape);
		for(Character c:materials.keySet())
			recipe.set(name+".material."+c,materials.get(c) );
		for(int i=0;i<3;i++)
			recipe.set(name+".product."+i, product[i]);
		try {recipe.save(f);} catch (IOException e) {return false;}
		return true;
	}
	
	public static FurnaceRecipe load(String name){
		 File f=new File(Data.DATAFOLDER+File.separator+"recipe"+File.separator+"furnace"+File.separator+name+".yml");
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
		 
		 return new FurnaceRecipe(name,recipe.getInt(name+".needTime"),recipe.getInt(name+".saveTime"),
				 recipe.getDouble(name+".minTemperature"),recipe.getDouble(name+".maxTemperature"),
				 recipe.getStringList(name+".shape"), materials, product);
	}
	
	public static boolean createFurnaceRecipe(Inventory inv,FurnaceRecipe fr){
		short index=0;
		char charArray[]="ABCDEFGHIJKLMNOP".toCharArray();
		String s="";
		HashMap<ItemStack,Character> m=new HashMap<>();
		ItemStack is=inv.getItem(FurnaceGUI.mSlot.get(0));
		if(new File(Data.DATAFOLDER+File.separator+"recipe"+File.separator+"furnace"+File.separator+fr.name+".yml").exists())
			return false;
		
		if(is!=null)
			m.put(is,charArray[index]);

		for(int i:FurnaceGUI.mSlot){
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
		
		FurnaceRecipe wr=new FurnaceRecipe(fr.name ,fr.needTime,fr.saveTime,fr.minTemperature,fr.maxTemperature,
				Arrays.asList(s.substring(0,3),s.substring(3, 6)),m2, new ItemStack[]{inv.getItem(FurnaceGUI.pSlot.get(0)),
						inv.getItem(FurnaceGUI.pSlot.get(1)),inv.getItem(FurnaceGUI.pSlot.get(2))});
		return wr.save();
		
	}
	
	public boolean containsShape(Inventory inv){
		short index=0;
		for(String s:shape)
			for(char c:s.toCharArray()){
				if(c==' ')
					if(inv.getItem(FurnaceGUI.mSlot.get(index++))==null)
						continue;
					else 
						return false;
				else{
					ItemStack is=inv.getItem(FurnaceGUI.mSlot.get(index++));
					if(is==null)return false;
					if(is.equals(materials.get(c)))
						continue;
					else 
						return false;
				}
			}
		return true;
	}
	
	public boolean isTrue(Inventory inv){
		short index=0;
		for(String s:shape)
			for(char c:s.toCharArray()){
				if(c==' ')
					if(inv.getItem(FurnaceGUI.mSlot.get(index++))==null)
						continue;
					else 
						return false;
				else{
					ItemStack is=inv.getItem(FurnaceGUI.mSlot.get(index++));
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
				inv.setItem(FurnaceGUI.mSlot.get(index++), materials.get(c));
			}
		index=0;
		while(index!=3)
			inv.setItem(FurnaceGUI.pSlot.get(index), product[index++]);
		inv.setItem(4, Items.createPItem((short)0, " "));
		return inv;
	}
	
/*	public static double getBlocks(Location l){
		//5x5x5
		double _tem=0;
		int x=l.getBlockX()+(int)((Data.L-1)*0.5);
		int y=l.getBlockY()+(int)((Data.H-1)*0.5);
		int z=l.getBlockZ()+(int)((Data.W-1)*0.5);
		
		for(int sx=x-(Data.L-1);sx<x;sx++)
			for(int sy=y-(Data.H-1);sy<y;sy++)
				for(int sz=z-(Data.W-1);sz<z;sz++){
					Block temp=l.getWorld().getBlockAt(sx, sy, sz);
					if(Data.HEATSOURCE.containsKey(temp.getType().name()))
						_tem+=Data.HEATSOURCE.get(temp.getType().name())*Math.pow(
								1-Data.DISTANCEEFFECT, Math.sqrt(Math.pow(sx-x, 2)+Math.pow(
										sy-y, 2)+Math.pow(sz-z, 2)));
				}
		return _tem;
	}*/

	public int getSaveTime() {
		return saveTime;
	}

	public void setSaveTime(int saveTime) {
		this.saveTime = saveTime;
	}

	public double getMinTemperature() {
		return minTemperature;
	}

	public void setMinTemperature(double minTemperature) {
		this.minTemperature = minTemperature;
	}

	public double getMaxTemperature() {
		return maxTemperature;
	}

	public void setMaxTemperature(double maxTemperature) {
		this.maxTemperature = maxTemperature;
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
