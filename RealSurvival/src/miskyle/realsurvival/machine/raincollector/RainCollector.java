package miskyle.realsurvival.machine.raincollector;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.github.miskyle.mcpt.i18n.I18N;

import miskyle.realsurvival.machine.MachineManager;
import miskyle.realsurvival.machine.util.GuiItemCreater;

public class RainCollector {
  private static int[] slots = new int[] { 19, 21, 26, 24, 22, 18, 20, 23, 25, 12, 16, 9, 10, 13, 15, 17, 11, 14, 4, 0,
      6, 5, 8, 3, 1, 2, 7 };

  public static void open(Player p, Location loc) {
    // LIGHT_BLUE_STAINED_GLASS_PANE 3
    // GRAY_STAINED_GLASS_PANE7
    RainCollectorHolder holder = new RainCollectorHolder(loc);
    Inventory inv = Bukkit.createInventory(holder, 27, I18N.tr("machine.rain-collector.title"));
    int size = 0;
    holder.setInv(inv);
    if (MachineManager.isActiveTimer(loc)) {
      size = ((RainCollectorTimer) MachineManager.getTImer(loc)).getWaterNumber();
    }
    int index = 0;
    for (int i : slots) {
      if (index < size) {
        inv.setItem(i, GuiItemCreater.getItem("LIGHT_BLUE_STAINED_GLASS_PANE", "STAINED_GLASS_PANE", (short) 3,
            I18N.tr("status.thirst.water.rainwater")));
      } else {
        inv.setItem(i, GuiItemCreater.getItem("GRAY_STAINED_GLASS_PANE", "STAINED_GLASS_PANE", (short) 7, " "));
      }
      index++;
    }
    p.openInventory(inv);
  }
}
