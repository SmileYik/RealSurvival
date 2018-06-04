package com.outlook.schooluniformsama.data.player;

import com.outlook.schooluniformsama.data.Data;

public class Thirst {
	private double thirst;
	private double oldThirst;
	private double other;
	private double thirstMax;
	public Thirst(double thirst, double thirstMax) {
		super();
		this.thirst = thirst;
		this.thirstMax = thirstMax;
	}
	
	public double getThirst(){
		return thirst;
	}
	
	public String getState(){
		if(thirst<Data.thirst[2])
			return "ThirstMin";
		else if(thirst>getThirstMax()*0.9)
			return "ThirstMax";
		return null;
	}
	
	/**get message*/
	public String getInfo(){
		if(thirst<=oldThirst*Data.thirst[2]&&oldThirst>oldThirst*Data.thirst[2])
			return "dehydration";
		if(thirst<=oldThirst*Data.thirst[1]&&oldThirst>oldThirst*Data.thirst[1])
			return  "thirst";
		if(thirst>=oldThirst*0.9&&oldThirst<oldThirst*0.9)
			return "full-of-moisture";
		return null;
	}
	
	public void change(double num){
		oldThirst = thirst;
		thirst+=num;
		if(thirst<0)
			thirst=0;
		else if(thirst>getThirstMax())
			thirst=getThirstMax();
	}

	public void setThirst(double thirst) {
		this.thirst = thirst;
	}
	
	public double getThirstMax() {
		return thirstMax+other;
	}
}
