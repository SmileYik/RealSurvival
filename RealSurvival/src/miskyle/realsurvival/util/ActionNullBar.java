package miskyle.realsurvival.util;

import org.bukkit.entity.Player;
import com.github.miskyle.mcpt.nms.actionbar.NMSActionBar;

public class ActionNullBar implements NMSActionBar {

  @Override
  public void sendActionBar(Player arg0, String arg1) {
    arg0.sendMessage(arg1);
  }

}
