package miskyle.realsurvival.machine.crafttable;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import com.github.miskyle.mcpt.i18n.I18N;
import miskyle.realsurvival.Msg;
import miskyle.realsurvival.data.recipe.CraftTableRecipe;
import miskyle.realsurvival.machine.MachineManager;
import miskyle.realsurvival.machine.MachineStatus;
import miskyle.realsurvival.machine.MachineTimer;
import miskyle.realsurvival.machine.util.GuiItemCreater;

public class CraftTable {
  public static final List<Integer> materials = 
      Arrays.asList(10, 11, 12, 13, 19, 20, 21, 22, 28, 29, 30, 31, 37, 38, 39, 40);
  public static final List<Integer> products = Arrays.asList(24, 25, 33, 34);

  
  /**
   * 为已有仓库摆放熔炉界面物品.

   * @param inv 需要摆放界面的仓库, 必须为9*6大小.
   */
  public static void generatorGui(Inventory inv) {
    for (int i = 0; i < 54; i++) {
      if (!(materials.contains(i) || products.contains(i))) {
        inv.setItem(i, GuiItemCreater.getItem(
            "BLACK_STAINED_GLASS_PANE", "STAINED_GLASS_PANE", (short) 15, " "));
      }
    }
  }
  
  /**
   * 打开一个基础的工作台界面.

   * @param p 需要打开工作台界面的玩家
   * @param loc 工作台所在位置
   * @param matchineName 工作台名字
   * @param title 界面标题
   */
  public static void openDefaultGui(Player p, Location loc, String matchineName, String title) {
    Inventory inv;
    CraftTableHolder holder = new CraftTableHolder(loc, matchineName, MachineStatus.NOTHING);
    inv = Bukkit.createInventory(holder, 54, title);
    holder.setInv(inv);
    for (int i = 0; i < 54; i++) {
      if (!(materials.contains(i) || products.contains(i))) {
        inv.setItem(i, GuiItemCreater.getItem(
            "BLACK_STAINED_GLASS_PANE", "STAINED_GLASS_PANE", (short) 15, " "));
      }
    }

    if (MachineManager.isActiveTimer(loc)) {
      MachineTimer mt = MachineManager.getTimer(loc);
      if (!(mt instanceof CraftTableTimer)) {
        p.sendMessage(Msg.getPrefix() + I18N.tr("machine.open-gui.error-1"));
        return;
      }
      CraftTableTimer timer = (CraftTableTimer) mt;
      CraftTableRecipe recipe = timer.getRecipe();
      int index = 0;
      for (char c : recipe.getMaterialShape().toCharArray()) {
        if (c != ' ') {
          inv.setItem(materials.get(index), recipe.getMaterials().get(c));
        }
        index++;
      }
      index = 0;
      for (ItemStack item : recipe.getProducts()) {
        inv.setItem(products.get(index++), item);
      }
      holder.setStatus(MachineStatus.CRAFTING);
      holder.setTimer(timer);
      CraftTableOpenEvent.openEvent(holder, p.getName());
    } else {
      CraftTableTimer timer = new CraftTableTimer(p.getName(), loc);
      holder.setTimer(timer);
      inv.setItem(49, GuiItemCreater.getItem(
          "RED_STAINED_GLASS_PANE", "STAINED_GLASS_PANE", (short) 14,
          I18N.tr("machine.craft-table.ok-slot-name-3")));
    }
    p.openInventory(inv);
  }

  /**
   * 打开配方编辑器.

   * @param p 玩家
   * @param loc 地点
   * @param matchineName 机器名
   * @param title 标题
   * @param recipe 配方
   */
  public static void openCreatorGui(Player p, Location loc, String matchineName, String title,
      CraftTableRecipe recipe) {
    Inventory inv;
    CraftTableHolder holder = new CraftTableHolder(loc, matchineName, recipe);
    inv = Bukkit.createInventory(holder, 54, title);
    holder.setInv(inv);
    for (int i = 0; i < 54; i++) {
      if (!(materials.contains(i) || products.contains(i))) {
        inv.setItem(i, GuiItemCreater.getItem(
            "BLACK_STAINED_GLASS_PANE", "STAINED_GLASS_PANE", (short) 15, " "));
      }
    }
    inv.setItem(49, GuiItemCreater.getItem(
        "RED_STAINED_GLASS_PANE", "STAINED_GLASS_PANE", (short) 14,
        I18N.tr("machine.craft-table.ok-slot-name-3")));
    p.openInventory(inv);
  }
}
