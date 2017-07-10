package com.outlook.z815007320.commands;

import java.io.File;
import java.io.IOException;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.outlook.z815007320.RealSurvival;
import com.outlook.z815007320.data.Items;
import com.outlook.z815007320.data.PluginRS;
import com.outlook.z815007320.gui.CraftItemGUI;
import com.outlook.z815007320.gui.FireCraftTableGUI;

public class WorkbenchCMD extends PluginRS{
/**
 * 打开一个测试工作台
 * @param p 玩家
 * @param ID 工作台ID
 */
	public static void openTestWorkbench(Player p,String ID){
		if(rs.getWorkbench().containsKey(ID))
			p.openInventory(CraftItemGUI.openWorkbenchGUI(Items.createItemKey(Material.STAINED_GLASS_PANE, (short)15,ID)));
		else
			p.openInventory(CraftItemGUI.createWorkbenchGUI(Items.createItemKey(Material.STAINED_GLASS_PANE, (short)15,ID)));
		return;
	}
	/**
	 * 打开一个配方列表
	 * @param p 玩家
	 */
	public static void openSFList(Player p){
		p.openInventory(new CraftItemGUI().sFList(0,p));
	}
	/**
	 * 创建一个配方
	 * @param player 玩家
	 * @param sfName 配方名
	 * @param time 时间
	 * @return 是否成功
	 */
	public static boolean createSF(Player player,String sfName,String time){
		
		if(new File(rs.getDataFolder()+File.separator+"SyntheticFormula"+File.separator+sfName+".yml").exists()){
			 player.sendMessage("§9[RealSurvival] §b合成公式重名了...");
			 return false;
		 }
		 long l=-1;
		 try{ l=Long.parseLong(time);}catch(Exception e){}
		 if(l<0){
			 player.sendMessage("§9[RealSurvival] §b制造时间错误!...");
			 return false;
		 }
		player.openInventory(CraftItemGUI.createWorkbenchRecipeGUI(Items.createItemKey(Material.STAINED_GLASS_PANE, (short)15,sfName+":"+time)));
		return true;
	}
	/**
	 * 创建一个配方
	 * @param player 玩家
	 * @param sfName 配方名
	 * @param time 时间
	 * @return 是否成功
	 */
	public static boolean createFCTR(Player player,String name,String time,String maxTime,String tem){
		if(new File(rs.getDataFolder()+File.separator+"SyntheticFormula"+File.separator+"FireCraftTable"+File.separator+name+".yml").exists()){
			 player.sendMessage("§9[RealSurvival] §b合成公式重名了...");
			 return false;
		 }
		 try{
			 Integer.parseInt(time);
			 Integer.parseInt(maxTime);
			 Double.parseDouble(tem);
		 }catch(Exception e){
			 player.sendMessage("§9[RealSurvival] §b 制造时间 | 保存时间 | 温度  中某个填错了!...");
			 return false;
		 }
		player.openInventory(FireCraftTableGUI.createFCTPGUI(Items.createItemKey(Material.STAINED_GLASS_PANE, (short)15,name+":"+time+":"+maxTime+":"+tem)));
		return true;
	}
	/**
	 * 重设定一个配方
	 * @param player 玩家
	 * @param sfName 配方名
	 * @param time 时间
	 * @return 是否成功
	 */
	public static boolean resetSF(Player player,String sfName,String time){
		 if(!new File(rs.getDataFolder()+File.separator+"SyntheticFormula"+sfName+".yml").exists()){
			 player.sendMessage("§9[RealSurvival] §b没找到指定的合成公式...");
			 return false;
		 }
		 long l=-1;
		 try{ l=Long.parseLong(time);}catch(Exception e){}
		 if(l<0){
			 player.sendMessage("§9[RealSurvival] §b制造时间错误!...");
			 return false;
		 }
		player.openInventory(CraftItemGUI.resetSF(Items.createItemKey(Material.STAINED_GLASS_PANE, (short)15,sfName+":"+time)));
		return true;
	}
	/**
	 * 设定工作台的组成
	 * @param face 工作台的面 一共为 main,up,down,left,right,behind,front
	 * @param blockName 方块的名字
	 * @return 如果成功返回true,失败返回false
	 */
	public static boolean setWorkbench(String face,String blockName){
		if(!(face.equals("Main")||face.equals("UP")||face.equals("Left")||
				face.equals("Right")||face.equals("Behind")||face.equals("Front")))
			return false;
		
		rs.getConfig().set("workbench."+face, blockName);
		try {
			rs.getConfig().save(rs.getDataFolder()+File.separator+"config.yml");
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	/**
	 * 设定插件,
	 * 不要随意更改
	 * @param rs 插件
	 */
	public static void setrsin(RealSurvival rs){
		WorkbenchCMD.rs=rs;
	}
}
