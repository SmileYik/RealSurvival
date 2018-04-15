package com.outlook.schooluniformsama.data.recipes;

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
import com.outlook.schooluniformsama.data.item.Items;
import com.outlook.schooluniformsama.data.recipes.Recipe;
import com.outlook.schooluniformsama.data.recipes.WorkbenchType;
import com.outlook.schooluniformsama.gui.Workbench;

public class WorkbenchRecipe extends Recipe{
	private HashMap<Character,ItemStack> materials;
	private ItemStack[] product;
	
	/**
	 * Create a Workbench Recipe
	 * @param name Recipe's Name
	 * @param name Recipe's File
	 * @param type	
	 * @param needTime
	 * @param shape
	 * @param materials
	 * @param product
	 */
	public WorkbenchRecipe(String name,int needTime,
			List<String> shape, HashMap<Character, ItemStack> materials,ItemStack[] product){
		super(name, WorkbenchType.WORKBENCH,shape,needTime);
		this.materials = materials;
		this.product = product;
	}
	
	public WorkbenchRecipe(String name,int needTime){
		super(name, WorkbenchType.WORKBENCH,needTime);
	}
	
	public static String getRecipePath(String recipeName){
		return Data.DATAFOLDER+File.separator+"recipe"+File.separator+"workbench"+File.separator+recipeName+".yml";
	}
	
	public static  String getRecipePath(){
		return Data.DATAFOLDER+File.separator+"recipe"+File.separator+"workbench"+File.separator;
	}
	
	public boolean save(){
		YamlConfiguration recipe;
		File f=new File(Data.DATAFOLDER+File.separator+"recipe"+File.separator+"workbench"+File.separator+name+".yml");
			if(!f.exists())
				try {f.createNewFile();} catch (IOException e1) {return false;}
		recipe=YamlConfiguration.loadConfiguration(f);
		recipe.set(name+".type", type.name());
		recipe.set(name+".name", name);
		recipe.set(name+".needTime", needTime);
		recipe.set(name+".shape", shape);
		for(Character c:materials.keySet())
			recipe.set(name+".material."+c,materials.get(c) );
		for(int i=0;i<4;i++)
			recipe.set(name+".product."+i, product[i]);
		try {recipe.save(f);} catch (IOException e) {return false;}
		return true;
	}
	
	/**
	 * Load a recipe from a file
	 * @param name
	 * @param name
	 * @return
	 */
	public static WorkbenchRecipe load(String name){
		 File f=new File(Data.DATAFOLDER+File.separator+"recipe"+File.separator+"workbench"+File.separator+name+".yml");
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
		 for(int i=0;i<4;i++)
			 product[i]=recipe.getItemStack(name+".product."+i);
		 return new WorkbenchRecipe(name,  recipe.getInt(name+".needTime"), recipe.getStringList(name+".shape"), materials, product);
	}
	
	/**
	 * Create a Workbench's Recipe
	 * @param inv
	 * @return
	 */
	public static boolean createRecipe(Inventory inv,WorkbenchRecipe r){
		short index=0;
		char charArray[]="ABCDEFGHIJKLMNOP".toCharArray();
		String s="";
		HashMap<ItemStack,Character> m=new HashMap<>();
		ItemStack is=inv.getItem(Workbench.materials.get(0));
		if(new File(Data.DATAFOLDER+File.separator+"recipe"+File.separator+"workbench"+File.separator+r.name+".yml").exists())
			return false;
		
		if(is!=null){
			is.setAmount(1);
			m.put(is,charArray[index]);
		}
		
		for(int i:Workbench.materials){
			ItemStack temp=inv.getItem(i);
			if(temp==null){
				s+=" ";
				continue;
			}
			temp.setAmount(1);
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
		
		WorkbenchRecipe wr=new WorkbenchRecipe(r.name,r.needTime, 
				Arrays.asList(s.substring(0,4),s.substring(4, 8),s.substring(8,12),s.substring(12,16)),m2, 
				new ItemStack[]{inv.getItem(Workbench.products.get(0)),inv.getItem(
						Workbench.products.get(1)),inv.getItem(Workbench.products.get(2)),
						inv.getItem(Workbench.products.get(3))});
		
		return wr.save();
		
	}
	
	public boolean containsShape(Inventory inv){
		short index=0;
		for(String s:shape)
			for(char c:s.toCharArray()){
				if(c==' ')
					if(inv.getItem(Workbench.materials.get(index++))==null)
						continue;
					else 
						return false;
				else{
					ItemStack is=inv.getItem(Workbench.materials.get(index++));
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
				inv.setItem(Workbench.materials.get(index++), materials.get(c));
			}
		inv.setItem(16, Items.createPItem((short)15, " "));
		index=0;
		while(index!=4)
			inv.setItem(Workbench.products.get(index), product[index++]);
		return inv;
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
