package com.outlook.schooluniformsama.task;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.outlook.schooluniformsama.RealSurvival;
import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.player.PlayerData;
import com.outlook.schooluniformsama.util.Msg;

public class TemperatureTask implements Runnable{

	@Override
	public void run() {
		for(PlayerData pd:Data.playerData.values()){
			Player p = RealSurvival.getPlayer(pd.getUuid());
			
			if(p==null || pd==null || p.isDead())return;
			
			double temperature = p.getWorld().getTemperature(p.getLocation().getBlockX(), p.getLocation().getBlockZ())-0.5;
			long time = p.getWorld().getTime();
			
			if(time>=12000L&&time<13800L)
				temperature += Math.random()*0.5-(time-12000L)/6000.0;
			else if(time>=13800L&&time<22200)
				temperature -= Math.random()*0.5;
			else if(time>=0&&time<12000)
				temperature += Math.random()*0.5;
			else
				temperature -= Math.random()*0.5+(time-22200L)/6000.0;
			if(p.getWorld().hasStorm())
				temperature-=Math.random()*0.2;
			temperature+=getBlocks(p.getLocation());
			temperature+=getInv(p);
			if(p.isSprinting())
				temperature+=Math.random()*0.02;
			Msg.sendTitleToPlayer(p, pd.getTemperature().change(temperature*0.01),true);
		}
	}
	public static double getBlocks(Location l){
		//5x5x5
		double _tem=0;
		int x=l.getBlockX()+(int)((Data.temperature[0]-1)*0.5);
		int y=l.getBlockY()+(int)((Data.temperature[2]-1)*0.5);
		int z=l.getBlockZ()+(int)((Data.temperature[1]-1)*0.5);
		
		for(int sx=x-((int)Data.temperature[0]-1);sx<x;sx++)
			for(int sy=y-((int)Data.temperature[2]-1);sy<y;sy++)
				for(int sz=z-((int)Data.temperature[1]-1);sz<z;sz++){
					Block temp=l.getWorld().getBlockAt(sx, sy, sz);
					if(Data.itemData.containsKey(temp.getType().name()))
						_tem+=Data.itemData.get(temp.getType().name()).getHeat()*Math.pow(
								1-Data.temperature[4], Math.sqrt(Math.pow(sx-x, 2)+Math.pow(
										sy-y, 2)+Math.pow(sz-z, 2)));
				}
		return _tem;
	}
	
	private double getInv(Player p){
		double aTem=0;
		for(ItemStack is:p.getInventory().getContents())
			if(is!=null&&Data.itemData.containsKey(is.getType().name()))
				aTem+=Data.itemData.get(is.getType().name()).getHeat();
	      return aTem;
	}
}
