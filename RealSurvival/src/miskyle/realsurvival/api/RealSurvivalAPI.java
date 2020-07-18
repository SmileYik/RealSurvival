package miskyle.realsurvival.api;

import miskyle.realsurvival.api.player.PlayerData;

/**
 * a.
 * 
 * @author MiSkYle
 *
 */
public interface RealSurvivalAPI {
  /**
   * \u83b7\u53d6\u73a9\u5bb6\u6570\u636e.
   * 
   * @param playerName \u73a9\u5bb6\u540d
   * @return
   */
  public PlayerData getPlayerData(String playerName);

  /**
   * \u68c0\u6d4b\u73a9\u5bb6\u76f8\u5bf9\u4e8e.
   * RealSurvival\u6765\u8bb2\u662f\u5426\u6d3b\u52a8.
   * 
   * @param playerName \u73a9\u5bb6\u540d
   * @return
   */
  public boolean isPlayerActive(String playerName);
}
