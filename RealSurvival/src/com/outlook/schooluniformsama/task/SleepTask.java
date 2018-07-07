package com.outlook.schooluniformsama.task;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.MainHand;

import com.outlook.schooluniformsama.RealSurvival;
import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.player.EffectType;
import com.outlook.schooluniformsama.data.player.PlayerData;
import com.outlook.schooluniformsama.util.Util;

import net.minecraft.server.v1_11_R1.EnumHand;

public class SleepTask implements Runnable{
//l.getBlock().setType(Material.BED_BLOCK);
	@Override
	public void run() {
		for(PlayerData pd:Data.playerData.values()){
			Player p = RealSurvival.getPlayer(pd.getUuid());
			
			if(p==null || pd==null || p.isDead())return;
			
			if(pd.getSleep().isHasSleep()){
				byte lightLevel = p.getWorld().getBlockAt(p.getLocation()).getLightLevel();
				if(lightLevel>10)
					pd.change(EffectType.SLEEP, Util.randomNum(Data.sleep[4]*0.1, Data.sleep[4]*0.25));
				else if(lightLevel>5)
					pd.change(EffectType.SLEEP, Util.randomNum(Data.sleep[4]*0.35, Data.sleep[4]*0.55));
				else
					pd.change(EffectType.SLEEP, Util.randomNum(Data.sleep[4]*0.8, Data.sleep[4]));
			}else{
				pd.change(EffectType.SLEEP, -Util.randomNum(Data.sleep[3]*0.9, Data.sleep[3]*1.1));
				//TODO 
				if(pd.getSleep().getSleep()<=1){
					Location l = getLocation(p.getLocation());
					Data.bnms.sleep(p, l);
					Bukkit.getPluginManager().callEvent(new PlayerBedEnterEvent(p, l.getBlock()));
					//l.getBlock().setType(Material.BED_BLOCK);
					/*Data.bnms.sleep(p, l);*/
					//Bukkit.getPluginManager().callEvent(new PlayerInteractEvent(p, Action.RIGHT_CLICK_BLOCK, null, l.getBlock(),BlockFace.UP,EquipmentSlot.HAND));
/*					Bukkit.getPluginManager().callEvent(new PlayerBedEnterEvent(p, l.getBlock()));*/
				}
			}
		}
	}
	
	private Location getLocation(Location l){
/*		if(l.getBlock().getType()==Material.AIR)return l;
		else if(l.getBlock().getRelative(BlockFace.EAST).getType() == Material.AIR)return l.getBlock().getRelative(BlockFace.EAST).getLocation();
		else if(l.getBlock().getRelative(BlockFace.WEST).getType() == Material.AIR)return l.getBlock().getRelative(BlockFace.WEST).getLocation();
		else if(l.getBlock().getRelative(BlockFace.SOUTH).getType() == Material.AIR)return l.getBlock().getRelative(BlockFace.SOUTH).getLocation();
		else if(l.getBlock().getRelative(BlockFace.NORTH).getType() == Material.AIR)return l.getBlock().getRelative(BlockFace.NORTH).getLocation();*/
		return l.add(0,-1,0);
	}
}
