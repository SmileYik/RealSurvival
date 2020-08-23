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

  public FurnaceHolder(String worldName, int x, int y, int z, String furnaceName, MachineStatus status) {
    super(worldName, x, y, z);
    this.furnaceName = furnaceName;
    this.status = status;
  }

  public FurnaceHolder(Location loc, String craftTableName, MachineStatus status) {
    super(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    this.furnaceName = craftTableName;
    this.status = status;
  }

  public FurnaceHolder(Location loc, String craftTableName, FurnaceTimer timer) {
    super(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    this.furnaceName = craftTableName;
    this.status = MachineStatus.CRAFTING;
    this.timer = timer;
  }

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
