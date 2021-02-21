package miskyle.realsurvival.randomday;

import java.util.HashMap;
import miskyle.realsurvival.api.Season;
import miskyle.realsurvival.util.RsEntry;

public class WorldData {
  private int seasonCycle = -1;

  private HashMap<Season, Integer> seasonDuration;
  private HashMap<Season, RsEntry<Double, Double>> humidity;
  private HashMap<Season, RsEntry<Double, Double>> windSpeed;
  private HashMap<Season, RsEntry<Double, Double>> windFrequency;
  private HashMap<Season, RsEntry<Double, Double>> rainFrequency;
  private HashMap<Season, RsEntry<Double, Double>> baseTemperature; // 50~65
  private HashMap<Season, RsEntry<Double, Double>> dayTemperature;
  private HashMap<Season, RsEntry<Double, Double>> nightTemperature;
  private HashMap<Season, RsEntry<Double, Double>> rainTemperature;

  /**
   * 初始化.
   */
  public WorldData() {
    seasonDuration = new HashMap<Season, Integer>();
    humidity = new HashMap<Season, RsEntry<Double, Double>>();
    windFrequency = new HashMap<Season, RsEntry<Double, Double>>();
    windSpeed = new HashMap<Season, RsEntry<Double, Double>>();
    rainFrequency = new HashMap<Season, RsEntry<Double, Double>>();
    baseTemperature = new HashMap<Season, RsEntry<Double, Double>>();
    dayTemperature = new HashMap<Season, RsEntry<Double, Double>>();
    nightTemperature = new HashMap<Season, RsEntry<Double, Double>>();
    rainTemperature = new HashMap<Season, RsEntry<Double, Double>>();
  }

  /**
   * 根据天数获取季节.

   * @param day 天数
   * @return 相对应的季节.
   */
  public Season getSeason(int day) {
    if (seasonCycle == -1) {
      seasonCycle = 0;
      for (int i : seasonDuration.values()) {
        seasonCycle += i;        
      }
    }
    day %= seasonCycle;
    day -= getSeasonDuration(Season.SPRING);
    if (day <= 0) {
      return Season.SPRING;      
    }
    day -= getSeasonDuration(Season.SUMMER);
    if (day <= 0) {
      return Season.SUMMER;      
    }
    day -= getSeasonDuration(Season.AUTUMN);
    if (day <= 0) {
      return Season.AUTUMN;      
    }
    day -= getSeasonDuration(Season.WINTER);
    if (day <= 0) {
      return Season.WINTER;      
    }
    return Season.SPRING;
  }

  public HashMap<Season, Integer> getSeasonDuration() {
    return seasonDuration;
  }
  
  /**
   * 获取季节持续时间.

   * @param season 季节
   * @return
   */
  private int getSeasonDuration(Season season) {
    if (seasonDuration.containsKey(season)) {
      return seasonDuration.get(season);      
    }
    return 0;
  }

  public void setSeasonDuration(HashMap<Season, Integer> seasonDuration) {
    this.seasonDuration = seasonDuration;
  }

  public HashMap<Season, RsEntry<Double, Double>> getHumidity() {
    return humidity;
  }

  public void setHumidity(HashMap<Season, RsEntry<Double, Double>> humidity) {
    this.humidity = humidity;
  }

  public HashMap<Season, RsEntry<Double, Double>> getWindSpeed() {
    return windSpeed;
  }

  public void setWindSpeed(HashMap<Season, RsEntry<Double, Double>> windSpeed) {
    this.windSpeed = windSpeed;
  }

  public HashMap<Season, RsEntry<Double, Double>> getWindFrequency() {
    return windFrequency;
  }

  public void setWindFrequency(HashMap<Season, RsEntry<Double, Double>> windFrequency) {
    this.windFrequency = windFrequency;
  }

  public HashMap<Season, RsEntry<Double, Double>> getRainFrequency() {
    return rainFrequency;
  }

  public void setRainFrequency(HashMap<Season, RsEntry<Double, Double>> rainFrequency) {
    this.rainFrequency = rainFrequency;
  }

  public HashMap<Season, RsEntry<Double, Double>> getBaseTemperature() {
    return baseTemperature;
  }

  public void setBaseTemperature(HashMap<Season, RsEntry<Double, Double>> baseTemperature) {
    this.baseTemperature = baseTemperature;
  }

  public HashMap<Season, RsEntry<Double, Double>> getDayTemperature() {
    return dayTemperature;
  }

  public void setDayTemperature(HashMap<Season, RsEntry<Double, Double>> dayTemperature) {
    this.dayTemperature = dayTemperature;
  }

  public HashMap<Season, RsEntry<Double, Double>> getNightTemperature() {
    return nightTemperature;
  }

  public void setNightTemperature(HashMap<Season, RsEntry<Double, Double>> nightTemperature) {
    this.nightTemperature = nightTemperature;
  }

  public HashMap<Season, RsEntry<Double, Double>> getRainTemperature() {
    return rainTemperature;
  }

  public void setRainTemperature(HashMap<Season, RsEntry<Double, Double>> rainTemperature) {
    this.rainTemperature = rainTemperature;
  }

}
