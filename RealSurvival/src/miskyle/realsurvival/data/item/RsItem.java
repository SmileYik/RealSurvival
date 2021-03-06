package miskyle.realsurvival.data.item;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.github.miskyle.mcpt.MCPT;
import miskyle.realsurvival.data.ItemManager;

public class RsItem {
  private ItemStack item;

  public RsItem(ItemStack item) {
    this.item = item;
    this.item.setAmount(1);
  }

  public static RsItem load(String fileName) {
    return load(new File(MCPT.plugin.getDataFolder() + "/item/" + fileName.toLowerCase() + ".yml"));
  }

  private static RsItem load(File file) {
    if (file == null || !file.exists()) {
      return null;      
    }
    YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
    ItemStack item = data.getItemStack("item", null);
    if (item == null) {
      return null;      
    }
    if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) {
      return new RsItem(item);      
    }
    List<String> lore = new ArrayList<String>();
    item.getItemMeta().getLore().forEach(line -> {
      for (Map.Entry<String, String> entry : ItemManager.getLabels().entrySet()) {
        if (line.contains("${" + entry.getKey() + "}")) {
          lore.add(line.replace("${" + entry.getKey() + "}", entry.getValue()));
          break;
        }
      }
      lore.add(line);
    });
    ItemMeta im = item.getItemMeta();
    im.setLore(lore);
    item.setItemMeta(im);
    return new RsItem(item);
  }

  /**
   * 保存物品数据.

   * @param fileName 文件名.
   * @return
   */
  public boolean save(String fileName) {
    if (fileName == null || fileName.isEmpty()) {
      return false;      
    }
    return save(new File(MCPT.plugin.getDataFolder() + "/item/" + fileName.toLowerCase() + ".yml"));
  }

  private boolean save(File file) {
    if (file == null || file.exists()) {
      return false;      
    }
    file.getParentFile().mkdirs();
    YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
    data.set("item", getSaveItem());
    try {
      data.save(file);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  /**
   * 获取保存到文件中的物品数据.

   * @return
   */
  public ItemStack getSaveItem() {
    if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) {
      return item;      
    }
    List<String> lore = new ArrayList<String>();
    item.getItemMeta().getLore().forEach(line -> {
      for (Map.Entry<String, String> entry : ItemManager.getLabels().entrySet()) {
        if (line.contains(entry.getValue())) {
          lore.add(line.replace(entry.getValue(), "${" + entry.getKey() + "}"));
          break;
        }
      }
      lore.add(line);
    });
    ItemStack is = item.clone();
    ItemMeta im = is.getItemMeta();
    im.setLore(lore);
    is.setItemMeta(im);
    return item;
  }

  public ItemStack getItem() {
    return item.clone();
  }
}
