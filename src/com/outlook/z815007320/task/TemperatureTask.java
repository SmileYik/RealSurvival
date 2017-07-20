package com.outlook.z815007320.task;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.outlook.z815007320.commands.TestCMD;
import com.outlook.z815007320.data.PlayerData;
import com.outlook.z815007320.data.PluginRS;

public class TemperatureTask extends PluginRS implements Runnable{

	@Override
	public void run() {
		double _temper=-0.1;
		long time;
		Set<UUID> players=rs.getPlayerDatas().keySet();
		for(UUID ps:players){
			  Player p = rs.getServer().getPlayer(ps);
			  if(p==null||p.isDead())continue;
		      if ((p.hasPermission("RealSurvival.unlimited")||p.hasPermission("RealSurvival.Admin"))&&!TestCMD.contains(p)) 
		    	  continue;
		      PlayerData pd=rs.getPlayerData(p);
		      _temper=p.getWorld().getTemperature(p.getLocation().getBlockX(), p.getLocation().getBlockZ());
		      time=p.getWorld().getTime();
		      //12000-13800 日落
		      //13800-22200 黑夜
		      //22200-0          日出
		      //0-12000          白天
		      //Material.
		      if(_temper>=0.5)
		    	  _temper-=0.5;
		      else _temper-=0.5;
		      if(time>=12000L&&time<22200L)
		    	  _temper-=Math.random()*0.5;
		      else
		    	  _temper+=Math.random()*0.5;
		      if(p.getWorld().hasStorm())
		    	  _temper-=Math.random()*0.5;
		      _temper+=getBlocks(p.getLocation());
		      _temper+=getInv(p);
		      if(p.getWorld().getBlockAt(p.getLocation()).getLightLevel()<=5)
		    	  _temper-=0.5*(5-p.getWorld().getBlockAt(p.getLocation()).getLightLevel()*0.1);
		      if(p.isSprinting())
		    	  _temper+=Math.random()*0.02;
		      
		      /*if(_temper>0.2)
		    	  pd.changeTemperature(0.2);
		      else if(_temper<0.2)
		    	  pd.changeTemperature(-0.2);
		      else*/
		    	  pd.changeTemperature(_temper*0.02);
		      continue;
	    }
	}

	
	public static double getBlocks(Location l){
		double _tem=0;
		int x=l.getBlockX()+2;
		int y=l.getBlockY()+2;
		int z=l.getBlockZ()+2;
		
		for(int sx=x-4;sx<x;sx++)
			for(int sy=y-4;sy<y;sy++)
				for(int sz=z-4;sz<z;sz++){
					Block temp=l.getWorld().getBlockAt(sx, sy, sz);
					if(rs.getHeatSource().containsKey(temp.getType().name()))
						_tem+=rs.getHeatSource().get(temp.getType().name())*Math.pow(
								1-rs.getDistanceEffect(), Math.sqrt(Math.pow(sx-x, 2)+Math.pow(
										sy-y, 2)+Math.pow(sz-z, 2)));
					
					//System.out.println(sx+" "+sy+" "+sz+" "+temp.getType().name());
				}
		return _tem;
	}
	
	private double getInv(Player p){
		double aTem=0;
		  for(int i=0;i<p.getInventory().getSize();i++)
			  if(p.getInventory().getItem(i)!=null&&rs.getHeatSource().containsKey(p.getInventory().getItem(i).getType().name()))
					aTem+=rs.getHeatSource().get(p.getInventory().getItem(i).getType().name());
		  
		  for(ItemStack is:p.getInventory().getArmorContents())
			  if(is!=null&&rs.getHeatSource().containsKey(is.getType().name()))
					aTem+=rs.getHeatSource().get(is.getType().name());
	      return aTem;
	}
}
