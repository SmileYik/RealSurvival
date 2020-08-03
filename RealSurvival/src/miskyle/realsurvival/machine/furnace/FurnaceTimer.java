package miskyle.realsurvival.machine.furnace;

import org.bukkit.Location;

import miskyle.realsurvival.blockarray.BlockArrayCreator;
import miskyle.realsurvival.data.recipe.FurnaceRecipe;
import miskyle.realsurvival.machine.MachineManager;
import miskyle.realsurvival.machine.MachineTimer;
import miskyle.realsurvival.machine.MachineType;
import miskyle.realsurvival.randomday.RandomDayManager;

public class FurnaceTimer extends MachineTimer{
  private FurnaceRecipe recipe;
  private int times = 1;
  private double extraTemperature = 0;
  private double waitTime = 0;
  public FurnaceTimer(String playerName, Location loc) {
    super(MachineType.FURNACE, playerName, loc);
  }
  
  public FurnaceTimer(String playerName, Location loc, FurnaceRecipe recipe) {
    super(MachineType.FURNACE, playerName, loc);
    this.recipe = recipe;
  }
  
  public FurnaceTimer(String playerName, Location loc, FurnaceRecipe recipe, int times) {
    super(MachineType.FURNACE, playerName, loc);
    this.recipe = recipe;
    this.times = times;
  }

  public FurnaceTimer(String playerName, String worldName, int x, int y, int z,
      FurnaceRecipe recipe, int time, int times, double extraTemperature) {
    super(playerName, MachineType.FURNACE, time, worldName, x, y, z);
    this.recipe = recipe;
    this.times = times;
    this.extraTemperature = extraTemperature;
  }

  public FurnaceRecipe getRecipe() {
    return recipe;
  }

  public void setRecipe(FurnaceRecipe recipe) {
    this.recipe = recipe;
  }

  public int getTimes() {
    return times;
  }

  public void setTimes(int times) {
    this.times = times;
  }

  public double getExtraTemperature() {
    return extraTemperature;
  }

  public void setExtraTemperature(double extraTemperature) {
    this.extraTemperature = extraTemperature;
  }
  
  public boolean isValid() {
    return getTime()>=recipe.getForgeTime();
  }
  
  public double getBaseTemperature() {
    return RandomDayManager.getTemperature(getLocation());
  }
  
  public double getTemperature() {
    return extraTemperature + getBaseTemperature();
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
      if(getTime() >= times*recipe.getForgeTime()) {
        return;
      }
      
      double temperature = getTemperature();
      
      if (recipe.getMinTemperature() >= 0) {
        if (temperature > recipe.getMaxTemperature()) {
          waitTime++;
          if(waitTime >= 10) {
            takeIt();
          }
          return;
        } else if (temperature >= recipe.getMinTemperature()) {
          waitTime = 0;
          modifyTime(1);
        }
      } else if ( temperature <= recipe.getMinTemperature() 
          && temperature > recipe.getMaxTemperature()){
        modifyTime(1);
      }
    }
  }
}
