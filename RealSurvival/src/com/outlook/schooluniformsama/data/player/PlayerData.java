package com.outlook.schooluniformsama.data.player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.effect.EffectType;
import com.outlook.schooluniformsama.util.HashMap;
import com.outlook.schooluniformsama.util.Msg;
import com.outlook.schooluniformsama.util.Util;

public class PlayerData {
	//Data 
	private UUID uuid;
	private String world;
	
	private Thirst thirst;
	private Sleep sleep;
	private Energy energy;
	private Temperature temperature;
	private Weight weight;
	private Illnesses illness;

	/**
	 * Create a new player data
	 * @param uuid
	 * @param world
	 * @param isRandom
	 */
	public PlayerData(UUID uuid, String world,boolean isRandom) {
		super();
		this.uuid = uuid;
		this.world = world;
		sleep = new Sleep(Data.sleep[0], 1, 0, 0, false);
		thirst = new Thirst(Data.thirst[0], 1,0,0);
		energy = new Energy(Data.energy[0], 1,0,0);
		temperature = new Temperature(37, 1,0,0);
		weight = new Weight(0, 1,0);
		illness = new Illnesses(new HashMap<>(), 1,0,0);
		if(isRandom){
			thirst.levelUp((int)Util.randomNum(0, 10));
			weight.levelUp((int)Util.randomNum(0, 10));
			energy.levelUp((int)Util.randomNum(0, 10));
			temperature.levelUp((int)Util.randomNum(0, 10));
			sleep.levelUp((int)Util.randomNum(0, 10));
			illness.levelUp((int)Util.randomNum(0, 10));
		}
	}
	
	/**
	 * load a player data
	 * @param uuid
	 * @param world
	 * @param thirst
	 * @param sleep
	 * @param energy
	 * @param temperature
	 * @param weight
	 * @param illness
	 */
	public PlayerData(UUID uuid, String world, Thirst thirst, Sleep sleep, Energy energy, Temperature temperature,
			Weight weight, Illnesses illness) {
		super();
		this.uuid = uuid;
		this.world = world;
		this.thirst = thirst;
		this.sleep = sleep;
		this.energy = energy;
		this.temperature = temperature;
		this.weight = weight;
		this.illness = illness;
	}
	
