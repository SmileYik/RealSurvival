package miskyle.realsurvival.data;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;

import com.github.miskyle.mcpt.nms.actionbar.NMSActionBar;
import com.github.miskyle.mcpt.nms.sleep.NMSSleep;
import com.github.miskyle.mcpt.nms.title.NMSTitle;

import miskyle.realsurvival.RealSurvival;
import miskyle.realsurvival.data.playerdata.PlayerData;
import miskyle.realsurvival.util.ActionNullBar;

public class PlayerManager {
	public static NMSTitle title;
	public static NMSActionBar bar;
	public static NMSSleep sleep;
	
	private static ConcurrentHashMap<String, PlayerData> playerDatas 
									= new ConcurrentHashMap<String, PlayerData>();
	private static ArrayList<String> freezingPlayer = new ArrayList<String>();
	
	public static void init() {
		String version = RealSurvival.getVersion();
		title = NMSTitle.getTitle(version);
		bar = NMSActionBar.getActionBar(version);
		sleep = NMSSleep.getNMSSleep(version);
		if(bar == null) {
		  bar = new ActionNullBar();
		}
	}
	
	public static void addPlayer(Player p) {
		if(playerDatas.containsKey(p.getName())) {
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
		if(playerDatas.containsKey(playerName)) 
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
	
	public static boolean isActive(Player p) {
		return isActive(p.getName());
	}
	
	public static boolean isActive(String playerName) {
		return playerDatas.containsKey(playerName);
	}
	
	public static PlayerData getPlayerData(String playerName) {
		return playerDatas.get(playerName);
	}
	
}
