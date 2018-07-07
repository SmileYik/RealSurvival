package com.outlook.schooluniformsama.randomday;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.BiConsumer;

import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.item.ItemLoreData;
import com.outlook.schooluniformsama.util.HashMap;
import com.outlook.schooluniformsama.util.Msg;

public class RandomDayManager {
	private static long tick;
	/**The Biome base temperature*/
	private static HashMap<Biome,Double> biomeBaseTemperature = new HashMap<>();
	private static YamlConfiguration config;
	private static HashMap<String, RandomDayTask> randomDay = new HashMap<>();
	/** night day rain high*/
	private static double[] temperaturefix;
	
	public RandomDayManager(Plugin plugin){
		if(config == null){
			writerYml();
			config = YamlConfiguration.loadConfiguration(new File(Data.DATAFOLDER+"/randomday.yml"));
			load();
		}
		for(String worldName:Data.worlds){
			/** duration humidity wind-speed frequency base-temperature*/
			double[] spring,summer,autumn,winter;
			/** day humidity wind-speed frequency base-temperature */
			double[] todayData = null,tomorrowData = null;
			try {
				spring = new double[]{
						config.getDouble("setting."+worldName+".season.spring.duration"),
						Double.parseDouble(config.getString("setting."+worldName+".season.spring.humidity").split("--")[0]),
						Double.parseDouble(config.getString("setting."+worldName+".season.spring.humidity").split("--")[1]),
						Double.parseDouble(config.getString("setting."+worldName+".season.spring.wind-speed").split("--")[0]),
						Double.parseDouble(config.getString("setting."+worldName+".season.spring.wind-speed").split("--")[1]),
						Double.parseDouble(config.getString("setting."+worldName+".season.spring.frequency").split("--")[0]),
						Double.parseDouble(config.getString("setting."+worldName+".season.spring.frequency").split("--")[1]),
						Double.parseDouble(config.getString("setting."+worldName+".season.spring.base-temperature").split("--")[0]),
						Double.parseDouble(config.getString("setting."+worldName+".season.spring.base-temperature").split("--")[1]),
				};
				summer = new double[]{
						config.getDouble("setting."+worldName+".season.summer.duration"),
						Double.parseDouble(config.getString("setting."+worldName+".season.summer.humidity").split("--")[0]),
						Double.parseDouble(config.getString("setting."+worldName+".season.summer.humidity").split("--")[1]),
						Double.parseDouble(config.getString("setting."+worldName+".season.summer.wind-speed").split("--")[0]),
						Double.parseDouble(config.getString("setting."+worldName+".season.summer.wind-speed").split("--")[1]),
						Double.parseDouble(config.getString("setting."+worldName+".season.summer.frequency").split("--")[0]),
						Double.parseDouble(config.getString("setting."+worldName+".season.summer.frequency").split("--")[1]),
						Double.parseDouble(config.getString("setting."+worldName+".season.summer.base-temperature").split("--")[0]),
						Double.parseDouble(config.getString("setting."+worldName+".season.summer.base-temperature").split("--")[1]),
				};
				autumn = new double[]{
						config.getDouble("setting."+worldName+".season.autumn.duration"),
						Double.parseDouble(config.getString("setting."+worldName+".season.autumn.humidity").split("--")[0]),
						Double.parseDouble(config.getString("setting."+worldName+".season.autumn.humidity").split("--")[1]),
						Double.parseDouble(config.getString("setting."+worldName+".season.autumn.wind-speed").split("--")[0]),
						Double.parseDouble(config.getString("setting."+worldName+".season.autumn.wind-speed").split("--")[1]),
						Double.parseDouble(config.getString("setting."+worldName+".season.autumn.frequency").split("--")[0]),
						Double.parseDouble(config.getString("setting."+worldName+".season.autumn.frequency").split("--")[1]),
						Double.parseDouble(config.getString("setting."+worldName+".season.autumn.base-temperature").split("--")[0]),
						Double.parseDouble(config.getString("setting."+worldName+".season.autumn.base-temperature").split("--")[1]),
				};
				winter = new double[]{
						config.getDouble("setting."+worldName+".season.winter.duration"),
						Double.parseDouble(config.getString("setting."+worldName+".season.winter.humidity").split("--")[0]),
						Double.parseDouble(config.getString("setting."+worldName+".season.winter.humidity").split("--")[1]),
						Double.parseDouble(config.getString("setting."+worldName+".season.winter.wind-speed").split("--")[0]),
						Double.parseDouble(config.getString("setting."+worldName+".season.winter.wind-speed").split("--")[1]),
						Double.parseDouble(config.getString("setting."+worldName+".season.winter.frequency").split("--")[0]),
						Double.parseDouble(config.getString("setting."+worldName+".season.winter.frequency").split("--")[1]),
						Double.parseDouble(config.getString("setting."+worldName+".season.winter.base-temperature").split("--")[0]),
						Double.parseDouble(config.getString("setting."+worldName+".season.winter.base-temperature").split("--")[1]),
				};
				todayData = new double[]{
						config.getDouble("data."+worldName+".today.day"),
						config.getDouble("data."+worldName+".today.humidity"),
						config.getDouble("data."+worldName+".today.wind-speed"),
						config.getDouble("data."+worldName+".today.frequency"),
						config.getDouble("data."+worldName+".today.base-temperature"),
				};
				
				tomorrowData = new double[]{
						config.getDouble("data."+worldName+".tomorrow.day"),
						config.getDouble("data."+worldName+".tomorrow.humidity"),
						config.getDouble("data."+worldName+".tomorrow.wind-speed"),
						config.getDouble("data."+worldName+".tomorrow.frequency"),
						config.getDouble("data."+worldName+".tomorrow.base-temperature"),
				};
				RandomDayTask rdt = new RandomDayTask(worldName, spring, summer, autumn, winter, todayData, tomorrowData);
				randomDay.put(worldName,rdt );
				Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, rdt,0,tick);
			} catch (Exception e) {
				RandomDayTask rdt = new RandomDayTask(worldName, new double[]{
						10,10,20,10,20,10,20,10,20
				}, new double[]{
						10,10,20,10,20,10,20,10,20
				}, new double[]{
						10,10,20,10,20,10,20,10,20
				}, new double[]{
						10,10,20,10,20,10,20,10,20
				}, todayData, tomorrowData);
				randomDay.put(worldName,rdt );
				Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, rdt,0,tick);
				save(worldName, rdt.getSpring(), rdt.getSummer(), rdt.getAutumn(), rdt.getWinter());
			}
		}
	}
	
	public static double getBiomeTemperature(Biome b){
		if(biomeBaseTemperature.containsKey(b))return biomeBaseTemperature.get(b);
		return 0;
	}
	
	private static void saveConfig(){
		try {
			config.save(new File(Data.DATAFOLDER+"/randomday.yml"));
		} catch (IOException e) {

		}
		
		config = YamlConfiguration.loadConfiguration(new File(Data.DATAFOLDER+"/randomday.yml"));
	}
	
	public static void save(){
		randomDay.forEach(new BiConsumer<String, RandomDayTask>() {
			@Override
			public void accept(String worldName, RandomDayTask value) {
				double[] todayData = value.getTodayData(),
						tomorrowData = value.getTomorrowData();
				config.set("data."+worldName+".today.day", todayData[0]);
				config.set("data."+worldName+".today.humidity", todayData[1]);
				config.set("data."+worldName+".today.wind-speed", todayData[2]);
				config.set("data."+worldName+".today.frequency", todayData[3]);
				config.set("data."+worldName+".today.base-temperature", todayData[4]);
				
				config.set("data."+worldName+".tomorrow.day", tomorrowData[0]);
				config.set("data."+worldName+".tomorrow.humidity", tomorrowData[1]);
				config.set("data."+worldName+".tomorrow.wind-speed", tomorrowData[2]);
				config.set("data."+worldName+".tomorrow.frequency", tomorrowData[3]);
				config.set("data."+worldName+".tomorrow.base-temperature", tomorrowData[4]);
			}
		});
		saveConfig();
	}
	
	private void save(String worldName,double[] spring,double[] summer,double[] autumn,double[] winter){
		config.set("setting."+worldName+".season.spring.duration", spring[0]);
		config.set("setting."+worldName+".season.spring.humidity",spring[1]+"--"+spring[2]);
		config.set("setting."+worldName+".season.spring.wind-speed",spring[3]+"--"+spring[4]);
		config.set("setting."+worldName+".season.spring.frequency",spring[5]+"--"+spring[6]);
		config.set("setting."+worldName+".season.spring.base-temperature",spring[7]+"--"+spring[8]);
		config.set("setting."+worldName+".season.summer.duration", summer[0]);
		config.set("setting."+worldName+".season.summer.humidity",summer[1]+"--"+summer[2]);
		config.set("setting."+worldName+".season.summer.wind-speed",summer[3]+"--"+summer[4]);
		config.set("setting."+worldName+".season.summer.frequency",summer[5]+"--"+summer[6]);
		config.set("setting."+worldName+".season.summer.base-temperature",summer[7]+"--"+summer[8]);
		config.set("setting."+worldName+".season.autumn.duration", autumn[0]);
		config.set("setting."+worldName+".season.autumn.humidity",autumn[1]+"--"+autumn[2]);
		config.set("setting."+worldName+".season.autumn.wind-speed",autumn[3]+"--"+autumn[4]);
		config.set("setting."+worldName+".season.autumn.frequency",autumn[5]+"--"+autumn[6]);
		config.set("setting."+worldName+".season.autumn.base-temperature",autumn[7]+"--"+autumn[8]);
		config.set("setting."+worldName+".season.winter.duration", winter[0]);
		config.set("setting."+worldName+".season.winter.humidity",winter[1]+"--"+winter[2]);
		config.set("setting."+worldName+".season.winter.wind-speed",winter[3]+"--"+winter[4]);
		config.set("setting."+worldName+".season.winter.frequency",winter[5]+"--"+winter[6]);
		config.set("setting."+worldName+".season.winter.base-temperature",winter[7]+"--"+winter[8]);
		saveConfig();
	}
	
	private void writerYml(){
		
		if(new File(Data.DATAFOLDER+"/randomday.yml").exists())return;
		
		InputStream is=null;
		OutputStream os=null;
		try {
			is=Msg.class.getResourceAsStream("/randomday.yml");
			os=new FileOutputStream(new File(Data.DATAFOLDER+"/randomday.yml"));
			int i;
			byte[] buffer = new byte[1024];
			while((i=is.read(buffer))!=-1)
				os.write(buffer,0,i);
			os.flush();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}finally {
			if(is!=null)
				try {
					is.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
			if(os!=null)
				try {
					os.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
		}
		
	}
	
	private void load(){
		tick = config.getLong("setting.tick",600);
		temperaturefix = new double[]{
				config.getDouble("setting.temperature-fix.night"),
				config.getDouble("setting.temperature-fix.day"),
				config.getDouble("setting.temperature-fix.rain"),
				config.getDouble("setting.temperature-fix.high"),
		};
		
		for(Biome biome: Biome.values()){
			double temperature = config.getDouble("setting.biome-temperature."+biome.name().toLowerCase(), ItemLoreData.badCode());
			if(temperature == ItemLoreData.badCode())continue;
			biomeBaseTemperature.put(biome, temperature);
		}
		
	}
	
	public static long getTick(){
		return tick;
	}

	/** night day rain high*/
	public static double[] getTemperaturefix() {
		return temperaturefix;
	}
	
	public static double[] getTodayData(String worldName){
		return randomDay.get(worldName).getTodayData();
	}
	
}
