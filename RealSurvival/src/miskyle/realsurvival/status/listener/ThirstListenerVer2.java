package miskyle.realsurvival.status.listener;

import java.util.Set;

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
 * 适配1.8及以上
 * 
 * @author MiSkYle
 * @version 2.0.0
 */
public class ThirstListenerVer2 implements Listener {

  @EventHandler
  public void onPlayerGetWater(final PlayerInteractEvent e) {
    if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
        || !PlayerManager.isActive(e.getPlayer().getName()) || e.getMaterial() != Material.GLASS_BOTTLE)
      return;

    Block block = e.getPlayer().getTargetBlock((Set<Material>) null, 6);
    if (!(block.getType().name().contains("WATER")))
      return;
    e.setCancelled(true);

    e.getItem().setAmount(e.getItem().getAmount() - 1);
    String biome = block.getBiome().name().toUpperCase();
    ItemStack item = ConfigManager.getThirstConfig().getWater(biome).getItem();
    for (ItemStack is : e.getPlayer().getInventory().addItem(item).values())
      e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), is);
  }

}
