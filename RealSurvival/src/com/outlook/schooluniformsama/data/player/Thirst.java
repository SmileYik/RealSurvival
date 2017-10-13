package com.outlook.schooluniformsama.data.player;

import com.outlook.schooluniformsama.data.Data;

public class Thirst {
	private double thirst;
	private double thirstBuff=1;
	private int thirstLevel=0;
	private double addThirst;
	private float effect=0;
	public Thirst(double thirst, double thirstBuff, int thirstLevel, double addThirst) {
		super();
		this.thirst = thirst;
		this.thirstBuff = thirstBuff;
		this.thirstLevel = thirstLevel;
		this.addThirst = addThirst;
	}
	
	public void changeEffect(float f){
		effect+=f;
	}
	
	public double getMaxThirst(){
		return addThirst+Data.thirst[0];
	}
	
	public double getThirst(){
		return thirst;
	}
	
	public String errorThirst(){
		if(thirst<Data.thirst[2])
			return "ThirstMin";
		else if(thirst>getMaxThirst()*0.9)
			return "ThirstMax";
		return null;
	}
	
	public void levelUp(int i){
		thirstLevel+=i;
		double sum = 0;
		for(int j=1;j<=thirstLevel;j++){
			sum+=Math.log(j);
		}
		addThirst=Math.sqrt(sum);
	}
	
	public String change(double num){
		num*=thirstBuff;
		num+=num*effect;
		thirst+=num;
		if(thirst<0)
			thirst=0;
		else if(thirst>getMaxThirst())
			thirst=getMaxThirst();
		
		if(thirst<=Data.thirst[2]&&(thirst-num)>Data.thirst[2])
			return "dehydration";
		else if(thirst<=Data.thirst[1]&&(thirst-num)>Data.thirst[1])
			return  "thirst";
		else if(thirst>=Data.thirst[0]*0.9&&(thirst-num)<Data.thirst[0]*0.9)
			return "full-of-moisture";
		return null;
	}
	
	private void reCheckLevel(){
		double sum = 0;
		for(int j=1;j<=thirstLevel;j++){
			sum+=Math.log(j);
		}
		addThirst=Math.sqrt(sum);
	}

	public double getThirstBuff() {
		return thirstBuff;
	}

	public void setThirstBuff(double thirstBuff) {
		this.thirstBuff = thirstBuff;
	}

	public int getThirstLevel() {
		return thirstLevel;
	}

	public void setThirstLevel(int thirstLevel) {
		this.thirstLevel = thirstLevel;
		reCheckLevel();  
	}

	public double getAddThirst() {
		return addThirst;
	}

	public void setAddThirst(double addThirst) {
		this.addThirst = addThirst;
	}

	public void setThirst(double thirst) {
		this.thirst = thirst;
	}
	
	
}
