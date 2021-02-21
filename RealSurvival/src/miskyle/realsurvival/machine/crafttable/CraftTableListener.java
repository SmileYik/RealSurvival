package miskyle.realsurvival.machine.crafttable;

import com.github.miskyle.mcpt.i18n.I18N;
import miskyle.realsurvival.Msg;
import miskyle.realsurvival.machine.MachinePermission;
import miskyle.realsurvival.machine.MachineStatus;
import miskyle.realsurvival.machine.MachineType;
import miskyle.realsurvival.machine.util.RecipeUtils;
import miskyle.realsurvival.util.RsEntry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class CraftTableListener implements Listener {
  
  /**
   * 对有效的工作台界面点击进行处理.

   * @param e 仓库点击事件
   */
  @EventHandler
  public void onCrafting(final InventoryClickEvent e) {
    if (e.isCancelled()) {
      return;
    }
    if (!(e.getWhoClicked() instanceof Player) 
        || !(e.getInventory().getHolder() instanceof CraftTableHolder)
        || e.getRawSlot() < 0) {
      return;
    }

    Player p = (Player) e.getWhoClicked();
    CraftTableHolder holder = (CraftTableHolder) e.getInventory().getHolder();
    if (holder.getStatus() == MachineStatus.CRAFTING) {
      if (e.getRawSlot() < 54) {
        e.setCancelled(true);
        p.updateInventory();
      }
      if (e.getRawSlot() == 49) {
        if (holder.getTimer().isValid()) {
          int times = holder.getTimer().takeIt();
          holder.getTimer().getRecipe().getProducts().forEach(item -> {
            p.getInventory().addItem(item.clone()).values().forEach(item2 -> {
              p.getWorld().dropItem(p.getLocation(), item2);
            });
          });
          if (times > 0) {
            p.sendMessage(
                Msg.getPrefix() + I18N.tr("machine.craft-table.crafting.amount-left", times));
            return;
          } else {
            p.closeInventory();
          }
        } else {
          p.sendMessage(Msg.getPrefix() + I18N.tr("machine.craft-table.crafting.not-yet"));
          return;
        }
      } else {
        return;
      }
    } else if (holder.getStatus() == MachineStatus.NOTHING) {
      if (e.getRawSlot() >= 54 || CraftTable.materials.contains(e.getRawSlot())) {
        return;
      } else if (e.getRawSlot() <= 54) {
        e.setCancelled(true);
        p.updateInventory();
        if (e.getRawSlot() == 49) {
          RsEntry<String, Integer> recipe = 
              RecipeUtils.cheekRecipe(holder.getCraftTableName(), MachineType.CRAFT_TABLE,
              e.getClickedInventory());
          //判断是否为有效配方, 若有效则进一步判断玩家是否有相应权限.
          if (recipe != null
              && MachinePermission.checkPermission(
                  p, holder.getCraftTableName(), recipe.getLeft())) {
            RecipeUtils.startForgeRecipe(
                holder.getCraftTableName(), recipe.getLeft(), MachineType.CRAFT_TABLE,
                recipe.getRight(), e.getClickedInventory(), p, holder.getTimer());
            return;
          } else {
            return;
          }
        } else {
          return;
        }
      }
    } else if (holder.getStatus() == MachineStatus.CREATOR) {
      if (e.getRawSlot() == 49) {
        // 创建配方
        if (RecipeUtils.createRecipe(holder.getRecipe(), p, e.getClickedInventory())) {
          p.closeInventory();
          return;
        }
      }
    }
  }

  /**
   * 当玩家关闭一个有效的工作台配方制作窗口时执行此指令.

   * @param e 仓库关闭事件
   */
  @EventHandler
  public void onCloseInv(final InventoryCloseEvent e) {
    if (e.getInventory().getHolder() instanceof CraftTableHolder) {
      CraftTableHolder holder = (CraftTableHolder) e.getInventory().getHolder();
      CraftTableOpenEvent.cancelEvent(e.getPlayer().getName());
      if (holder.getStatus() == MachineStatus.NOTHING) {
        for (int i : CraftTable.materials) {
          ItemStack item = e.getInventory().getItem(i);
          if (item != null) {
            giveItemToPlayer((Player) e.getPlayer(), item);
          }
        }
      } else if (holder.getStatus() == MachineStatus.CREATOR) {
        for (int i : CraftTable.materials) {
          ItemStack item = e.getInventory().getItem(i);
          if (item != null) {
            giveItemToPlayer((Player) e.getPlayer(), item);
          }
        }
        for (int i : CraftTable.products) {
          ItemStack item = e.getInventory().getItem(i);
          if (item != null) {
            giveItemToPlayer((Player) e.getPlayer(), item);
          }
        }
      }
    }
  }

  private void giveItemToPlayer(Player p, ItemStack item) {
    p.getInventory().addItem(item).values().forEach(i -> {
      p.getWorld().dropItem(p.getLocation(), i);
    });
  }
}
