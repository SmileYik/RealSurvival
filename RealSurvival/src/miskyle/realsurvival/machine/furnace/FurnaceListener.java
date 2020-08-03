package miskyle.realsurvival.machine.furnace;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import com.github.miskyle.mcpt.i18n.I18N;

import miskyle.realsurvival.Msg;
import miskyle.realsurvival.machine.MachineStatus;
import miskyle.realsurvival.machine.MachineType;
import miskyle.realsurvival.machine.util.RecipeUtils;
import miskyle.realsurvival.util.RSEntry;

public class FurnaceListener implements Listener{
  @EventHandler
  public void onCrafting(final InventoryClickEvent e) {
    if(e.isCancelled()) {
      return;
    }
    if(!(e.getWhoClicked() instanceof Player)
        || !(e.getInventory().getHolder() instanceof FurnaceHolder)
        || e.getRawSlot()<0) {
      return;
    }
    
    Player p = (Player)e.getWhoClicked();
    FurnaceHolder holder = (FurnaceHolder) e.getInventory().getHolder();
    if(holder.getStatus() == MachineStatus.CRAFTING) {
      if(e.getRawSlot() < 54) {
        e.setCancelled(true);
        p.updateInventory();
      }
      if(e.getRawSlot() == 49) {
        if(holder.getTimer().isValid()) {
          int times = holder.getTimer().takeIt();
          holder.getTimer().getRecipe().getProducts().forEach(item->{
            p.getInventory().addItem(item.clone()).values().forEach(item2->{
              p.getWorld().dropItem(p.getLocation(), item2);
            });
          });
          if(times>0) {
            p.sendMessage(Msg.getPrefix()+I18N.tr("machine.craft-table.crafting.amount-left",times));
            return;
          }else {
            p.closeInventory();
          }
        }else {
          p.sendMessage(Msg.getPrefix()+I18N.tr("machine.craft-table.crafting.not-yet"));
          return;
        }
      }else {
        return;
      }
    }else if(holder.getStatus() == MachineStatus.NOTHING) {
      if(e.getRawSlot() >= 54 || Furnace.MATERIAL_SLOTS.contains(e.getRawSlot())) {
        return;
      }else if(e.getRawSlot()<=54) {
        e.setCancelled(true);
        p.updateInventory();
        if(e.getRawSlot() == 49) {
          RSEntry<String, Integer> recipe = RecipeUtils.cheekRecipe(
              holder.getFurnaceName(), MachineType.FURNACE, e.getClickedInventory());
          if(recipe != null) {
            RecipeUtils.startForgeRecipe(holder.getFurnaceName(), 
                recipe.getLeft(), MachineType.FURNACE,recipe.getRight(), 
                e.getClickedInventory(), p, holder.getTimer());
            return;
          }else {
            return;
          }
        }else {
          return;
        }
      }
    }else if(holder.getStatus() == MachineStatus.CREATOR) {
      if(e.getRawSlot() == 49) {
        //创建配方
        if(RecipeUtils.createRecipe(holder.getRecipe(), p, e.getClickedInventory())) {
          p.closeInventory();
          return;
        }
      }
    }
  }
  
  @EventHandler
  public void onCloseInv(final InventoryCloseEvent e) {
    if(e.getInventory().getHolder() instanceof FurnaceHolder) {
      FurnaceHolder holder = (FurnaceHolder) e.getInventory().getHolder();
      FurnaceOpenEvent.cancelEvent(e.getPlayer().getName());
      if(holder.getStatus() == MachineStatus.NOTHING) {
        for(int i:Furnace.MATERIAL_SLOTS) {
          ItemStack item = e.getInventory().getItem(i);
          if(item!=null) {
            giveItemToPlayer((Player)e.getPlayer(), item);
          }
        }
      }else if(holder.getStatus() == MachineStatus.CREATOR) {
        for(int i:Furnace.MATERIAL_SLOTS) {
          ItemStack item = e.getInventory().getItem(i);
          if(item!=null) {
            giveItemToPlayer((Player)e.getPlayer(), item);
          }
        }
        for(int i:Furnace.PRODUCT_SLOTS) {
          ItemStack item = e.getInventory().getItem(i);
          if(item!=null) {
            giveItemToPlayer((Player)e.getPlayer(), item);
          }
        }
      }
    }
  }
  
  
  private void giveItemToPlayer(Player p,ItemStack item) {
    p.getInventory().addItem(item).values().forEach(i->{
      p.getWorld().dropItem(p.getLocation(), i);
    });
  }
}
