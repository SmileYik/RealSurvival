package com.outlook.schooluniformsama.papi;

import org.bukkit.entity.Player;

import com.outlook.schooluniformsama.RealSurvival;
import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.player.PlayerData;
import com.outlook.schooluniformsama.util.Msg;

import me.clip.placeholderapi.external.EZPlaceholderHook;

public class Papi extends EZPlaceholderHook {
	private int index=0;
	private int time=1000;
	
	public Papi(RealSurvival Data) {
		super(Data, "rs");
	}

	@Override
	public String onPlaceholderRequest(Player p, String arg) {
		if (p == null) {return "";}
		PlayerData pd = Data.playerData.get(p.getUniqueId());
		if(pd==null)return null;
		
		if(time>=1000){
			time=0;
			if(pd.getIllness().isIllness())
				index=(int) (Math.random()*pd.getIllness().getIllness().size());
			else 
				index=-1;
		}else time++;
		
		if(arg.equalsIgnoreCase("sleep"))
			return _2f(pd.getSleep().getSleep()/pd.getSleep().getMaxSleep()*100)+"%";
		
		if(arg.equalsIgnoreCase("thirst"))
			return _2f(pd.getThirst().getThirst()/pd.getThirst().getMaxThirst()*100)+"%";
		
		if(arg.equalsIgnoreCase("temperature")){
			if(pd.getTemperature().getTemperature()>pd.getTemperature().getOldTemperature())
				return _2f(pd.getTemperature().getTemperature())+"℃ §c§l↑§f";
			else if(pd.getTemperature().getTemperature()<pd.getTemperature().getOldTemperature())
				return _2f(pd.getTemperature().getTemperature())+"℃ §3§l↓§f";
			else if(pd.getTemperature().getTemperature()==pd.getTemperature().getOldTemperature())
				return _2f(pd.getTemperature().getTemperature())+"℃ §f§l-§f";
		}
		
		if(arg.equalsIgnoreCase("illness")){
			while(index>=0&&index>=pd.getIllness().getIllness().size())
				index--;
			if(index<0)
				return Msg.getMsg("body2", false);
			else
				return pd.getIllness().getIllness().keySet().toArray(new String[pd.getIllness().getIllness().size()])[index];
			
		}
		
		
		if(arg.equalsIgnoreCase("weight"))
			return _2f(pd.getWeight().getWeight());
		
		if(arg.equalsIgnoreCase("energy"))
			return _2f(pd.getEnergy().getEnergy()/Data.energy[0]*100)+"%";
		
		if(arg.equalsIgnoreCase("md")){
			while(index>=0&&index>=pd.getIllness().getIllness().size())
				index--;
			if(index==-1)
				return "0s";
			else
				return pd.getIllness().getIllness().get(pd.getIllness().getIllness().keySet().toArray(
						new String[pd.getIllness().getIllness().size()])[index]).getDuratio()+"s";
		}
		
		if(arg.equalsIgnoreCase("recovery")){
			while(index>=0&&index>=pd.getIllness().getIllness().size())
				index--;
			if(index==-1)
				return "100%";
			else
				return _2f(pd.getIllness().getIllness().get(pd.getIllness().getIllness().keySet().toArray(
						new String[pd.getIllness().getIllness().size()])[index]).getRecovery())+"%";
		}
		
		return null;
	}
	
	private String _2f(double d){
		return String.format("%.2f", d);
	}
}
