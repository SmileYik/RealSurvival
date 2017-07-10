package com.outlook.z815007320.data;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;

import com.outlook.z815007320.utils.Utils;

public class PlayerData extends PluginRS{
	private UUID uuid;
	private String world;
	
	private double sleep;
	private double thirst;
	private double temperature;
	private double oldTemperature;
	private double weight;
	private double physical_strength;
	private double sleepLevel=1;
	private byte light;

	private boolean sick;
	private boolean isSleep;
	// sickKind --- double(recovery duration,tEffect) boolean(isMedication)
	private HashMap<String,Object[]> sickKind;
	
	public void changeTemperature(double d){
		oldTemperature=temperature;
		temperature+=d;
		if(temperature>=38&&temperature-d<38)
	    	  Utils.sendMsgToPlayer(this, "Fever");
		else if(temperature<=36&&temperature-d>36)
	    	 Utils.sendMsgToPlayer(this, "Cold");
	}
	
	public void changeSleep(double d){
		sleep+=d*sleepLevel;
		if(sleep<0){
			sleep=0;
		}else if(sleep>rs.getSleepMax())
			sleep=rs.getSleepMax();
		
		if(sleep<=rs.getSleepMin()&&(sleep-d)>rs.getSleepMin())
			Utils.sendMsgToPlayer( this, "VeryTired");
		else if(sleep<=rs.getSleepMid()&&(sleep-d)>rs.getSleepMid())
			Utils.sendMsgToPlayer( this, "Tired");
		else if(sleep>=rs.getSleepMax()*0.8&&(sleep-d)<rs.getSleepMax()*0.8)
			Utils.sendMsgToPlayer(this, "Spirit");
	}
	
	public boolean changeThirst(double d){
		thirst+=d;
		if(thirst<0){
			thirst=0;
			return false;
		}else if(thirst>rs.getThirstMax())
			thirst=rs.getThirstMax();
		
		if(thirst<=rs.getThirstMin()&&(thirst-d)>rs.getThirstMin())
			Utils.sendMsgToPlayer( this, "Dehydration");
		else if(thirst<=rs.getThirstMid()&&(thirst-d)>rs.getThirstMid())
			Utils.sendMsgToPlayer(this, "Thirst");
		else if(thirst>=rs.getThirstMax()*0.8&&(thirst-d)<rs.getThirstMax()*0.8)
			Utils.sendMsgToPlayer(this, "FullOfMoisture");
		
		return true;
	}
	
	public boolean changeRecovery(double d,String sickKind) {
		Object[] o=this.sickKind.get(sickKind);
		o[0]=Double.parseDouble(o[0].toString())+d;
		if(Double.parseDouble(o[0].toString())<0){
			o[0]=0;
			this.sickKind.replace(sickKind, o);
			return false;
		}else if(Double.parseDouble(o[0].toString())>=100){
			removeSickKind(sickKind);
			if(this.sickKind.size()==0){
				Utils.sendMsgToPlayer( this, "Recovery");
				sick=false;
			}
			else
				Utils.sendMsgToPlayer( this, "RecoverySick",sickKind);
		}
		this.sickKind.replace(sickKind, o);
		
		return true;
	}
	
	public void changeAllRecovery(double d){
		Iterator<Map.Entry<String, Object[]>> ite = sickKind.entrySet().iterator();  
		while(ite.hasNext()){
			Entry<String, Object[]> entity = ite.next();
			Object[] o=entity.getValue();
			o[0]=Double.parseDouble(o[0].toString())+d;
			if(Double.parseDouble(o[0].toString())<0){
				o[0]=0;
				this.sickKind.replace(entity.getKey(), o);
				continue;
			}else if(Double.parseDouble(o[0].toString())>100){
				removeSickKind(entity.getKey());
				if(this.sickKind.size()==0){
					Utils.sendMsgToPlayer( this, "Recovery");
					sick=false;
				}
				else
					Utils.sendMsgToPlayer( this, "RecoverySick",entity.getKey());
				continue;
			}
			sickKind.replace(entity.getKey(), o);
		}
	}
	
	
	public boolean changePS(double d){
		physical_strength+=d;
		if(physical_strength<0){
			physical_strength=0;
			return false;
		}else if(physical_strength>rs.getPhysical_strength())
			physical_strength=rs.getPhysical_strength();
		
		if(physical_strength<=rs.getPhysical_strength_min()&&(physical_strength-d>rs.getPhysical_strength_min()))
			Utils.sendMsgToPlayer( this, "NoEnergy");
		
		return true;
	}
	
