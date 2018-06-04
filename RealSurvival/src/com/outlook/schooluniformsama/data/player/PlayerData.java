package com.outlook.schooluniformsama.data.player;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.task.EffectTask;
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
	private HashMap<String,Illness> illness;
	private boolean unlimited = false;
	private String stateData;

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
		sleep = new Sleep(Data.sleep[0], Data.sleep[0], false);
		thirst = new Thirst(Data.thirst[0], Data.thirst[0]);
		energy = new Energy(Data.energy[0], Data.energy[0]);
		temperature = new Temperature(37);
		weight = new Weight(0, Data.weight);
		illness = new HashMap<>();
		if(isRandom){
			sleep = new Sleep(Data.sleep[0], Util.randomNum(Data.randomData[0],Data.randomData[1]), false);
			thirst = new Thirst(Data.thirst[0], Util.randomNum(Data.randomData[0],Data.randomData[1]));
			energy = new Energy(Data.energy[0], Util.randomNum(Data.randomData[0],Data.randomData[1]));
			weight = new Weight(0, Util.randomNum(Data.randomData[0],Data.randomData[1]));
			temperature = new Temperature(Util.randomNum(Data.randomData[0],Data.randomData[1]));
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
			Weight weight, HashMap<String,Illness> illness, boolean unlimited) {
		super();
		this.uuid = uuid;
		this.world = world;
		this.thirst = thirst;
		this.sleep = sleep;
		this.energy = energy;
		this.temperature = temperature;
		this.weight = weight;
		this.illness = illness;
		this.unlimited = unlimited;
	}
	
	public void save(){
		YamlConfiguration data = YamlConfiguration.loadConfiguration(new File(Data.DATAFOLDER+"/playerdata/"+uuid.toString()+".yml"));
		data.set("world", world);
		//Sleep
		data.set("sleep.sleep", sleep.getSleep());
		data.set("sleep.sleepMax", sleep.getSleepMax());
		data.set("sleep.hasSleep", sleep.isHasSleep());
		
		data.set("thirst.thirst", thirst.getThirst());
		data.set("thirst.thirstMax", thirst.getThirstMax());
		
		data.set("energy.energy", energy.getEnergy());
		data.set("energy.energyMax", energy.getEnergyMax());
		
		data.set("weight.weight", weight.getWeight());
		data.set("weight.weightMax", weight.getWeightMax());
		
		data.set("temperature.temperature", temperature.getTemperature());
		
		data.set("illnesses.list", illness.keySet().toArray(new String[illness.size()]));
		for(Illness i:illness.values()){
			data.set("illnesses.illness."+i.getName()+".name",i.getName());
			data.set("illnesses.illness."+i.getName()+".duration",i.getDuratio());
			data.set("illnesses.illness."+i.getName()+".medicineEfficacy",i.getMedicineEfficacy());
			data.set("illnesses.illness."+i.getName()+".recovery",i.getRecovery());
			data.set("illnesses.illness."+i.getName()+".isTakeMedicine",i.isTakeMedicine());
		}
		data.set("unlimited",unlimited);
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
		
		return new PlayerData(uuid, data.getString("world"), new Thirst(data.getDouble("thirst.thirst"), data.getDouble("thirst.thirstMax",Data.thirst[0])), 
				new Sleep(data.getDouble("sleep.sleep"), data.getDouble("sleep.sleepMax",Data.sleep[0]), data.getBoolean("sleep.hasSleep")),
				new Energy(data.getDouble("energy.energy"), data.getDouble("energy.energyMax",Data.energy[0])), 
				new Temperature(data.getDouble("temperature.temperature")), 
				new Weight(data.getDouble("weight.weight"), data.getDouble("weight.weightMax",Data.weight)), 
				illness,
				data.getBoolean("unlimited",false));
	}
	
	public void sendData(boolean isCoolDown){
		if(stateData==null)createStateData();
		else if(!isCoolDown)createStateData();
		getPlayer().sendMessage(stateData);
		if(isCoolDown)Msg.sendMsgToPlayer(getPlayer(), "state-command-cooldown", true);
	}
	
	private void createStateData(){
		String body;
		String illnessData="";
		if(isIllness()){
			body=Msg.getMsg("body1", false);
			for(Illness i:illness.values()){
				illnessData+=i.getName()+":\n";
				illnessData+="  "+Msg.getMsg("recovery", new String[]{"%recovery%"},new String[]{Util.RDP(i.getRecovery(),2)},false)+"\n";
				if(i.isTakeMedicine()){
					illnessData+="  "+Msg.getMsg("is-take-medicine", new String[]{"%b1%"},new String[]{Msg.getMsg("yes1", false)},false)+"\n";
					illnessData+="  "+Msg.getMsg("medicine-efficacy", new String[]{"%me%"},new String[]{Util.RDP(i.getMedicineEfficacy(),2)},false)+"\n";
					illnessData+="  "+Msg.getMsg("duratio", new String[]{"%time%"},new String[]{i.getDuratio()+""},false)+"\n";
				}else{
					illnessData+="  "+Msg.getMsg("is-take-medicine", new String[]{"%b1%"},new String[]{Msg.getMsg("not1", false)},false)+"\n";
				}
			}
			illnessData=illnessData.substring(0, illnessData.length()-1);
		}
		else{
			body=Msg.getMsg("body2", false);
		}
		
		stateData = Msg.getRandomMsgArray("player-data",
				new String[]{
						"%body%",
						"%Player%",
						"%thirst%","%thirst-max%","%thirst-now%",
						"%sleep%","%sleep-max%","%sleep-now%",
						"%energy%","%energy-max%","%energy-now%",
						"%temperature%",
						"%weight%","%weight-max%","%weight-now%",
						"%illnesses%"}, 
				new String[]{
						body,Bukkit.getPlayer(uuid).getName(),
						Util.RDP(thirst.getThirst()/thirst.getThirstMax() *100, 2),Util.RDP(thirst.getThirstMax(), 2),Util.RDP(thirst.getThirst(), 2),
						Util.RDP(sleep.getSleep()/sleep.getSleepMax() *100, 2),Util.RDP(sleep.getSleepMax(), 2),Util.RDP(sleep.getSleep(), 2),
						Util.RDP(energy.getEnergy()/energy.getEnergyMax() *100, 2),Util.RDP(energy.getEnergyMax(), 2),Util.RDP(energy.getEnergy(), 2),
						Util.RDP(temperature.getTemperature(), 2),
						Util.RDP(weight.getWeight()/weight.getWeightMax() *100 , 2),Util.RDP(weight.getWeightMax(), 2),Util.RDP(weight.getWeight(), 2),
						illnessData}, false);
		
	}
	
	public Double getEffectMax(com.outlook.schooluniformsama.data.effect.EffectType effect){
		switch(effect){
		case ENERGY_L:
			return energy.getEnergyMax();
		case ENERGY_P:
			return energy.getEnergyMax();
		case SLEEP_L:
			return sleep.getSleepMax();
		case SLEEP_P:
			return sleep.getSleepMax();
		case THIRST_L:
			return thirst.getThirstMax();
		case THIRST_P:
			return thirst.getThirstMax();
		case WEIGHT:
			return weight.getWeightMax();
		default:
			return null;
		}
	}
	
	public void change(EffectType type, double num){
		double afterFix = num;
		switch(type){
			case ENERGY:
				if(num<0){
					EffectTask.EffectData ed = EffectTask.getEffect(getPlayer(), com.outlook.schooluniformsama.data.effect.EffectType.ENERGY_L);
					if(ed!=null) afterFix += ed.isPercentage()?num*ed.getAmplifier():ed.getAmplifier();
					if(afterFix>0) afterFix = 0;
				}else{
					EffectTask.EffectData ed = EffectTask.getEffect(getPlayer(), com.outlook.schooluniformsama.data.effect.EffectType.ENERGY_P);
					if(ed!=null) afterFix += ed.isPercentage()?num*ed.getAmplifier():ed.getAmplifier();
					if(afterFix<0) afterFix = 0;
				}
				energy.change(afterFix);
				if(energy.getInfo()!=null)Msg.sendTitleToPlayer(getPlayer(), energy.getInfo() , Data.enablePrefixInTitle);
				//EffectTask.addEffectStatic(energy.getState(), getPlayer(), this);
				break;
			case SLEEP:
				if(num<0){
					EffectTask.EffectData ed = EffectTask.getEffect(getPlayer(), com.outlook.schooluniformsama.data.effect.EffectType.SLEEP_L);
					if(ed!=null) afterFix += ed.isPercentage()?num*ed.getAmplifier():ed.getAmplifier();
					if(afterFix>0) afterFix = 0;
				}else{
					EffectTask.EffectData ed = EffectTask.getEffect(getPlayer(), com.outlook.schooluniformsama.data.effect.EffectType.SLEEP_P);
					if(ed!=null) afterFix += ed.isPercentage()?num*ed.getAmplifier():ed.getAmplifier();
					if(afterFix<0) afterFix = 0;
				}
				sleep.change(afterFix);
				if(sleep.getInfo()!=null)Msg.sendTitleToPlayer(getPlayer(), sleep.getInfo() , Data.enablePrefixInTitle);
				//EffectTask.addEffectStatic(sleep.getState(), getPlayer(), this);
				break;
			case THIRST:
				if(num<0){
					EffectTask.EffectData ed = EffectTask.getEffect(getPlayer(), com.outlook.schooluniformsama.data.effect.EffectType.THIRST_L);
					if(ed!=null) afterFix += ed.isPercentage()?num*ed.getAmplifier():ed.getAmplifier();
					if(afterFix>0) afterFix = 0;
				}else{
					EffectTask.EffectData ed = EffectTask.getEffect(getPlayer(), com.outlook.schooluniformsama.data.effect.EffectType.THIRST_P);
					if(ed!=null) afterFix += ed.isPercentage()?num*ed.getAmplifier():ed.getAmplifier();
					if(afterFix<0) afterFix = 0;
				}
				thirst.change(afterFix);
				if(thirst.getInfo()!=null)Msg.sendTitleToPlayer(getPlayer(), thirst.getInfo() , Data.enablePrefixInTitle);
				//EffectTask.addEffectStatic(thirst.getState(), getPlayer(), this);
				break;
			case WEIGHT:
				{
					EffectTask.EffectData ed = EffectTask.getEffect(getPlayer(), com.outlook.schooluniformsama.data.effect.EffectType.WEIGHT);
					if(ed!=null) afterFix += ed.isPercentage()?num*ed.getAmplifier():ed.getAmplifier();
					String info = weight.setWeight(afterFix);
					if(info!=null)Msg.sendRandomTitleToPlayer(getPlayer(), info , Data.enablePrefixInTitle);
					//EffectTask.addEffectStatic(weight.isOverWeight(), getPlayer(), this);
					break;
				}
			case TEMPERATURE:
				{
					EffectTask.EffectData ed = EffectTask.getEffect(getPlayer(), com.outlook.schooluniformsama.data.effect.EffectType.TEMPERATURE);
					if(ed!=null) afterFix += ed.isPercentage()?num*ed.getAmplifier():ed.getAmplifier();
					String info = temperature.change(afterFix);
					if(info!=null)Msg.sendTitleToPlayer(getPlayer(), info , Data.enablePrefixInTitle);
					break;				
				}
			
		}
	}
	
	public void eatMedicine(String[] list,double medicineEfficacy, long duratio){
		if(list==null){
			for(String str:illness.keySet())
				illness.get(str).eatMedicine(medicineEfficacy, duratio);
			return;
		}
		for(String str:list){
			if(illness.keySet().contains(str))
				illness.get(str).eatMedicine(medicineEfficacy, duratio);
			
		}
	}
	
	public boolean addIllness(String name,double chance,String remove){
		double afterFix = chance;
		EffectTask.EffectData ed = EffectTask.getEffect(getPlayer(), com.outlook.schooluniformsama.data.effect.EffectType.IMMUNE);
		if(ed!=null) afterFix += ed.isPercentage()?chance*ed.getAmplifier():ed.getAmplifier();	
		if(Math.random()*100<afterFix){
			if(name.equalsIgnoreCase("Slight") && illness.containsKey("Severe"))
				name = "Severe";
			if(illness.containsKey(name))
				illness.remove(name);
			if(remove!=null)
				illness.remove(remove);
			illness.put(name, new Illness(name));
			return true;
		}else return false;
	}
	
	public void recoverIllnesses(Player p){
		for(Map.Entry<String, Illness> entity:illness.entrySet()){
			if(entity.getValue().isTakeMedicine()){
				if(entity.getValue().subTime(p))
					illness.remove(entity.getKey());				
			}else{
				entity.getValue().change();
			}
		}
	}
	
	public boolean isIllness(){
		if(illness==null||illness.size()<1)
			return false;
		return true;
	}

	public HashMap<String, Illness> getIllness() {
		return illness;
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
	
	public boolean isUnlimited(){
		return unlimited;
	}
	
	public void setUnlimited(boolean bool){
		unlimited = bool;
	}
}
