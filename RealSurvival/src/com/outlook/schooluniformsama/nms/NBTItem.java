package com.outlook.schooluniformsama.nms;


import com.outlook.schooluniformsama.RealSurvival;
import com.outlook.schooluniformsama.nms.item.*;

public class NBTItem {
	private static com.outlook.schooluniformsama.nms.item.NBTItem item;
	
	public static void init(){
		switch (RealSurvival.getVersion()) {
		case "v1_15_R1":
			item = new Item_1_15_R1();
			break;
		case "v1_14_R1":
			item = new Item_1_14_R1();
			break;
		case "v1_13_R2":
			item = new Item_1_13_R2();
			break;
		case "v1_12_R1":
			item = new Item_1_12_R1();
			break;
		case "v1_11_R1":
			item = new Item_1_11_R1();
			break;
		case "v1_10_R1":
			item = new Item_1_10_R1();
			break;
		case "v1_9_R2":
			item = new Item_1_9_R2();
			break;
		case "v1_8_R3":
			item = new Item_1_8_R3();
			break;
		case "v1_8_R1":
			item = new Item_1_8_R1();
			break;
		case "v1_7_R4":
			item = new Item_1_7_R4();
			break;
		default:
			item = null;
			break;
		}
	}
	
	public static org.bukkit.inventory.ItemStack setNBT(org.bukkit.inventory.ItemStack is, String key, String value){
		if(item == null) return null;
		else return item.addNBT(is, key, value);
	}
	
	public static String getNBTValue(org.bukkit.inventory.ItemStack is, String key) {
		if(item == null) return null;
		else return item.getNBTValue(is, key);
	}
	
	
/*	import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;

	import net.minecraft.server.v1_11_R1.ItemStack;
	import net.minecraft.server.v1_11_R1.NBTTagCompound;
	import net.minecraft.server.v1_11_R1.NBTTagString;*/
}
