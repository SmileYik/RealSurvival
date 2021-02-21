package miskyle.realsurvival.util.watermaker;

import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.github.miskyle.mcpt.i18n.I18N;
import miskyle.realsurvival.data.ItemManager;
import miskyle.realsurvival.data.item.RsItem;
import miskyle.realsurvival.data.item.RsItemData;

public class WaterMakerVer {
  /**
   * 初始化创建未知水物品.
   */
  public static void makeUnknownWater() {
    ItemStack item = new ItemStack(Material.POTION);
    ItemMeta im = item.getItemMeta();
    im.setDisplayName(I18N.tr("status.thirst.water.unknown-biome"));
    im.setLore(Arrays.asList(I18N.tr("status.thirst.water.unknown-biome.des").split(";")));
    item.setItemMeta(im);
    RsItemData data = new RsItemData();
    data.setStatusValue("THIRST", 10, 20);
    data.save("water/unknown");
    ItemStack is = ItemManager.getNbt().setNBT(item, "RSNBT", "water/unknown");
    new RsItem(is).save("water/unknown");
  }

  /**
   * 初始化创建雨水.
   */
  public static void makeRainwater() {
    ItemStack item = new ItemStack(Material.POTION);
    ItemMeta im = item.getItemMeta();
    im.setDisplayName(I18N.tr("status.thirst.water.rainwater"));
    im.setLore(Arrays.asList(I18N.tr("status.thirst.water.rainwater.des").split(";")));
    item.setItemMeta(im);
    RsItemData data = new RsItemData();
    data.setStatusValue("THIRST", 10, 20);
    data.save("water/rainwater");
    ItemStack is = ItemManager.getNbt().setNBT(item, "RSNBT", "water/rainwater");
    new RsItem(is).save("water/rainwater");
  }
}
