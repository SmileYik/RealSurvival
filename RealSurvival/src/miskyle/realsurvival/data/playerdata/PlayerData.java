package miskyle.realsurvival.data.playerdata;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import com.github.miskyle.mcpt.MCPT;
import com.github.miskyle.mcpt.mysql.MySQLManager;

import miskyle.realsurvival.data.ConfigManager;

public class PlayerData {
	private String playerName;
	
	private PlayerDataSleep 		sleep;
	private PlayerDataThirst 		thirst;
	private PlayerDataEnergy 	energy;
	private PlayerDataWeight 	weight;
	
	private String status;
	
	public PlayerData(String playerName,PlayerDataSleep sleep,
			PlayerDataThirst thirst,PlayerDataEnergy energy,PlayerDataWeight weight) {
		this.playerName = playerName;
		this.sleep = sleep;
		this.thirst = thirst;
		this.energy = energy;
		this.weight = weight;
	}
	
	public static PlayerData getPlayerData(String name) {
		PlayerDataSleep 	sleep = new PlayerDataSleep();
		PlayerDataThirst 	thirst = new PlayerDataThirst();
		PlayerDataEnergy 	energy = new PlayerDataEnergy();
		PlayerDataWeight weight = new PlayerDataWeight();
		List<String> 			extraValue = null;
		if(ConfigManager.isEnableMySql() && MySQLManager.connect()) {
			//Use MySQL
			try {
				ResultSet rs = MySQLManager.execute(
						"SELECT * FROM RealSurvival WHERE Name='"+name.toLowerCase()+"'").executeQuery();
				if(rs.next()) {
					//Load Player Data From MySQL
					sleep.setValue(rs.getDouble("Sleep"));
					thirst.setValue(rs.getDouble("Thirst"));
					energy.setValue(rs.getDouble("Energy"));
					weight.setValue(rs.getDouble("Weighr"));
					extraValue = Arrays.asList(rs.getString("ExtraValue").split(";"));
				}else {
					//Create New Player Data
					sleep.setValue(ConfigManager.getSleepConfig().getMaxValue());
					thirst.setValue(ConfigManager.getThirstConfig().getMaxValue());
					energy.setValue(ConfigManager.getEnergyConfig().getMaxValue());
					weight.setValue(0);
					extraValue = new ArrayList<String>();
				}
				rs.close();
				MySQLManager.disconnect();
			}catch (SQLException e) {
				e.printStackTrace();
				MySQLManager.disconnect();
				return null;
			}
		}else {
			File file = new File(
					MCPT.plugin.getDataFolder()+"/playerdata/"+name.toLowerCase()+".yml");
			YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
			if(file.exists()) {
				//Load Player Data From File
				sleep.setValue(data.getDouble("sleep"));
				thirst.setValue(data.getDouble("thirst"));
				energy.setValue(data.getDouble("energy"));
				weight.setValue(data.getDouble("weight"));
				extraValue = data.getStringList("extraValue");
			}else {
				//Create New Player Data
				sleep.setValue(ConfigManager.getSleepConfig().getMaxValue());
				thirst.setValue(ConfigManager.getThirstConfig().getMaxValue());
				energy.setValue(ConfigManager.getEnergyConfig().getMaxValue());
				weight.setValue(0);
				extraValue = new ArrayList<String>();
			}
		}
		
		for(String line : extraValue) {
			String[] temp = line.split(",");
			if(MCPT.plugin.getServer().getPluginManager().isPluginEnabled(temp[0])) {
				double sleepTemp = Double.parseDouble(temp[1]);
				double thirstTemp = Double.parseDouble(temp[2]);
				double energyTemp = Double.parseDouble(temp[3]);
				double weightTemp = Double.parseDouble(temp[4]);
				if(sleepTemp!=0) sleep.setExtraMaxValue(temp[0], sleepTemp);
				if(thirstTemp!=0) thirst.setExtraMaxValue(temp[0], thirstTemp);
				if(energyTemp!=0) energy.setExtraMaxValue(temp[0], energyTemp);
				if(weightTemp!=0) weight.setExtraMaxValue(temp[0], weightTemp);
			}
		}
		
		return new PlayerData(name, sleep, thirst, energy, weight);
	}
	
	public void save() {
		
	}

	public String getPlayerName() {
		return playerName;
	}

	public PlayerDataSleep getSleep() {
		return sleep;
	}

	public PlayerDataThirst getThirst() {
		return thirst;
	}

	public PlayerDataEnergy getEnergy() {
		return energy;
	}

	public PlayerDataWeight getWeight() {
		return weight;
	}

	public String getStatus() {
		return status;
	}
	
	
	
}
