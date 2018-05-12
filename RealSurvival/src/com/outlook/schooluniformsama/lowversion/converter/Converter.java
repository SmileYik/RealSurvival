package com.outlook.schooluniformsama.lowversion.converter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.recipes.FurnaceRecipe;
import com.outlook.schooluniformsama.gui.Furnace;
import com.outlook.schooluniformsama.gui.Workbench;

public class Converter {
	
	 final  List<Integer> cmaterials = Arrays.asList(10,11,12,13,19,20,21,22,28,29,30,31,37,38,39,40);
	 final  List<Integer> cproducts = Arrays.asList(24,25,33,34);
	
	private LinkedList<String> log = new LinkedList<>();
	
	public void check(){
		checkRecipe();
		
		try {
			new File(Data.DATAFOLDER+File.separator+"recipe/Converter.lock").createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void checkRecipe(){
		File path=new File(Data.DATAFOLDER+File.separator+"SyntheticFormula");
		if(!path.exists())return;
		if(new File(Data.DATAFOLDER+File.separator+"recipe/Converter.lock").exists())return;
		Bukkit.getLogger().info("[RealSurvival] Start conversion recipes.");
		convertWorkbench();
		convertFurnace();
		try {
			File logs = new File(Data.DATAFOLDER+File.separator+"Converter.log");
			logs.createNewFile();
			FileWriter fw = new FileWriter(logs);
			for(String line : log)
				fw.write(line+"\n");
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Bukkit.getLogger().info("[RealSurvival] Conversion over. Log file: "+Data.DATAFOLDER+File.separator+"Converter.log");
	}
	
	private String getLogHead(){
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		return "["+df.format(new Date())+"] > ";
	}
	
	private File moveRecipe(File newFile,String type){
		File dir = new File(Data.DATAFOLDER+File.separator+"recipe/oldrecipe/"+type+"/");
		if(!dir.exists())dir.mkdirs();
		newFile.renameTo(new File(dir+"/"+newFile.getName()));
		return new File(dir+"/"+newFile.getName());
	}
	
	private void convertWorkbench(){
		log.add(getLogHead()+"Start conversion workbench recipes");
		int total=0,su=0,fa=0,skip=0;
		File path=new File(Data.DATAFOLDER+File.separator+"SyntheticFormula");
		for(File sf:path.listFiles()){
			if(!sf.isFile())continue;
			String fileName=sf.getName();
			if(!fileName.substring(fileName.lastIndexOf(".")).equalsIgnoreCase(".yml"))continue;
			try {
				total++;
				File wrFile = new File(path+"/"+fileName);
				WorkbenchRecipe wr = getSF(fileName.substring(0,fileName.lastIndexOf(".")));
				if(wr==null){
					log.add(getLogHead()+"Skip file: \""+wrFile+"\"");
					skip++;
					continue;
				}
				log.add(getLogHead()+"Find the target file: \""+wrFile+"\"");
				log.add(getLogHead()+"Start converting recipe: \""+wrFile+"\"");
				Inventory inv = wr.setInv(Workbench.createWorkbenchGUI("Converter"));
				com.outlook.schooluniformsama.data.recipes.WorkbenchRecipe newWR = new com.outlook.schooluniformsama.data.recipes.WorkbenchRecipe(wr.getName(), (int) wr.getBuiltTime());
				if(com.outlook.schooluniformsama.data.recipes.WorkbenchRecipe.createRecipe(inv, newWR)){
					File newRecipe = new File(Data.DATAFOLDER+File.separator+"recipe"+File.separator+"workbench"+File.separator+newWR.getName()+".yml");
					log.add(getLogHead()+"Conversion successful!!: \""+moveRecipe(newRecipe, "workbench")+"\"");
					su++;
				}
				else{
					log.add(getLogHead()+"Conversion Failed! Maybe you should rename this recipe: \""+wrFile+"\"");
					fa++;
				}
			} catch (Exception e) {
				log.add(getLogHead()+"Load file failed: \""+path+"/"+fileName+"\"");
				fa++;
			}
		}
		
		log.add(getLogHead()+"Tatol: "+total+" , successful!: "+su+" , failed: "+fa+" , skip: "+skip);
	}
	
	private void convertFurnace(){
		log.add(getLogHead()+"Start conversion furnace recipes");
		int total=0,su=0,fa=0,skip=0;
		File path=new File(Data.DATAFOLDER+File.separator+"SyntheticFormula"+File.separator+"FireCraftTable");
		if(!path.exists()){
			log.add(getLogHead()+"Tatol: "+total+" , successful!: "+su+" , failed: "+fa+" , skip: "+skip);
			return;
		}
		
		for(File sf:path.listFiles()){
			if(!sf.isFile())continue;
			String fileName=sf.getName();
			if(!fileName.substring(fileName.lastIndexOf(".")).equalsIgnoreCase(".yml"))continue;
			try {
				File wrFile = new File(path+"/"+fileName);
				total++;
				FireCraftTableRecipe fctr = FireCraftTableRecipe.load(fileName.substring(0,fileName.lastIndexOf(".")));
				if(fctr==null){
					log.add(getLogHead()+"Skip file: \""+wrFile+"\"");
					skip++;
					continue;
				}
				log.add(getLogHead()+"Find the target file: \""+wrFile+"\"");
				log.add(getLogHead()+"Start converting recipe: \""+wrFile+"\"");
				
				Inventory inv = fctr.setInv(Furnace.createFurnaceGUI("Converter"));
				FurnaceRecipe fr = new FurnaceRecipe(fctr.getName(), fctr.getTime(), fctr.getmaxTime()-fctr.getTime(), fctr.getTemperature(), fctr.getTemperature()+100);
				if(FurnaceRecipe.createFurnaceRecipe(inv, fr)){
					File newRecipe = new File(Data.DATAFOLDER+File.separator+"recipe"+File.separator+"furnace"+File.separator+fr.getName()+".yml");
					log.add(getLogHead()+"Conversion successful!!: \""+moveRecipe(newRecipe, "furnace")+"\"");
					su++;
				}
				else{
					log.add(getLogHead()+"Conversion Failed! Maybe you should rename this recipe: \""+wrFile+"\"");
					fa++;
				}
			} catch (Exception e) {
				log.add(getLogHead()+"Load file failed: \""+path+"/"+fileName+"\"");
				fa++;
			}
		}
		
		log.add(getLogHead()+"Tatol: "+total+" , successful!: "+su+" , failed: "+fa+" , skip: "+skip);
	}
	
	public WorkbenchRecipe getSF(String name){
		YamlConfiguration temp=YamlConfiguration.loadConfiguration(new File(
				Data.DATAFOLDER+File.separator+"SyntheticFormula"+File.separator+name+".yml"));
		HashMap<Integer,ItemStack> material=new HashMap<Integer,ItemStack>();
		HashMap<Integer,ItemStack> product=new HashMap<Integer,ItemStack>();
		
		for(int i:cmaterials)
			material.put(i, temp.getItemStack(name+".material."+i));
		for(int i:cproducts)
			product.put(i, temp.getItemStack(name+".product."+i));
		
		return new WorkbenchRecipe(name,material, product,temp.getLong(name+".builtTime"));
	}
}
