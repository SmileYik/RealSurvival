package miskyle.realsurvival.data;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import com.github.miskyle.mcpt.MCPT;
import com.github.miskyle.mcpt.nms.nbtitem.NBTItem;

import miskyle.realsurvival.data.item.RSItemData;

public class ItemManager {
	private static ItemManager im;
	private NBTItem nbtItem;
	
	public ItemManager() {
		im = this;
		nbtItem = NBTItem.getNBTItem(MCPT.plugin.getServer().getBukkitVersion());
	}
	
	/**
	 * 从文件中获取物品信息
	 * @param fileName 文件名
	 * @return
	 */
	public static RSItemData loadItemData(String fileName) {
		File file = new File(MCPT.plugin.getDataFolder()+"/"+fileName+".yml");
		if(!file.exists()) return null;
		YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
		RSItemData item = new RSItemData();
		if(data.contains("sleep"))
			item.setSleep(data.getString("sleep").split("/"));
		if(data.contains("thirst"))
			item.setSleep(data.getString("thirst").split("/"));
		if(data.contains("energy"))
			item.setSleep(data.getString("energy").split("/"));
		if(data.contains("hunger"))
			item.setSleep(data.getString("hunger").split("/"));
		if(data.contains("health"))
			item.setSleep(data.getString("health").split("/"));
		if(data.contains("weight"))
			item.setWeight(data.getDouble("weight"));
		return item;
	}

}
