package com.outlook.schooluniformsama.event;

import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.outlook.schooluniformsama.I18n;
import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.player.PlayerData;
import com.outlook.schooluniformsama.util.Msg;
import com.outlook.schooluniformsama.util.Util;

public class SleepEvent implements Listener{
	public static boolean sleepDuringDay;
	private static LinkedList<String> sleep = new LinkedList<>();
	
	
	public SleepEvent(boolean sleepDuringDay) {
		SleepEvent.sleepDuringDay=sleepDuringDay;
	}
	
	@EventHandler
	public void Sleep(PlayerBedEnterEvent e){
		if(!Data.playerData.containsKey(e.getPlayer().getUniqueId()))return;
		PlayerData pd = Data.playerData.get(e.getPlayer().getUniqueId());
		pd.getSleep().setHasSleep(true);
		sleep.add(e.getPlayer().getName());
		return;
	}
	
	@EventHandler
	public void leaveSleep(PlayerBedLeaveEvent e){
		if(!Data.playerData.containsKey(e.getPlayer().getUniqueId()))return;
		PlayerData pd = Data.playerData.get(e.getPlayer().getUniqueId());
		pd.getSleep().setHasSleep(false);
		sleep.remove(e.getPlayer().getName());
		return;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void levelSleep(AsyncPlayerChatEvent e){
		if(!sleepDuringDay)return;
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
			Data.bed.level(p);
			Bukkit.getPluginManager().callEvent(new PlayerBedLeaveEvent(p, p.getLocation().getBlock()));
			e.setCancelled(true);
			sleep.remove(e.getPlayer().getName());
		}else if(p.isSleeping()){
			Data.bed.level(p);
			Bukkit.getPluginManager().callEvent(new PlayerBedLeaveEvent(p, p.getLocation().getBlock()));
			e.setCancelled(true);
			sleep.remove(e.getPlayer().getName());
		}else if(sleep.contains(p.getName())){
			Data.bed.level(p);
			Bukkit.getPluginManager().callEvent(new PlayerBedLeaveEvent(p, p.getLocation().getBlock()));
			e.setCancelled(true);
			sleep.remove(e.getPlayer().getName());
		}
	  }
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onSleep(PlayerCommandPreprocessEvent e){
		if(!sleepDuringDay)return;
		Player p = e.getPlayer();
		if(!Data.playerData.containsKey(p.getUniqueId()))return;
		PlayerData pd = Data.playerData.get(p.getUniqueId());
		if(pd.getSleep().isHasSleep()){
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void beHitWhenSleeping(EntityDamageEvent e) {
		if(!sleepDuringDay)return;
		if(!(e.getEntity() instanceof Player))return;
		Player p = (Player)e.getEntity();
		if(!Data.enableInPlayer(p.getUniqueId()))return;
		if(p.isSleeping() || Data.getPlayerData(p.getUniqueId()).getSleep().isHasSleep()){
			Data.bed.level(p);
			Bukkit.getPluginManager().callEvent(new PlayerBedLeaveEvent(p, p.getLocation().getBlock()));
			e.setCancelled(true);
			sleep.remove(p.getName());
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
		if(sleep.contains(e.getPlayer().getName()))return;
		Data.bed.sleep(e.getPlayer(), e.getClickedBlock().getLocation());
		sleep.add(e.getPlayer().getName());
		e.getPlayer().sendMessage(I18n.tr("sleep1"));
		Bukkit.getPluginManager().callEvent(new PlayerBedEnterEvent(e.getPlayer(), e.getClickedBlock()));
	  }
	
	public static void getUpPlayer(){
		for(String name:sleep){
			Player p = Bukkit.getServer().getPlayer(name);
			Data.bed.sleep(p,p.getLocation());
			Bukkit.getPluginManager().callEvent(new PlayerBedLeaveEvent(p, p.getLocation().getBlock()));
		}
		sleep.clear();
	}
}
