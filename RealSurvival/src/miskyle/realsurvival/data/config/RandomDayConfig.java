package miskyle.realsurvival.data.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.block.Biome;
import org.bukkit.configuration.file.YamlConfiguration;

import com.github.miskyle.mcpt.MCPT;

import miskyle.realsurvival.api.Season;
import miskyle.realsurvival.data.ConfigManager;
import miskyle.realsurvival.randomday.NewDay;
import miskyle.realsurvival.randomday.WorldData;
import miskyle.realsurvival.util.RSEntry;

public class RandomDayConfig {
  private static RandomDayConfig rdc;

  private YamlConfiguration config;
  private File configFile;

  private HashMap<String, WorldData> worldDatas;

  /**
   * 初始化RandomDay配置信息.
   */
  public RandomDayConfig() {
    rdc = this;
    configFile = new File(MCPT.plugin.getDataFolder() + "/randomday.yml");
    if (!configFile.exists()) {
      firstRun(configFile);
    }
    loadConfig();
  }

  private void loadConfig() {
    config = YamlConfiguration.loadConfiguration(configFile);

    worldDatas = new HashMap<String, WorldData>();
    for (String world : ConfigManager.getEnableWorlds()) {
      if (!config.contains(world)) {
        makeNewWorldData(world);
      }
      WorldData worldData = new WorldData();
      for (Season season : Season.values()) {
        String key = world + "." + season.name();
        if (!config.contains(key + ".duration")) {
          continue;
        }
        worldData.getSeasonDuration().put(season, config.getInt(key + ".duration"));
        worldData.getHumidity().put(season, 
            getRsEntryFromString(config.getString(key + ".humidity")));
        worldData.getWindSpeed().put(season, 
            getRsEntryFromString(config.getString(key + ".wind-speed")));
        worldData.getWindFrequency().put(season, 
            getRsEntryFromString(config.getString(key + ".wind-frequency")));
        worldData.getRainFrequency().put(season, 
            getRsEntryFromString(config.getString(key + ".rain-frequency")));
        worldData.getBaseTemperature().put(season, 
            getRsEntryFromString(config.getString(key + ".base-temperature")));
        worldData.getDayTemperature().put(season, 
            getRsEntryFromString(config.getString(key + ".day-temperature")));
        worldData.getNightTemperature().put(season, 
            getRsEntryFromString(config.getString(key + ".night-temperature")));
        worldData.getRainTemperature().put(season, 
            getRsEntryFromString(config.getString(key + ".rain-temperature")));
      }
      worldDatas.put(world, worldData);
    }
  }

  /**
   * 获取目的世界今天的数据.
   * @param worldName 目的世界名
   * @return
   */
  public static NewDay getTodayConfig(String worldName) {
    NewDay day = new NewDay();
    String key = worldName + ".day.today";
    if (!rdc.config.contains(key + ".day")) {
      return NewDay.newDay(worldName, getWorldData(worldName), 1);      
    }
    day.setWorldName(worldName);
    day.setDay(rdc.config.getInt(key + ".day"));
    day.setSeason(Season.valueOf(rdc.config.getString(key + ".season")));
    day.setHumidity(rdc.config.getDouble(key + ".humidity"));
    day.setWindSpeed(rdc.config.getDouble(key + ".wind-speed"));
    day.setWindFrequency(rdc.config.getDouble(key + ".wind-frequency"));
    day.setRainFrequency(rdc.config.getDouble(key + ".rain-frequency"));
    day.setBaseTemperature(rdc.config.getDouble(key + ".base-temperature"));
    day.setDayTemperature(rdc.config.getDouble(key + ".day-temperature"));
    day.setNightTemperature(rdc.config.getDouble(key + ".night-temperature"));
    day.setRainTemperature(rdc.config.getDouble(key + ".rain-temperature"));
    return day.init();
  }

  /**
   * 获取目的世界明天的数据.
   * @param worldName 目的世界名.
   * @return
   */
  public static NewDay getTomorrowConfig(String worldName) {
    NewDay day = new NewDay();
    String key = worldName + ".day.tomorrow";
    if (!rdc.config.contains(key + ".day")) {      
      return NewDay.newDay(worldName, getWorldData(worldName), 1);
    }
    day.setWorldName(worldName);
    day.setDay(rdc.config.getInt(key + ".day"));
    day.setSeason(Season.valueOf(rdc.config.getString(key + ".season")));
    day.setHumidity(rdc.config.getDouble(key + ".humidity"));
    day.setWindSpeed(rdc.config.getDouble(key + ".wind-speed"));
    day.setWindFrequency(rdc.config.getDouble(key + ".wind-frequency"));
    day.setRainFrequency(rdc.config.getDouble(key + ".rain-frequency"));
    day.setBaseTemperature(rdc.config.getDouble(key + ".base-temperature"));
    day.setDayTemperature(rdc.config.getDouble(key + ".day-temperature"));
    day.setNightTemperature(rdc.config.getDouble(key + ".night-temperature"));
    day.setRainTemperature(rdc.config.getDouble(key + ".rain-temperature"));
    return day.init();
  }

