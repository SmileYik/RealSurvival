package miskyle.realsurvival.machine;

import org.bukkit.Location;
import com.github.miskyle.mcpt.MCPT;

public class MachineTimer {
  private String playerName;
  private MachineType type;
  private int time;

  private String worldName;
  private int locX;
  private int locY;
  private int locZ;

  /**
   * 初始化机器Timer.

   * @param playerName 玩家名
   * @param type 机器类型
   * @param time 时间
   * @param worldName 世界名
   * @param x x坐标
   * @param y y坐标
   * @param z z坐标
   */
  public MachineTimer(String playerName, 
      MachineType type, int time, String worldName, int x, int y, int z) {
    super();
    this.playerName = playerName;
    this.type = type;
    this.time = time;
    this.worldName = worldName;
    this.locX = x;
    this.locY = y;
    this.locZ = z;
  }

  /**
   * 初始化机器Timer.

   * @param type 机器类型
   * @param playerName 玩家名
   * @param loc 机器所在地点
   */
  public MachineTimer(MachineType type, String playerName, Location loc) {
    this.playerName = playerName;
    this.type = type;
    this.worldName = loc.getWorld().getName();
    this.locX = loc.getBlockX();
    this.locY = loc.getBlockY();
    this.locZ = loc.getBlockZ();
  }

  public Location getLocation() {
    return new Location(MCPT.plugin.getServer().getWorld(worldName), locX, locY, locZ);
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
    return locX;
  }

  public void setX(int x) {
    this.locX = x;
  }

  public int getY() {
    return locY;
  }

  public void setY(int y) {
    this.locY = y;
  }

  public int getZ() {
    return locZ;
  }

  public void setZ(int z) {
    this.locZ = z;
  }

}
