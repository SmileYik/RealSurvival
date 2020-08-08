package miskyle.realsurvival.randomday;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

import com.github.miskyle.mcpt.MCPT;

import miskyle.realsurvival.api.Season;
import miskyle.realsurvival.data.ConfigManager;
import miskyle.realsurvival.data.config.RandomDayConfig;

public class RandomDayManager {
  private static HashMap<String, RandomDayTask> tasks = new HashMap<String, RandomDayTask>();
  
  public static void init() {
    new RandomDayConfig();
    RandomDayConfig.getWorldDatas().forEach((k,v)->{
      RandomDayTask task = new RandomDayTask(
          k, v, RandomDayConfig.getTodayConfig(k), RandomDayConfig.getTomorrowConfig(k));
      Bukkit.getScheduler().runTaskTimerAsynchronously(
          MCPT.plugin, task, 0L, RandomDayConfig.getTick());
      tasks.put(k, task);
    });
  }
  
  public static void save() {
    tasks.values().forEach(t->{
      RandomDayConfig.saveToday(t.getToday());
      RandomDayConfig.saveTomorrow(t.getTomorrow());
    });
    RandomDayConfig.saveAllConfig();
  }
  
  public static Season getWorldSeason(String worldName) {
    if(!tasks.containsKey(worldName))return null;
    return tasks.get(worldName).getToday().getSeason();
  }
  
  public static double getBaseTemperature(Location loc) {
    if(!tasks.containsKey(loc.getWorld().getName())) return 0;
    NewDay day = tasks.get(loc.getWorld().getName()).getToday();
    long time = loc.getWorld().getTime();
    double temperature = day.getBaseTemperature()
        *Math.sin(0.85*loc.getBlock().getTemperature())+day.getAlpheTemperature();
    temperature += RandomDayConfig.getBiomeBasicTemperation(loc.getBlock().getBiome());
    
    if(time>22550)temperature+=day.getDayTemperature()*(24000-time);
    else if(time<6000) temperature+=day.getDayTemperature()*(time-4550);
    else temperature-=day.getNightTemperature()*(time-6000);
    
    if(loc.getWorld().hasStorm()) 
      temperature-=day.getRainTemperature();
    
    return temperature;
  }
  
  public static double getBlockTemperature(Location b) {
    double temperature = 0;
    for(int x = b.getBlockX()-2;x<=b.getBlockX()+2;x++)
      for(int y = b.getBlockY()-2;y<=b.getBlockY()+2;y++)
        for(int z = b.getBlockZ()-2;z<=b.getBlockZ()+2;z++) {
          Block block = b.getWorld().getBlockAt(x,y,z);
          if(!block.isEmpty()) {
            double blockT=ConfigManager.getTemperatureConfig()
                .getBlockTemperature(block.getType().name());
            double distance = b.distanceSquared(block.getLocation());
            temperature+=blockT*Math.pow(0.8, distance);
          }
        }
    return temperature;
  }
  
  public static double getTemperature(Location loc) {
    return getBaseTemperature(loc)+getBlockTemperature(loc);
  }
  
}
