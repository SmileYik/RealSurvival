package com.outlook.z815007320.task;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.entity.Player;

import com.outlook.z815007320.commands.TestCMD;
import com.outlook.z815007320.data.PlayerData;
import com.outlook.z815007320.data.PluginRS;

public class SickTask extends PluginRS implements Runnable{

	@Override
	public void run() {
		Set<UUID> players=rs.getPlayerDatas().keySet();
		for(UUID ps:players){
			Player p = rs.getServer().getPlayer(ps);
			if(p==null||p.isDead())continue;
		      if ((p.hasPermission("RealSurvival.unlimited")||p.hasPermission("RealSurvival.Admin"))&&!TestCMD.contains(p)) 
		    	  continue;
		      PlayerData pd=rs.getPlayerData(p);
		      if(p==null||p.isDead())continue;
		      
		      Iterator<Map.Entry<String, Object[]>> ite = pd.getSickKindMap().entrySet().iterator();  
		      try{
				while(ite.hasNext()){
					Entry<String, Object[]> entity = ite.next();
					if(Boolean.parseBoolean(entity.getValue()[3].toString())){
						pd.changeDuration(1, entity.getKey());
						pd.changeRecovery(Double.parseDouble(entity.getValue()[2].toString()), entity.getKey());
					}else if(Double.parseDouble(entity.getValue()[2].toString())>0)
						pd.changeRecovery(-Double.parseDouble(entity.getValue()[2].toString()), entity.getKey());
					else pd.changeRecovery(Double.parseDouble(entity.getValue()[2].toString()), entity.getKey());
				}
		      }catch(Exception e){};
		}
	}
	
}
