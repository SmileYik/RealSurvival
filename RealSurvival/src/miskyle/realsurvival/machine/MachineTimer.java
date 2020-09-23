package miskyle.realsurvival.machine;

import org.bukkit.Location;

import com.github.miskyle.mcpt.MCPT;

public class MachineTimer {
  private String playerName;
  private MachineType type;
  private int time;

  private String worldName;
  private int x;
  private int y;
  private int z;

  public MachineTimer(String playerName, 
      MachineType type, int time, String worldName, int x, int y, int z) {
    super();
    this.playerName = playerName;
    this.type = type;
    this.time = time;
    this.worldName = worldName;
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public MachineTimer(MachineType type, String playerName, Location loc) {
    this.playerName = playerName;
    this.type = type;
    this.worldName = loc.getWorld().getName();
    this.x = loc.getBlockX();
    this.y = loc.getBlockY();
    this.z = loc.getBlockZ();
  }

  public Location getLocation() {
    return new Location(MCPT.plugin.getServer().getWorld(worldName), x, y, z);
  }

  public int modifyTime(int time) {
    this.time += time;
    return this.time;
  }

  public String getPlayerName() {
    return playerName;
  }

  public void setPlayerName(String playerName) {
    this.playerName = playerName;
  }

  public MachineType getType() {
    return type;
  }

  public void setType(MachineType type) {
    this.type = type;
  }

  public int getTime() {
    return time;
  }

  public void setTime(int time) {
    this.time = time;
  }

  public String getWorldName() {
    return worldName;
  }

  public void setWorldName(String worldName) {
    this.worldName = worldName;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getZ() {
    return z;
  }

  public void setZ(int z) {
    this.z = z;
  }

}
