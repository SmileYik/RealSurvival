package com.outlook.z815007320.data;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import com.outlook.z815007320.utils.Utils;

public class Items extends PluginRS{
	/**
	 * l.add("§2海水,很咸...");
		l.add("§4不仅不能解渴,§l还有几率生病!");
		l.add("§b需要用净水装置来净化后才能饮用!");
	 * @return 海水
	 */
	public static ItemStack getSeaWater(){
		ItemStack sw=new ItemStack(Material.POTION);
		PotionMeta im = (PotionMeta) sw.getItemMeta();
		im.setColor(Color.fromRGB(15, 103, 219));
		im.setDisplayName("§1§l海水");
		List<String> l=new LinkedList<String>();
		l.add("§a§l"+rs.getLoreTabel("Thirst")+": §b§l-"+Utils.random(2, 20)+"%");
		l.add("§2§l"+rs.getLoreTabel("SickKind")+": §c§l"+rs.defSick.split(";")[(int)Utils.random(0, rs.defSick.split(";").length)]);
		l.add("§2§l"+rs.getLoreTabel("Sickness")+": §c§l"+Utils.random(20, 30)+"%");
		l.add("");
		l.add("§7§l"+rs.getLoreTabel("weight")+": 1");
		l.add("");
		l.add("§2海水,很咸...");
		l.add("§4不仅不能解渴,§l还有几率生病!");
		l.add("§b需要用净水装置来净化后才能饮用!");
		im.setLore(l);
		sw.setItemMeta(im);
		return sw;
	}
	/**
	 * l.add("§2湖水,可以烧成开水");
		l.add("§4§l直接喝有几率生病!");
		l.add("§b需要用§e§l净水装置§b来净化后才能饮用!");
	 * @return湖水
	 */
	public static ItemStack getLakeWater(){
		ItemStack sw=new ItemStack(Material.POTION);
		PotionMeta im = (PotionMeta) sw.getItemMeta();
		im.setColor(Color.fromRGB(36, 165, 229));
		im.setDisplayName("§2§l湖水");
		List<String> l=new LinkedList<String>();
		l.add("§2§l"+rs.getLoreTabel("Thirst")+": §b§l"+Utils.random(5, 20)+"%");
		l.add("§2§l"+rs.getLoreTabel("SickKind")+": §c§l"+rs.defSick.split(";")[(int)Utils.random(0, rs.defSick.split(";").length)]);
		l.add("§2§l"+rs.getLoreTabel("Sickness")+": §c§l"+Utils.random(5, 15)+"%");
		l.add("");
		l.add("§7§l"+rs.getLoreTabel("weight")+": 1");
		l.add("");
		l.add("§4§l直接喝有几率生病!");
		l.add("§b需要用§e§l净水装置§b来净化后才能饮用!");
		im.setLore(l);
		sw.setItemMeta(im);
		return sw;
	}
	/**
	 * l.add("§2可以直接饮用");
		l.add("§a§l可以缓慢治疗疾病!");
		l.add("§c还可以暖身子!");
	 * @return 开水
	 */
	public static ItemStack getHotWater(){
		ItemStack sw=new ItemStack(Material.POTION);
		PotionMeta im = (PotionMeta) sw.getItemMeta();
		im.setColor(Color.fromRGB(36, 165, 229));
		im.setDisplayName("§d§l开水");
		List<String> l=new LinkedList<String>();
		l.add("§a§l"+rs.getLoreTabel("Thirst")+": §b§l"+Utils.random(10, 25)+"%");
		l.add("§c§l"+rs.getLoreTabel("tem")+": §b§l"+Utils.random(0.1, 1.5));
		l.add("");
		l.add("§7§l"+rs.getLoreTabel("weight")+": 1");
		l.add("");
		l.add("§2可以直接饮用");
		l.add("§a§l可以缓慢治疗疾病!");
		l.add("§c还可以暖身子!");
		im.setLore(l);
		sw.setItemMeta(im);
		return sw;
	}
	/**
	 * l.add("§2可以直接饮用");
		l.add("§b§l可以给身体降温!");
	 * @return 冰水
	 */
	public static ItemStack getIceWater(){
		ItemStack sw=new ItemStack(Material.POTION);
		PotionMeta im = (PotionMeta) sw.getItemMeta();
		im.setColor(Color.fromRGB(36, 165, 229));
		im.setDisplayName("§3§l冰水");
		List<String> l=new LinkedList<String>();
		l.add("§a§l"+rs.getLoreTabel("Thirst")+": §b§l"+Utils.random(10, 25)+"%");
		l.add("§3§l"+rs.getLoreTabel("tem")+": §c§l-"+Utils.random(0.1, 1.5)+"%");
		l.add("");
		l.add("§7§l"+rs.getLoreTabel("weight")+": 1");
		l.add("");
		l.add("§2可以直接饮用");
		l.add("§b§l可以给身体降温!");
		im.setLore(l);
		sw.setItemMeta(im);
		return sw;
	}
	/**
	 * l.add("§2可以直接饮用");
	 * @return 淡水
	 */
	public static ItemStack getFreshWater(){
		ItemStack sw=new ItemStack(Material.POTION);
		PotionMeta im = (PotionMeta) sw.getItemMeta();
		im.setColor(Color.fromRGB(36, 165, 229));
		im.setDisplayName("§3§l淡水");
		List<String> l=new LinkedList<String>();
		l.add("§a§l"+rs.getLoreTabel("Thirst")+": §b§l"+Utils.random(10, 25)+"%");
		l.add("");
		l.add("§7§l"+rs.getLoreTabel("weight")+": 1");
		l.add("");
		l.add("§2可以直接饮用");
		im.setLore(l);
		sw.setItemMeta(im);
		return sw;
	}
	
