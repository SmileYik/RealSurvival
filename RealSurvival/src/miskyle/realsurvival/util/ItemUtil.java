package miskyle.realsurvival.util;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import miskyle.realsurvival.data.ConfigManager;

public class ItemUtil {
  /**
   * 获取玩家主手中的物品.

   * @param p 玩家.
   * @return
   */
  @SuppressWarnings("deprecation")
  public static ItemStack getItemInMainHand(Player p) {
    if (ConfigManager.getBukkitVersion() >= 9) {
      return p.getEquipment().getItemInMainHand();
    } else {
      return p.getItemInHand();
    }
  }
  
  /**
   * 获取生物主手中的物品.

   * @param entity 实体
   * @return
   */
  @SuppressWarnings("deprecation")
  public static ItemStack getItemInMainHand(LivingEntity entity) {
    if (ConfigManager.getBukkitVersion() >= 9) {
      return entity.getEquipment().getItemInMainHand();
    } else {
      return entity.getEquipment().getItemInHand();
    }
  }
}
