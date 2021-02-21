package miskyle.realsurvival.status.listener;

import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.ItemStack;
import miskyle.realsurvival.api.status.StatusType;
import miskyle.realsurvival.data.ConfigManager;
import miskyle.realsurvival.data.PlayerManager;
import miskyle.realsurvival.data.config.status.EnergyBreakBlockData;
import miskyle.realsurvival.data.playerdata.PlayerData;
import miskyle.realsurvival.status.task.EnergyTask;
import miskyle.realsurvival.util.ItemUtil;

public class EnergyListener implements Listener {
  private HashMap<String, Location> jumpCheck = new HashMap<>();

  /**
   * 玩家移动事件.

   * @param e 玩家移动事件.
   */
  @EventHandler
  public void playerMove(PlayerMoveEvent e) {
    if (!PlayerManager.isActive(e.getPlayer().getName())) {
      return;      
    }
    PlayerData pd = PlayerManager.getPlayerData(e.getPlayer().getName());
    if (ConfigManager.getEnergyConfig().getDecreaseSwimming() != 0
        && e.getFrom().getBlock().getType().name().contains("WATER")) {
      // Swimming
      if (e.getFrom().getBlockX() != e.getTo().getBlockX() 
          || e.getFrom().getBlockZ() != e.getTo().getBlockZ()
          || e.getFrom().getBlockY() < e.getTo().getBlockY()) {
        if (pd.getEnergy().getValue() < ConfigManager.getEnergyConfig().getDecreaseSwimming()) {
          e.setCancelled(true);
        } else {
          EnergyTask.sendMessage(e.getPlayer(), pd.getEnergy().modify(
              -ConfigManager.getEnergyConfig().getDecreaseSwimming(), 
              pd.getEffect().getValue(StatusType.ENERGY)));
        }
      }
    } else if (ConfigManager.getEnergyConfig().getDecreaseJumping() != 0
        && isJump(e.getPlayer().getName(), e.getFrom(), e.getTo())) {
      if (pd.getEnergy().getValue() < ConfigManager.getEnergyConfig().getDecreaseJumping()
          || pd.getWeight().getValue() > pd.getWeight().getMaxValue()) {
        e.setCancelled(true);
        e.getPlayer().teleport(jumpCheck.remove(e.getPlayer().getName()));
      } else {
        EnergyTask.sendMessage(e.getPlayer(), pd.getEnergy()
            .modify(-ConfigManager.getEnergyConfig().getDecreaseJumping(), 
                pd.getEffect().getValue(StatusType.ENERGY)));
      }
      //jumpCheck.remove(e.getPlayer().getName());
    }
  }

  /**
   * 玩家破坏方块事件.

   * @param e 玩家破坏方块事件.
   */
  @EventHandler
  public void onbreakBlock(final BlockBreakEvent e) {
    if (!PlayerManager.isActive(e.getPlayer().getName())) {
      return;      
    }
    PlayerData pd = PlayerManager.getPlayerData(e.getPlayer().getName());
    /*
     * TODO 1.8.8 无双持
     */
    ItemStack item = ItemUtil.getItemInMainHand(e.getPlayer());
    EnergyBreakBlockData data = new EnergyBreakBlockData(e.getBlock().getType().name(),
        ConfigManager.getEnergyConfig().getToolList().contains(item.getType().name()) 
        ? item.getType().name() : "AIR");
    Double value = ConfigManager.getEnergyConfig().getActionDecrease().get(data);
    if (value != null) {
      pd.modifyWithEffect(StatusType.ENERGY, -value);
    }
  }

  /**
   * 玩家疾走.

   * @param e 玩家疾走事件.
   */
  @EventHandler
  public void onSprint(final PlayerToggleSprintEvent e) {
    if (!PlayerManager.isActive(e.getPlayer().getName())) {
      return;      
    }
    PlayerData pd = PlayerManager.getPlayerData(e.getPlayer().getName());
    if (pd.getEnergy().getValue() < ConfigManager.getEnergyConfig().getDecreaseSprinting()) {
      e.setCancelled(true);
      e.getPlayer().setSprinting(false);
    }
  }

  /**
   * 玩家潜行.

   * @param e 玩家潜行事件.
   */
  @EventHandler
  public void onSneak(final PlayerToggleSneakEvent e) {
    if (!PlayerManager.isActive(e.getPlayer().getName())) {
      return;      
    }
    PlayerData pd = PlayerManager.getPlayerData(e.getPlayer().getName());
    if (pd.getEnergy().getValue() < ConfigManager.getEnergyConfig().getDecreaseSneaking()) {
      e.setCancelled(true);
      e.getPlayer().setSneaking(false);
    }
  }

  private boolean isJump(String playerName, Location from, Location to) {
    if (from.getY() > to.getY()) { // jump down
      if (jumpCheck.containsKey(playerName)) {
        return true;
      }
      // System.out.println("JumpDown Y:" + from.getY()+", BlockY:
      // "+from.getBlockY()+"; Y:" + to.getY()+", BlockY: "+to.getBlockY()+"; ");
    } else if (from.getY() < to.getY()) { // jump up
      if (jumpCheck.containsKey(playerName)) {
        return false;        
      }
      jumpCheck.put(playerName, from);
      // System.out.println("JumpUP Y:" + from.getY()+", BlockY: "+from.getBlockY()+";
      // Y:" + to.getY()+", BlockY: "+to.getBlockY()+"; ");
    } else if (from.getX() != to.getX() || from.getZ() != to.getZ()) { // walk
      jumpCheck.remove(playerName);
    }
    return false;
  }
}
