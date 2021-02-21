package miskyle.realsurvival.listener.usehealthitem;

import org.bukkit.entity.Player;
import miskyle.realsurvival.data.item.RsItemData;
import miskyle.realsurvival.data.playerdata.PlayerData;

public interface UseHealthItem {
  public void useHealthItem(Player p, PlayerData pd, RsItemData itemData);
}
