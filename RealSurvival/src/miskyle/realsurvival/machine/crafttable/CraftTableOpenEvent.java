package miskyle.realsurvival.machine.crafttable;

import java.util.HashMap;
import org.bukkit.Bukkit;
import com.github.miskyle.mcpt.MCPT;
import com.github.miskyle.mcpt.i18n.I18N;
import com.sun.istack.internal.NotNull;
import miskyle.realsurvival.machine.util.GuiItemCreater;

public class CraftTableOpenEvent implements Runnable {
  private static HashMap<String, Integer> taskId = new HashMap<String, Integer>();
  private final CraftTableHolder holder;

  private CraftTableOpenEvent(@NotNull CraftTableHolder holder) {
    this.holder = holder;
  }

  @Override
  public void run() {
    double pass = 
        (double) holder.getTimer().getTime() / holder.getTimer().getRecipe().getForgeTime();
    int amount = (int) pass;
    pass %= 1;
    if (amount < 1) {
      holder.getInventory().setItem(49, 
          GuiItemCreater.getItem("RED_STAINED_GLASS_PANE", "STAINED_GLASS_PANE",
          (short) 14, I18N.tr("machine.craft-table.ok-slot-name", formatF(pass * 100))));
    } else {
      holder.getInventory().setItem(49, 
          GuiItemCreater.getItem("GREEN_STAINED_GLASS_PANE", "STAINED_GLASS_PANE",
          (short) 5, I18N.tr("machine.craft-table.ok-slot-name-2", formatF(pass * 100), amount)));
    }

  }

  private String formatF(double d) {
    return String.format("%.2f", d);
  }

  /**
   * 注册gui动画.

   * @param holder holder
   * @param playerName 玩家名.
   */
  public static void openEvent(@NotNull CraftTableHolder holder, String playerName) {
    CraftTableOpenEvent ctoe = new CraftTableOpenEvent(holder);
    int taskId = 
        Bukkit.getScheduler().runTaskTimerAsynchronously(MCPT.plugin, ctoe, 0, 20L).getTaskId();
    CraftTableOpenEvent.taskId.put(playerName, taskId);
  }

  /**
   * 注销gui动画.

   * @param playerName 玩家名.
   */
  public static void cancelEvent(String playerName) {
    if (taskId.containsKey(playerName)) {
      Bukkit.getScheduler().cancelTask(taskId.get(playerName));
    }
  }
}
