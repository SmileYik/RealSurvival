package com.outlook.z815007320.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.outlook.z815007320.RealSurvival;
import com.outlook.z815007320.data.PlayerData;

public class StateCMD {
	public static boolean state(String[] args,Player p,RealSurvival plug){
		if(args.length<5)
			return false;
		PlayerData pd=getPlayerData(args[3], p, plug);
		if(pd==null)return false;
		if(args[1].equalsIgnoreCase("add")){
			//add
			double d =toDouble(args[4], p);
			if(d==-1)return false;
			if(args[2].equalsIgnoreCase("sleep"))
				pd.changeSleep(d);
			else if(args[2].equalsIgnoreCase("thirst"))
				pd.changeThirst(d);
			else if(args[2].equalsIgnoreCase("tem"))
				pd.changeTemperature(d);
			else if(args[2].equalsIgnoreCase("ps"))
				pd.changePS(d);
			else if(args[2].equalsIgnoreCase("recovery"))
				pd.changeAllRecovery(d);
			else return false;
		}else if(args[1].equalsIgnoreCase("set")){
			//set
			if(args[2].equalsIgnoreCase("sick")){
				if(args[4].equalsIgnoreCase("true")){
					pd.setSick(true);
					pd.addSickKind(args[5]);
				}else if(args[4].equalsIgnoreCase("false"))
					pd.changeAllRecovery(1000);
				return true;
			}
			double d =toDouble(args[4], p);
			if(d==-1)return false;
			if(args[2].equalsIgnoreCase("sleep"))
				pd.setSleep(d);
			else if(args[2].equalsIgnoreCase("thirst"))
				pd.setThirst(d);
			else if(args[2].equalsIgnoreCase("tem"))
				pd.setTemperature(d);
			else if(args[2].equalsIgnoreCase("ps"))
				pd.setPhysical_strength(d);
			else if(args[2].equalsIgnoreCase("recovery"))
				pd.setAllRecovery(d);
			else return false;
		}
		return true;
	}
	
	private static double toDouble(String str,Player p){
		double d;
		try{
			d=Double.parseDouble(str);
		}catch (Exception e) {
			p.sendMessage("§9[RealSurvival] §b数值错误!...");
			return -1;
		}
		return d;
	}
	
	private static PlayerData getPlayerData(String name,Player pl,RealSurvival plug){
		Player p=Bukkit.getServer().getPlayer(name);
		if(p==null){
			pl.sendMessage("§9[RealSurvival] §b玩家未在线...");
			return null;
		}
		return plug.getPlayerData(p);
	}
}
