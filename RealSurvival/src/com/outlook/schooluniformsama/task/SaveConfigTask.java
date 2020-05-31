package com.outlook.schooluniformsama.task;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.player.PlayerData;
import com.outlook.schooluniformsama.data.recipe.timer.FurnaceTimer;
import com.outlook.schooluniformsama.data.recipe.timer.Timer;
import com.outlook.schooluniformsama.randomday.RandomDayManager;

public class SaveConfigTask implements Runnable{

	@Override
	public void run() {
		for(PlayerData pd:Data.playerData.values()){
			if( pd==null)return;
			pd.save();
		}
		
		saveWorkbench();
		RandomDayManager.save();
	}
	
	public static void saveWorkbench(){
		File data=new File(Data.DATAFOLDER+File.separator+"timer.yml");
		data.delete();
		YamlConfiguration dataC=YamlConfiguration.loadConfiguration(data);
		for(Map.Entry<String, Timer> entry : Data.timer.entrySet()){
			dataC.set(entry.getKey()+".playerName", entry.getValue().getPlayerName());
			dataC.set(entry.getKey()+".worldName", entry.getValue().getWorldName());
			dataC.set(entry.getKey()+".workbenchName", entry.getValue().getWorkbenchName());
			dataC.set(entry.getKey()+".x", entry.getValue().getX());
			dataC.set(entry.getKey()+".y", entry.getValue().getY());
			dataC.set(entry.getKey()+".z", entry.getValue().getZ());
			dataC.set(entry.getKey()+".recipeName", entry.getValue().getRecipeName());
			dataC.set(entry.getKey()+".type", entry.getValue().getType().name());
			dataC.set(entry.getKey()+".time", entry.getValue().getTime());
			
			switch(entry.getValue().getType()){
				case FURNACE:
					FurnaceTimer ft=(FurnaceTimer)entry.getValue();
					dataC.set(entry.getKey()+".extra", ft.getExtraTemperature());
					dataC.set(entry.getKey()+".isBad", ft.isBad());
					break;
			default:
				break;
			}
			
		}
		try {dataC.save(data);} catch (IOException e) {}
	}
}