	public void changeDuration(double d,String sickKind){
		Object[] o=this.sickKind.get(sickKind);
		o[1]=Double.parseDouble(o[1].toString())-d;
		if(Double.parseDouble(o[1].toString())<=0){
			o[1]=0;
			o[3]=false;
			this.sickKind.replace(sickKind, o);
			Utils.sendMsgToPlayer(this, "EfficacyOver",sickKind);
		}
		this.sickKind.replace(sickKind, o);
	}
	
	public void changeAllDuration(double d){
		Iterator<Map.Entry<String, Object[]>> ite = sickKind.entrySet().iterator();  
		while(ite.hasNext()){
			Entry<String, Object[]> entity = ite.next();
			Object[] o=entity.getValue();
			o[1]=Double.parseDouble(o[1].toString())+d;
			if(Double.parseDouble(o[1].toString())<=0){
				o[1]=0;
				o[3]=false;
				this.sickKind.replace(entity.getKey(), o);
				Utils.sendMsgToPlayer(this, "EfficacyOver",entity.getKey());
				continue;
			}
			sickKind.replace(entity.getKey(), o);
		}
	}
	
	public void savaData(){
		File data=new File(rs.getDataFolder()+File.separator+"PlayerDatas"+File.separator+uuid.toString()+".yml");
		if(!data.exists())
			try {data.createNewFile();} catch (IOException e) {}
		YamlConfiguration dataC=YamlConfiguration.loadConfiguration(data);
		dataC.set("world", world);
		dataC.set("sleep", sleep);
		dataC.set("thirst", thirst);
		dataC.set("weight", weight);
		
		Iterator<Map.Entry<String, Object[]>> ite = sickKind.entrySet().iterator();  
		List<String> sickKindList=new LinkedList<String>();
		List<String> rSickKindList=new LinkedList<String>();
		while(ite.hasNext()){
			Entry<String, Object[]> entity = ite.next();
			sickKindList.add((entity.getKey()+"|||"+entity.getValue()[0].toString()+","+entity.getValue()[1].toString()+","
										+entity.getValue()[2].toString()+","+entity.getValue()[3].toString()).toString());
			rSickKindList.add(entity.getKey());
		}
		dataC.set("rSickKind", rSickKindList);
		dataC.set("sickKind", sickKindList);
		dataC.set("physical_strength", physical_strength);
		dataC.set("temperature", temperature);
		dataC.set("sick", sick);
		dataC.set("isSleep", isSleep);
		dataC.set("light", light);
		dataC.set("sickKind", sickKind);
		try {
			dataC.save(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getData(){
		if(sick)
			return rs.getMessage("stat_sick", this);
		return rs.getMessage("stat", this);
	}
	/**
	 * 初始化个玩家数据
	 * @param rs 插件
	 * @param uuid 玩家uuid
	 * @param world 玩家/床所在世界
	 * @param sleep 睡眠度
	 * @param thirst 口渴度
	 * @param recovery 疾病恢复度
	 * @param temperature 体温
	 * @param weight 负重
	 * @param physical_strength 体力
	 */
	public PlayerData( UUID uuid, String world, double sleep, double thirst, double recovery,
			double temperature, double weight, double physical_strength) {
		super();
		this.uuid = uuid;
		this.world = world;
		this.sleep = sleep;
		this.thirst = thirst;
		this.temperature = temperature;
		this.weight = weight;
		this.physical_strength = physical_strength;
		sickKind=new HashMap<String,Object[]>();
	}	
	
	public byte getLight(){
		return light;
	}

	public void setBed(Block bed){
		light=bed.getLightLevel();
	}
	/**
	 * 初始化一个玩家数据
	 * @param uuid 玩家uuid
	 * @param world 玩家所在世界
	 * @param sleep 玩家睡眠值
	 * @param thirst  玩家口渴度值
	 * @param temperature  玩家体温
	 * @param weight  玩家当前负重
	 * @param physical_strength  玩家的体力
	 * @param tEffect  治愈度
	 * @param sick   玩家是否生病
	 * @param isSleep 是否睡觉
	 * @param light  睡觉地方的亮度
	 * @param sickKind  生病种类
	 */
	public PlayerData(UUID uuid, String world, double sleep, double thirst,double temperature,
			double weight, double physical_strength, boolean sick, boolean isSleep, 
			byte light,HashMap<String,Object[]> sickKind) {
		super();
		this.uuid = uuid;
		this.world = world;
		this.sleep = sleep;
		this.thirst = thirst;
		this.temperature = temperature;
		this.weight = weight;
		this.physical_strength = physical_strength;
		this.sick = sick;
		this.isSleep = isSleep;
		this.light = light;
		this.sickKind=sickKind;
	}
	
	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getWorld() {
		return world;
	}

	public double getSleep() {
		return sleep;
	}

	public void setSleep(double sleep) {
		this.sleep = sleep;
	}

	public double getThirst() {
		return thirst;
	}

	public void setThirst(double thirst) {
		this.thirst = thirst;
	}

	public double getRecovery(String sickKind) {
		return Double.parseDouble(this.sickKind.get(sickKind)[0].toString());
	}
	
	public String[] getRecovery(){
		LinkedList<String> l=new LinkedList<>();
		for(Object[] o:sickKind.values())
			l.add(Utils._2f(Double.parseDouble(o[0].toString())));
		if(l.size()==0)
			return null;
		return (String[])l.toArray(new String[l.size()]);
	}

	public void setRecovery(double recovery,String sickKind) {
		Object[] o=this.sickKind.get(sickKind);
		if(recovery<0){
			o[0]=0;
			this.sickKind.replace(sickKind, o);
		}else if(recovery>100){
			removeSickKind(sickKind);
			if(this.sickKind.size()==0){
				Utils.sendMsgToPlayer( this, "Recovery");
				sick=false;
			}
			else
				Utils.sendMsgToPlayer( this, "RecoverySick",sickKind);
		}
		o[0]=recovery;
		this.sickKind.replace(sickKind, o);
	}
	
	public void setAllRecovery(double recovery){
		Iterator<Map.Entry<String, Object[]>> ite = sickKind.entrySet().iterator();  
		while(ite.hasNext()){
			Entry<String, Object[]> entity = ite.next();
			Object[] o=entity.getValue();
			if(recovery<0){
				o[0]=0;
				this.sickKind.replace(entity.getKey(), o);
			}else if(recovery>100){
				removeSickKind(entity.getKey());
				if(this.sickKind.size()==0){
					Utils.sendMsgToPlayer( this, "Recovery");
					sick=false;
				}
				else
					Utils.sendMsgToPlayer( this, "RecoverySick",entity.getKey());
			}
			o[0]=recovery;
			sickKind.replace(entity.getKey(), o);
		}
	}

	public double getTemperature() {
		return temperature;
	}

	public double getOldTemperature() {
		return oldTemperature;
	}
	
	public void setTemperature(double temperature) {
		oldTemperature=temperature;
		this.temperature = temperature;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		if(weight>rs.getWeight()&&this.weight<=rs.getWeight()){
			this.weight = weight;
			Utils.sendMsgToPlayer( this, "WeightOver");			
		}
		this.weight = weight;
		if(weight<0)
			this.weight=0;
	}

	public double getPhysical_strength() {
		return physical_strength;
	}

	public void setPhysical_strength(double physical_strength) {
		this.physical_strength = physical_strength;
	}

	public boolean isSick() {
		return sick;
	}

	public void setSick(boolean sick) {
		this.sick = sick;
	}

	public boolean isSleep() {
		return isSleep;
	}

	public void setSleep(boolean isSleep,double sleepLevel) {
		this.isSleep = isSleep;
		this.sleepLevel=sleepLevel;
	}

	public boolean isMedication(String sickKind) {
		return Boolean.parseBoolean(this.sickKind.get(sickKind)[3].toString());
	}

	public void setMedication(boolean isMedication,String sickKind) {
		Object[] o=this.sickKind.get(sickKind);
		o[3]=isMedication;
		this.sickKind.replace(sickKind, o);
	}
	
	public void setAllMedication(boolean isMedication){
		Iterator<Map.Entry<String, Object[]>> ite = sickKind.entrySet().iterator();  
		while(ite.hasNext()){
			Entry<String, Object[]> entity = ite.next();
			Object[] o=entity.getValue();
			o[3]=isMedication;
			sickKind.replace(entity.getKey(), o);
		}
	}

	public double getDuration(String sickKind) {
		return Double.parseDouble(this.sickKind.get(sickKind)[1].toString());
	}
	
	public String[] getDuration(){
		LinkedList<String> l=new LinkedList<>();
		for(Object[] o:sickKind.values())
			l.add(Utils._2f(Double.parseDouble(o[1].toString())));
		if(l.size()==0)
			return null;
		return (String[])l.toArray(new String[l.size()]);
	}

	public void setDuration(double duration,String sickKind) {
		Object[] o=this.sickKind.get(sickKind);
		o[1]=duration;
		this.sickKind.replace(sickKind, o);
	}
	
	public void setAllDuration(double duration){
		Iterator<Map.Entry<String, Object[]>> ite = sickKind.entrySet().iterator();  
		while(ite.hasNext()){
			Entry<String, Object[]> entity = ite.next();
			Object[] o=entity.getValue();
			o[1]=duration;
			sickKind.replace(entity.getKey(), o);
		}
	}

	public double getTEffect(String sickKind) {
		return Double.parseDouble(this.sickKind.get(sickKind)[2].toString());
	}

	public void settEffect(double tEffect,String sickKind) {
		Object[] o=this.sickKind.get(sickKind);
		o[2]=tEffect;
		this.sickKind.replace(sickKind, o);
	}
	
	public void setAllTEffect(double tEffect){
		Iterator<Map.Entry<String, Object[]>> ite = sickKind.entrySet().iterator();  
		while(ite.hasNext()){
			Entry<String, Object[]> entity = ite.next();
			Object[] o=entity.getValue();
			o[2]=tEffect;
			sickKind.replace(entity.getKey(), o);
		}
	}

	public String[] getSickKind() {
		LinkedList<String> temp=new LinkedList<>();
		for(String name:sickKind.keySet())
			temp.add(name);
		if(temp.size()==0)
			return null;
		return (String[])temp.toArray(new String[temp.size()]);
	}
	
	public List<String> getSickKindList() {
		LinkedList<String> temp=new LinkedList<>();
		for(String name:sickKind.keySet())
			temp.add(name);
		return temp;
	}

	public void addSickKind(String sickKind) {
		if(!this.sickKind.containsKey(sickKind))
			this.sickKind.put(sickKind, new Object[]{0,0,0,false});
		Utils.sendMsgToPlayer(this, "Sick",sickKind);
	}
	
	public void removeSickKind(String sickKind){
		Iterator<String> ite = this.sickKind.keySet().iterator();
		while(ite.hasNext())
			if(ite.next().equalsIgnoreCase(sickKind))
				ite.remove();
		this.sickKind.remove(sickKind);
	}
	
	public HashMap<String,Object[]> getSickKindMap(){
		return sickKind;
	}
	
	public void removeAllSick(){
		sickKind=new HashMap<>();
	}
}
