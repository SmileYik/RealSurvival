package com.outlook.schooluniformsama.data.player;

import org.bukkit.entity.Player;

import com.outlook.schooluniformsama.task.EffectTask;
import com.outlook.schooluniformsama.util.Msg;
import com.outlook.schooluniformsama.util.Util;

public class Illness {
	private String name;
	private double recovery;
	private double medicineEfficacy;
	private long duratio;
	private boolean isTakeMedicine;
	
	public Illness(String name, double recovery, double medicineEfficacy, long duratio, boolean isTakeMedicine) {
		super();
		this.name = name;
		this.recovery = recovery;
		this.medicineEfficacy = medicineEfficacy;
		this.duratio = duratio;
		this.isTakeMedicine = isTakeMedicine;
	}
	
	public Illness(String name) {
		this.name = name;
		recovery=0;
		medicineEfficacy=0;
		duratio=0;
		isTakeMedicine=false;
	}
	
	public void eatMedicine(double medicineEfficacy, long duratio ){
		this.isTakeMedicine=true;
		this.duratio+=duratio;
		if(this.medicineEfficacy==0)
			this.medicineEfficacy=(this.medicineEfficacy+medicineEfficacy)*0.5;
		else this.medicineEfficacy=medicineEfficacy;
	}
	
	public void change(){
		if(recovery>0){
			recovery-=medicineEfficacy*Util.randomNum(0.1, 0.8);
			if(recovery<0)
				recovery=0;			
		}
	}
	
	public boolean subTime(Player p){
		if(duratio<=0&&recovery>0){	//No efficacy
			recovery-=medicineEfficacy*Util.randomNum(0.1, 0.8);
			if(recovery<0)
				recovery=0;
			return false;
		}
		//TODO efficacy
		
		double afterFix = medicineEfficacy;
		
		if(medicineEfficacy<0){
			EffectTask.EffectData ed = EffectTask.getEffect(p, com.outlook.schooluniformsama.data.effect.EffectType.CURESPEED);
			if(ed!=null) afterFix += ed.isPercentage()?medicineEfficacy*ed.getAmplifier():ed.getAmplifier();
			if(afterFix>0) afterFix = 0;
		}else{
			EffectTask.EffectData ed = EffectTask.getEffect(p, com.outlook.schooluniformsama.data.effect.EffectType.CURESPEED);
			if(ed!=null) afterFix += ed.isPercentage()?medicineEfficacy*ed.getAmplifier():ed.getAmplifier();
			if(afterFix<0) afterFix = 0;
		}
		
		recovery+=afterFix;
		if(--duratio<=0){
			duratio=0;
			Msg.send(p, "messages.illness.efficacy-over",name);
			isTakeMedicine=false;
		}
		
		if(recovery>=100){
			Msg.send(p, "messages.illness.recovery-sick",name);
			return true;
		}
		return false;
	}

	public String getName() {
		return name;
	}

	public double getRecovery() {
		return recovery;
	}

	public double getMedicineEfficacy() {
		return medicineEfficacy;
	}

	public long getDuratio() {
		return duratio;
	}

	public boolean isTakeMedicine() {
		return isTakeMedicine;
	}
}
