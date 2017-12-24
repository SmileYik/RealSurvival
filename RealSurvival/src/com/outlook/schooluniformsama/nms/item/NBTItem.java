package com.outlook.schooluniformsama.nms.item;

import org.bukkit.inventory.ItemStack;

public interface NBTItem {
	public ItemStack addNBT(ItemStack is,String key,String value);
	public String getNBTValue(ItemStack is,String key);
}
