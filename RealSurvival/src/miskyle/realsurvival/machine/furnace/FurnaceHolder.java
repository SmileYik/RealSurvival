package miskyle.realsurvival.machine.furnace;

import org.bukkit.Location;
import miskyle.realsurvival.data.recipe.FurnaceRecipe;
import miskyle.realsurvival.machine.MachineHolder;
import miskyle.realsurvival.machine.MachineStatus;

public class FurnaceHolder extends MachineHolder {
  private String furnaceName;
  private MachineStatus status;

  private FurnaceTimer timer;
  private FurnaceRecipe recipe;

  /**
   * 初始化.

   * @param worldName 世界名
   * @param x 坐标x
   * @param y 坐标y
   * @param z 坐标z
   * @param furnaceName 熔炉类工作台(机器)名称
   * @param status 机器状态.
   */
  public FurnaceHolder(String worldName,
      int x, int y, int z, String furnaceName, MachineStatus status) {
    super(worldName, x, y, z);
    this.furnaceName = furnaceName;
    this.status = status;
  }

  /**
   * 初始化.

   * @param loc 位置
   * @param craftTableName 熔炉类工作台(机器)名称
   * @param status 机器状态
   */
  public FurnaceHolder(Location loc, String craftTableName, MachineStatus status) {
    super(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    this.furnaceName = craftTableName;
    this.status = status;
  }

  /**
   * 初始化.

   * @param loc 位置 
   * @param craftTableName 熔炉类工作台(机器)名称
   * @param timer 对应的timer.
   */
  public FurnaceHolder(Location loc, String craftTableName, FurnaceTimer timer) {
    super(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    this.furnaceName = craftTableName;
    this.status = MachineStatus.CRAFTING;
    this.timer = timer;
  }

  /**
   * 初始化.

   * @param loc 位置 
   * @param craftTableName 熔炉类工作台(机器)名称
   * @param recipe 配方
   */
  public FurnaceHolder(Location loc, String craftTableName, FurnaceRecipe recipe) {
    super(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    this.furnaceName = craftTableName;
    this.status = MachineStatus.CREATOR;
    this.recipe = recipe;
  }

  public String getFurnaceName() {
    return furnaceName;
  }

  public void setFurnaceName(String furnaceName) {
    this.furnaceName = furnaceName;
  }

  public MachineStatus getStatus() {
    return status;
  }

  public void setStatus(MachineStatus status) {
    this.status = status;
  }

  public FurnaceTimer getTimer() {
    return timer;
  }

  public void setTimer(FurnaceTimer timer) {
    this.timer = timer;
  }

  public FurnaceRecipe getRecipe() {
    return recipe;
  }

  public void setRecipe(FurnaceRecipe recipe) {
    this.recipe = recipe;
  }

}
