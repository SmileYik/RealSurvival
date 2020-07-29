package miskyle.realsurvival.listener.usehealthitem;

import org.bukkit.entity.Player;

import miskyle.realsurvival.data.item.RSItemData;
import miskyle.realsurvival.data.playerdata.PlayerData;

/**
 * 适配1.7.10-1.8.8
 * @author MiSkYle
 * @version 2.0.0
 */
public class UseHealthItemVer2 implements UseHealthItem{
  public void useHealthItem(Player p,PlayerData pd,RSItemData itemData) {
    double v;
    double max = p.getMaxHealth();
    if(itemData.isMaxHealth()) {
        v =p.getHealth()+max*itemData.getHealthValue()/100D;
    }else {
        v = p.getHealth()+itemData.getHealthValue();
    }
    if(v>max) {
        v = max;
    }else if(v<0) {
        p.damage(max, p);
    }
    p.setHealth(v);
  }
}