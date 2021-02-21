package miskyle.realsurvival.machine;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class MachineHolder implements InventoryHolder {
  private Inventory inv;
  private String worldName;
  private int locX;
  private int locY;
  private int locZ;

  /**
   * 初始化.

   * @param worldName 世界名
   * @param x 坐标x
   * @param y 坐标y
   * @param z 坐标z
   */
  public MachineHolder(String worldName, int x, int y, int z) {
    super();
    this.worldName = worldName;
    this.locX = x;
    this.locY = y;
    this.locZ = z;
  }

  public String getWorldName() {
    return worldName;
  }

  public void setWorldName(String worldName) {
    this.worldName = worldName;
  }

  public int getX() {
    return locX;
  }

  public void setX(int x) {
    this.locX = x;
  }

  public int getY() {
    return locY;
  }

  public void setY(int y) {
    this.locY = y;
  }

  public int getZ() {
    return locZ;
  }

  public void setZ(int z) {
    this.locZ = z;
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
