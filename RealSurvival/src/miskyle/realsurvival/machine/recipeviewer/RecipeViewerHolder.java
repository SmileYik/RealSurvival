package miskyle.realsurvival.machine.recipeviewer;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class RecipeViewerHolder implements InventoryHolder {
  private Inventory inventory;
  
  @Override
  public Inventory getInventory() {
    // TODO Auto-generated method stub
    return inventory;
  }
  
  public void setInventory(Inventory inventory) {
    this.inventory = inventory;
  }
  
}
