package miskyle.realsurvival.machine.crafttable;

import org.bukkit.Location;
import miskyle.realsurvival.blockarray.BlockArrayCreator;
import miskyle.realsurvival.data.recipe.CraftTableRecipe;
import miskyle.realsurvival.machine.MachineManager;
import miskyle.realsurvival.machine.MachineTimer;
import miskyle.realsurvival.machine.MachineType;

public class CraftTableTimer extends MachineTimer {
  private CraftTableRecipe recipe;
  private int times = 1;

  public CraftTableTimer(String playerName, Location loc) {
    super(MachineType.CRAFT_TABLE, playerName, loc);
  }

  public CraftTableTimer(String playerName, Location loc, CraftTableRecipe recipe) {
    super(MachineType.CRAFT_TABLE, playerName, loc);
    this.recipe = recipe;
  }

  /**
   * 初始化.

   * @param playerName 玩家名
   * @param loc 路径
   * @param recipe 配方
   * @param times 计时器
   */
  public CraftTableTimer(String playerName, Location loc, CraftTableRecipe recipe, int times) {
    super(MachineType.CRAFT_TABLE, playerName, loc);
    this.recipe = recipe;
    this.times = times;
  }

  /**
   * 初始化.

   * @param playerName 玩家名
   * @param worldName 世界名
   * @param x 坐标x
   * @param y 坐标y
   * @param z 坐标z
   * @param recipe 配方
   * @param time 时间
   * @param times 次数
   */
  public CraftTableTimer(String playerName, 
      String worldName, int x, int y, int z, CraftTableRecipe recipe, int time,
      int times) {
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
    return getTime() >= recipe.getForgeTime();
  }

  /**
   * 拿取一个物品.

   * @return 返回剩余可取出次数,0代表获取完毕
   */
  public int takeIt() {
    modifyTime(-recipe.getForgeTime());
    times--;
    if (times == 0) {
      MachineManager.removeTimer(this);
    }
    return times;
  }

  /**
   * 运行.
   */
  public void running() {
    String nowBlock = BlockArrayCreator.getBlockKey(getLocation().getBlock());
    String aimBlock = MachineManager.getMachineCube(recipe.getMachineName()).getMid().getMain();

    if (getLocation().getBlock().isEmpty() || !nowBlock.equalsIgnoreCase(aimBlock)) {
      MachineManager.removeTimer(this);
    } else {
      if (getTime() < times * recipe.getForgeTime()) {
        modifyTime(1);
      }
    }
  }
}
