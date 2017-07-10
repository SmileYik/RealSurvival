package com.outlook.z815007320.task;

import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.outlook.z815007320.commands.TestCMD;
import com.outlook.z815007320.data.PlayerData;
import com.outlook.z815007320.data.PluginRS;

public class SleepTask extends PluginRS implements Runnable{

	@Override
	public void run() {
		if(!rs.isOver())return;
		rs.setOver(false);
		Set<UUID> players=rs.getPlayerDatas().keySet();
		for(UUID ps:players){
			Player p = rs.getServer().getPlayer(ps);
			if(p.isDead())continue;
	      if ((p.hasPermission("RealSurvival.unlimited")||p.hasPermission("RealSurvival.Admin"))&&!TestCMD.contains(p)) 
	    	  continue;
	      PlayerData pd=rs.getPlayerData(p);
	      if(pd.isSleep())
	      {
	    	  byte lightLevel=pd.getLight();
	    	  if(lightLevel>10){
	    		  pd.changeSleep(rs.getSleepAdd()*0.3);
	    		  continue;
	    	  }
	    	  else if(lightLevel>5){
	    		  pd.changeSleep(rs.getSleepAdd()*0.6);
	    		  continue;
	    	  }
	    	  pd.changeSleep(rs.getSleepAdd());
    		  continue; 
	      }else{
	    	  pd.changeSleep(-rs.getSleepSub());
    		  continue;
	      }
	    }
		rs.setOver(true);
	}
	
}
