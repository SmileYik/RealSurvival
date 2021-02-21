package miskyle.realsurvival.machine.crafttable;

import org.bukkit.Location;
import miskyle.realsurvival.data.recipe.CraftTableRecipe;
import miskyle.realsurvival.machine.MachineHolder;
import miskyle.realsurvival.machine.MachineStatus;

public class CraftTableHolder extends MachineHolder {
  private String craftTableName;
  private MachineStatus status;

  private CraftTableRecipe recipe;
  private CraftTableTimer timer;

  /**
   * 初始化.

   * @param craftTableName 工作台(机器名)名
   * @param worldName 时间名
   * @param x 坐标x
   * @param y 坐标y
   * @param z 坐标z
   * @param status 机器状态
   */
  public CraftTableHolder(
      String craftTableName, String worldName, int x, int y, int z, MachineStatus status) {
    super(worldName, x, y, z);
    this.craftTableName = craftTableName;
    this.status = status;
  }

  /**
   * 初始化.

   * @param loc 坐标
   * @param craftTableName 工作台(机器)名
   * @param status 机器状态
   */
  public CraftTableHolder(Location loc, String craftTableName, MachineStatus status) {
    super(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    this.craftTableName = craftTableName;
    this.status = status;
  }

  /**
   * 初始化. 

   * @param loc 坐标
   * @param craftTableName 工作台(机器)名
   * @param timer 计时器
   */
  public CraftTableHolder(Location loc, String craftTableName, CraftTableTimer timer) {
    super(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    this.craftTableName = craftTableName;
    this.status = MachineStatus.CRAFTING;
    this.timer = timer;
  }

  /**
   * 初始化.

   * @param loc 位置
   * @param craftTableName 工作台(机器名) 
   * @param recipe 配方
   */
  public CraftTableHolder(Location loc, String craftTableName, CraftTableRecipe recipe) {
    super(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    this.craftTableName = craftTableName;
    this.status = MachineStatus.CREATOR;
    this.recipe = recipe;
  }

  public String getCraftTableName() {
    return craftTableName;
  }

  public void setCraftTableName(String craftTableName) {
    this.craftTableName = craftTableName;
  }

  public MachineStatus getStatus() {
    return status;
  }

  public void setStatus(MachineStatus status) {
    this.status = status;
  }

  public CraftTableRecipe getRecipe() {
    return recipe;
  }

  public void setRecipe(CraftTableRecipe recipe) {
    this.recipe = recipe;
  }

  public CraftTableTimer getTimer() {
    return timer;
  }

  public void setTimer(CraftTableTimer timer) {
    this.timer = timer;
  }

}
