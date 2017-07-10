package com.outlook.z815007320.papi;

import org.bukkit.entity.Player;

import com.outlook.z815007320.RealSurvival;
import com.outlook.z815007320.data.PlayerData;

import me.clip.placeholderapi.external.EZPlaceholderHook;

public class Papi extends EZPlaceholderHook {

	private RealSurvival plugin;
	private int sickKind;
	private int time=1000;
	
	public Papi(RealSurvival plugin) {
		super(plugin, "rs");
		this.plugin=plugin;
	}

	@Override
	public String onPlaceholderRequest(Player p, String arg) {
		if (p == null) {return "";}
		time++;
		if(plugin.getPlayerData(p)==null)return null;
		if(plugin.getPlayerData(p).getSickKindMap().size()>1){
			if(time>=1000){
				time=0;
				sickKind=(int) (Math.random()*plugin.getPlayerData(p).getSickKindMap().size());
			}
		}else if(plugin.getPlayerData(p).getSickKindMap().size()==1)
			sickKind=0;
		else sickKind=-1;
		
		if(arg.equalsIgnoreCase("sleep"))
			return _2f(plugin.getPlayerData(p).getSleep()/plugin.getSleepMax()*100)+"%";
		
		if(arg.equalsIgnoreCase("thirst"))
			return _2f(plugin.getPlayerData(p).getThirst()/plugin.getThirstMax()*100)+"%";
		
		if(arg.equalsIgnoreCase("tem")){
			PlayerData pd=plugin.getPlayerData(p);
			if(pd.getTemperature()>pd.getOldTemperature())
				return _2f(pd.getTemperature())+"℃ §c§l↑§f";
			else if(pd.getTemperature()<pd.getOldTemperature())
				return _2f(pd.getTemperature())+"℃ §3§l↓§f";
			else if(pd.getTemperature()==pd.getOldTemperature())
				return _2f(pd.getTemperature())+"℃ §f§l-§f";
		}
		
		if(arg.equalsIgnoreCase("sick"))
			if(sickKind!=-1)
				return plugin.getPlayerData(p).getSickKind()[sickKind];
			else return "身体正常";
		
		
		if(arg.equalsIgnoreCase("weight"))
			return _2f(plugin.getPlayerData(p).getWeight());
		
		if(arg.equalsIgnoreCase("ps"))
			return _2f(plugin.getPlayerData(p).getPhysical_strength()/plugin.getPhysical_strength()*100)+"%";
		
		if(arg.equalsIgnoreCase("md"))
			if(sickKind!=-1)
				return plugin.getPlayerData(p).getDuration()[sickKind];
			else return "0s";
		
		if(arg.equalsIgnoreCase("recovery"))
			if(sickKind!=-1)
				return plugin.getPlayerData(p).getRecovery()[sickKind]+"%";
			else return "100%";
		
		return null;
	}
	
	private String _2f(double d){
		return String.format("%.2f", d);
	}
}
