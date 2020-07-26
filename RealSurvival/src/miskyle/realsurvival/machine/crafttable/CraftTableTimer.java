package miskyle.realsurvival.machine.crafttable;

import org.bukkit.Location;

import miskyle.realsurvival.blockarray.BlockArrayCreator;
import miskyle.realsurvival.data.recipe.CraftTableRecipe;
import miskyle.realsurvival.machine.MachineManager;
import miskyle.realsurvival.machine.MachineTimer;
import miskyle.realsurvival.machine.MachineType;

public class CraftTableTimer extends MachineTimer{
  private CraftTableRecipe recipe;
  private int times = 1;
  
  public CraftTableTimer(String playerName, Location loc) {
    super(MachineType.CRAFT_TABLE, playerName, loc);
  }
  public CraftTableTimer(String playerName, Location loc,CraftTableRecipe recipe) {
    super(MachineType.CRAFT_TABLE, playerName, loc);
    this.recipe = recipe;
  }
  public CraftTableTimer(String playerName, Location loc,CraftTableRecipe recipe, int times) {
    super(MachineType.CRAFT_TABLE, playerName, loc);
    this.recipe = recipe;
    this.times = times;
  }
  public CraftTableTimer(String playerName, String worldName,int x,int y,int z,
      CraftTableRecipe recipe, int time, int times) {
    super(playerName, MachineType.CRAFT_TABLE, time, worldName, x, y, z);
    this.recipe = recipe;
    this.times = times;
  }
  public CraftTableRecipe getRecipe() {
    return recipe;
  }
  public void setRecipe(CraftTableRecipe recipe) {
    this.recipe = recipe;
  }
  public int getTimes() {
    return times;
  }
  public void setTimes(int times) {
    this.times = times;
  }
  public boolean isValid() {
    return getTime()>=recipe.getForgeTime();
  }
  /**
   * 拿取一个物品
   * @return 返回剩余可取出次数,0代表获取完毕
   */
  public int takeIt() {
    modifyTime(-recipe.getForgeTime());
    times --;
    if(times == 0) {
      MachineManager.removeTimer(this);
    }
    return times;
  }
  public void running() {
    String nowBlock = BlockArrayCreator.getBlockKey(getLocation().getBlock());
    String aimBlock = MachineManager.getMachineCube(
        recipe.getMachineName()).getMid().getMain();
    
    if(getLocation().getBlock().isEmpty()
        || !nowBlock.equalsIgnoreCase(aimBlock)) {
      MachineManager.removeTimer(this);
    }else {
      if(getTime()<times*recipe.getForgeTime()){
        modifyTime(1);
      }
    }
  }
}
