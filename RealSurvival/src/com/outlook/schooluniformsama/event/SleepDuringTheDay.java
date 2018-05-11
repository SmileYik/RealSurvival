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
import com.outlook.schooluniformsama.util.Msg;

public class SleepDuringTheDay implements Listener{
	
	
	@EventHandler
	public void levelSleep(AsyncPlayerChatEvent e)
	  {
		if(!Data.playerData.containsKey(e.getPlayer().getUniqueId()))return;
		if(!e.getMessage().equalsIgnoreCase("leave"))return;
		if(Data.playerData.get(e.getPlayer().getUniqueId()).getSleep().isHasSleep()){
			Bukkit.getPluginManager().callEvent(new PlayerBedLeaveEvent(e.getPlayer(), null));
			e.setCancelled(true);
		}
	  }
	
	
	@EventHandler
	  public void wantSleep(PlayerInteractEvent e)
	  {
		if(!Data.playerData.containsKey(e.getPlayer().getUniqueId()))return;
		if(e.getClickedBlock()==null||e.getClickedBlock().getType()!=Material.BED_BLOCK)return;
		if(e.getAction()!=Action.RIGHT_CLICK_BLOCK)return;
		Location l=e.getPlayer().getLocation();
		Location z=e.getClickedBlock().getLocation();
		if(l.getZ()-z.getZ()>2 || l.getY()-z.getY()>2 || l.getX()-z.getX()>2 || z.getY()-1>l.getY()){
			Msg.sendMsgToPlayer(e.getPlayer(), "BedSoFar", true);
			return;
		}
		Data.bnms.sleep(e.getPlayer(), e.getClickedBlock().getLocation());
		Bukkit.getPluginManager().callEvent(new PlayerBedEnterEvent(e.getPlayer(), e.getClickedBlock()));
		e.setCancelled(true);
	  }

}
