package miskyle.realsurvival.status.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import miskyle.realsurvival.data.ConfigManager;
import miskyle.realsurvival.data.PlayerManager;

/**
 * 适配1.7.10

 * @author MiSkYle
 * @version 1.0.0
 */
public class ThirstListenerVer1 implements Listener {

  /**
   * 玩家从蓄水器里获取雨水.

   * @param e 玩家交互事件
   */
  @SuppressWarnings("deprecation")
  @EventHandler(ignoreCancelled = true)
  public void onPlayerGetWater(final PlayerInteractEvent e) {
    if (e.isCancelled()) {
      return;
    }
    if (!(e.getAction() == Action.RIGHT_CLICK_AIR 
        || e.getAction() == Action.RIGHT_CLICK_BLOCK)
        || !PlayerManager.isActive(e.getPlayer().getName()) 
        || e.getMaterial() != Material.GLASS_BOTTLE) {
      return;      
    }
    if (e.getItem().getAmount() > 1) {
      return;
    }
    
    if (e.getAction() == Action.RIGHT_CLICK_AIR) {
      e.setCancelled(true);
      return;
    }

    Block block = e.getPlayer().getTargetBlock(null, 6);
    if (!(block.getType().name().contains("WATER"))) {
      return;      
    }
    e.setCancelled(true);
    int amount = e.getItem().getAmount() - 1;
    if (amount > 0) {
      e.getItem().setAmount(amount);
    } else {
      e.getPlayer().setItemInHand(null);
    }
    String biome = block.getBiome().name().toUpperCase();
    ItemStack item = ConfigManager.getThirstConfig().getWater(biome).getItem();
    for (ItemStack is : e.getPlayer().getInventory().addItem(item).values()) {
      e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), is);      
    }
  }

}
