package miskyle.realsurvival.data.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.block.Biome;
import org.bukkit.configuration.file.YamlConfiguration;

import com.github.miskyle.mcpt.MCPT;

import miskyle.realsurvival.api.Season;
import miskyle.realsurvival.data.ConfigManager;
import miskyle.realsurvival.randomday.WorldData;
import miskyle.realsurvival.util.RSEntry;

public class RandomDayConfig {
	private static RandomDayConfig rdc;
	
	private YamlConfiguration 			config;
	private File 									configFile;
	
	private HashMap<String, WorldData> worldDatas;
	
	public RandomDayConfig() {
		configFile = new File(MCPT.plugin.getDataFolder()+"/randomday.yml");
		if(!configFile.exists()) {
			firstRun(configFile);
		}
		loadConfig();
	}
	
	private void loadConfig() {
		config = YamlConfiguration.loadConfiguration(configFile);
		
		worldDatas = new HashMap<String, WorldData>();
		for(String world:ConfigManager.getEnableWorlds()) {
			WorldData worldData = new WorldData();
			for(Season season : Season.values()) {
				String key = world+"."+season.name();
				worldData.getSeasonDuration().put(season, config.getInt(key+".duration"));
				worldData.getHumidity().put(season, getRSEntryFromString(config.getString(key+".humidity")));
				worldData.getWindSpeed().put(season, getRSEntryFromString(config.getString(key+".wind-speed")));
				worldData.getWindFrequency().put(season, getRSEntryFromString(config.getString(key+".wind-frequency")));
				worldData.getRainFrequency().put(season, getRSEntryFromString(config.getString(key+".rain-frequency")));
				worldData.getBaseTemperature().put(season, getRSEntryFromString(config.getString(key+".base-temperature")));
				worldData.getDayTemperature().put(season, getRSEntryFromString(config.getString(key+".day-temperature")));
				worldData.getNightTemperature().put(season, getRSEntryFromString(config.getString(key+"night-temperature")));
				worldData.getRainTemperature().put(season, getRSEntryFromString(config.getString(key+".rain-temperature")));
			}
			worldDatas.put(world, worldData);
		}
	}
	
	public static int getTick() {
		return rdc.config.getInt("setting.tick");
	}
	
	public static double getBiomeBasicTemperation(Biome biome) {
		return rdc.config.getDouble("setting.biome."+biome.name(), 0D);
	}
	
	public static WorldData getWorldData(String world) {
		return rdc.worldDatas.get(world);
	}
	
	public void firstRun(File file) {
		config = YamlConfiguration.loadConfiguration(file);
		config.set("setting.tick", 600);
		for(Biome biome : Biome.values()) {
			config.set("setting.biome."+biome.name(), 0D);
		}
		for(String world:ConfigManager.getEnableWorlds()) {
			makeNewWorldData(world);
		}
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void makeNewWorldData(String world) {
		for(Season season : Season.values()) {
			String key = world+"."+season.name();
			rdc.config.set(key+".duration", (int)(10+Math.random()*30));
			rdc.config.set(key+".humidity", "70/80");
			rdc.config.set(key+".wind-speed", "0.6/0.8");
			rdc.config.set(key+".wind-frequency","0.16/0.65" );
			rdc.config.set(key+".rain-frequence","0.56/0.65" );
			rdc.config.set(key+".base-temperature","45/65" );
			rdc.config.set(key+".day-temperature", "3/6");
			rdc.config.set(key+".night-temperature","3/6" );
			rdc.config.set(key+".rain-temperature","3/6" );
		}
	}
	
	private RSEntry<Double, Double> getRSEntryFromString(String str){
		String[] temp = str.split("/");
		return new RSEntry<Double, Double>(
				Double.parseDouble(temp[0]),
				Double.parseDouble( temp[1]));
	}
}
