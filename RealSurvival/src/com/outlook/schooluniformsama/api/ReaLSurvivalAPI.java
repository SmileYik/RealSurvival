package com.outlook.schooluniformsama.api;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.outlook.schooluniformsama.api.data.EffectType;
import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.item.RSItem;
import com.outlook.schooluniformsama.data.player.PlayerData;
import com.outlook.schooluniformsama.randomday.RandomDayManager;

/**
 * Use  <p><code>RealSurvivalAPI api = (RealSurvivalAPI) Bukkit.getPluginManager().getPlugin("RealSurvival");</code></p> to get API
 * @version 0.3.3
 * @author School_Uniform
 *
 */
public interface ReaLSurvivalAPI {
	
	/**
	 * check the player's data is enable  <p>
	 * \u68c0\u67e5\u8be5\u73a9\u5bb6\u6570\u636e\u662f\u5426\u542f\u7528
	 * @param p
	 * @return
	 */
	public default boolean checkPlayerData(Player p){
		return Data.enableInPlayer(p.getUniqueId());
	}
	
	/**
	 * Change one's data.  <p>
	 * \u5bf9\u73a9\u5bb6\u7684\u5c5e\u6027\u8fdb\u884c\u63a7\u5236
	 * @param p
	 * @param number
	 * @param type 
	 */
	public default void changePlayerData(Player p, double number, EffectType type){
		if(!checkPlayerData(p))return;
		switch (type) {
		case ENERGY:
			Data.getPlayerData(p.getUniqueId()).getEnergy().change(number);
			break;
		case SLEEP:
			Data.getPlayerData(p.getUniqueId()).getSleep().change(number);
			break;
		case TEMPERATURE:
			Data.getPlayerData(p.getUniqueId()).getTemperature().change(number);
			break;
		case THIRST:
			Data.getPlayerData(p.getUniqueId()).getThirst().change(number);
			break;
		case WEIGHT:
			Data.getPlayerData(p.getUniqueId()).getWeight().setWeight(number);
			break;
		default:
			break;
		}
	}
	
	/**
	 * get a player's data  <p>
	 * \u83b7\u53d6\u67d0\u4e00\u73a9\u5bb6\u7684\u72b6\u6001
	 * @param p
	 * @param type
	 * @return -1 or a double.  <p>
	 * -1 means failed.
	 */
	public default double getPlayerData(Player p, EffectType type){
		if(!checkPlayerData(p))return -1;
		switch (type) {
		case ENERGY:
			Data.getPlayerData(p.getUniqueId()).getEnergy().getEnergy();
			break;
		case SLEEP:
			Data.getPlayerData(p.getUniqueId()).getSleep().getSleep();
			break;
		case TEMPERATURE:
			Data.getPlayerData(p.getUniqueId()).getTemperature().getTemperature();
			break;
		case THIRST:
			Data.getPlayerData(p.getUniqueId()).getThirst().getThirst();
			break;
		case WEIGHT:
			Data.getPlayerData(p.getUniqueId()).getWeight().getWeight();
			break;
		default:
			break;
		}
		return -1;
	}
	
	/**
	 * get a player's all illnesses  <p>
	 * \u83b7\u53d6\u67d0\u4e00\u73a9\u5bb6\u6240\u5f97\u7684\u6240\u6709\u7684\u75c5
	 * @param p
	 * @return a Set of illnesses' name
	 */
	public default Set<String> getPlayerIllness(Player p){
		if(!checkPlayerData(p))return new HashSet<>();
		return Data.getPlayerData(p.getUniqueId()).getIllness().keySet();
	}
	
	/**
	 * Let a player get sick  <p>
	 * \u8ba9\u67d0\u4e2a\u73a9\u5bb6\u5f97\u67d0\u79cd\u75c5
	 * @param p
	 * @param name
	 */
	public default void addIllness(Player p, String name){
		if(!checkPlayerData(p))return;
		Data.getPlayerData(p.getUniqueId()).addIllness(name, 100, null);
	}
	
	/**
	 * Remove a player’s illness  <p>
	 * \u79fb\u9664\u67d0\u4e00\u73a9\u5bb6\u7684\u4e00\u79cd\u75c5
	 * @param p
	 * @param name
	 */
	public default void removeIllness(Player p, String name){
		if(!checkPlayerData(p))return;
		Data.getPlayerData(p.getUniqueId()).getIllness().remove(name);
	}
	
	/**
	 * Remove a player’s all illnesses  <p>
	 * \u79fb\u9664\u67d0\u4e00\u73a9\u5bb6\u7684\u6240\u6709\u75c5
	 * @param p
	 */
	public default void removeAllIllness(Player p){
		if(!checkPlayerData(p))return;
		Data.getPlayerData(p.getUniqueId()).getIllness().clear();
	}
	
	/**
	 * Set player's data Unlimited <p>
	 * \u51bb\u7ed3\u67d0\u4e00\u73a9\u5bb6\u7684\u5c5e\u6027
	 * @param p
	 * @param bool - true is freezing player's data
	 */
	public default void setPlayerUnlimited(Player p, boolean bool){
		PlayerData pd;
		if(Data.enableInPlayer(p.getUniqueId())){
			pd = Data.getPlayerData(p.getUniqueId());
			Data.removePlayer(p.getUniqueId());
		}else{
			pd = PlayerData.load(p.getUniqueId());
		}
		pd.setUnlimited(bool);
		pd.save();
		Data.addPlayer(p);
	}
	
	/**
	 * get a item from a file <p>
	 * \u83b7\u53d6\u4e00\u4e2aRealSurvival\u672c\u5730\u7269\u54c1
	 * @param name is local file name
	 * @return A item in local
	 */
	public default ItemStack getRSItem(String name){
		RSItem rsi = RSItem.loadItem(name);
		return rsi==null?null:rsi.getItem().clone();
	}
	
	/**
	 * Get a world's season <p>
	 * \u83b7\u53d6\u67d0\u4e00\u4e16\u754c\u7684\u5b63\u8282
	 * @param worldName
	 * @return the world's season
	 */
	public default Season getSeason(String worldName){
		return RandomDayManager.getSeason(worldName);
	}
	
	/**
	 * Get Biome's base temperature <p>
	 * \u83b7\u53d6\u67d0\u4e00\u751f\u7269\u7fa4\u7cfb\u7684\u57fa\u7840\u6e29\u5ea6
	 * @param biome
	 * @return A double about the biome's base temperature
	 */
	public default double getBiomeTemperature(Biome biome){
		return RandomDayManager.getBiomeTemperature(biome);
	}
	
	
	
}
