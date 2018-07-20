package com.outlook.schooluniformsama.event;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.outlook.schooluniformsama.I18n;
import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.player.PlayerData;
import com.outlook.schooluniformsama.nms.NBTPlayer;
import com.outlook.schooluniformsama.util.Msg;
import com.outlook.schooluniformsama.util.Util;

public class SleepEvent implements Listener{
	private boolean sleepDuringDay;
	public SleepEvent(boolean sleepDuringDay) {
		this.sleepDuringDay=sleepDuringDay;
	}
	
	@EventHandler
	public void Sleep(PlayerBedEnterEvent e){
		if(!Data.playerData.containsKey(e.getPlayer().getUniqueId()))return;
		PlayerData pd = Data.playerData.get(e.getPlayer().getUniqueId());
		pd.getSleep().setHasSleep(true);
		e.getPlayer().sendMessage(I18n.tr("sleep1"));
		return;
	}
	
	@EventHandler
	public void leaveSleep(PlayerBedLeaveEvent e){
		if(!Data.playerData.containsKey(e.getPlayer().getUniqueId()))return;
		PlayerData pd = Data.playerData.get(e.getPlayer().getUniqueId());
		pd.getSleep().setHasSleep(false);
		return;
	}
	
	@EventHandler
	public void levelSleep(AsyncPlayerChatEvent e){
		Player p = e.getPlayer();
		if(!Data.playerData.containsKey(p.getUniqueId()))return;
		if(!e.getMessage().equalsIgnoreCase("leave"))return;
		PlayerData pd = Data.playerData.get(p.getUniqueId());
		if(pd.getSleep().isHasSleep()){
			if((pd.getSleep().getSleep()/pd.getSleep().getSleepMax())*100<10){
				Msg.send(p, "messages.sleep.sleeping");
				e.setCancelled(true);
				return;
			}
			NBTPlayer.leaveBed(p);
			Bukkit.getPluginManager().callEvent(new PlayerBedLeaveEvent(p, p.getLocation().getBlock()));
			e.setCancelled(true);
		}else if(p.isSleeping()){
			NBTPlayer.leaveBed(p);
			Bukkit.getPluginManager().callEvent(new PlayerBedLeaveEvent(p, p.getLocation().getBlock()));
			e.setCancelled(true);
		}
	  }
	
	
	@EventHandler
	  public void wantSleep(PlayerInteractEvent e){
		if(!sleepDuringDay)return;
		if(!Data.playerData.containsKey(e.getPlayer().getUniqueId()))return;
		if(e.getClickedBlock()==null || e.getClickedBlock().getType()!=Material.BED_BLOCK)return;
		if(e.getAction()!=Action.RIGHT_CLICK_BLOCK)return;
		if(Util.getDistanceBetweenPoints(e.getPlayer().getLocation(), e.getClickedBlock().getLocation())>2){
			e.getPlayer().sendMessage(I18n.tr("sleep3"));
			e.setCancelled(true);
			return;
		}
		e.setCancelled(true);
		NBTPlayer.sleep(e.getPlayer(), e.getClickedBlock().getLocation());	
		Bukkit.getPluginManager().callEvent(new PlayerBedEnterEvent(e.getPlayer(), e.getClickedBlock()));
/*		plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, ()->{
			if(e.getPlayer().isSleeping())
				Bukkit.getPluginManager().callEvent(new PlayerBedEnterEvent(e.getPlayer(), e.getClickedBlock()));
			else 
				NBTPlayer.leaveBed(e.getPlayer());
		}, 10);*/

	  }
}
