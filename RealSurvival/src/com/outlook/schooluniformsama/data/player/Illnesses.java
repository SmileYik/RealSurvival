package com.outlook.schooluniformsama.data.player;

import java.util.Map;

import org.bukkit.entity.Player;

import com.outlook.schooluniformsama.util.HashMap;

public class Illnesses {
	private HashMap<String,Illness> illness;
	private double illnessBuff=1;
	private int illnessLevel=0;
	private double addIllness;
	private float effect=0;

	public Illnesses(HashMap<String, Illness> illness, double illnessBuff, int illnessLevel, double addIllness) {
		super();
		this.illness = illness;
		this.illnessBuff = illnessBuff;
		this.illnessLevel = illnessLevel;
		this.addIllness=addIllness;
	}
	
	public void changeEffect(float f){
		effect+=f;
	}
	
	public void levelUp(int i){
		illnessLevel+=i;
		double sum = 0;
		for(int j=1;j<=illnessLevel;j++){
			sum+=Math.log(j);
		}
		addIllness=Math.sqrt(sum)*0.2;
	}
	
	public void eatMedicine(String[] list,double medicineEfficacy, long duratio){
		if(list==null){
			for(String str:illness.keySet())
				illness.get(str).eatMedicine(medicineEfficacy, duratio);
			return;
		}
		for(String str:list){
			if(illness.keySet().contains(str))
				illness.get(str).eatMedicine(medicineEfficacy, duratio);
			System.out.println(str);
			
		}
	}
	
	public boolean addIllness(String name,double chance,String remove){
		if(Math.random()*100<(chance-addIllness-effect*chance)){
			if(illness.containsKey(name))
				illness.remove(name);
			if(remove!=null)
				illness.remove(remove);
			illness.put(name, new Illness(name));
			return true;
		}else return false;
	}
	
	public void subTime(Player p){
		for(Map.Entry<String, Illness> entity:illness.entrySet()){
			if(entity.getValue().isTakeMedicine()){
				if(entity.getValue().subTime(illnessBuff, p))
					illness.remove(entity.getKey());				
			}else{
				entity.getValue().change();
			}
		}
	}
	
	public boolean isIllness(){
		if(illness==null||illness.size()<1)
			return false;
		return true;
	}

	public HashMap<String, Illness> getIllness() {
		return illness;
	}

	public void setIllness(HashMap<String, Illness> illness) {
		this.illness = illness;
	}

	public double getIllnessBuff() {
		return illnessBuff;
	}

	public void setIllnessBuff(double illnessBuff) {
		this.illnessBuff = illnessBuff;
	}

	public int getIllnessLevel() {
		return illnessLevel;
	}

	public void setIllnessLevel(int illnessLevel) {
		this.illnessLevel = illnessLevel;
	}

	public double getAddIllness() {
		return addIllness;
	}

	public void setAddIllness(double addIllness) {
		this.addIllness = addIllness;
	}
	
	
	
}
