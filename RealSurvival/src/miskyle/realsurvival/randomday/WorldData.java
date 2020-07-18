package miskyle.realsurvival.randomday;

import java.util.HashMap;

import miskyle.realsurvival.api.Season;
import miskyle.realsurvival.util.RSEntry;

public class WorldData {
	private int 																					seasonCycle = -1;
	
	private HashMap<Season, Integer> 										seasonDuration;
	private HashMap<Season, RSEntry<Double, Double>> 		humidity;
	private HashMap<Season, RSEntry<Double, Double>> 		windSpeed;
	private HashMap<Season, RSEntry<Double, Double>> 		windFrequency;
	private HashMap<Season, RSEntry<Double, Double>> 		rainFrequency;
	private HashMap<Season, RSEntry<Double, Double>> 		baseTemperature;//50~65
	private HashMap<Season, RSEntry<Double, Double>> 		dayTemperature;
	private HashMap<Season, RSEntry<Double, Double>> 		nightTemperature;
	private HashMap<Season, RSEntry<Double, Double>> 		rainTemperature;
	
	public WorldData() {
		seasonDuration		= new HashMap<Season, Integer>();
		humidity 					= new HashMap<Season, RSEntry<Double,Double>>();
		windFrequency 		= new HashMap<Season, RSEntry<Double,Double>>();
		windSpeed 			= new HashMap<Season, RSEntry<Double,Double>>();
		rainFrequency 		= new HashMap<Season, RSEntry<Double,Double>>();
		baseTemperature 	= new HashMap<Season, RSEntry<Double,Double>>();
		dayTemperature 	= new HashMap<Season, RSEntry<Double,Double>>();
		nightTemperature 	= new HashMap<Season, RSEntry<Double,Double>>();
		rainTemperature 	= new HashMap<Season, RSEntry<Double,Double>>();
	}
	
	public Season getSeason(int day) {
		if(seasonCycle == -1) {
			seasonCycle = 0;
			for(int i : seasonDuration.values())
				seasonCycle+=i;
		}
		day%=seasonCycle;
		day -= getSeasonDuration(Season.SPRING);
		if(day<=0) return Season.SPRING;
		day -= getSeasonDuration(Season.SUMMER);
		if(day<=0) return Season.SUMMER;
		day -= getSeasonDuration(Season.AUTUMN);
		if(day<=0) return Season.AUTUMN;
		day -= getSeasonDuration(Season.WINTER);
		if(day<=0) return Season.WINTER;
		return Season.SPRING;
	}
	
	public HashMap<Season, Integer> getSeasonDuration() {
		return seasonDuration;
	}
	public void setSeasonDuration(HashMap<Season, Integer> seasonDuration) {
		this.seasonDuration = seasonDuration;
	}
	public HashMap<Season, RSEntry<Double, Double>> getHumidity() {
		return humidity;
	}
	public void setHumidity(HashMap<Season, RSEntry<Double, Double>> humidity) {
		this.humidity = humidity;
	}
	public HashMap<Season, RSEntry<Double, Double>> getWindSpeed() {
		return windSpeed;
	}
	public void setWindSpeed(HashMap<Season, RSEntry<Double, Double>> windSpeed) {
		this.windSpeed = windSpeed;
	}
	public HashMap<Season, RSEntry<Double, Double>> getWindFrequency() {
		return windFrequency;
	}
	public void setWindFrequency(HashMap<Season, RSEntry<Double, Double>> windFrequency) {
		this.windFrequency = windFrequency;
	}
	public HashMap<Season, RSEntry<Double, Double>> getRainFrequency() {
		return rainFrequency;
	}
	public void setRainFrequency(HashMap<Season, RSEntry<Double, Double>> rainFrequency) {
		this.rainFrequency = rainFrequency;
	}
	public HashMap<Season, RSEntry<Double, Double>> getBaseTemperature() {
		return baseTemperature;
	}
	public void setBaseTemperature(HashMap<Season, RSEntry<Double, Double>> baseTemperature) {
		this.baseTemperature = baseTemperature;
	}
	public HashMap<Season, RSEntry<Double, Double>> getDayTemperature() {
		return dayTemperature;
	}
	public void setDayTemperature(HashMap<Season, RSEntry<Double, Double>> dayTemperature) {
		this.dayTemperature = dayTemperature;
	}
	public HashMap<Season, RSEntry<Double, Double>> getNightTemperature() {
		return nightTemperature;
	}
	public void setNightTemperature(HashMap<Season, RSEntry<Double, Double>> nightTemperature) {
		this.nightTemperature = nightTemperature;
	}
	public HashMap<Season, RSEntry<Double, Double>> getRainTemperature() {
		return rainTemperature;
	}
	public void setRainTemperature(HashMap<Season, RSEntry<Double, Double>> rainTemperature) {
		this.rainTemperature = rainTemperature;
	}
	private int getSeasonDuration(Season season) {
		if(seasonDuration.containsKey(season))
			return seasonDuration.get(season);
		return 0;
	}
	
}
