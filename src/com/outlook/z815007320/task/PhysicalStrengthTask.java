package com.outlook.z815007320.task;

import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.outlook.z815007320.commands.TestCMD;
import com.outlook.z815007320.data.PlayerData;
import com.outlook.z815007320.data.PluginRS;

public class PhysicalStrengthTask extends PluginRS implements Runnable{

	@Override
	public void run() {
		Set<UUID> players=rs.getPlayerDatas().keySet();
		for(UUID ps:players){
			Player p = rs.getServer().getPlayer(ps);
			if(p==null||p.isDead())continue;
			if ((p.hasPermission("RealSurvival.unlimited")||p.hasPermission("RealSurvival.Admin"))&&!TestCMD.contains(p)) 
		    	  continue;
		      PlayerData pd=rs.getPlayerData(p);
		      if(p.isSneaking())
		    	  if(pd.getPhysical_strength()<rs.getSneaking())
		    		  p.setSneaking(false);
		    	  else{
		    		  pd.changePS(-rs.getSneaking());
		    		  continue;
		    	  }
		      else if(p.isSprinting())
		    	  if(pd.getPhysical_strength()<rs.getSprinting())
		    		  p.setSprinting(false);
		    	  else{
		    		  pd.changePS(-rs.getSprinting());
		    		  continue;
		    	  }
		      else
		    	  pd.changePS(rs.getPhysical_strength_add());
		    }
	}
	
}