	/**
	 *l.add("§2虽然可以解渴,不过...");
		l.add("§e有极大几率生病....");
	 * @return 沼泽水
	 */
	public static ItemStack getSwamplandWater(){
		ItemStack sw=new ItemStack(Material.POTION);
		PotionMeta im = (PotionMeta) sw.getItemMeta();
		im.setColor(Color.fromRGB(50, 69, 107));
		im.setDisplayName("§8§l沼泽水");
		List<String> l=new LinkedList<String>();
		l.add("§a§l"+rs.getLoreTabel("Thirst")+": §b§l"+Utils.random(2, 15)+"%");
		l.add("§2§l"+rs.getLoreTabel("SickKind")+": §c§l"+rs.defSick.split(";")[(int)Utils.random(0, rs.defSick.split(";").length)]);
		l.add("§2§l"+rs.getLoreTabel("Sickness")+": §c§l"+Utils.random(30, 50)+"%");
		l.add("");
		l.add("§7§l"+rs.getLoreTabel("weight")+": 1");
		l.add("");
		l.add("§2虽然可以解渴,不过...");
		l.add("§e有极大几率生病....");
		im.setLore(l);
		sw.setItemMeta(im);
		return sw;
	}
	
	/**
		l.add("§2雨水相对来说还是比较干净的");
		l.add("§b但是还是有较小几率得病");
	 * @return 雨水
	 */
	public static ItemStack getRainwater(){
		ItemStack sw=new ItemStack(Material.POTION);
		PotionMeta im = (PotionMeta) sw.getItemMeta();
		im.setColor(Color.fromRGB(36, 165, 229));
		im.setDisplayName("§b§l雨水");
		List<String> l=new LinkedList<String>();
		l.add("§a§l"+rs.getLoreTabel("Thirst")+": §b§l"+Utils.random(5, 25)+"%");
		l.add("§2§l"+rs.getLoreTabel("SickKind")+": §c§l"+rs.defSick.split(";")[(int)Utils.random(0, rs.defSick.split(";").length)]);
		l.add("§2§l"+rs.getLoreTabel("Sickness")+": §c§l"+Utils.random(2, 10)+"%");
		l.add("");
		l.add("§7§l"+rs.getLoreTabel("weight")+": 1");
		l.add("");
		l.add("§2雨水相对来说还是比较干净的");
		l.add("§b但是还是有较小几率得病");
		im.setLore(l);
		sw.setItemMeta(im);
		return sw;
	}
	
	/**
	 * l.add("§2可以治疗自己得的所有病的药");
		l.add("§e也许你需要注意按时吃药.");
	 * @return 药
	 */
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
	/**
	 * 
	 * @return 绷带
	 */
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
	
	/**
	 * 
	 * @return 炉灰
	 */
	public static ItemStack getJunk(){
		ItemStack sw=new ItemStack(Material.MELON_SEEDS);
		ItemMeta im = sw.getItemMeta();
		im.setDisplayName("§7§l炉灰");
		List<String> l=new LinkedList<String>();
		l.add("§7§l"+rs.getLoreTabel("Thirst")+": -"+Utils.random(45,80)+"%");
		l.add("§7§l"+rs.getLoreTabel("SickKind")+": "+rs.defSick.split(";")[(int)Utils.random(0, rs.defSick.split(";").length)]);
		l.add("§7§l"+rs.getLoreTabel("Sickness")+": "+Utils.random(99,101)+"%");
		l.add("");
		l.add("§7§l"+rs.getLoreTabel("weight")+": 1");
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
	
	/**
	 * l.add("§9返回到上个界面");
	 * @return 返回
	 */
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
	
	/**
	 * l.add("§9下一页");
	 * @return 下一页
	 */
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
	
	/**
	 * name=配方名
	 *		l.add("§9点击查看对应配方");
	 * @return 返回
	 */
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

	/**
	 * 创建一个包含世界地址的占位符
	 * @param x 
	 * @param y
	 * @param z
	 * @param world
	 * @param id
	 * @return
	 */
	public static ItemStack createWorldPlaceholder(int x,int y,int z,String world,short id){
		ItemStack sw=new ItemStack(Material.STAINED_GLASS_PANE,1,id);
		ItemMeta im = sw.getItemMeta();
		im.setDisplayName(world+","+x+","+y+","+z);
		sw.setItemMeta(im);
		return sw;
	}


	public static ItemStack createItemKey(Material type,short damage,String world,int x,int y,int z){
		ItemStack is=new ItemStack(type,1,damage);
		ItemMeta im=is.getItemMeta();
		//set Key
		im.setDisplayName(world+Long.toHexString(Long.parseLong((x+""+y+""+z+"").replace("-", "010"))));
		im.setLore(Arrays.asList(world,x+"",y+"",z+""));
		is.setItemMeta(im);
		return is;
	}
	
	
	public static ItemStack createPItem(short damage,String name){
		ItemStack is=new ItemStack(Material.STAINED_GLASS_PANE,1,damage);
		ItemMeta im=is.getItemMeta();
		im.setDisplayName(name);
		is.setItemMeta(im);
		return is;
	}
	
	public static ItemStack createItemKey(Material type,short damage,String key){
		ItemStack is=new ItemStack(type,1,damage);
		ItemMeta im=is.getItemMeta();
		//set Key
		im.setDisplayName(key);
		is.setItemMeta(im);
		return is;
	}
}
