package miskyle.realsurvival.machine.util;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import miskyle.realsurvival.data.ConfigManager;

public class GuiItemCreater {
  @SuppressWarnings("deprecation")
  public static ItemStack getItem(String type, String oldType, short damage, String name, List<String> lore) {
    ItemStack item;
    if (ConfigManager.getBukkitVersion() >= 13) {
      item = new ItemStack(Material.valueOf(type));
    } else {
      item = new ItemStack(Material.valueOf(oldType), 1, damage);
    }
    ItemMeta im = item.getItemMeta();
    im.setDisplayName(name);
    im.setLore(lore);
    item.setItemMeta(im);
    return item.clone();
  }

  public static ItemStack getItem(String type, String oldType, short damage, String name) {
    return getItem(type, oldType, damage, name, null);
  }

}
