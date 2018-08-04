package com.outlook.schooluniformsama.api.data;

import java.util.Set;

import org.bukkit.entity.Player;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.player.PlayerData;

/**
 * The player's data in RealSurvival.
 * @author school_uniform
 *
 */
public class RealSurvivalPlayer {
	private PlayerData pd;
	private RealSurvivalPlayer(PlayerData pd) {
		this.pd = pd;
	}
	
	/**
	 * Get a RealSurvivalPlayer from Player <p>
	 * \u4ecePlayer\u83b7\u5f97RealSurvivalPlayer
	 * @param p Player
	 * @return 	A RealSurvivalPlayer <p>
	 * 			If the player's data don't exists, will return null. <p>
	 * 			\u5f53\u8fd9\u4e2a\u73a9\u5bb6\u7684\u6570\u636e\u4e0d\u5b58\u5728\uff0c\u5219\u5c06\u8fd4\u56denull
	 */
	public static RealSurvivalPlayer getRealSurvivalPlayer(Player p) {
		if(Data.enableInPlayer(p.getUniqueId())) 
			return new RealSurvivalPlayer(Data.getPlayerData(p.getUniqueId()));
		else if(PlayerData.isNewPlayer(p.getUniqueId()))
			return null;
		else
			return new RealSurvivalPlayer(PlayerData.load(p.getUniqueId()));
	}
	
	/**
	 * Change player's value,like sleep. <p>
	 * \u6539\u53d8\u8fd9\u4e2a\u73a9\u5bb6\u7684\u4e00\u4e2a\u5c5e\u6027\u503c,\u4f8b\u5982\u7761\u7720\u3002
	 * @param type EffectType
	 * @param num a number what you like.
	 */
	public void change(EffectType type, double num) {
		pd.change(type, num);
	}
	
	/**
	 * Take medicine. <p>
	 * \u5403\u836f.
	 * @param illness Disease that can be cured <p>
	 * 		 		  \u53ef\u4ee5\u6cbb\u6108\u7684\u75be\u75c5
	 * @param medicineEfficacy Drug effect <p>
	 * 		 				   \u836f\u7684\u6548\u679c
	 * @param duratio Duration of the drug <p>
	 * 				  \u836f\u7684\u6301\u7eed\u65f6\u95f4
	 * 				
	 */
	public void takeMedicine(String[] illness,double medicineEfficacy, long duratio) {
		pd.eatMedicine(illness, medicineEfficacy, duratio);
	}
	
	/**
	 * Let the player get a illness. <p>
	 * \u8ba9\u8be5\u73a9\u5bb6\u60a3\u75c5
	 * @param name Illness's name which you like. <p>
	 * 				\u60f3\u8ba9\u8be5\u73a9\u5bb6\u5f97\u7684\u75c5\u7684\u540d\u5b57
	 * @param chance Player's chance of getting sick <p>
	 * 				\u5f97\u75c5\u7684\u51e0\u7387
	 */
	public void addIllness(String name,double chance){
		pd.addIllness(name, chance, null);
	}
	
	/**
	 * Remove a illness. <p>
	 * \u79fb\u9664\u4e00\u79cd\u75c5
	 * @param name Illness's name which you like. <p>
	 * 				\u75c5\u540d
	 */
	public void removeIllness(String name) {
		pd.getIllness().remove(name);
	}
	
	/**
	 * Get illnesses what the player have. <p>
	 * \u83b7\u53d6\u73a9\u5bb6\u7684\u6240\u4ee5\u75be\u75c5
	 * @return A Set of the player's illnesses. <p>
	 * 		\u4e00\u4e2a\u5305\u542b\u73a9\u5bb6\u6240\u6709\u75be\u75c5\u540d\u5b57\u7684Set
	 */
	public Set<String> getIllnesses(){
		return pd.getIllness().keySet();
	}
	
	/**
	 * Get the player's attribute limit. <p>
	 * \u83b7\u53d6\u73a9\u5bb6\u7684\u5c5e\u6027\u4e0a\u9650
	 * @param effect EffectType
	 * @return the attribute limit <p>
	 * 		\u5c5e\u6027\u4e0a\u9650
	 */
	public double getEffectMax(EffectType effect) {
		return pd.getEffectMax(effect);
	}
	
	/**
	 * Check the player is online or not. <p>
	 * The 'Online' means The player's data is enable in RealSurvival. <p>
	 * \u68c0\u67e5\u73a9\u5bb6\u662f\u5426\u5728\u7ebf <p>
	 * \u5728\u7ebf\u610f\u4e3a\u7740\u73a9\u5bb6\u6570\u636e\u5728RealSurvival\u4e2d\u542f\u7528
	 * @return return true means online. <p>
	 * 			\u8fd4\u56detrue\u4ee3\u8868\u5728\u7ebf
	 */
	public boolean isOnline() {
		return !Data.enableInPlayer(pd.getUuid());
	}
	
	/**
	 * Check the player is sick or not. <p>
	 * \u68c0\u67e5\u73a9\u5bb6\u662f\u5426\u751f\u75c5
	 * @return return true means sick <p>
	 * 			\u8fd4\u56detrue\u610f\u5473\u7740\u73a9\u5bb6\u751f\u75c5\u4e86
	 */
	public boolean isSick() {
		return pd.isIllness();
	}
	
	/**
	 * Check the player's data is freezing or not. <p>
	 * \u68c0\u6d4b\u73a9\u5bb6\u7684\u6570\u636e\u662f\u5426\u88ab\u51bb\u7ed3
	 * @return true means the data is freezed. <p>
	 * 			true\u4ee3\u8868\u6570\u636e\u5df2\u7ecf\u88ab\u51bb\u7ed3
	 */
	public boolean isUnlimited(){
		return pd.isUnlimited();
	}
	
	/**
	 * Freezing the player's data or not. <p>
	 * \u51bb\u7ed3\u73a9\u5bb6\u7684\u6570\u636e
	 * @param unlimited true means freeze. <p>
	 * 					true \u4ee3\u8868\u51bb\u7ed3
	 */
	public void setUnlimited(boolean unlimited) {
		pd.setUnlimited(unlimited);
	}
}
