package com.outlook.schooluniformsama.data.player;

import org.bukkit.Location;

import com.outlook.schooluniformsama.data.Data;

public class Sleep {
	private double sleep;
	private double oldSleep;
	private double sleepMax;
	private double other;
	private boolean hasSleep;
	private Location sleepLocation;
	public Sleep(double sleep,double sleepMax,boolean hasSleep) {
		super();
		this.sleep = sleep;
		this.sleepMax = sleepMax;
		this.hasSleep=hasSleep;
	}
	
	public String getState(){
		if(sleep>getSleepMax()*0.9)
			return "SleepMax";
		else if(sleep<getSleepMax()*Data.sleep[2])
			return "SleepMin";
		return null;
	}
	
	public String getInfo(){
		if(sleep<=getSleepMax()*Data.sleep[2] && oldSleep > getSleepMax()*Data.sleep[2]) return "very-tired";
		if(sleep<=getSleepMax()*Data.sleep[1] && oldSleep>getSleepMax()*Data.sleep[1]) return  "tired";
		if(sleep>=getSleepMax()*0.9                   && oldSleep<getSleepMax()*0.9) return "spirit";
		return null;
	}
	
	public void change(double num){
		oldSleep = sleep;
		sleep+=num;
		if(sleep<0)
			sleep=0;
		else if(sleep>getSleepMax())
			sleep=getSleepMax();
	}

	public void setSleep(double sleep) {
		this.sleep = sleep;
	}

	public boolean isHasSleep() {
		return hasSleep;
	}

	public void setHasSleep(boolean hasSleep,Location sleepLocation) {
		this.hasSleep = hasSleep;
		this.sleepLocation = sleepLocation;
	}

	public double getSleep() {
		return sleep;
	}
	
	public double getSleepMax() {
		return sleepMax+other;
	}

	public Location getSleepLocation() {
		return sleepLocation;
	}
	
	
	
}
