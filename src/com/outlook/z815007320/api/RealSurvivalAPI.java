package com.outlook.z815007320.api;

import org.bukkit.entity.Player;

import com.outlook.z815007320.data.PluginRS;

public interface RealSurvivalAPI {
	/**
	 * 改变一个玩家的属性值
	 * 类型有4种,分别是: 
	 * 	sleep | thiPluginRS.rst | temperature | physical_strength
	 * @param p 玩家
	 * @param type 改变数据的类型
	 * @param number 要 增加(+)/减少(-) 的数值
	 * @return 修改成功返回 true,失败返回false
	 */
	public default boolean changeData(Player p,String type,double number){
		if(p==null)return false;
		//sleep | thiPluginRS.rst | temperature | weight | physical_strength
		switch(type){
		case "sleep":
			PluginRS.rs.getPlayerData(p).changeSleep(number);
			break;
		case "temperature":
			PluginRS.rs.getPlayerData(p).changeTemperature(number);
			break;
		case "thiPluginRS.rst":
			PluginRS.rs.getPlayerData(p).changeThirst(number);
			break;
		case "physical_strength":
			PluginRS.rs.getPlayerData(p).changePS(number);
			break;
		default:
			return false;
		}
		return true;
	}
	/**
	 * 类型有4种,分别是: 
	 * 	sleep | thiPluginRS.rst | temperature | physical_strength
	 * @param p 玩家
	 * @param type 查看数据的类型
	 * @return  成功则返回相对应的数据, 失败则返回-1
	 */
	public default double getData(Player p,String type){
		switch(type){
		case "sleep":
			return PluginRS.rs.getPlayerData(p).getSleep();
		case "temperature":
			return PluginRS.rs.getPlayerData(p).getTemperature();
		case "thiPluginRS.rst":
			return PluginRS.rs.getPlayerData(p).getThirst();
		case "physical_strength":
			return PluginRS.rs.getPlayerData(p).getPhysical_strength();
		default:
			return -1;
		}
	}
	/**
	 * 玩家是否在睡觉
	 * @param p 玩家
	 * @return 睡觉为true,其他为false
	 */
	public default boolean isSleep(Player p){
		return PluginRS.rs.getPlayerData(p).isSleep();
	}
	/**
	 * 玩家是否在生病
	 * @param p 玩家
	 * @return 生病为true,其他为false
	 */
	public default boolean isSick(Player p){
		return PluginRS.rs.getPlayerData(p).isSick();
	}
	/**
	 * 获取玩家得病列表
	 * @param p 玩家
	 * @return 获取玩家生病列表,失败返回null
	 */
	public default String[] getSickList(Player p){
		if(!PluginRS.rs.getPlayerData(p).isSick())return null;
		return PluginRS.rs.getPlayerData(p).getSickKind();
	}
	/**
	 * 获取玩家所得的某种病的详细信息
	 * @param p 玩家
	 * @param sickType 病的种类
	 * @return 返回一个带有信息长度为4的Object数组,前三个为double型,后一个为boolean
	 * 第一个为该病的治愈程度,第二个为该病的药物持续时间,
	 * 第三个为每秒恢复百分之多少,第四个为是否吃了药,若获取失败则返回null
	 */
	public default Object[] getSickData(Player p,String sickType){
		if(!PluginRS.rs.getPlayerData(p).isSick())return null;
		return PluginRS.rs.getPlayerData(p).getSickKindMap().get(sickType);
	}
	
	/**
	 * 给玩家增加一种病
	 * @param p 玩家
	 * @param sickType 病名
	 * @return 增加成功返回true,增加失败返回false
	 */
	public default boolean addSick(Player p,String sickType){
		if(!PluginRS.rs.getPlayerData(p).isSick())PluginRS.rs.getPlayerData(p).setSick(true);
		if(PluginRS.rs.getPlayerData(p).getSickKindList().contains(sickType))return false;
		PluginRS.rs.getPlayerData(p).addSickKind(sickType);
		return true;
	}
	/**
	 * 设置玩家上一种病的数据
	 * @param p 玩家
	 * @param sickType 病名
	 * @param recovery 病治愈程度
	 * @param time 药物持续时间
	 * @param effect 药物治疗效果
	 * @param isAteMedication 是否吃了药
	 * @return 成功true,失败false
	 */
	public default boolean setSickKind(Player p,String sickType,double recovery,double time,double effect,boolean isAteMedication) {
		if(!PluginRS.rs.getPlayerData(p).isSick())return false;
		if(!PluginRS.rs.getPlayerData(p).getSickKindList().contains(sickType))return false;
		PluginRS.rs.getPlayerData(p).setDuration(time, sickType);
		PluginRS.rs.getPlayerData(p).settEffect(effect, sickType);
		PluginRS.rs.getPlayerData(p).setMedication(isAteMedication, sickType);
		PluginRS.rs.getPlayerData(p).setRecovery(recovery, sickType);
		return true;
	}
}
