package miskyle.realsurvival.status.listener;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import miskyle.realsurvival.data.ConfigManager;
import miskyle.realsurvival.data.PlayerManager;
import miskyle.realsurvival.data.playerdata.PlayerData;
import miskyle.realsurvival.status.task.EnergyTask;

public class EnergyListener implements Listener{
	private HashMap<String, Location> jumpCheck = new HashMap<>();
	
	@EventHandler
	public void playerMove(PlayerMoveEvent e) {
		if(!PlayerManager.isActive(e.getPlayer().getName())) return;
		PlayerData pd = PlayerManager.getPlayerData(e.getPlayer().getName());
		if(ConfigManager.getEnergyConfig().getDecreaseSwimming()!=0
				&&e.getFrom().getBlock().getType().name().contains("WATER")){
			//Swimming
			if(e.getFrom().getBlockX()!=e.getTo().getBlockX() 
					|| e.getFrom().getBlockZ()!=e.getTo().getBlockZ() 
					|| e.getFrom().getBlockY()<e.getTo().getBlockY()) {
				if(pd.getEnergy().getValue()<ConfigManager.getEnergyConfig().getDecreaseSwimming()) {
					e.setCancelled(true);
				}else {
					EnergyTask.sendMessage(e.getPlayer(), 
							pd.getEnergy().modify(-ConfigManager.getEnergyConfig().getDecreaseSwimming()));
				}
			}
		}else if(ConfigManager.getEnergyConfig().getDecreaseJumping()!=0
				&&isJump(e.getPlayer().getName(),e.getFrom(),e.getTo())){
			if(pd.getEnergy().getValue()<ConfigManager.getEnergyConfig().getDecreaseJumping()
					|| pd.getWeight().getValue()>pd.getWeight().getMaxValue()){
				e.setCancelled(true);
				e.getPlayer().teleport(jumpCheck.get(e.getPlayer().getName()));
			}else {
				EnergyTask.sendMessage(e.getPlayer(), 
						pd.getEnergy().modify(-ConfigManager.getEnergyConfig().getDecreaseJumping()));
			}
			jumpCheck.remove(e.getPlayer().getName());
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
