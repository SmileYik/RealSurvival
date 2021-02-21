package miskyle.realsurvival.machine.recipeviewer;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import miskyle.realsurvival.data.recipe.Recipe;

public class RecipeAlbumHolder implements InventoryHolder {
  private boolean menu = false;
  private Recipe[] viewers;
  private Recipe now;
  
  private Inventory inventory;
  
  @Override
  public Inventory getInventory() {
    return inventory;
  }
  
  public void setInventory(Inventory inventory) {
    this.inventory = inventory;
  }

  public boolean isMenu() {
    return menu;
  }

  public void setMenu(boolean menu) {
    this.menu = menu;
  }

  public Recipe[] getViewers() {
    return viewers;
  }

  public void setViewers(Recipe[] viewers) {
    this.viewers = viewers;
  }

  public Recipe getNow() {
    return now;
  }

  public void setNow(Recipe now) {
    this.now = now;
  }
  
  
}
