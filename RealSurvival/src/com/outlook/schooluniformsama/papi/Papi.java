package com.outlook.schooluniformsama.papi;

import org.bukkit.entity.Player;

import com.outlook.schooluniformsama.I18n;
import com.outlook.schooluniformsama.RealSurvival;
import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.player.PlayerData;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class Papi extends PlaceholderExpansion  {
	private  RealSurvival rs;

	public Papi(RealSurvival data) {
		rs = data;
	}

	@Override
	public String onPlaceholderRequest(Player p, String arg) {
		if (p == null) {return "";}
		PlayerData pd = Data.playerData.get(p.getUniqueId());
		if(pd==null)return I18n.tr("state11");
		
		pd.updateIllnessIndex();
		int index = pd.getIllnessIndex();
		
		if(arg.equalsIgnoreCase("sleep"))
			return _2f(pd.getSleep().getSleep()/pd.getSleep().getSleepMax()*100)+"%";
		
		if(arg.equalsIgnoreCase("thirst"))
			return _2f(pd.getThirst().getThirst()/pd.getThirst().getThirstMax()*100)+"%";
		
		if(arg.equalsIgnoreCase("temperature")){
			if(pd.getTemperature().getTemperature()>pd.getTemperature().getOldTemperature())
				return _2f(pd.getTemperature().getTemperature())+"\u2103 §c§l↑§f";
			else if(pd.getTemperature().getTemperature()<pd.getTemperature().getOldTemperature())
				return _2f(pd.getTemperature().getTemperature())+"\u2103 §3§l↓§f";
			else if(pd.getTemperature().getTemperature()==pd.getTemperature().getOldTemperature())
				return _2f(pd.getTemperature().getTemperature())+"\u2103 §f§l-§f";
		}
		
		if(arg.equalsIgnoreCase("illness")){
			//System.out.println(index);
			while(index>=-1&&index>=pd.getIllness().size()) index--;
			if(index<0)
				return I18n.tr("state2");
			else
				return pd.getIllness().keySet().toArray(new String[pd.getIllness().size()])[index];
			
		}
		
		
		if(arg.equalsIgnoreCase("weight"))
			return _2f(pd.getWeight().getWeight()/pd.getWeight().getWeightMax()*100)+"%";
		
		if(arg.equalsIgnoreCase("energy"))
			return _2f(pd.getEnergy().getEnergy()/pd.getEnergy().getEnergyMax()*100)+"%";
		
		if(arg.equalsIgnoreCase("md")){
			while(index>=0&&index>=pd.getIllness().size())
				index--;
			if(index==-1)
				return "0s";
			else
				return pd.getIllness().get(pd.getIllness().keySet().toArray(
						new String[pd.getIllness().size()])[index]).getDuratio()+"s";
		}
		
		if(arg.equalsIgnoreCase("recovery")){
			while(index>=0&&index>=pd.getIllness().size())
				index--;
			if(index==-1)
				return "100%";
			else
				return _2f(pd.getIllness().get(pd.getIllness().keySet().toArray(
						new String[pd.getIllness().size()])[index]).getRecovery())+"%";
		}
		
		return "";
	}
	
	private String _2f(double d){
		return String.format("%.2f", d);
	}

	@Override
	public String getIdentifier() {
		return "rs";
	}

	@Override
	public String getAuthor() {
		return "miSkYle";
	}

	@Override
	public String getVersion() {
		return rs.getDescription().getVersion();
	}
}
