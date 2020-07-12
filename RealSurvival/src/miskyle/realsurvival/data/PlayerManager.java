package miskyle.realsurvival.data;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;

import miskyle.realsurvival.data.playerdata.PlayerData;

public class PlayerManager {
	private static ConcurrentHashMap<String, PlayerData> playerDatas 
									= new ConcurrentHashMap<String, PlayerData>();
	private static ArrayList<String> freezingPlayer = new ArrayList<String>();
	
	public static void addPlayer(Player p) {
		if(playerDatas.contains(p.getName())) {
			if(!ConfigManager.enableInWorld(p.getWorld().getName())) {
				playerDatas.remove(p.getName());
			}
			return;
		}else if(!p.hasMetadata("NPC")
						&&!freezingPlayer.contains(p.getName())
						&&ConfigManager.enableInWorld(p.getWorld().getName())) {
			PlayerData pd = PlayerData.getPlayerData(p.getName());
			if(pd!=null) playerDatas.put(p.getName(), pd);
		}
		
	}
	
	public static void removePlayer(String playerName) {
		if(playerDatas.contains(playerName)) 
			playerDatas.remove(playerName).save();
	}
	
	public static void freezePlayer(String name) {
		removePlayer(name);
		if(!freezingPlayer.contains(name)) 
			freezingPlayer.add(name);
	}
	
	public static void activePlayer(Player p) {
		if(freezingPlayer.contains(p.getName())) 
			freezingPlayer.remove(p.getName());
		addPlayer(p);
	}
	
	public static ConcurrentHashMap<String, PlayerData> getActivePlayers(){
		return playerDatas;
	}
	
}
