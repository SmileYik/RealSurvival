package com.outlook.schooluniformsama.event;


import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.outlook.schooluniformsama.api.data.EffectType;
import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.player.PlayerData;

public class EnergyEvent implements Listener {
	
	//private LinkedList<String> jumpCheck = new LinkedList<>();
	private HashMap<String, Location> jumpCheck = new HashMap<>();
	
	@EventHandler
	public void jump(PlayerMoveEvent e){
		if(!Data.playerData.containsKey(e.getPlayer().getUniqueId()))return;
		PlayerData pd = Data.playerData.get(e.getPlayer().getUniqueId());
		if(!e.getFrom().getBlock().getType().name().contains("WATER")){ //jump
			if(isJump(e.getPlayer().getName(),e.getFrom(),e.getTo())){
				if( (pd.getEnergy().getEnergy()<Data.energy[5]) || pd.getWeight().isOver()){
					e.setCancelled(true);
					e.getPlayer().teleport(jumpCheck.get(e.getPlayer().getName()));
					}
				else
					pd.change(EffectType.ENERGY, -Data.energy[5]);
				
				jumpCheck.remove(e.getPlayer().getName());
			}
		}else if(e.getFrom().getBlockX()!=e.getTo().getBlockX() || e.getFrom().getBlockZ()!=e.getTo().getBlockZ() || e.getFrom().getBlockY()<e.getTo().getBlockY()) {
			if(pd.getEnergy().getEnergy()<Data.energy[6]){
				e.setCancelled(true);
			}else{
				pd.change(EffectType.ENERGY, -Data.energy[6]);
			}
		}
	}
	
	private boolean isJump(String playerName,Location from,Location to){
		if(from.getY()>to.getY()){ //jump down
			if(jumpCheck.containsKey(playerName)){
				return true;
			}
			//System.out.println("JumpDown Y:" + from.getY()+", BlockY: "+from.getBlockY()+"; Y:" + to.getY()+", BlockY: "+to.getBlockY()+"; ");
		}else if(from.getY()<to.getY()){ //jump up
			if(jumpCheck.containsKey(playerName))return false;
			jumpCheck.put(playerName,from);
			//System.out.println("JumpUP Y:" + from.getY()+", BlockY: "+from.getBlockY()+"; Y:" + to.getY()+", BlockY: "+to.getBlockY()+"; ");
		}else if(from.getX()!=to.getX() || from.getZ()!=to.getZ()){ //walk
			if(jumpCheck.containsKey(playerName)){
				jumpCheck.remove(playerName);
			}
		}
		return false;
	}
}
