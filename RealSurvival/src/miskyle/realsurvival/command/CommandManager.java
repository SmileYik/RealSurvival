package miskyle.realsurvival.command;

import miskyle.realsurvival.RealSurvival;
import miskyle.realsurvival.data.ConfigManager;

public class CommandManager {
  public CommandManager(RealSurvival plugin) {
    regesitCommands(plugin);
  }

  private void regesitCommands(RealSurvival plugin) {
    // 加载对物品操作有关的指令
    RsCommand cmdItem = new RsCommand();
    // 检测服务器核心版本,并加载适应的指令方法
    if (ConfigManager.getBukkitVersion() >= 9) {
      cmdItem.initialization(ItemCommandsVer2.class.getDeclaredMethods(), new ItemCommandsVer2(),
          "rsi,realsurvivali,rsitem,realsurvivalitem");
    } else {
      cmdItem.initialization(ItemCommandsVer1.class.getDeclaredMethods(), new ItemCommandsVer1(),
          "rsi,realsurvivali,rsitem,realsurvivalitem");
    }
    plugin.getCommand("RealSurvivalItem").setExecutor(cmdItem);
    plugin.getCommand("RealSurvivalItem").setTabCompleter(cmdItem);

    // 加载与玩家有关的命令
    RsCommand cmdPlayer = new RsCommand();
    cmdPlayer.initialization(
        PlayerCommands.class.getDeclaredMethods(), new PlayerCommands(), "rs,realsurvival");
    plugin.getCommand("RealSurvival").setExecutor(cmdPlayer);
    plugin.getCommand("RealSurvival").setTabCompleter(cmdPlayer);

    // 加载与多方块结构编辑有关的指令
    RsCommand cmdCube = new RsCommand();
    cmdCube.initialization(CubeArrayCommands.class.getDeclaredMethods(), new CubeArrayCommands(),
        "rsc,rscube,realsurvivalc,realsurvivalcube");
    plugin.getCommand("RealSurvivalCube").setExecutor(cmdCube);
    plugin.getCommand("RealSurvivalCube").setTabCompleter(cmdCube);

    // 加载与配方及工作台有关的指令
    RsCommand cmdRecipe = new RsCommand();
    cmdRecipe.initialization(RecipeCommands.class.getDeclaredMethods(), new RecipeCommands(),
        "rsr,rsrecipe,realsurvivalr,realsurvivalrecipe");
    plugin.getCommand("RealSurvivalRecipe").setExecutor(cmdRecipe);
    plugin.getCommand("RealSurvivalRecipe").setTabCompleter(cmdRecipe);
  }

  /**
   * 比较子指令.

   * @param args 参数
   * @param subCmd 子指令参数.
   * @return
   */
  public static boolean compareSubCommand(String[] args, String[] subCmd) {
    for (int i = 0; i < subCmd.length; i++) {
      if (!subCmd[i].equalsIgnoreCase(args[i])) {
        return false;        
      }
    }
    return true;
  }
}
