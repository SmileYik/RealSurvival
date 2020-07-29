package miskyle.realsurvival.machine.crafttable;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.miskyle.mcpt.i18n.I18N;

import miskyle.realsurvival.data.recipe.CraftTableRecipe;
import miskyle.realsurvival.machine.MachineManager;
import miskyle.realsurvival.machine.MachineStatus;
import miskyle.realsurvival.machine.util.GuiItemCreater;

public class CraftTable {
  public static final  List<Integer> materials = Arrays.asList(10,11,12,13,19,20,21,22,28,29,30,31,37,38,39,40);
  public static final  List<Integer> products = Arrays.asList(24,25,33,34);
  
//  public static Inventory createRecipeViewerGUI(WorkbenchRecipe recipe){
//      if(recipe==null)return null;
//      Inventory inv=Bukkit.createInventory(null, 54,I18N.tr("recipe1"));
//      for(int i=0;i<54;i++)
//          if(!(materials.contains(i)||products.contains(i)))
//              inv.setItem(i, Items.createPItem((short)15, " "));
//      ItemStack item =  Items.createPItem((short) 14, I18n.tr("recipe6"));
//      ItemMeta im = item.getItemMeta();
//      im.setLore(Arrays.asList(I18n.tr("recipe5",Util.RDP(recipe.getNeedTime(), 2))));
//      item.setItemMeta(im);
//      inv.setItem(49,item.clone());
//      return recipe.setInv(inv);
//  }
  
  public static void openDefaultGUI(Player p,Location loc,String matchineName,String title) {
    Inventory inv;
    CraftTableHolder holder = new CraftTableHolder(loc, matchineName,MachineStatus.NOTHING);
    inv = Bukkit.createInventory(holder, 54,title);
    holder.setInv(inv);
    for(int i = 0; i<54;i++) {
      if(!(materials.contains(i)||products.contains(i))) {
        inv.setItem(i, GuiItemCreater.getItem(
            Material.BLACK_STAINED_GLASS_PANE, "STAINED_GLASS_PANE", (short) 15, " "));
      }
    }
    
    
    if(MachineManager.isActiveTimer(loc)) {
      CraftTableTimer timer = (CraftTableTimer)MachineManager.getTImer(loc);
      CraftTableRecipe recipe = timer.getRecipe();
      int index = 0;
      for(char c:recipe.getMaterialShape().toCharArray()) {
        if(c!=' ') {
          inv.setItem(materials.get(index), recipe.getMaterials().get(c));
        }
        index ++;
      }
      index = 0;
      for(ItemStack item : recipe.getProducts()) {
        inv.setItem(products.get(index++), item);
      }
      holder.setStatus(MachineStatus.CRAFTING);
      holder.setTimer(timer);
      CraftTableOpenEvent.openEvent(holder, p.getName());
    }else {
      CraftTableTimer timer = new CraftTableTimer(p.getName(), loc);
      holder.setTimer(timer);
      inv.setItem(49, GuiItemCreater.getItem(
          Material.RED_STAINED_GLASS_PANE, "STAINED_GLASS_PANE", (short) 14, 
          I18N.tr("machine.craft-table.ok-slot-name-3")));
    }
    p.openInventory(inv);
  }
  
  public static void openCreatorGUI(Player p,Location loc,String matchineName,String title,CraftTableRecipe recipe) {
    Inventory inv;
    CraftTableHolder holder = new CraftTableHolder(loc, matchineName,recipe);
    inv = Bukkit.createInventory(holder, 54,title);
    holder.setInv(inv);
    for(int i = 0; i<54;i++) {
      if(!(materials.contains(i)||products.contains(i))) {
        inv.setItem(i, GuiItemCreater.getItem(
            Material.BLACK_STAINED_GLASS_PANE, "STAINED_GLASS_PANE", (short) 15, " "));
      }
    }
    inv.setItem(49, GuiItemCreater.getItem(
        Material.RED_STAINED_GLASS_PANE, "STAINED_GLASS_PANE", (short) 14, 
        I18N.tr("machine.craft-table.ok-slot-name-3")));
    p.openInventory(inv);
  }
}
