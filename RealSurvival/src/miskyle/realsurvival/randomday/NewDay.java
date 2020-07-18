package miskyle.realsurvival.randomday;

import miskyle.realsurvival.api.Season;
import miskyle.realsurvival.util.RSEntry;

public class NewDay {
	private String			worldName;
	private Season 		season;
	private int 				day;
	private double 		humidity;
	private double 		windSpeed;
	private double 		windFrequency;
	private double 		rainFrequency;
	private double 		baseTemperature;
	private double 		dayTemperature;
	private double 		nightTemperature;
	private double 		rainTemperature;
	private double 		alpheTemperature;
	public NewDay() {
		
	}
	
	public static NewDay newDay(String worldName,WorldData worldData,int day) {
		NewDay newDay = new NewDay();
		newDay.setWorldName(worldName);
		newDay.setDay(day);
		newDay.setSeason(worldData.getSeason(day));
		newDay.setHumidity(getRandomNumber(worldData.getHumidity().get(newDay.getSeason())));
		newDay.setWindSpeed(getRandomNumber(worldData.getWindSpeed().get(newDay.getSeason())));
		newDay.setWindFrequency(getRandomNumber(worldData.getWindFrequency().get(newDay.getSeason())));
		newDay.setRainFrequency(getRandomNumber(worldData.getRainFrequency().get(newDay.getSeason())));
		newDay.setBaseTemperature(getRandomNumber(worldData.getBaseTemperature().get(newDay.getSeason())));
		newDay.setDayTemperature(getRandomNumber(worldData.getDayTemperature().get(newDay.getSeason())));
		newDay.setNightTemperature(getRandomNumber(worldData.getNightTemperature().get(newDay.getSeason())));
		newDay.setRainTemperature(getRandomNumber(worldData.getRainTemperature().get(newDay.getSeason())));
		return newDay.init();
	}
	
	public NewDay init() {
		/*
		 * 这里为什么是0.85*0.15呢? 这里采用的函数模型是
		 * f(T) = Bsin(wT)+A
		 * 了解了一下,在Minecraft中温度低于0.15时下雪,
		 * 不妨令Minecraft中0.15的温度为0摄氏度
		 * Minecraft基础温度最大值为2.0.
		 * 不妨把温度为2.0时取得sin函数的最大值,
		 * 那么四分之一周期为1.85. 根据w=2π/T(周期)
		 * 四舍五入取得w=0.85.
		 */
		alpheTemperature = -baseTemperature*Math.sin(0.85*0.15);
		alpheTemperature = Math.floor(alpheTemperature*10)/10;
		baseTemperature = Math.floor(baseTemperature*10)/10;
		return this;
	}

	public String getWorldName() {
		return worldName;
	}

	public void setWorldName(String worldName) {
		this.worldName = worldName;
	}

	public Season getSeason() {
		return season;
	}

	public void setSeason(Season season) {
		this.season = season;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public double getHumidity() {
		return humidity;
	}

	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}

	public double getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}

	public double getWindFrequency() {
		return windFrequency;
	}

	public void setWindFrequency(double windFrequency) {
		this.windFrequency = windFrequency;
	}

	public double getRainFrequency() {
		return rainFrequency;
	}

	public void setRainFrequency(double rainFrequency) {
		this.rainFrequency = rainFrequency;
	}

	public double getBaseTemperature() {
		return baseTemperature;
	}

	public void setBaseTemperature(double baseTemperature) {
		this.baseTemperature = baseTemperature;
	}

	public double getDayTemperature() {
		return dayTemperature;
	}

	public void setDayTemperature(double dayTemperature) {
		this.dayTemperature = dayTemperature;
	}

	public double getNightTemperature() {
		return nightTemperature;
	}

	public void setNightTemperature(double nightTemperature) {
		this.nightTemperature = nightTemperature;
	}

	public double getRainTemperature() {
		return rainTemperature;
	}

	public void setRainTemperature(double rainTemperature) {
		this.rainTemperature = rainTemperature;
	}

	private static double getRandomNumber(RSEntry<Double, Double> entry) {
		return Math.abs(entry.getLeft()-entry.getRight())*Math.random()
				+Math.min(entry.getRight(), entry.getLeft());
	}

	protected double getAlpheTemperature() {
		return alpheTemperature;
	}
	
}
