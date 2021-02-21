package miskyle.realsurvival.machine.raincollector;

import org.bukkit.Location;
import miskyle.realsurvival.machine.MachineManager;
import miskyle.realsurvival.machine.MachineTimer;
import miskyle.realsurvival.machine.MachineType;

public class RainCollectorTimer extends MachineTimer {
  private static final int WATER_TIME = 60;

  public RainCollectorTimer(String playerName, Location loc) {
    super(MachineType.RAIN_COLLECTOR, playerName, loc);
  }

  public RainCollectorTimer(String playerName, int time, String worldName, int x, int y, int z) {
    super(playerName, MachineType.RAIN_COLLECTOR, time, worldName, x, y, z);
  }

  public int getWaterNumber() {
    return getTime() / WATER_TIME;
  }

  public boolean hasWater() {
    return getTime() > WATER_TIME;
  }

  public void getWater() {
    modifyTime(-WATER_TIME);
  }

  /**
   * 运行.
   */
  public void running() {
    if (getLocation().getBlock().isEmpty()) {
      MachineManager.removeTimer(this);
    }
    if (getWaterNumber() < 30) {
      modifyTime(1);
    }
  }

}
