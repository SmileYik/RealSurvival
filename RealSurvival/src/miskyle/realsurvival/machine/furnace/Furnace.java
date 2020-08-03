package miskyle.realsurvival.machine.furnace;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.miskyle.mcpt.i18n.I18N;

import miskyle.realsurvival.Msg;
import miskyle.realsurvival.data.recipe.FurnaceRecipe;
import miskyle.realsurvival.machine.MachineManager;
import miskyle.realsurvival.machine.MachineStatus;
import miskyle.realsurvival.machine.MachineTimer;
import miskyle.realsurvival.machine.util.GuiItemCreater;

public class Furnace {
  public static final List<Integer> MATERIAL_SLOTS = Arrays.asList(12, 13, 14, 21, 22, 23);
  public static final List<Integer> PRODUCT_SLOTS = Arrays.asList(39, 40, 41);
  public static final List<Integer> TEMPERATURE_SLOTS = Arrays.asList(16, 25, 34, 43);
  public static final List<Integer> PROGRESS_SLOTS = Arrays.asList(10, 19, 28, 37);
  
  public static void openDefaultGUI(Player p,Location loc,String matchineName,String title) {
    FurnaceHolder holder = new FurnaceHolder(loc, matchineName, MachineStatus.NOTHING);
    Inventory inv = Bukkit.createInventory(holder, 54, title);
    holder.setInv(inv);
    for(int i = 0; i<54;i++) {
      if(!(MATERIAL_SLOTS.contains(i) || PRODUCT_SLOTS.contains(i))) {
        if (TEMPERATURE_SLOTS.contains(i) || PROGRESS_SLOTS.contains(i)) {
          inv.setItem(i, GuiItemCreater.getItem(
              Material.WHITE_STAINED_GLASS_PANE, "STAINED_GLASS_PANE", (short) 0, " "));
        } else {
          inv.setItem(i, GuiItemCreater.getItem(
              Material.BLACK_STAINED_GLASS_PANE, "STAINED_GLASS_PANE", (short) 15, " "));          
        }
      }
    }
    
    if (MachineManager.isActiveTimer(loc)) {
      MachineTimer mt = MachineManager.getTImer(loc);
      if( !(mt instanceof FurnaceTimer)) {
        p.sendMessage(Msg.getPrefix()+I18N.tr("machine.open-gui.error-1"));
        return;
      }
      FurnaceTimer timer = (FurnaceTimer)mt;
      FurnaceRecipe recipe = timer.getRecipe();
      int index = 0;
      for(char c:recipe.getMaterialShape().toCharArray()) {
        if(c!=' ') {
          inv.setItem(MATERIAL_SLOTS.get(index), recipe.getMaterials().get(c));
        }
        index ++;
      }
      index = 0;
      for(ItemStack item : recipe.getProducts()) {
        inv.setItem(PRODUCT_SLOTS.get(index++), item);
      }
      holder.setStatus(MachineStatus.CRAFTING);
      holder.setTimer(timer);
    } else {
      FurnaceTimer timer = new FurnaceTimer(p.getName(), loc);
      holder.setTimer(timer);
      inv.setItem(49, GuiItemCreater.getItem(
          Material.RED_STAINED_GLASS_PANE, "STAINED_GLASS_PANE", (short) 14, 
          I18N.tr("machine.craft-table.ok-slot-name-3")));
    }
    FurnaceOpenEvent.openEvent(holder, p.getName());
    p.openInventory(inv);
  }
  
  public static void openCreatorGUI(Player p, Location loc, String machineName, String title, FurnaceRecipe recipe) {
    FurnaceHolder holder = new FurnaceHolder(loc, machineName, recipe);
    Inventory inv = Bukkit.createInventory(holder, 54, title);
    holder.setInv(inv);
    for(int i = 0; i<54;i++) {
      if(!(MATERIAL_SLOTS.contains(i) || PRODUCT_SLOTS.contains(i))) {
        if (TEMPERATURE_SLOTS.contains(i) || PROGRESS_SLOTS.contains(i)) {
          inv.setItem(i, GuiItemCreater.getItem(
              Material.WHITE_STAINED_GLASS_PANE, "STAINED_GLASS_PANE", (short) 0, " "));
        } else {
          inv.setItem(i, GuiItemCreater.getItem(
              Material.BLACK_STAINED_GLASS_PANE, "STAINED_GLASS_PANE", (short) 15, " "));          
        }
      }
    }
    inv.setItem(49, GuiItemCreater.getItem(
        Material.RED_STAINED_GLASS_PANE, "STAINED_GLASS_PANE", (short) 14, 
        I18N.tr("machine.craft-table.ok-slot-name-3")));
    p.openInventory(inv);
  }
  
}
