package com.outlook.schooluniformsama.randomday;

import org.bukkit.Bukkit;

import com.outlook.schooluniformsama.api.Season;
import com.outlook.schooluniformsama.util.Util;

public class RandomDayTask implements Runnable{
	private String world;
	/** duration humidity wind-speed frequency base-temperature*/
	private double[] spring,summer,autumn,winter;
	/** day humidity wind-speed frequency base-temperature */
	private double[] todayData,tomorrowData;


	public RandomDayTask(String world,double[] spring,double[] summer,double[] autumn,double[] winter,double[] todayData,double[] tomorrowData) {
		this.world = world;
		this.spring = spring;
		this.summer = summer;
		this.autumn = autumn;
		this.winter = winter;
		this.todayData = todayData;
		this.tomorrowData = tomorrowData;
		if(tomorrowData==null){
			randomTomorrow();
		}
		if(todayData==null){
			this.todayData = this.tomorrowData;
			randomTomorrow();
		}
	}

	@Override
	public void run() {
		if(Bukkit.getWorld(world).getTime()<=RandomDayManager.getTick()){
			todayData = tomorrowData;
			randomTomorrow();
		}
	}
	
	public void randomTomorrow(){
		double day;
		if(todayData == null){
			day = 0;
		}else day  = todayData[0]+1;
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
	
	public String getWorld() {
		return world;
	}

	public double[] getSpring() {
		return spring;
	}

	public double[] getSummer() {
		return summer;
	}

	public double[] getAutumn() {
		return autumn;
	}

	public double[] getWinter() {
		return winter;
	}

	public double[] getTodayData() {
		return todayData;
	}
	
	public Season getSeason(){
		int today =  (int) (todayData[0] % (spring[0]+summer[0]+autumn[0]+winter[0]));
		if(today<spring[0])
			return Season.Spring;
		else if (today < summer[0])
			return Season.Summer;
		else if (today < autumn[0])
			return Season.Autumn;
		else if (today < winter[0])
			return Season.Winter;
		else return null;
	}

	public double[] getTomorrowData() {
		return tomorrowData;
	}
}
