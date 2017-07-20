package com.outlook.z815007320.task;

import java.util.Set;
import java.util.UUID;
import com.outlook.z815007320.data.PluginRS;
import com.outlook.z815007320.utils.Utils;

public class SaveConfigTask extends PluginRS implements Runnable{

	@Override
	public void run() {
		Set<UUID> players=rs.getPlayerDatas().keySet();
		for(UUID ps:players)
	      rs.getPlayerData( rs.getServer().getPlayer(ps)).savaData();
		Utils.saveWorkbench();
	}
	

}
