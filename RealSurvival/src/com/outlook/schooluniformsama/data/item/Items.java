package com.outlook.schooluniformsama.data.item;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.util.HashMap;
import com.outlook.schooluniformsama.util.Msg;

public class Items{
	public static ItemStack createPItem(short damage,String name){
		ItemStack is=new ItemStack(Material.STAINED_GLASS_PANE,1,damage);
		ItemMeta im=is.getItemMeta();
		im.setDisplayName(name);
		is.setItemMeta(im);
		return is;
	}
	
	public static ItemStack getWater(String name){
		//Msg.getMsg(name, false)
		return YamlConfiguration.loadConfiguration(new File(Data.DATAFOLDER+"/items/waters.yml")).getItemStack(name);
	}
	
	public boolean createWater(){
		File waterFile=new File(Data.DATAFOLDER+"/items/waters.yml");
		if(waterFile.exists())return true;
		YamlConfiguration data = YamlConfiguration.loadConfiguration(waterFile);
		
		ItemStack is=new ItemStack(Material.POTION);
		ItemMeta im=is.getItemMeta();
		
		im.setDisplayName(Msg.getMsg("Seawater", false));
		is.setItemMeta(im);
		new NBTItemData(-20, ItemLoreData.badCode(), ItemLoreData.badCode(), 2, new HashMap<>(), new LinkedList<>(), 
				ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode()).save("Seawater");
		data.set("Seawater",Data.nbtitem.addNBT(is.clone(), "RealSurvival", "Seawater") );
		
		im.setDisplayName(Msg.getMsg("IceSeawater", false));
		is.setItemMeta(im);
		new NBTItemData(-20, ItemLoreData.badCode(), ItemLoreData.badCode(), 2, new HashMap<>(), new LinkedList<>(), 
				ItemLoreData.badCode(), ItemLoreData.badCode(), -0.5, ItemLoreData.badCode()).save("IceSeawater");
		data.set("IceSeawater",Data.nbtitem.addNBT(is.clone(), "RealSurvival", "IceSeawater") );
		
		im.setDisplayName(Msg.getMsg("SwampWater", false));
		is.setItemMeta(im);
		new NBTItemData(5, ItemLoreData.badCode(), ItemLoreData.badCode(), 2, new HashMap<>(), new LinkedList<>(), 
				ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode()).save("SwampWater");
		data.set("SwampWater",Data.nbtitem.addNBT(is.clone(), "RealSurvival", "SwampWater") );
		
		im.setDisplayName(Msg.getMsg("LakeWater", false));
		is.setItemMeta(im);
		new NBTItemData(15, ItemLoreData.badCode(), ItemLoreData.badCode(), 2, new HashMap<>(), new LinkedList<>(), 
				ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode()).save("LakeWater");
		data.set("LakeWater",Data.nbtitem.addNBT(is.clone(), "RealSurvival", "LakeWater") );
		
		im.setDisplayName(Msg.getMsg("IceLakeWater", false));
		is.setItemMeta(im);
		new NBTItemData(15, ItemLoreData.badCode(), ItemLoreData.badCode(), 2, new HashMap<>(), new LinkedList<>(), 
				ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode()).save("IceLakeWater");
		data.set("IceLakeWater",Data.nbtitem.addNBT(is.clone(), "RealSurvival", "IceLakeWater") );
		
		im.setDisplayName(Msg.getMsg("HotLakeWater", false));
		is.setItemMeta(im);
		new NBTItemData(15, ItemLoreData.badCode(), ItemLoreData.badCode(), 2, new HashMap<>(), new LinkedList<>(), 
				ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode()).save("HotLakeWater");
		data.set("HotLakeWater",Data.nbtitem.addNBT(is.clone(), "RealSurvival", "HotLakeWater") );
		
		im.setDisplayName(Msg.getMsg("Freshwater", false));
		is.setItemMeta(im);
		new NBTItemData(30, ItemLoreData.badCode(), ItemLoreData.badCode(), 2, new HashMap<>(), new LinkedList<>(), 
				ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode()).save("Freshwater");
		data.set("Freshwater",Data.nbtitem.addNBT(is.clone(), "RealSurvival", "Freshwater") );
		
		im.setDisplayName(Msg.getMsg("HotWater", false));
		is.setItemMeta(im);
		new NBTItemData(30, ItemLoreData.badCode(), ItemLoreData.badCode(), 2, new HashMap<>(), new LinkedList<>(), 
				ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode()).save("HotWater");
		data.set("HotWater",Data.nbtitem.addNBT(is.clone(), "RealSurvival", "HotWater") );
		
		im.setDisplayName(Msg.getMsg("IceWater", false));
		is.setItemMeta(im);
		new NBTItemData(30, ItemLoreData.badCode(), ItemLoreData.badCode(), 2, new HashMap<>(), new LinkedList<>(), 
				ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode()).save("IceWater");
		data.set("IceWater",Data.nbtitem.addNBT(is.clone(), "RealSurvival", "IceWater") );
		
		im.setDisplayName(Msg.getMsg("Rainwater", false));
		is.setItemMeta(im);
		new NBTItemData(23, ItemLoreData.badCode(), ItemLoreData.badCode(), 2, new HashMap<>(), new LinkedList<>(), 
				ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode(), ItemLoreData.badCode()).save("Rainwater");
		data.set("Rainwater",Data.nbtitem.addNBT(is.clone(), "RealSurvival", "Rainwater") );
		
		try {
			data.save(waterFile);
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
/*	public static ItemStack getSeaWater(){
		ItemStack sw=new ItemStack(Material.POTION);
		PotionMeta im = (PotionMeta) sw.getItemMeta();
		im.setColor(Color.fromRGB(15, 103, 219));
		im.setDisplayName("§1§l海水");
		List<String> l=new LinkedList<String>();
		l.add("§a§l"+rs.getLoreTabel("Thirst")+": §b§l-"+OldUtils.random(2, 20)+"%");
		l.add("§2§l"+rs.getLoreTabel("SickKind")+": §c§l"+rs.defSick.split(";")[(int)OldUtils.random(0, rs.defSick.split(";").length)]);
		l.add("§2§l"+rs.getLoreTabel("Sickness")+": §c§l"+OldUtils.random(20, 30)+"%");
		l.add("");
		l.add("§7§l"+rs.getLoreTabel("Weight")+": 1");
		l.add("");
		l.add("§2海水,很咸...");
		l.add("§4不仅不能解渴,§l还有几率生病!");
		l.add("§b需要用净水装置来净化后才能饮用!");
		im.setLore(l);
		sw.setItemMeta(im);
		return sw;
	}
	public static ItemStack getLakeWater(){
		ItemStack sw=new ItemStack(Material.POTION);
		PotionMeta im = (PotionMeta) sw.getItemMeta();
		im.setColor(Color.fromRGB(36, 165, 229));
		im.setDisplayName("§2§l湖水");
		List<String> l=new LinkedList<String>();
		l.add("§2§l"+rs.getLoreTabel("Thirst")+": §b§l"+OldUtils.random(5, 20)+"%");
		l.add("§2§l"+rs.getLoreTabel("SickKind")+": §c§l"+rs.defSick.split(";")[(int)OldUtils.random(0, rs.defSick.split(";").length)]);
		l.add("§2§l"+rs.getLoreTabel("Sickness")+": §c§l"+OldUtils.random(5, 15)+"%");
		l.add("");
		l.add("§7§l"+rs.getLoreTabel("Weight")+": 1");
		l.add("");
		l.add("§4§l直接喝有几率生病!");
		l.add("§b需要用§e§l净水装置§b来净化后才能饮用!");
		im.setLore(l);
		sw.setItemMeta(im);
		return sw;
	}
	public static ItemStack getHotWater(){
		ItemStack sw=new ItemStack(Material.POTION);
		PotionMeta im = (PotionMeta) sw.getItemMeta();
		im.setColor(Color.fromRGB(36, 165, 229));
		im.setDisplayName("§d§l开水");
		List<String> l=new LinkedList<String>();
		l.add("§a§l"+rs.getLoreTabel("Thirst")+": §b§l"+OldUtils.random(10, 25)+"%");
		l.add("§c§l"+rs.getLoreTabel("Tem")+": §b§l"+OldUtils.random(0.1, 1.5));
		l.add("");
		l.add("§7§l"+rs.getLoreTabel("Weight")+": 1");
		l.add("");
		l.add("§2可以直接饮用");
		l.add("§a§l可以缓慢治疗疾病!");
		l.add("§c还可以暖身子!");
		im.setLore(l);
		sw.setItemMeta(im);
		return sw;
	}
	public static ItemStack getIceWater(){
		ItemStack sw=new ItemStack(Material.POTION);
		PotionMeta im = (PotionMeta) sw.getItemMeta();
		im.setColor(Color.fromRGB(36, 165, 229));
		im.setDisplayName("§3§l冰水");
		List<String> l=new LinkedList<String>();
		l.add("§a§l"+rs.getLoreTabel("Thirst")+": §b§l"+OldUtils.random(10, 25)+"%");
		l.add("§3§l"+rs.getLoreTabel("Tem")+": §c§l-"+OldUtils.random(0.1, 1.5)+"%");
		l.add("");
		l.add("§7§l"+rs.getLoreTabel("Weight")+": 1");
		l.add("");
		l.add("§2可以直接饮用");
		l.add("§b§l可以给身体降温!");
		im.setLore(l);
		sw.setItemMeta(im);
		return sw;
	}
	public static ItemStack getFreshWater(){
		ItemStack sw=new ItemStack(Material.POTION);
		PotionMeta im = (PotionMeta) sw.getItemMeta();
		im.setColor(Color.fromRGB(36, 165, 229));
		im.setDisplayName("§3§l淡水");
		List<String> l=new LinkedList<String>();
		l.add("§a§l"+rs.getLoreTabel("Thirst")+": §b§l"+OldUtils.random(10, 25)+"%");
		l.add("");
		l.add("§7§l"+rs.getLoreTabel("Weight")+": 1");
		l.add("");
		l.add("§2可以直接饮用");
		im.setLore(l);
		sw.setItemMeta(im);
		return sw;
	}

	public static ItemStack getSwamplandWater(){
		ItemStack sw=new ItemStack(Material.POTION);
		PotionMeta im = (PotionMeta) sw.getItemMeta();
		im.setColor(Color.fromRGB(50, 69, 107));
		im.setDisplayName("§8§l沼泽水");
		List<String> l=new LinkedList<String>();
		l.add("§a§l"+rs.getLoreTabel("Thirst")+": §b§l"+OldUtils.random(2, 15)+"%");
		l.add("§2§l"+rs.getLoreTabel("SickKind")+": §c§l"+rs.defSick.split(";")[(int)OldUtils.random(0, rs.defSick.split(";").length)]);
		l.add("§2§l"+rs.getLoreTabel("Sickness")+": §c§l"+OldUtils.random(30, 50)+"%");
		l.add("");
		l.add("§7§l"+rs.getLoreTabel("Weight")+": 1");
		l.add("");
		l.add("§2虽然可以解渴,不过...");
		l.add("§e有极大几率生病....");
		im.setLore(l);
		sw.setItemMeta(im);
		return sw;
	}

	public static ItemStack getRainwater(){
		ItemStack sw=new ItemStack(Material.POTION);
		PotionMeta im = (PotionMeta) sw.getItemMeta();
		im.setColor(Color.fromRGB(36, 165, 229));
		im.setDisplayName("§b§l雨水");
		List<String> l=new LinkedList<String>();
		l.add("§a§l"+rs.getLoreTabel("Thirst")+": §b§l"+OldUtils.random(5, 25)+"%");
		l.add("§2§l"+rs.getLoreTabel("SickKind")+": §c§l"+rs.defSick.split(";")[(int)OldUtils.random(0, rs.defSick.split(";").length)]);
		l.add("§2§l"+rs.getLoreTabel("Sickness")+": §c§l"+OldUtils.random(2, 10)+"%");
		l.add("");
		l.add("§7§l"+rs.getLoreTabel("Weight")+": 1");
		l.add("");
		l.add("§2雨水相对来说还是比较干净的");
		l.add("§b但是还是有较小几率得病");
		im.setLore(l);
		sw.setItemMeta(im);
		return sw;
	}
	
	*//**
	 * l.add("§2可以治疗自己得的所有病的药");
		l.add("§e也许你需要注意按时吃药.");
	 * @return 药
	 *//*
	public static ItemStack getMedicine01(){
		ItemStack sw=new ItemStack(Material.QUARTZ);
		ItemMeta im = sw.getItemMeta();
		im.setDisplayName("§a§l药");
		List<String> l=new LinkedList<String>();
		l.add("§2可以治疗自己得的所有病的药");
		l.add("§2不过治疗效率有点慢...");
		l.add("§e也许你需要注意按时吃药.");
		im.setLore(l);
		sw.setItemMeta(im);
		return sw;
	}
	*//**
	 * 
	 * @return 绷带
	 *//*
	public static ItemStack getMedicine02(){
		ItemStack sw=new ItemStack(Material.PAPER);
		ItemMeta im = sw.getItemMeta();
		im.setDisplayName("§a§l绷带");
		List<String> l=new LinkedList<String>();
		l.add("§2可以治疗自己的骨折");
		l.add("§e也许你需要注意按时换一次.");
		im.setLore(l);
		sw.setItemMeta(im);
		return sw;
	}
	
	*//**
	 * 
	 * @return 炉灰
	 *//*
	public static ItemStack getJunk(){
		ItemStack sw=new ItemStack(Material.MELON_SEEDS);
		ItemMeta im = sw.getItemMeta();
		im.setDisplayName("§7§l炉灰");
		List<String> l=new LinkedList<String>();
		l.add("§7§l"+rs.getLoreTabel("Thirst")+": -"+OldUtils.random(45,80)+"%");
		l.add("§7§l"+rs.getLoreTabel("SickKind")+": "+rs.defSick.split(";")[(int)OldUtils.random(0, rs.defSick.split(";").length)]);
		l.add("§7§l"+rs.getLoreTabel("Sickness")+": "+OldUtils.random(99,101)+"%");
		l.add("");
		l.add("§7§l"+rs.getLoreTabel("Weight")+": 1");
		l.add("");
		l.add("§c§l烧东西失败后的作品");
		l.add("§c§l先不说口感怎么样,但是你敢吃嘛?");
		im.setLore(l);
		sw.setItemMeta(im);
		return sw;
	}
	
	public static ItemStack getRainWaterCollector(){
		ItemStack sw=new ItemStack(Material.HOPPER);
		//ItemStack sw=new ItemStack(Material.CAULDRON_ITEM);
		ItemMeta im = sw.getItemMeta();
		im.setDisplayName("§3§l雨水收集器");
		sw.setItemMeta(im);
		return sw;
	}
	
	*//**
	 * l.add("§9返回到上个界面");
	 * @return 返回
	 *//*
	public static ItemStack getPlaceholder6(){
		ItemStack sw=new ItemStack(Material.NETHER_STAR);
		ItemMeta im = sw.getItemMeta();
		im.setDisplayName("§9§l返回");
		List<String> l=new LinkedList<String>();
		l.add("§9返回到上个界面");
		im.setLore(l);
		sw.setItemMeta(im);
		return sw;
	}
	
	*//**
	 * l.add("§9下一页");
	 * @return 下一页
	 *//*
	public static ItemStack getPlaceholder7(){
		ItemStack sw=new ItemStack(Material.EMERALD);
		ItemMeta im = sw.getItemMeta();
		im.setDisplayName("§9§l下一页");
		List<String> l=new LinkedList<String>();
		l.add("§9下一个界面");
		im.setLore(l);
		sw.setItemMeta(im);
		return sw;
	}
	
	*//**
	 * name=配方名
	 *		l.add("§9点击查看对应配方");
	 * @return 返回
	 *//*
	public static ItemStack getPlaceholder9(String name,ItemStack sw1,long time){
		ItemMeta im = sw1.getItemMeta();
		im.setDisplayName("§9§l"+name);
		List<String> l=im.getLore();
		if(l==null)
			l=new LinkedList<String>();
		l.add(0, "§e===============================");
		l.add(0, "§b制造所需时间: §3§l"+time+"§b秒");
		l.add(0, "§b配方名: §9§l"+name);
		l.add(0, "§e===============================");
		im.setLore(l);
		sw1.setItemMeta(im);
		return sw1;
	}
	
	public static  ItemStack getPlaceholder10(short data,int pass){
		ItemStack sw=new ItemStack(Material.STAINED_GLASS_PANE,1,data);
		ItemMeta im = sw.getItemMeta();
		if(pass>=100)
			im.setDisplayName("§9§l已完成 - 点击获取物品!");
		else
			im.setDisplayName("§9§l已完成 "+pass+"%");
		sw.setItemMeta(im);
		return sw;
	}

	public static ItemStack getPlaceholder13(){
		ItemStack sw=new ItemStack(Material.STAINED_GLASS_PANE,1,(short)9);
		ItemMeta im = sw.getItemMeta();
		im.setDisplayName("§3§l雨水");
		sw.setItemMeta(im);
		return sw;
	}

	*/
}
