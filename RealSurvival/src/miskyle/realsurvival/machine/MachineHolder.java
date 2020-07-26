package miskyle.realsurvival.machine;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class MachineHolder implements InventoryHolder{
  private Inventory inv;
  private String worldName;
  private int x;
  private int y;
  private int z;

  public MachineHolder(String worldName, int x, int y, int z) {
    super();
    this.worldName = worldName;
    this.x = x;
    this.y = y;
    this.z = z;
  }
  public String getWorldName() {
    return worldName;
  }
  public void setWorldName(String worldName) {
    this.worldName = worldName;
  }
  public int getX() {
    return x;
  }
  public void setX(int x) {
    this.x = x;
  }
  public int getY() {
    return y;
  }
  public void setY(int y) {
    this.y = y;
  }
  public int getZ() {
    return z;
  }
  public void setZ(int z) {
    this.z = z;
  }
  public Inventory getInv() {
    return inv;
  }
  public void setInv(Inventory inv) {
    this.inv = inv;
  }
  @Override
  public Inventory getInventory() {
    return inv;
  }
}
