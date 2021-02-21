package miskyle.realsurvival.listener.usehealthitem;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import miskyle.realsurvival.data.item.RsItemData;
import miskyle.realsurvival.data.playerdata.PlayerData;

/**
 * 适配1.9+

 * @author MiSkYle
 * @version 3.0.0
 */
public class UseHealthItemVer3 implements UseHealthItem {
  /**
   * 使用生命值的物品.
   */
  public void useHealthItem(Player p, PlayerData pd, RsItemData itemData) {
    double v;
    double max = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
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
    p.setHealth(v);
  }
}
