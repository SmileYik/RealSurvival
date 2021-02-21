package miskyle.realsurvival.machine.raincollector;

import java.util.ArrayList;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import com.github.miskyle.mcpt.i18n.I18N;
import miskyle.realsurvival.machine.MachineManager;
import miskyle.realsurvival.machine.util.GuiItemCreater;

public class RainCollector {
  private static int[] slots;
  
  static {
    ArrayList<Integer> temp2 = new ArrayList<>();
    ArrayList<Integer> temp = new ArrayList<>();
    for (int i = 18; i < 27; ++i) {
      temp.add(i);
    }
    temp.sort((a, b) -> {
      return new Random().nextInt(3) - 1;
    });
    temp2.addAll(temp);
    temp = new ArrayList<>();
    for (int i = 9; i < 18; ++i) {
      temp.add(i);
    }
    temp.sort((a, b) -> {
      return new Random().nextInt(3) - 1;
    });
    temp2.addAll(temp);
    temp = new ArrayList<>();
    for (int i = 0; i < 9; ++i) {
      temp.add(i);
    }
    temp.sort((a, b) -> {
      return new Random().nextInt(3) - 1;
    });
    temp2.addAll(temp);
    
    slots = new int[temp2.size()];
    for (int i : temp2) {
      slots[i] = temp2.get(i);
    }
    temp = null;
    temp2 = null;
  }

  /**
   * 打开雨水收集器界面.

   * @param p 玩家.
   * @param loc 雨水收集器的位置.
   */
  public static void open(Player p, Location loc) {
    // LIGHT_BLUE_STAINED_GLASS_PANE 3
    // GRAY_STAINED_GLASS_PANE7
    RainCollectorHolder holder = new RainCollectorHolder(loc);
    Inventory inv = Bukkit.createInventory(holder, 27, I18N.tr("machine.rain-collector.title"));
    int size = 0;
    holder.setInv(inv);
    if (MachineManager.isActiveTimer(loc)) {
      size = ((RainCollectorTimer) MachineManager.getTimer(loc)).getWaterNumber();
    }
    int index = 0;
    for (int i : slots) {
      if (index < size) {
        inv.setItem(i, 
            GuiItemCreater.getItem("LIGHT_BLUE_STAINED_GLASS_PANE", "STAINED_GLASS_PANE", (short) 3,
            I18N.tr("status.thirst.water.rainwater")));
      } else {
        inv.setItem(i, 
            GuiItemCreater.getItem(
                "GRAY_STAINED_GLASS_PANE", "STAINED_GLASS_PANE", (short) 7, " "));
      }
      index++;
    }
    p.openInventory(inv);
  }
}
