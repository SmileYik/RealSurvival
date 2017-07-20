package com.outlook.z815007320.event.basic;

import java.io.File;
import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.outlook.z815007320.data.PlayerData;
import com.outlook.z815007320.data.PluginRS;
import com.outlook.z815007320.utils.Utils;

public class BasicEvent extends PluginRS implements Listener{
	//Player Join
	
	@EventHandler
	public void join(PlayerJoinEvent e){
		Player p=e.getPlayer();
		if(p.hasMetadata("NPC"))return;
		if(!rs.worldExists(p.getWorld().getName()))return;
		File data=new File(rs.getDataFolder()+File.separator+"PlayerDatas"+File.separator+p.getUniqueId().toString()+".yml");
		if(!data.exists()){//检查玩家文件是否存在,如果不存在则初始化一个文件
			try {data.createNewFile();} catch (IOException e1) {}
			PlayerData pd=new PlayerData(p.getUniqueId(),p.getWorld().getName(), rs.getSleepMax(), rs.getThirstMax(), 0, 37, 0, rs.getPhysical_strength());
			pd.savaData();
		}
		rs.addPlayerData(p, Utils.getPlayerData(p));
		return;
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent e){
		if(e.getPlayer().hasMetadata("NPC"))return;
		if(rs.getPlayerData(e.getPlayer())==null)return;
		rs.deletePlayer(e.getPlayer());
		return;
	}
	
	@EventHandler
	public void teleport(PlayerTeleportEvent e){
		if(e.getPlayer().hasMetadata("NPC"))return;
		if(!rs.worldExists(e.getTo().getWorld().getName())){
			if(rs.getPlayerData(e.getPlayer())==null)return;
			rs.deletePlayer(e.getPlayer());
			return;
		}
		if(rs.getPlayerData(e.getPlayer())!=null)return;
		Player p=e.getPlayer();
		File data=new File(rs.getDataFolder()+File.separator+"PlayerDatas"+File.separator+p.getUniqueId().toString()+".yml");
		if(!data.exists()){//检查玩家文件是否存在,如果不存在则初始化一个文件
			try {data.createNewFile();} catch (IOException e1) {}
			PlayerData pd=new PlayerData(p.getUniqueId(),p.getWorld().getName(), rs.getSleepMax(), rs.getThirstMax(), 0, 37, 0, rs.getPhysical_strength());
			pd.savaData();
		}
		rs.addPlayerData(p, Utils.getPlayerData(p));
	}
	//Player Join End
	
	//Player Death
	@EventHandler
	public void death(org.bukkit.event.entity.PlayerDeathEvent e){
		Player p = e.getEntity();
		if(rs.getPlayerData(p)==null)return;
		PlayerData pd=rs.getPlayerData(p);
		if(pd.isSick())
			e.setDeathMessage(rs.getMessage("SickDeath", pd));
		else if(pd.getThirst()<rs.getThirstMin())
			e.setDeathMessage(rs.getMessage("ThirstDeath", pd));
		
		pd.changeSleep(rs.getOnDeath()[0]);
		pd.changeThirst(rs.getOnDeath()[1]);
		pd.changePS(rs.getOnDeath()[2]);
		pd.setTemperature(rs.getOnDeath()[3]);
		if(rs.getOnDeath()[4]==1)
			pd.changeAllRecovery(1000);
	}
	//Player Death End
}
