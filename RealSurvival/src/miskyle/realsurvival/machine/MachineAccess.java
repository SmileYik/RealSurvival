package miskyle.realsurvival.machine;

import java.util.ArrayList;
import java.util.List;

public class MachineAccess {
  /**
   * 白名单模式.
   */
  public static final short WHITELIST_MODE = 0;
  /**
   * 黑名单模式.
   */
  public static final short BLACKLIST_MODE = 1;
  private String owner;
  private short mode = BLACKLIST_MODE;
  private List<String> players = new ArrayList<>();
  
  public MachineAccess(String owner) {
    this.owner = owner.toUpperCase();
  }
  
  /**
   * 读取一个机器访问权限.

   * @param owner 机器创造者
   * @param mode 权限模式
   * @param players 玩家列表
   */
  public MachineAccess(String owner, short mode, List<String> players) {
    this.owner = owner.toUpperCase();
    this.mode = mode;
    this.players = players;
  }

  public short getMode() {
    return mode;
  }
  
  public void setMode(short mode) {
    this.mode = mode;
  }
  
  public void setOwner(String owner) {
    this.owner = owner.toUpperCase();
  }
  
  public String getOwner() {
    return owner;
  }
  
  public void setPlayers(List<String> players) {
    this.players = players;
  }
  
  public List<String> getPlayers() {
    return players;
  }
  
  public void toDefault() {
    mode = BLACKLIST_MODE;
    players = new ArrayList<String>();
  }
  
  /**
   * 加入一个玩家名.

   * @param playerName 玩家名
   */
  public void addPlayer(String playerName) {
    playerName = playerName.toUpperCase();
    if (!players.contains(playerName)) {
      players.add(playerName);      
    }
  }
  
  /**
   * 检查玩家是否有权限访问某一机器.

   * @param playerName 玩家名
   * @return 若可访问则返回true
   */
  public boolean isAccessable(String playerName) {
    playerName = playerName.toUpperCase();
    if (owner.equals(playerName)) {
      return true;
    }
    switch (mode) {
      case WHITELIST_MODE:
        return players.contains(playerName);
      default:
        return !players.contains(playerName);
    }
  }
}
