package com.outlook.schooluniformsama.randomday;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.block.Biome;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.Test;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.item.ItemLoreData;
import com.outlook.schooluniformsama.util.HashMap;
import com.outlook.schooluniformsama.util.Msg;
import com.outlook.schooluniformsama.util.Util;

public class RandomDayTask implements Runnable{
	/** duration humidity wind-speed frequency base-temperature*/
	private double[] spring,summer,autumn,winter;
	/** day humidity wind-speed frequency base-temperature */
	public static double[] todayData,tomorrowData;
	/** night day rain high*/
	public static double[] temperaturefix;
	/**The Biome base temperature*/
	private static HashMap<Biome,Double> biomeBaseTemperature = new HashMap<>();

	public RandomDayTask() {
		writerYml();
		load();
	}
	
	public static double getBiomeTemperature(Biome b){
		if(biomeBaseTemperature.containsKey(b))return biomeBaseTemperature.get(b);
		return 0;
	}
	
	@Test
	public void test(){
		//print all of biomes
		for(Biome b: Biome.values())
			System.out.println("    "+b.name().toLowerCase()+": 1.0");
	}
	
	private void save(){
		YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(Data.DATAFOLDER+"/randomday.yml"));
		config.set("data.day", todayData[0]);
		config.set("data.humidity", todayData[1]);
		config.set("data.wind-speed", todayData[2]);
		config.set("data.frequency", todayData[3]);
		config.set("data.base-temperature", todayData[4]);
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
	
	public void load(){
		YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(Data.DATAFOLDER+"/randomday.yml"));
		
		spring = new double[]{
				config.getDouble("setting.season.spring.duration"),
				Double.parseDouble(config.getString("setting.season.spring.humidity").split("--")[0]),
				Double.parseDouble(config.getString("setting.season.spring.humidity").split("--")[1]),
				Double.parseDouble(config.getString("setting.season.spring.wind-speed").split("--")[0]),
				Double.parseDouble(config.getString("setting.season.spring.wind-speed").split("--")[1]),
				Double.parseDouble(config.getString("setting.season.spring.frequency").split("--")[0]),
				Double.parseDouble(config.getString("setting.season.spring.frequency").split("--")[1]),
				Double.parseDouble(config.getString("setting.season.spring.base-temperature").split("--")[0]),
				Double.parseDouble(config.getString("setting.season.spring.base-temperature").split("--")[1]),
		};
		summer = new double[]{
				config.getDouble("setting.season.summer.duration"),
				Double.parseDouble(config.getString("setting.season.summer.humidity").split("--")[0]),
				Double.parseDouble(config.getString("setting.season.summer.humidity").split("--")[1]),
				Double.parseDouble(config.getString("setting.season.summer.wind-speed").split("--")[0]),
				Double.parseDouble(config.getString("setting.season.summer.wind-speed").split("--")[1]),
				Double.parseDouble(config.getString("setting.season.summer.frequency").split("--")[0]),
				Double.parseDouble(config.getString("setting.season.summer.frequency").split("--")[1]),
				Double.parseDouble(config.getString("setting.season.summer.base-temperature").split("--")[0]),
				Double.parseDouble(config.getString("setting.season.summer.base-temperature").split("--")[1]),
		};
		autumn = new double[]{
				config.getDouble("setting.season.autumn.duration"),
				Double.parseDouble(config.getString("setting.season.autumn.humidity").split("--")[0]),
				Double.parseDouble(config.getString("setting.season.autumn.humidity").split("--")[1]),
				Double.parseDouble(config.getString("setting.season.autumn.wind-speed").split("--")[0]),
				Double.parseDouble(config.getString("setting.season.autumn.wind-speed").split("--")[1]),
				Double.parseDouble(config.getString("setting.season.autumn.frequency").split("--")[0]),
				Double.parseDouble(config.getString("setting.season.autumn.frequency").split("--")[1]),
				Double.parseDouble(config.getString("setting.season.autumn.base-temperature").split("--")[0]),
				Double.parseDouble(config.getString("setting.season.autumn.base-temperature").split("--")[1]),
		};
		winter = new double[]{
				config.getDouble("setting.season.winter.duration"),
				Double.parseDouble(config.getString("setting.season.winter.humidity").split("--")[0]),
				Double.parseDouble(config.getString("setting.season.winter.humidity").split("--")[1]),
				Double.parseDouble(config.getString("setting.season.winter.wind-speed").split("--")[0]),
				Double.parseDouble(config.getString("setting.season.winter.wind-speed").split("--")[1]),
				Double.parseDouble(config.getString("setting.season.winter.frequency").split("--")[0]),
				Double.parseDouble(config.getString("setting.season.winter.frequency").split("--")[1]),
				Double.parseDouble(config.getString("setting.season.winter.base-temperature").split("--")[0]),
				Double.parseDouble(config.getString("setting.season.winter.base-temperature").split("--")[1]),
		};
		
		temperaturefix = new double[]{
				config.getDouble("setting.temperature-fix.night"),
				config.getDouble("setting.temperature-fix.day"),
				config.getDouble("setting.temperature-fix.rain"),
				config.getDouble("setting.temperature-fix.high"),
		};
		
		todayData = new double[]{
				config.getDouble("data.day"),
				config.getDouble("data.humidity"),
				config.getDouble("data.wind-speed"),
				config.getDouble("data.frequency"),
				config.getDouble("data.base-temperature"),
		};
		
		for(Biome biome: Biome.values()){
			double temperature = config.getDouble("setting.biome-temperature."+biome.name().toLowerCase(), ItemLoreData.badCode());
			if(temperature == ItemLoreData.badCode())continue;
			biomeBaseTemperature.put(biome, temperature);
		}
		
		randomTomorrow();
		
	}
	
	@Override
	public void run() {
		todayData = tomorrowData;
		randomTomorrow();
		save();
	}
	
	public void randomTomorrow(){
		double day = todayData[0]+1;
		double temp = day % (spring[0]+summer[0]+autumn[0]+winter[0]);
		
		if(temp<=spring[0]){
			tomorrowData = new double[]{
					day,
					Util.randomNum(spring[1], spring[2]),
					Util.randomNum(spring[3], spring[4]),
					Util.randomNum(spring[5], spring[6]),
					Util.randomNum(spring[7], spring[8]),
			};
		}else if(temp-spring[0]<=summer[0]){
			tomorrowData = new double[]{
					day,
					Util.randomNum(summer[1], summer[2]),
					Util.randomNum(summer[3], summer[4]),
					Util.randomNum(summer[5], summer[6]),
					Util.randomNum(summer[7], summer[8]),
			};
		}else if(temp-spring[0]-summer[0]<=autumn[0]){
			tomorrowData = new double[]{
					day,
					Util.randomNum(autumn[1], autumn[2]),
					Util.randomNum(autumn[3], autumn[4]),
					Util.randomNum(autumn[5], autumn[6]),
					Util.randomNum(autumn[7], autumn[8]),
			};
		}else{
			tomorrowData = new double[]{
					day,
					Util.randomNum(winter[1], winter[2]),
					Util.randomNum(winter[3], winter[4]),
					Util.randomNum(winter[5], winter[6]),
					Util.randomNum(winter[7], winter[8]),
			};
		}
	}
}
