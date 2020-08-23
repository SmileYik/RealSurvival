package miskyle.realsurvival.machine.waterpurifier;

import org.bukkit.block.BlockFace;
import org.bukkit.block.Dropper;
import org.bukkit.block.Hopper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import miskyle.realsurvival.data.ItemManager;
import miskyle.realsurvival.data.PlayerManager;

public class WaterPurifierListener implements Listener {
  @EventHandler
  public void playerWantUsePurifier(final PlayerInteractEvent e) {
    if (!PlayerManager.isActive(e.getPlayer().getName()) || e.getAction() != Action.RIGHT_CLICK_BLOCK || !e.hasItem()
        || !e.getItem().getType().name().equals("GLASS_BOTTLE") || !(e.getClickedBlock().getState() instanceof Hopper)
        || !(e.getClickedBlock().getRelative(BlockFace.UP).getState() instanceof Dropper)) {
      return;
    }
    // 获取第一个有效过滤物品.
    Inventory purifierItemInv = ((Hopper) e.getClickedBlock().getState()).getInventory();
    ItemStack purifierItem = null;
    double purifierValue = 0;
    for (ItemStack item : purifierItemInv.getContents()) {
      double temp = ItemManager.getPurifierValue(item);
      if (temp == 0) {
        continue;
      } else {
        purifierItem = item;
        purifierValue = temp;
        break;
      }
    }
    if (purifierItem == null || purifierValue == 0) {
      return;
    }

    // 获取第一个有效需要过滤物品
    Inventory inv = ((Dropper) e.getClickedBlock().getRelative(BlockFace.UP).getState()).getInventory();

  }
}
