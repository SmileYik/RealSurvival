package miskyle.realsurvival.listener.usehealthitem;

import org.bukkit.entity.Player;
import miskyle.realsurvival.data.item.RsItemData;
import miskyle.realsurvival.data.playerdata.PlayerData;

/**
 * 适配1.7.10以下(不包括1.7.10).

 * @author MiSkYle
 * @version 1.0.0
 */
public class UseHealthItemVer1 implements UseHealthItem {
  /**
   * 使用影响生命值的物品.
   */
  public void useHealthItem(Player p, PlayerData pd, RsItemData itemData) {
    double v;
    /*
     * TODO 1.8.8下 获取生命值最大值有问题
     */
    int max = p.getMaxHealth();
    if (itemData.isMaxHealth()) {
      v = p.getHealth() + max * itemData.getHealthValue() / 100D;
    } else {
      v = p.getHealth() + itemData.getHealthValue();
    }
    if (v > max) {
      v = max;
    } else if (v < 0) {
      p.damage(max, p);
    }
    p.setHealth((int) v);
  }
}
