package miskyle.realsurvival.listener.usehealthitem;

import org.bukkit.entity.Player;

import miskyle.realsurvival.data.item.RSItemData;
import miskyle.realsurvival.data.playerdata.PlayerData;

public interface UseHealthItem {
  public void  useHealthItem(Player p,PlayerData pd,RSItemData itemData);
}
 