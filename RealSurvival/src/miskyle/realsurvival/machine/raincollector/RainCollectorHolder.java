package miskyle.realsurvival.machine.raincollector;

import org.bukkit.Location;

import miskyle.realsurvival.machine.MachineHolder;

public class RainCollectorHolder extends MachineHolder {

  public RainCollectorHolder(String worldName, int x, int y, int z) {
    super(worldName, x, y, z);
  }

  public RainCollectorHolder(Location loc) {
    super(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
  }

}