  /**
   * 保存某个世界今天的信息.
   * @param day 某个世界今天的数据
   */
  public static void saveToday(NewDay day) {
    String key = day.getWorldName() + ".day.today";
    rdc.config.set(key + ".day", day.getDay());
    rdc.config.set(key + "..season", day.getSeason().name());
    rdc.config.set(key + ".humidity", day.getHumidity());
    rdc.config.set(key + ".wind-speed", day.getWindSpeed());
    rdc.config.set(key + ".wind-frequency", day.getWindFrequency());
    rdc.config.set(key + ".rain-frequency", day.getRainFrequency());
    rdc.config.set(key + ".base-temperature", day.getBaseTemperature());
    rdc.config.set(key + ".day-temperature", day.getDayTemperature());
    rdc.config.set(key + ".night-temperature", day.getNightTemperature());
    rdc.config.set(key + ".rain-temperature", day.getRainTemperature());
  }
  
  /**
   * 保存某个世界明天的信息.
   * @param day 某个世界明天的数据
   */
  public static void saveTomorrow(NewDay day) {
    String key = day.getWorldName() + ".day.tomorrow";
    rdc.config.set(key + ".day", day.getDay());
    rdc.config.set(key + ".season", day.getSeason().name());
    rdc.config.set(key + ".humidity", day.getHumidity());
    rdc.config.set(key + ".wind-speed", day.getWindSpeed());
    rdc.config.set(key + ".wind-frequency", day.getWindFrequency());
    rdc.config.set(key + ".rain-frequency", day.getRainFrequency());
    rdc.config.set(key + ".base-temperature", day.getBaseTemperature());
    rdc.config.set(key + ".day-temperature", day.getDayTemperature());
    rdc.config.set(key + ".night-temperature", day.getNightTemperature());
    rdc.config.set(key + ".rain-temperature", day.getRainTemperature());
  }

  /**
   * 保存所有配置.
   */
  public static void saveAllConfig() {
    try {
      rdc.config.save(rdc.configFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static int getTick() {
    return rdc.config.getInt("setting.tick");
  }

  public static double getBiomeBasicTemperation(Biome biome) {
    return rdc.config.getDouble("setting.biome." + biome.name(), 0D);
  }

  public static WorldData getWorldData(String world) {
    return rdc.worldDatas.get(world);
  }

  public static HashMap<String, WorldData> getWorldDatas() {
    return rdc.worldDatas;
  }

  /**
   * 第一次运行时的必要操作.
   * @param file 配置文件路径
   */
  public void firstRun(File file) {
    config = YamlConfiguration.loadConfiguration(file);
    config.set("setting.tick", 600);
    for (Biome biome : Biome.values()) {
      config.set("setting.biome." + biome.name(), 0D);
    }
    for (String world : ConfigManager.getEnableWorlds()) {
      makeNewWorldData(world);
    }
    try {
      config.save(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 生成新的世界配置.
   * @param world 世界名
   */
  public static void makeNewWorldData(String world) {
    for (Season season : Season.values()) {
      String key = world + "." + season.name();
      rdc.config.set(key + ".duration", (int) (10 + Math.random() * 30));
      rdc.config.set(key + ".humidity", "70/80");
      rdc.config.set(key + ".wind-speed", "0.6/0.8");
      rdc.config.set(key + ".wind-frequency", "0.16/0.65");
      rdc.config.set(key + ".rain-frequency", "0.56/0.65");
      rdc.config.set(key + ".base-temperature", "45/65");
      rdc.config.set(key + ".day-temperature", "0.00002/0.00006");
      rdc.config.set(key + ".night-temperature", "0.00002/0.00006");
      rdc.config.set(key + ".rain-temperature", "3/6");
    }
  }

  /**
   * 从字符串中获取RSEntity数据.
   * @param str 字符串
   * @return
   */
  private RSEntry<Double, Double> getRsEntryFromString(String str) {
    String[] temp = str.split("/");
    return new RSEntry<Double, Double>(Double.parseDouble(temp[0]), Double.parseDouble(temp[1]));
  }
}
