package com.outlook.schooluniformsama.api;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.player.EffectType;
import com.outlook.schooluniformsama.data.item.RSItem;
import com.outlook.schooluniformsama.data.player.PlayerData;

public interface ReaLSurvivalAPI {
	
	/**
	 * 检查玩家数据是否存在
	 * @param p
	 * @return
	 */
	public default boolean checkPlayerData(Player p){
		return Data.playerData.containsKey(p.getUniqueId());
	}
	
	/**
	 * Change one's data.
	 * @param p
	 * @param number
	 * @param type 
	 */
	public default void changePlayerData(Player p,double number,EffectType type){
		if(!checkPlayerData(p))return;
		switch (type) {
		case ENERGY:
			Data.playerData.get(p.getUniqueId()).getEnergy().change(number);
			break;
		case SLEEP:
			Data.playerData.get(p.getUniqueId()).getSleep().change(number);
			break;
		case TEMPERATURE:
			Data.playerData.get(p.getUniqueId()).getTemperature().change(number);
			break;
		case THIRST:
			Data.playerData.get(p.getUniqueId()).getThirst().change(number);
			break;
		case WEIGHT:
			Data.playerData.get(p.getUniqueId()).getWeight().setWeight(number);
			break;
		default:
			break;
		}
	}
	
	/**
	 * get a player's data
	 * @param p
	 * @param type
	 * @return -1 or a double.
	 */
	public default double getPlayerData(Player p,EffectType type){
		if(!checkPlayerData(p))return -1;
		switch (type) {
		case ENERGY:
			Data.playerData.get(p.getUniqueId()).getEnergy().getEnergy();
			break;
		case SLEEP:
			Data.playerData.get(p.getUniqueId()).getSleep().getSleep();
			break;
		case TEMPERATURE:
			Data.playerData.get(p.getUniqueId()).getTemperature().getTemperature();
			break;
		case THIRST:
			Data.playerData.get(p.getUniqueId()).getThirst().getThirst();
			break;
		case WEIGHT:
			Data.playerData.get(p.getUniqueId()).getWeight().getWeight();
			break;
		default:
			break;
		}
		return -1;
	}
	
	/**
	 * get a player's all illnesses
	 * @param p
	 * @return a set of illnesses' name
	 */
	public default Set<String> getPlayerIllness(Player p){
		if(!checkPlayerData(p))return new HashSet<>();
		return Data.playerData.get(p.getUniqueId()).getIllness().keySet();
	}
	
	/**
	 * Let a player get sick
	 * @param p
	 * @param name
	 */
	public default void addIllness(Player p,String name){
		if(!checkPlayerData(p))return;
		Data.playerData.get(p.getUniqueId()).addIllness(name, 100, null);
	}
	
	/**
	 * Remove a player’s illness
	 * @param p
	 * @param name
	 */
	public default void removeIllness(Player p,String name){
		if(!checkPlayerData(p))return;
		Data.playerData.get(p.getUniqueId()).getIllness().remove(name);
	}
	
	/**
	 * Remove a player’s all illnesses
	 * @param p
	 */
	public default void removeAllIllness(Player p){
		if(!checkPlayerData(p))return;
		Data.playerData.get(p.getUniqueId()).getIllness().clear();
	}
	
	/**
	 * Set player's data Unlimited
	 * @param p
	 * @param bool
	 */
	public default void setPlayerUnlimited(Player p,boolean bool){
		PlayerData pd;
		if(Data.playerData.containsKey(p.getUniqueId())){
			pd = Data.playerData.get(p.getUniqueId());
			Data.playerData.remove(p.getUniqueId());
		}else{
			pd = PlayerData.load(p.getUniqueId());
		}
		pd.setUnlimited(bool);
		pd.save();
		Data.addPlayer(p);
	}
	
	/**
	 * get a item from a file
	 * @param name is local file name
	 * @return
	 */
	public default ItemStack getRSItem(String name){
		RSItem rsi = RSItem.loadItem(name);
		return rsi==null?null:rsi.getItem().clone();
	}
	
}
