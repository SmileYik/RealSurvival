package com.outlook.schooluniformsama.data.player;

import com.outlook.schooluniformsama.data.Data;

public class Sleep {
	private double sleep;
	private double sleepBuff=1;
	private int sleepLevel=0;
	private double addSleep;
	private boolean hasSleep;
	private float effect=0;
	public Sleep(double sleep, double sleepBuff, int sleepLevel, double addSleep,boolean hasSleep) {
		super();
		this.sleep = sleep;
		this.sleepBuff = sleepBuff;
		this.sleepLevel = sleepLevel;
		this.addSleep = addSleep;
		this.hasSleep=hasSleep;
	}
	
	public void changeEffect(float f){
		effect+=f;
	}
	
	public double getMaxSleep(){
		return Data.sleep[0]+addSleep;
	}
	
	public String errorSleep(){
		if(sleep>getMaxSleep()*0.8)
			return "SleepMax";
		else if(sleep<Data.sleep[2])
			return "SleepMin";
		return null;
	}
	
	public void levelUp(int i){
		sleepLevel+=i;
		double sum = 0;
		for(int j=1;j<=sleepLevel;j++){
			sum+=Math.log(j);
		}
		addSleep=Math.sqrt(sum);
	}
	
	public String change(double num){
		num*=sleepBuff;
		num+=num*effect;
		sleep+=num;
		if(sleep<0)
			sleep=0;
		else if(sleep>getMaxSleep())
			sleep=getMaxSleep();
		
		if(sleep<=Data.sleep[2]&&(sleep-num)>Data.sleep[2])
			return "very-tired";
		else if(sleep<=Data.sleep[1]&&(sleep-num)>Data.sleep[1])
			return  "tired";
		else if(sleep>=Data.sleep[0]*0.9&&(sleep-num)<Data.sleep[0]*0.9)
			return "spirit";
		return null;
	}
	
	private void reCheckLevel(){
		double sum = 0;
		for(int j=1;j<=sleepLevel;j++){
			sum+=Math.log(j);
		}
		addSleep=Math.sqrt(sum);
	}

	public double getSleepBuff() {
		return sleepBuff;
	}

	public void setSleepBuff(double sleepBuff) {
		this.sleepBuff = sleepBuff;
	}

	public int getSleepLevel() {
		return sleepLevel;
	}

	public void setSleepLevel(int sleepLevel) {
		this.sleepLevel = sleepLevel;
		reCheckLevel();
	}

	public double getAddSleep() {
		return addSleep;
	}

	public void setAddSleep(double addSleep) {
		this.addSleep = addSleep;
	}

	public void setSleep(double sleep) {
		this.sleep = sleep;
	}

	public boolean isHasSleep() {
		return hasSleep;
	}

	public void setHasSleep(boolean hasSleep) {
		this.hasSleep = hasSleep;
	}

	public double getSleep() {
		return sleep;
	}
	
}
