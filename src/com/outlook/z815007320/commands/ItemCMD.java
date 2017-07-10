package com.outlook.z815007320.commands;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.outlook.z815007320.data.PluginRS;
import com.outlook.z815007320.utils.Util_Colours;
import com.outlook.z815007320.utils.Utils;

public class ItemCMD extends PluginRS{
	private static String getLore(String[] args,int start){
		String lore="";
		for(int i=start;i<args.length;i++) lore+=args[i]+" ";
		lore.substring(0, lore.length()-2);
		return Util_Colours.replaceTooltipColour(lore);
	}
	
	//rs item lore xxx xxx
	public static void addLore(Player p,String[] args){
		if(args.length<3){
			p.sendMessage("§9[RealSurvival] §4指令格式错了哦,正确格式是§b§l/rs item lore [lore]");
			return;
		}
		ItemStack item=p.getInventory().getItemInMainHand();
		if(item.getType()==Material.AIR){//如果是空气则返回
			p.sendMessage("§9[RealSurvival] §4貌似你手上什么都没有拿.");
			return;
		}
		ItemMeta itemLore=item.getItemMeta();
		List<String> lore=itemLore.getLore();
		if(lore==null)
			lore=new LinkedList<String>();
		lore.add(getLore(args,2));
		itemLore.setLore(lore);
		item.setItemMeta(itemLore);
		p.getInventory().setItemInMainHand(item);
	}
	
	//rs item setlore int xxx xxx
		public static void setLore(Player p,String[] args){
			if(args.length<4){
				p.sendMessage("§9[RealSurvival] §4指令格式错了哦,正确格式是§b§l/rs item setlore [line] [lore]");
				return;
			}
			
			ItemStack item=p.getInventory().getItemInMainHand();
			if(item.getType()==Material.AIR){//如果是空气则返回
				p.sendMessage("§9[RealSurvival] §4貌似你手上什么都没有拿.");
				return;
			}
			
			int line=-1;
			try{
				 line=Integer.parseInt(args[2]);
			}catch(Exception e){
				
			}
			if(line==-1){
				p.sendMessage("§9[RealSurvival] §4行数要是数字哦.正确格式是§b§l/rs item setlore [line] [lore]");
				return;
			}
			ItemMeta itemLore=item.getItemMeta();
			List<String> lore=itemLore.getLore();
			if(lore==null)
				lore=new LinkedList<String>();
			lore.set(line-1,getLore(args,3) );
			itemLore.setLore(lore);
			item.setItemMeta(itemLore);
			p.getInventory().setItemInMainHand(item);
		}
		
		//rs item setname xxx xxx
		public static void setName(Player p,String[] args){
			if(args.length<3){
				p.sendMessage("§9[RealSurvival] §4指令格式错了哦,正确格式是§b§l/rs item setName [name]");
				return;
			}
			ItemStack item=p.getInventory().getItemInMainHand();
			if(item.getType()==Material.AIR){//如果是空气则返回
				p.sendMessage("§9[RealSurvival] §4貌似你手上什么都没有拿.");
				return;
			}
			ItemMeta itemLore=item.getItemMeta();
			itemLore.setDisplayName(getLore(args,2));
			item.setItemMeta(itemLore);
			p.getInventory().setItemInMainHand(item);
		}
		
		//rs item removelore line
		public static void removeLore(Player p,String[] args){
			if(args.length!=3){
				p.sendMessage("§9[RealSurvival] §4指令格式错了哦,正确格式是§b§l/rs item removelore [line]");
				return;
			}
			
			ItemStack item=p.getInventory().getItemInMainHand();
			if(item.getType()==Material.AIR){//如果是空气则返回
				p.sendMessage("§9[RealSurvival] §4貌似你手上什么都没有拿.");
				return;
			}
			
			if(item.getItemMeta()==null||item.getItemMeta().getLore()==null){
				p.sendMessage("§9[RealSurvival] §4你手上拿的东西无效.");
				return;
			}
			
			int line=-1;
			try{
				 line=Integer.parseInt(args[2]);
			}catch(Exception e){
				
			}
			if(line==-1){
				p.sendMessage("§9[RealSurvival] §4行数要是数字哦.正确格式是§b§l/rs item removelore [line]");
				return;
			}
			
			ItemMeta itemLore=item.getItemMeta();
			List<String> lore=itemLore.getLore();
			lore.remove(line-1);
			itemLore.setLore(lore);
			item.setItemMeta(itemLore);
			p.getInventory().setItemInMainHand(item);
		}
		
		//rs item save xxx
		public static void saveItem(Player p,String args){
			ItemStack item=p.getInventory().getItemInMainHand();
			if(item.getType()==Material.AIR){//如果是空气则返回
				p.sendMessage("§9[RealSurvival] §4貌似你手上什么都没有拿.");
				return;
			}
			try {
				File save=new File(rs.getDataFolder()+File.separator+"Items"+File.separator+args+".yml");
				if(!save.exists())
					save.createNewFile();
				else{
					p.sendMessage("§9[RealSurvival] §4保存的文件重名了!");
					return;
				}
				YamlConfiguration temp = YamlConfiguration.loadConfiguration(save);
				temp.set(args, item);//保存文件
				try {temp.save(save);} catch (IOException e) {}
			} catch (IOException e) {}
		}
		
		//rs item get xxx
		public static ItemStack getItem(Player p, String args){
			if(Utils.getItem(args)!=null)return Utils.getItem(args);
			File save=new File(rs.getDataFolder()+File.separator+"Items"+File.separator+args+".yml");
			if(!save.exists())
			{
				p.sendMessage("§9[RealSurvival] §4没找到对应物品的文件!");
				return null;
			}
			YamlConfiguration temp = YamlConfiguration.loadConfiguration(save);
			//p.getInventory().addItem(temp.getItemStack(args));
			return temp.getItemStack(args);
		}
		
		public static String getItemList(){
			String list="§9§l=====================RealSurvival Itemsl=====================\n";
			File path=new File(rs.getDataFolder()+File.separator+"Items");
			for(File i:path.listFiles()){
				if(!i.isFile())continue;
				String fileName=i.getName();
				if(!fileName.substring(fileName.lastIndexOf(".")).equalsIgnoreCase(".yml"))continue;
				list+="§b"+fileName.substring(0,fileName.lastIndexOf("."))+"§9§l | ";
			}
			list+="§b绷带§9§l |§b药§9§l | §b冰水§9§l | §b开水 §9§l| §b沼泽水 §9§l| §b海水§9§l | §b雨水§9§l | §b淡水§9§l | §b湖水\n"
					+ "§9§l=====================RealSurvival Itemsl=====================";
			return list;
		}
}
