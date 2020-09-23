package miskyle.realsurvival.blockarray;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.github.miskyle.mcpt.i18n.I18N;

import miskyle.realsurvival.Msg;

public class BlockArrayCreatorListener implements Listener {
  private String playerName;

  public BlockArrayCreatorListener(String playerName) {
    this.playerName = playerName;
  }

  /**
   * 选择一个方块.
   * 仅当玩家处于创建多方块结构模式时生效.
   * @param event 玩家交互事件
   */
  @EventHandler
  public void chooseBlock(PlayerInteractEvent event) {
    if (event.getPlayer().getName().equalsIgnoreCase(playerName) && event.hasBlock()) {
      event.setCancelled(true);
      BlockArrayCreator.chooesBlock(playerName, event.getClickedBlock().getLocation());
      event.getPlayer().sendMessage(Msg.getPrefix() 
          + I18N.tr("cube-array.create.info.choose-block-finish",
          event.getClickedBlock().getX(), 
          event.getClickedBlock().getY(),
          event.getClickedBlock().getZ()));
      event.getHandlers().unregister(this);
    }
  }
}
