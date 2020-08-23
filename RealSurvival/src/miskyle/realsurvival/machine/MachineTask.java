package miskyle.realsurvival.machine;

import miskyle.realsurvival.machine.crafttable.CraftTableTimer;
import miskyle.realsurvival.machine.furnace.FurnaceTimer;
import miskyle.realsurvival.machine.raincollector.RainCollectorTimer;

public class MachineTask implements Runnable {

  @Override
  public void run() {
    MachineManager.getTimers().forEach(MachineManager.getTimers().mappingCount(), (k, v) -> {
      if (v.getType() == MachineType.RAIN_COLLECTOR) {
        ((RainCollectorTimer) v).running();
      } else if (v.getType() == MachineType.CRAFT_TABLE) {
        ((CraftTableTimer) v).running();
      } else if (v.getType() == MachineType.FURNACE) {
        ((FurnaceTimer) v).running();
      }
    });
  }

}
