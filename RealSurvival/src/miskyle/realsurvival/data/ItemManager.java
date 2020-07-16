package miskyle.realsurvival.data;

import java.io.File;
import java.util.HashMap;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import com.github.miskyle.mcpt.MCPT;
import com.github.miskyle.mcpt.nms.nbtitem.NBTItem;

import miskyle.realsurvival.RealSurvival;
import miskyle.realsurvival.data.item.RSItemData;
import miskyle.realsurvival.util.Utils;

public class ItemManager {
	private static ItemManager im;
	private NBTItem nbtItem;
	
	private String split;
	private HashMap<String, String> labels;
	
	private HashMap<String, RSItemData> nbtItemData;
	
	public ItemManager() {
		im = this;
		nbtItem = NBTItem.getNBTItem(RealSurvival.getVersion());
		split = MCPT.plugin.getConfig().getString("label.split",":");
		labels = new HashMap<String, String>();
		for(String line : MCPT.plugin.getConfig().getStringList("label.labels")) {
			String[] temp = line.split(":");
			labels.put(temp[0], temp[1]);
		}
		nbtItemData = new HashMap<String, RSItemData>();
		loadNBTItem(new File(MCPT.plugin.getDataFolder()+"/nbtitem/"),
				new File(MCPT.plugin.getDataFolder()+"/nbtitem/").getAbsolutePath());
		
	}
	
	private void loadNBTItem(File path,String root) {
		for(File f : path.listFiles()) {
			if(f.isDirectory()) {
				loadNBTItem(f,root);				
			}else {
				String name = f.getAbsolutePath().replace(root, "").toLowerCase().replace(".yml", "").replace("\\", "/");
				while(name.charAt(0) == '/')
					name = name.substring(1);
				
				nbtItemData.put(name, loadItemData(f));
			}
		}
	}
	
	/**
	 * 从文件中获取物品信息
	 * @param fileName 文件名 大小写不敏感
	 * @return
	 */
	public static RSItemData loadItemData(String fileName) {
		return loadItemData(
				new File(MCPT.plugin.getDataFolder()+"/nbtitem/"+fileName.toLowerCase()+".yml"));
	}
	
	public static RSItemData loadItemData(File file) {
		if(!file.exists()) {
			//TODO 返回原版物品对应数据
		}
		YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
		RSItemData item = new RSItemData();
		if(data.contains("sleep")) {
			item.setSleep(data.getString("sleep").split("/"));
			item.setMaxSleep(data.getBoolean("sleep-max",false));
		}
		if(data.contains("thirst")) {
			item.setThirst(data.getString("thirst").split("/"));
			item.setMaxThirst(data.getBoolean("thirst-max",false));
		}
		if(data.contains("energy")) {
			item.setEnergy(data.getString("energy").split("/"));
			item.setMaxEnergy(data.getBoolean("energy-max",false));
		}
		if(data.contains("hunger")) {
			item.setHunger(data.getString("hunger").split("/"));
			item.setMaxHunger(data.getBoolean("hunger-max",false));
		}
		if(data.contains("health")) {
			item.setHealth(data.getString("health").split("/"));
			item.setMaxHealth(data.getBoolean("health-max"));
		}
		if(data.contains("weight"))
			item.setWeight(data.getDouble("weight"));
		return item;
	}
	
	public static RSItemData loadItemData(ItemStack item) {
		if(item==null)return null;
		String name = im.nbtItem.getString(item, "RSNBT");
		if(name!=null) {
			name = name.toLowerCase();
			if(im.nbtItemData.containsKey(name))
				return im.nbtItemData.get(name);
		}
		
		if(!item.hasItemMeta() || !item.getItemMeta().hasLore()) {
			
			//TODO 返回原版物品对应数据
			return new RSItemData();
		}
		
		RSItemData rsItem = new RSItemData();
		item.getItemMeta().getLore().forEach(s->{
			String ss = Utils.removeColor(s);
			im.labels.forEach((k,v)->{
				if(ss.contains(v) && ss.contains(im.split)) {
					String temp = ss.replaceAll("[^0-9+-/%]", "");
					if(temp.contains("%")) {
						switch (k.toUpperCase()) {
							case "SLEEP":
								rsItem.setMaxSleep(true);
								break;
							case "THIRST":
								rsItem.setMaxThirst(true);
								break;
							case "ENERGY":
								rsItem.setMaxEnergy(true);
								break;
							case "HEALTH":
								rsItem.setMaxHealth(true);
								break;
							case "HUNGER":
								rsItem.setMaxHunger(true);
								break;
						}
					}
					if(temp.contains("/")) {
						//范围类型
						String[] temp2 = temp.split("/");
						rsItem.addStatusValue(
								k, Double.parseDouble(temp2[0]), Double.parseDouble(temp2[1]));
					}else {
						//单个数值类型
						double a = Double.parseDouble(temp);
						rsItem.addStatusValue(k, a, a);
					}
				}
			});
		});
		return rsItem;
	}
	
	public static double getStatusValue(String status,ItemStack item) {
		if(item==null)return 0;
		String name = im.nbtItem.getString(item, "RSNBT");
		if(name!=null) {
			name = name.toLowerCase();
			if(im.nbtItemData.containsKey(name))
				return im.nbtItemData.get(name).getValue(status);
		}
		double value = 0;
		String key = im.labels.get(status);
		if(!item.hasItemMeta() || !item.getItemMeta().hasLore()) {
			
			//TODO 返回原版物品对应数据
		}
		for(String s : item.getItemMeta().getLore()) {
			String ss = Utils.removeColor(s);
			if(ss.contains(key) && ss.contains(im.split)) {
				String temp = ss.replaceAll("[^0-9+-/]", "");
				if(temp.contains("/")) {
					//范围类型
					String[] temp2 = temp.split("/");
					value+=im.random(Double.parseDouble(temp2[0]), Double.parseDouble(temp2[1]));
				}else {
					//单个数值类型
					value+=Double.parseDouble(temp);
				}
			}
		}
		return value;
	}
	
	public static double getStatusValueOnly(String status,ItemStack item) {
		if(item==null)return 0;
		String name = im.nbtItem.getString(item, "RSNBT");
		if(name!=null) {
			name = name.toLowerCase();
			if(im.nbtItemData.containsKey(name))
				return im.nbtItemData.get(name).getValue(status);
		}
		String key = im.labels.get(status);
		if(!item.hasItemMeta() || !item.getItemMeta().hasLore()) {
			
			//TODO 返回原版物品对应数据
			return 0;
		}
		for(String s : item.getItemMeta().getLore()) {
			String ss = Utils.removeColor(s);
			if(ss.contains(key) && ss.contains(im.split)) {
				String temp = ss.replaceAll("[^0-9+-/]", "");
				if(temp.contains("/")) {
					//范围类型
					String[] temp2 = temp.split("/");
					return im.random(Double.parseDouble(temp2[0]), Double.parseDouble(temp2[1]));
				}else {
					//单个数值类型
					return Double.parseDouble(temp);
				}
			}
		}
		return 0;
	}
	
	private double random(double a,double b) {
		return Math.abs(a-b)*Math.random()+Math.min(a, b);
	}

	public static HashMap<String, String> getLabels() {
		return im.labels;
	}
	
	public static NBTItem getNBT() {
		return im.nbtItem;
	}

}
