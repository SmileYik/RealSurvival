package miskyle.realsurvival.machine;

import miskyle.realsurvival.data.recipe.Recipe;
import org.bukkit.entity.Player;

public class MachinePermission {
  private static final String rootPermission = "RealSurvival.Recipe.";
  
  /**
   * 检查玩家是否有相应权限使用该配方.

   * @param p 玩家
   * @param recipe 要使用的配方
   * @return 如果有权限则返回true
   */
  public static boolean checkPermission(Player p, Recipe recipe) {
    return checkPermission(p, recipe.getMachineName(), recipe.getRecipeName());
  }
  
  /**
   * 检查玩家是否有相应权限使用该配方.

   * @param p 玩家
   * @param machineName 配方对应的机器名
   * @param recipeName 配方名
   * @return
   */
  public static boolean checkPermission(Player p, String machineName, String recipeName) {
    if (p.hasPermission(getAllRecipePermission())) {
      return true;
    } else if (p.hasPermission(getAllMecinePermission(machineName))) {
      return true;
    } else {
      return p.hasPermission(getPermission(machineName, recipeName));
    }
  }
  
  private static String getAllRecipePermission() {
    return rootPermission + "*";
  }
  
  private static String getAllMecinePermission(String machineName) {
    return rootPermission + machineName + ".*";
  }
  
  private static String getPermission(String machineName, String recipeName) {
    return rootPermission + machineName + "." + recipeName;
  }
}