	public void save(){
		YamlConfiguration data = YamlConfiguration.loadConfiguration(new File(Data.DATAFOLDER+"/playerdata/"+uuid.toString()+".yml"));
		data.set("world", world);
		//Sleep
		data.set("sleep.sleep", sleep.getSleep());
		data.set("sleep.sleepBuff", sleep.getSleepBuff());
		data.set("sleep.sleepLevel", sleep.getSleepLevel());
		data.set("sleep.addSleep", sleep.getAddSleep());
		data.set("sleep.hasSleep", sleep.isHasSleep());
		
		data.set("thirst.thirst", thirst.getThirst());
		data.set("thirst.thirstBuff", thirst.getThirstBuff());
		data.set("thirst.thirstLevel", thirst.getThirstLevel());
		data.set("thirst.addThirst", thirst.getAddThirst());
		
		data.set("energy.energy", energy.getEnergy());
		data.set("energy.energyBuff", energy.getEnergyBuff());
		data.set("energy.energyLevel", energy.getEnergyLevel());
		data.set("energy.addEnergy", energy.getAddEnergy());
		
		data.set("weight.weight", weight.getWeight());
		data.set("weight.weightLevel", weight.getWeightLevel());
		data.set("weight.addWeight", weight.getAddWeight());
		
		data.set("temperature.temperature", temperature.getTemperature());
		data.set("temperature.temperatureBuff", temperature.getTemperatureBuff());
		data.set("temperature.temperatureLevel", temperature.getTemperatureLevel());
		data.set("temperature.addTemperature", temperature.getAddTemperature());
		
		data.set("illnesses.illnessesBuff", illness.getIllnessBuff());
		data.set("illnesses.illnessesLevel", illness.getIllnessLevel());
		data.set("illnesses.addIllnesses", illness.getAddIllness());
		data.set("illnesses.list", illness.getIllness().keySet().toArray(new String[illness.getIllness().size()]));
		for(Illness i:illness.getIllness().values()){
			data.set("illnesses.illness."+i.getName()+".name",i.getName());
			data.set("illnesses.illness."+i.getName()+".duration",i.getDuratio());
			data.set("illnesses.illness."+i.getName()+".medicineEfficacy",i.getMedicineEfficacy());
			data.set("illnesses.illness."+i.getName()+".recovery",i.getRecovery());
			data.set("illnesses.illness."+i.getName()+".isTakeMedicine",i.isTakeMedicine());
		}
		//TODO Save File
		try {
			//data.save(new File("su.yml"));
			data.save(new File(Data.DATAFOLDER+"/playerdata/"+uuid.toString()+".yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static PlayerData load(UUID uuid){
		File dataFile = new File(Data.DATAFOLDER+"/playerdata/"+uuid.toString()+".yml");
		if(!dataFile.exists())
			return new PlayerData(uuid, Bukkit.getPlayer(uuid).getWorld().getName(), Data.switchs[0]);
		YamlConfiguration data = YamlConfiguration.loadConfiguration(dataFile);
		
		HashMap<String,Illness> illness = new HashMap<>();
		
		for(String illnessName:data.getStringList("illnesses.list")){
			illness.put(illnessName, new Illness(data.getString("illnesses.illness."+illnessName+".name"),data.getDouble("illnesses.illness."+illnessName+".recovery"), 
					data.getDouble("illnesses.illness."+illnessName+".medicineEfficacy"), data.getLong("illnesses.illness."+illnessName+".duration"),
					data.getBoolean("illnesses.illness."+illnessName+".isTakeMedicine")));
		}
		
		return new PlayerData(uuid, data.getString("world"), new Thirst(data.getDouble("thirst.thirst"), data.getDouble("thirst.thirstBuff"), data.getInt("thirst.thirstLevel"), data.getDouble("thirst.addThirst")), 
				new Sleep(data.getDouble("sleep.sleep"), data.getDouble("sleep.sleepBuff"), data.getInt("sleep.sleepLevel"), data.getDouble("sleep.addSleep"),data.getBoolean("sleep.hasSleep")),
				new Energy(data.getDouble("energy.energy"), data.getDouble("energy.energyBuff"), data.getInt("energy.energyLevel"), data.getDouble("energy.addEnergy")), 
				new Temperature(data.getDouble("temperature.temperature"), data.getDouble("temperature.temperatureBuff"), data.getInt("temperature.temperatureLevel"), data.getDouble("temperature.addTemperature")), 
				new Weight(data.getDouble("weight.weight"), data.getInt("weight.weightLevel"), data.getDouble("weight.addWeight")), 
				new Illnesses(illness, data.getDouble("illnesses.illnessesBuff"), data.getInt("illnesses.illnessesLevel"), data.getDouble("illnesses.addIllnesses")));
	}
	
	public void sendData(){
		String body;
		String illnessData="";
		if(illness.isIllness()){
			body=Msg.getMsg("body1", false);
			for(Illness i:illness.getIllness().values()){
				illnessData+=i.getName()+":\n";
				illnessData+="  "+Msg.getMsg("recovery", new String[]{"%recovery%"},new String[]{Util.ReservedDecimalPlaces(i.getRecovery(),2)},false)+"\n";
				if(i.isTakeMedicine()){
					illnessData+="  "+Msg.getMsg("is-take-medicine", new String[]{"%bool%"},new String[]{Msg.getMsg("yes", false)},false)+"\n";
					illnessData+="    "+Msg.getMsg("medicine-efficacy", new String[]{"%me%"},new String[]{Util.ReservedDecimalPlaces(i.getMedicineEfficacy(),2)},false)+"\n";
					illnessData+="    "+Msg.getMsg("duratio", new String[]{"%time%"},new String[]{i.getDuratio()+""},false)+"\n";
				}else{
					illnessData+="  "+Msg.getMsg("is-take-medicine", new String[]{"%bool%"},new String[]{Msg.getMsg("no", false)},false)+"\n";
				}
			}
			illnessData=illnessData.substring(0, illnessData.length()-2);
		}
		else{
			body=Msg.getMsg("body2", false);
		}
		
		Msg.sendRandomArrayToPlayer(Bukkit.getPlayer(uuid), "player-data", new String[]{"%Player%","%thirst%","%sleep%","%energy%","%temperature%","%body%","%weight%","illnesses"}, 
				new String[]{Bukkit.getPlayer(uuid).getName(),Util.ReservedDecimalPlaces(thirst.getThirst(), 2),Util.ReservedDecimalPlaces(sleep.getSleep(), 2),Util.ReservedDecimalPlaces(energy.getEnergy(), 2),
						Util.ReservedDecimalPlaces(temperature.getTemperature(), 2),body,Util.ReservedDecimalPlaces(weight.getWeight(), 2),illnessData}, false);
	}
	
	public static void main(String[] args) {
		
/*		long l=System.currentTimeMillis();
		int level = 500;
		for(int i=1;i<=level;i++){
			double sum = 0;
			for(int j=1;j<=i;j++){
				sum+=Math.log(j);
			}
			System.out.println("Level "+i+": "+Math.sqrt(sum));
		}
		System.out.println(System.currentTimeMillis()-l);*/
		try {
			PlayerData one = new PlayerData(new UUID(0, 0), "That is new World", true);
			one.thirst.levelUp(10);
			one.energy.change(50);
			one.sleep.setHasSleep(true);
			one.illness.addIllness("This is One Illness",100,null);
			one.illness.addIllness("My name is Two",100,null);
			one.illness.addIllness("Three",100,null);
			//one.illness.eatMedicine(Arrays.asList("Three"), 71, 50);
			one.save();
			PlayerData d=load(new UUID(0, 0));
			System.out.println(d.getIllness().getIllness());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		//d.save();
	}
	
	public void changeEffect(EffectType type,float num){
		switch(type){
			case ENERGY:
				energy.changeEffect(num);
			case IMMUNE:
				illness.changeEffect(num);
			case SLEEP:
				sleep.changeEffect(num);
			case THIRST:
				thirst.changeEffect(num);
			case WEIGHT:
				weight.changeEffect(num);
		default:
			break;
		}
	}
	
	public Player getPlayer(){
		return Bukkit.getPlayer(uuid);
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getWorld() {
		return world;
	}

	public Thirst getThirst() {
		return thirst;
	}

	public Sleep getSleep() {
		return sleep;
	}

	public Energy getEnergy() {
		return energy;
	}

	public Temperature getTemperature() {
		return temperature;
	}

	public Weight getWeight() {
		return weight;
	}

	public Illnesses getIllness() {
		return illness;
	}
	
}
