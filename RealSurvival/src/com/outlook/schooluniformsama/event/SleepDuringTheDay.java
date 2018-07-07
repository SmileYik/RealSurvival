package com.outlook.schooluniformsama.event;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.player.PlayerData;
import com.outlook.schooluniformsama.util.HashMap;
import com.outlook.schooluniformsama.util.Msg;
import com.outlook.schooluniformsama.util.Util;

public class SleepDuringTheDay implements Listener{
	private static HashMap<Location, Material> sleepLocation = new HashMap<>();
	
	@EventHandler
	public void levelSleep(AsyncPlayerChatEvent e)
	  {
		if(!Data.playerData.containsKey(e.getPlayer().getUniqueId()))return;
		if(!e.getMessage().equalsIgnoreCase("leave"))return;
		PlayerData pd = Data.playerData.get(e.getPlayer().getUniqueId());
		if(pd.getSleep().isHasSleep()){
			if(sleepLocation.containsKey(pd.getSleep().getSleepLocation())){
				if((pd.getSleep().getSleep()/pd.getSleep().getSleepMax())*100<10){
					Msg.sendTitleToPlayer(e.getPlayer(), "sleep.sleeping", false);
					e.setCancelled(true);
					return;
				}else{
					pd.getSleep().getSleepLocation().getBlock().setType(sleepLocation.get(pd.getSleep().getSleepLocation()));
				}
			}
			Data.bnms.level(pd.getPlayer());
			Bukkit.getPluginManager().callEvent(new PlayerBedLeaveEvent(e.getPlayer(), pd.getSleep().getSleepLocation().getBlock()));
			e.setCancelled(true);	
			
		}
	  }
	
	
	@EventHandler
	  public void wantSleep(PlayerInteractEvent e)
	  {
		if(!Data.playerData.containsKey(e.getPlayer().getUniqueId()))return;
		if(e.getClickedBlock()==null || e.getClickedBlock().getType()!=Material.BED_BLOCK)return;
		if(e.getAction()!=Action.RIGHT_CLICK_BLOCK)return;
		if(Util.getDistanceBetweenPoints(e.getPlayer().getLocation(), e.getClickedBlock().getLocation())>1.2){
			Msg.sendMsgToPlayer(e.getPlayer(), "BedSoFar", true);
			return;
		}
		Data.bnms.sleep(e.getPlayer(), e.getClickedBlock().getLocation());
		Bukkit.getPluginManager().callEvent(new PlayerBedEnterEvent(e.getPlayer(), e.getClickedBlock()));
		e.setCancelled(true);
	  }
	

}
