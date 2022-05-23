package tk.smileyik.realsurvival.nms.nbt;

import tk.smileyik.realsurvival.nms.nbt.impls.NBTToolImpl;

public interface NBTTool {
  static NBTTool newNBTTool(org.bukkit.inventory.ItemStack itemStack) {
    return NBTToolImpl.newNBTTool(itemStack);
  }

  NBTTool setNBT(String key, String value);

  org.bukkit.inventory.ItemStack asBukkitItem();

  boolean hasNBT(String key);

  String getNBTString(String key);
}
