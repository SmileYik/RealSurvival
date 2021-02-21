package miskyle.realsurvival.machine.recipeviewer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import miskyle.realsurvival.data.recipe.Recipe;

public class RecipeViewerListener implements Listener {
  
  /**
   * 玩家在查看配方界面点击事件.

   * @param event 玩家点击事件.
   */
  @EventHandler
  public void onPlayerClick(InventoryClickEvent event) {
    if (event.isCancelled()) {
      return;
    }
    
    if (event.getClickedInventory() == null 
        || event.getClickedInventory().getHolder() == null
        || !(event.getClickedInventory().getHolder() instanceof RecipeAlbumHolder)) {
      return;
    }
    event.setCancelled(true);
    
    RecipeAlbumHolder holder = (RecipeAlbumHolder) event.getClickedInventory().getHolder();
    if (holder.isMenu()) {
      Player p = (Player) event.getWhoClicked();
      if (RecipeAlbum.SLOTS.contains(event.getRawSlot())) {
        Recipe recipe = holder.getViewers()[RecipeAlbum.SLOTS.indexOf(event.getRawSlot())];
        RecipeAlbum.openRecipeViewer(p, recipe, holder);
        return;
      }
    } else {
      if (event.getClick() == ClickType.RIGHT && holder.getNow() != null) {
        RecipeAlbum.openMachineBuildHelper(
            (Player) event.getWhoClicked(), holder.getNow().getMachineName(), holder);
        return;
      }
    }
  }
  
  /**
   * 玩家关闭仓库事件.

   * @param event 玩家关闭仓库事件
   */
  @EventHandler
  public void onPlayerCloseInv(InventoryCloseEvent event) {
    if (event.getInventory() == null 
        || !(event.getInventory().getHolder() instanceof RecipeAlbumHolder)) {
      return;
    }
    if (event.getInventory().getHolder() instanceof RecipeViewerHolder) {
      return;
    }
    RecipeAlbumHolder holder = (RecipeAlbumHolder) event.getInventory().getHolder();
    if (!holder.isMenu()) {
      holder.setMenu(true);
      event.getPlayer().openInventory(holder.getInventory());
    }
  }

}
