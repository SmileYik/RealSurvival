package miskyle.realsurvival.data.playerdata;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import miskyle.realsurvival.api.status.StatusType;
import miskyle.realsurvival.util.RSEntry;

public class PlayerDataEffect {
	//储存的都是比例值
	//在修改属性大小时,将会把修改值乘上此比例值
	
	private HashMap<StatusType, ConcurrentHashMap<String, RSEntry<Double, Integer>>> effect;
	
	public PlayerDataEffect() {
		effect = new HashMap<StatusType, ConcurrentHashMap<String,RSEntry<Double,Integer>>>();
		effect.put(StatusType.SLEEP, new ConcurrentHashMap<String, RSEntry<Double,Integer>>());
		effect.put(StatusType.THIRST, new ConcurrentHashMap<String, RSEntry<Double,Integer>>());
		effect.put(StatusType.ENERGY, new ConcurrentHashMap<String, RSEntry<Double,Integer>>());
		effect.put(StatusType.WEIGHT, new ConcurrentHashMap<String, RSEntry<Double,Integer>>());
	}
	
	public void addEffect(StatusType status,String effectName,double effectValue,int duration) {
		ConcurrentHashMap<String, RSEntry<Double,Integer>> map = effect.get(status);
		if(map.contains(effectName)) {
			map.replace(effectName, new RSEntry<Double, Integer>(effectValue, duration));
		}else {
			map.put(effectName, new RSEntry<Double, Integer>(effectValue, duration));
		}
	}
	
	/**
	 * 获取某一属性最终倍率值,
	 * 此值不可直接与修改值相乘(未加上 1)
	 * @param status
	 * @return
	 */
	public double getValue(StatusType status) {
		double value = 0;
		for(RSEntry<Double, Integer> v : effect.get(status).values())
			value+=v.getLeft();
		return value;
	}
	
	public void removeEffect(StatusType status,String effectName) {
		effect.get(status).remove(effectName);
	}

	public HashMap<StatusType, ConcurrentHashMap<String, RSEntry<Double, Integer>>> getEffect() {
		return effect;
	}

	public void setEffect(HashMap<StatusType, ConcurrentHashMap<String, RSEntry<Double, Integer>>> effect) {
		this.effect = effect;
	}
	
	public void setEffect(String str) {
		if(str == null)return;
		for(String status : str.split(";")) {
			String[] temp1 = status.split(":");
			for(String effects : temp1[1].split("/")) {
				String[] temp2 = effects.split(",");
				addEffect(StatusType.valueOf(status), temp2[0], Double.parseDouble(temp2[1]), Integer.parseInt(temp2[2]));
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		effect.forEach((k,v)->{
			sb.append(k.name());
			sb.append(":");
			v.forEach((name,entry)->{
				sb.append(name+",");
				sb.append(entry.getLeft()+",");
				sb.append(entry.getRight()+"/");
			});
			sb.setCharAt(sb.length()-1, ';');
		});
		return sb.deleteCharAt(sb.length()-1).toString();
	}
}
