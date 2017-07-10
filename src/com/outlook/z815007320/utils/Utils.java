package com.outlook.z815007320.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.outlook.z815007320.data.Items;
import com.outlook.z815007320.data.PlayerData;
import com.outlook.z815007320.data.PluginRS;

public class Utils extends PluginRS{
	/**
	 * 获取PlayerData
	 * @param rs 主类路径
	 * @param p Player类型
	 * @return PlayerData类型
	 */
	public static PlayerData getPlayerData(Player p){
		File data=new File(rs.getDataFolder()+File.separator+"PlayerDatas"+File.separator+p.getUniqueId().toString()+".yml");
		PlayerData pd;
		if(!data.exists()){
			try {data.createNewFile();} catch (IOException e) {}
			pd=new PlayerData(p.getUniqueId(),p.getWorld().getName(), rs.getSleepMax(), 
												rs.getThirstMax(), 0, 37,0, rs.getPhysical_strength());
			return pd;
		}
		YamlConfiguration dataC=YamlConfiguration.loadConfiguration(data);
		HashMap<String,Object[]> sickKind=new HashMap<String,Object[]>();
		for(String temp:dataC.getStringList("rSickKind")){
			List<String> l=dataC.getStringList("sickKind."+temp);
			sickKind.put(temp,new Object[]{l.get(0),l.get(1),l.get(2),l.get(3)} );
		}
		pd=new PlayerData( p.getUniqueId(),dataC.getString("world"), dataC.getDouble("sleep"),
				dataC.getDouble("thirst"), dataC.getDouble("temperature"), dataC.getDouble("weight"), 
				dataC.getDouble("physical_strength"),dataC.getBoolean("sick"), 
				dataC.getBoolean("isSleep"),Byte.parseByte(dataC.getString("light")),sickKind);
		return pd;
	}
	
	public static void addPotionEffect(Player p,List<String> effects){
		if(effects==null)return;
		for(String eff:effects)
  		  p.addPotionEffect(new PotionEffect(PotionEffectType.getByName(eff.split(",")[0]),
  				  Integer.parseInt(eff.split(",")[1]),Integer.parseInt(eff.split(",")[2])),true);
	}
	
	public static ItemStack getItem(String name){
		switch(name){
			case "海水":
				return Items.getSeaWater();
			case "湖水":
				return Items.getLakeWater();
			case "沼泽水":
				return Items.getSwamplandWater();
			case "开水":
				return Items.getHotWater();
			case "冰水":
				return Items.getIceWater();
			case "药":
				return Items.getMedicine01();
			case "雨水收集器":
				return Items.getRainWaterCollector();
			case "淡水":
				return Items.getFreshWater();
			case "绷带":
				return Items.getMedicine02();
			case "雨水":
				return Items.getRainwater();
			default:
				return null;
		}
	}
	
	public static double getLore(String tabel,List<String> lore){
		for(String line:lore){
			line=Util_Colours.removeTooltipColour(Util_Colours.removeRawTooltipColour(line));
			if(line.contains(tabel)){
				line=line.split(":")[1];
				line=line.replaceAll("%", "");
				line=line.replaceAll("'", "");
				line=line.replaceAll(" ", "");
				line=line.replaceAll("秒", "");
				line=line.replaceAll("℃", "");
				line=line.replaceAll("s", "");
				line=line.replaceAll("S", "");
				if(line.contains("-"))
					return Double.parseDouble(line.split("-")[0])+Math.random()*(
							Double.parseDouble(line.split("-")[1])-Double.parseDouble(line.split("-")[0]));
				else
					return Double.parseDouble(line);
			}
		}
		return -1;
	}
	
	public static String getLoreString(String tabel,List<String> lore){
		for(String line:lore){
			line=Util_Colours.removeTooltipColour(Util_Colours.removeRawTooltipColour(line));
			if(line.contains(tabel))
				return line.split(":")[1].replace(" ", "");
		}
		return null;
	}
	
	public static void sendMsgToPlayer(UUID uuid,String msg){
		if(Bukkit.getPlayer(uuid).isOnline())Bukkit.getPlayer(uuid).sendTitle(rs.getPrefix(),msg,10,30,10);
	}
	
	public static void sendMsgToPlayer(String name,String msg){
		if(Bukkit.getPlayer(name).isOnline())Bukkit.getPlayer(name).sendTitle(rs.getPrefix(),msg,10,30,10);
	}
	/**
	 * 向玩家发送一条信息
	 * @param rs 插件
	 * @param pd 玩家数据
	 * @param name messageID
	 */
	public static void sendMsgToPlayer(PlayerData pd,String name){
		if(!Bukkit.getPlayer(pd.getUuid()).isOnline())return;
		if(rs.getTitleManager()!=null)
			rs.getTitleManager().sendTitles(Bukkit.getPlayer(pd.getUuid()),rs.getPrefix(),rs.getMessage(name, pd),10,30,10 );
		else
			Bukkit.getPlayer(pd.getUuid()).sendTitle(rs.getPrefix(),rs.getMessage(name, pd),10,30,10);
	}
	
	public static void sendMsgToPlayer(PlayerData pd,String name,String kind){
		if(!Bukkit.getPlayer(pd.getUuid()).isOnline())return;
		if(rs.getTitleManager()!=null)
			rs.getTitleManager().sendTitles(Bukkit.getPlayer(pd.getUuid()),rs.getPrefix(),rs.getMessage(name, pd).replaceAll("%pd_sick%", kind),10,30,10 );
		else
			Bukkit.getPlayer(pd.getUuid()).sendTitle(rs.getPrefix(),rs.getMessage(name, pd).replaceAll("%pd_sick%", kind),10,30,10);
	}
	
	public static void saveWorkbench(){
		File data=new File(rs.getDataFolder()+File.separator+"Workbench.yml");
		YamlConfiguration dataC=YamlConfiguration.loadConfiguration(data);
		List<String> l=new LinkedList<String>();
		for(String temp:rs.getWorkbench().keySet())
			l.add(temp+":"+rs.getWorkbench().get(temp));
		dataC.set("workbench", l);
		
		l=new LinkedList<String>();
		for(String temp:rs.getRwC().keySet())
			l.add(temp+":"+rs.getRwC().get(temp));
		dataC.set("RainwaterCollector", l);
		
		l=new LinkedList<String>();
		for(String temp:rs.getFCT().keySet())
			l.add(temp+":"+rs.getFCT().get(temp));
		dataC.set("FireCraftTable", l);
		
		try {dataC.save(data);} catch (IOException e) {}
		
		
	}
	
	public static String toWKey(Block b){
		String nums=(b.getX()+""+b.getY()+""+b.getZ()+"").replace("-", "010");
		long a=Long.parseLong(nums);
		return b.getWorld().getName()+Long.toHexString(a);
	}
	
	public static String _2f(double d){
		return String.format("%.2f", d);
	}
	
	public static double random(double min,double max){
		return Double.parseDouble(_2f(min+Math.random()*(max-min)));
	}
}
