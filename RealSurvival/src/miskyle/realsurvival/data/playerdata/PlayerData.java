package miskyle.realsurvival.data.playerdata;

import java.io.File;
import java.io.IOException;
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
		List<String> 			extraSleepValue = null;
		List<String> 			extraThirstValue = null;
		List<String> 			extraEnergyValue = null;
		List<String> 			extraWeightValue = null;
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
					extraSleepValue = Arrays.asList(rs.getString("ExtraSleepValue").split(";"));
					extraThirstValue = Arrays.asList(rs.getString("ExtraThirstValue").split(";"));
					extraEnergyValue = Arrays.asList(rs.getString("ExtraEnergyValue").split(";"));
					extraWeightValue = Arrays.asList(rs.getString("ExtraWeightValue").split(";"));
				}else {
					//Create New Player Data
					sleep.setValue(ConfigManager.getSleepConfig().getMaxValue());
					thirst.setValue(ConfigManager.getThirstConfig().getMaxValue());
					energy.setValue(ConfigManager.getEnergyConfig().getMaxValue());
					weight.setValue(0);
					extraSleepValue = new ArrayList<String>();
					extraThirstValue = new ArrayList<String>();
					extraEnergyValue = new ArrayList<String>();
					extraWeightValue = new ArrayList<String>();
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
				extraSleepValue = data.getStringList("extra-sleep");
				extraThirstValue = data.getStringList("extra-thirst");
				extraEnergyValue = data.getStringList("extra-energy");
				extraWeightValue = data.getStringList("extra-weight");
			}else {
				//Create New Player Data
				sleep.setValue(ConfigManager.getSleepConfig().getMaxValue());
				thirst.setValue(ConfigManager.getThirstConfig().getMaxValue());
				energy.setValue(ConfigManager.getEnergyConfig().getMaxValue());
				weight.setValue(0);
				extraSleepValue = new ArrayList<String>();
				extraThirstValue = new ArrayList<String>();
				extraEnergyValue = new ArrayList<String>();
				extraWeightValue = new ArrayList<String>();
			}
		}
		
		initExtraValue(weight, extraWeightValue);
		initExtraValue(energy, extraEnergyValue);
		initExtraValue(thirst, extraThirstValue);
		initExtraValue(sleep, extraSleepValue);
		
		return new PlayerData(name, sleep, thirst, energy, weight);
	}
	
	private static void initExtraValue(PlayerDataStatus status,List<String> extraValue) {
		extraValue.forEach(line->{
			String[] temp = line.split(":");
			if(MCPT.plugin.getServer().getPluginManager().isPluginEnabled(temp[0])) {
				status.setExtraMaxValue(temp[0], Double.parseDouble(temp[1]));
			}
		});
	}
	private static List<String> getExtraValueList(PlayerDataStatus status) {
		List<String> temp = new ArrayList<String>();
		status.getExtraMaxValue().forEach((s,d)->{
			temp.add(s+":"+d);
		});
		return temp;
	}
	
	private static String getExtraValueString(PlayerDataStatus status) {
		StringBuilder sb = new StringBuilder();
		status.getExtraMaxValue().forEach((s,d)->{
			sb.append(s+":"+d);
			sb.append(";");
		});
		return sb.substring(0, sb.length()-1);
	}
	
	public void save() {
		if(ConfigManager.isEnableMySql() && MySQLManager.connect()) {
			ResultSet rs;
			String extraSleepString = getExtraValueString(sleep);
			String extraThirstString = getExtraValueString(thirst);
			String extraEnergyString = getExtraValueString(energy);
			String extraWeightString = getExtraValueString(weight);
			
			try {
				rs = MySQLManager.execute(
						"SELECT * FROM RealSurvival WHERE Name='"+playerName.toLowerCase()+"'")
						.executeQuery();
				if(rs.next()) {
					String sql = "UPDATA RealSurvival SET "
							+"Sleep = '"+sleep.getValue()+"',"
							+"Thirst = '"+thirst.getValue()+"',"
							+"Energy = '"+energy.getValue()+"',"
							+"Weight = '"+weight.getValue()+"',"
							+"ExtraSleepValue = '"+extraThirstString+"',"
							+"ExtraSleepValue = '"+extraThirstString+"',"
							+"ExtraSleepValue = '"+extraThirstString+"',"
							+"ExtraSleepValue = '"+extraThirstString+"'"
							+" WHERE Name = '"+playerName.toLowerCase()+"'";
					MySQLManager.execute(sql);
				}else {
					String sql = "INSERT INTO RealSurvival VALUES ('"
								+playerName.toLowerCase()+"','"
								+sleep.getValue()+"','"
								+thirst.getValue()+"','"
								+energy.getValue()+"','"
								+weight.getValue()+"','"
								+extraSleepString+"','"
								+extraThirstString+"','"
								+extraEnergyString+"','"
								+extraWeightString+"')";
					MySQLManager.execute(sql);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else {
			File file = new File(
					MCPT.plugin.getDataFolder()+"/playerdata/"+playerName.toLowerCase()+".yml");
			YamlConfiguration data = 
					YamlConfiguration.loadConfiguration(file);
			data.set("sleep", sleep.getValue());
			data.set("thirst", thirst.getValue());
			data.set("weight", weight.getValue());
			data.set("energy", energy.getValue());
			data.set("extra-sleep", getExtraValueList(sleep));
			data.set("extra-thirst", getExtraValueList(thirst));
			data.set("extra-weight", getExtraValueList(weight));
			data.set("extra-energy", getExtraValueList(energy));
			try {
				data.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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

	@Override
	public String toString() {
		return "PlayerData [playerName=" + playerName + "]";
	}
	
	
	
}
