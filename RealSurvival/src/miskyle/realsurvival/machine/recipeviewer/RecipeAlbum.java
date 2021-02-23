package miskyle.realsurvival.machine.recipeviewer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import com.github.miskyle.mcpt.i18n.I18N;
import miskyle.realsurvival.data.blockarray.CubeData;
import miskyle.realsurvival.data.recipe.FurnaceRecipe;
import miskyle.realsurvival.data.recipe.Recipe;
import miskyle.realsurvival.data.recipe.RecipeType;
import miskyle.realsurvival.machine.MachineManager;
import miskyle.realsurvival.machine.MachineType;
import miskyle.realsurvival.machine.crafttable.CraftTable;
import miskyle.realsurvival.machine.furnace.Furnace;
import miskyle.realsurvival.machine.util.GuiItemCreater;

public class RecipeAlbum {
  public static final List<Integer> SLOTS = Arrays.asList(10, 11, 12, 13, 14, 15, 16);
  public static final Inventory STD_INV;
  
  static {
    STD_INV = Bukkit.createInventory(null, 27);
    for (int i = 0; i < 27; i++) {
      if (!SLOTS.contains(i)) {
        STD_INV.setItem(i, 
            GuiItemCreater.getItem(
                "WHITE_STAINED_GLASS_PANE", "STAINED_GLASS_PANE", (short) 0, " "));
      }
    }
    
    STD_INV.setItem(9, 
        GuiItemCreater.getItem(
            "LIGHT_BLUE_STAINED_GLASS_PANE", "STAINED_GLASS_PANE", (short) 3, " "));
    STD_INV.setItem(17, 
        GuiItemCreater.getItem(
            "LIGHT_BLUE_STAINED_GLASS_PANE", "STAINED_GLASS_PANE", (short) 3, " "));
  }
  
  /**
   * 生成配方集界面.

   * @param item 存放配方集的成书物品
   * @return 生成好的配方集界面.
   */
  public static Inventory getInv(ItemStack item) {
    BookMeta meta = (BookMeta) item.getItemMeta();
    String title = meta.hasTitle() ? meta.getTitle() : I18N.tr("machine.recipe-album.no-title");
    String author = meta.hasAuthor() ? meta.getAuthor() : I18N.tr("machine.recipe-album.no-author");
    
    RecipeAlbumHolder holder = new RecipeAlbumHolder();
    Inventory inv = Bukkit.createInventory(holder, STD_INV.getSize(), title + " - " + author);
    for (int i = 0; i < STD_INV.getSize(); i++) {
      inv.setItem(i, STD_INV.getItem(i) == null ? null : STD_INV.getItem(i).clone());
    }
    holder.setInventory(inv);
    holder.setMenu(true);
    Recipe[] recipes = new Recipe[SLOTS.size()];
    if (meta.getPageCount() == 3) {
      String[] machineType = meta.getPage(2).replace("§0", "").split("\n");
      String[] recipeName = meta.getPage(3).replace("§0", "").split("\n");
      int times = Math.min(machineType.length, SLOTS.size());
      for (int i = 0; i < times; ++i) {
        if (machineType[i] != null) {
          recipes[i] = MachineManager.getRecipe(MachineType.valueOf(machineType[i]), recipeName[i]);
          inv.setItem(SLOTS.get(i), recipes[i].getProducts().get(0).clone());
        }
      }
      
      //开始检测是否为有效的配方集.
      boolean flag = true;
      for (Recipe recipe : recipes) {
        if (recipe != null) {
          flag = false;
          break;
        }
      }
      if (flag) {
        return null;
      }
      //检测完毕, 若recipes里所有配方数据都为null则打开失败.
      
      holder.setViewers(recipes);
      return inv;
    }
    return null;
  }
  
  /**
   * 帮助玩家打开配方集界面.

   * @param p 玩家.
   * @param item 存放配方集的物品.
   */
  public static void openInv(Player p, ItemStack item) {
    Inventory inv = getInv(item);
    if (inv == null) {
      return;
    }
    p.openInventory(inv);
  }
  
  /**
   * 简单判断物品是否存放着配方集数据.

   * @param item 目的物品.
   * @return true 则代表存放着有效配方集数据, 反之则不存在.
   */
  public static boolean isAlbum(ItemStack item) {
    if (item == null 
        || !item.getType().name().equalsIgnoreCase("WRITTEN_BOOK") 
        || !item.hasItemMeta()) {
      return false;
    }
    BookMeta meta = (BookMeta) item.getItemMeta();
    if (!meta.hasPages()) {
      return false;
    }
    String text = meta.getPage(1).replace("§0", "").split("\n")[0];
    if (text.isEmpty() || !text.equalsIgnoreCase("[RS.ALBUM]")) {
      return false;
    }
    return meta.getPageCount() == 3 
        && meta.getPage(2).split("\n").length == meta.getPage(3).split("\n").length;
  }
  
  /**
   * 打开配方阅读器.

   * @param p 玩家.
   * @param recipe 指定配方.
   * @param holder 配方集holder.
   */
  public static void openRecipeViewer(Player p, Recipe recipe, RecipeAlbumHolder holder) {
    if (recipe == null)  {
      return;
    }
    if (holder == null) {
      holder = new RecipeViewerHolder();
    }
    holder.setMenu(false);
    holder.setNow(recipe);
    Inventory inv = Bukkit.createInventory(holder, 9 * 6, I18N.tr("machine.recipe-album.title"));
    if (recipe.getType() == RecipeType.CRAFT_TABLE) {
      CraftTable.generatorGui(inv);
      int index = 0;
      for (char c : recipe.getMaterialShape().toCharArray()) {
        if (c != ' ') {
          inv.setItem(CraftTable.materials.get(index), recipe.getMaterials().get(c));
        }
        index++;
      }
      index = 0;
      for (ItemStack item : recipe.getProducts()) {
        inv.setItem(CraftTable.products.get(index++), item);
      }
    } else if (recipe.getType() == RecipeType.FURNACE) {
      Furnace.generatorGui(inv);
      int index = 0;
      for (char c : recipe.getMaterialShape().toCharArray()) {
        if (c != ' ') {
          inv.setItem(Furnace.MATERIAL_SLOTS.get(index), recipe.getMaterials().get(c));
        }
        index++;
      }
      index = 0;
      for (ItemStack item : recipe.getProducts()) {
        inv.setItem(Furnace.PRODUCT_SLOTS.get(index++), item);
      }
    }
    ItemStack item = inv.getItem(49);
    ItemMeta meta = item.getItemMeta();
    meta.setLore(getRecipeInfoList(recipe));
    item.setItemMeta(meta);
    p.openInventory(inv);
  }
  
  /**
   * 获取配方额外信息.

   * @param recipe 配方.
   * @return 返回一个消息列表.
   */
  public static List<String> getRecipeInfoList(Recipe recipe) {
    List<String> lores = new ArrayList<>();
    for (String line : I18N.tr(
        "machine.recipe-album.recipe-info", 
        recipe.getRecipeName(), 
        recipe.getMachineName(), 
        recipe.getForgeTime()
    ).split("\n")) {
      lores.add(line);
    }
    
    if (recipe.getType() == RecipeType.FURNACE) {
      for (String line : I18N.tr(
          "machine.recipe-album.recipe-info-f", 
          ((FurnaceRecipe) recipe).getMaxTemperature(), 
          ((FurnaceRecipe) recipe).getMinTemperature()
      ).split("\n")) {
        lores.add(line);
      }
    }
    return lores;
  }
  
  /**
   * 打开查看多方块结构如何摆放的gui.

   * @param p 玩家
   * @param machineName 机器名
   * @param holder 对应配方集holder.
   */
  public static void openMachineBuildHelper(
      Player p, String machineName, RecipeAlbumHolder holder) {
    if (holder == null) {
      holder = new RecipeViewerHolder();
    } else {
      holder.setMenu(true);
      p.closeInventory();
      holder.setMenu(false);
    }
    CubeData cube = MachineManager.getMachineCube(machineName);
    if (cube == null) {
      p.sendMessage(I18N.tr("machine.recipe-album.error"));
      return;
    }
    Inventory inv = Bukkit.createInventory(holder, 54, I18N.tr("machine.recipe-album.cube-title"));
    //上方为北
    String[] names = I18N.tr("machine.recipe-album.cube.tip-name").split("\n");
    inv.setItem(3, getCubeItemStack(cube.getMid().getNorthWest(), names[1]));
    inv.setItem(4, getCubeItemStack(cube.getMid().getNorth(), names[1]));
    inv.setItem(5, getCubeItemStack(cube.getMid().getNorthEast(), names[1]));
    inv.setItem(12, getCubeItemStack(cube.getMid().getWest(), names[1]));
    inv.setItem(13, getCubeItemStack(cube.getMid().getMain(), names[1]));
    inv.setItem(14, getCubeItemStack(cube.getMid().getEast(), names[1]));
    inv.setItem(21, getCubeItemStack(cube.getMid().getSouthWest(), names[1]));
    inv.setItem(22, getCubeItemStack(cube.getMid().getSouth(), names[1]));
    inv.setItem(23, getCubeItemStack(cube.getMid().getSouthEast(), names[1]));
    
    inv.setItem(27, getCubeItemStack(cube.getDown().getNorthWest(), names[0]));
    inv.setItem(28, getCubeItemStack(cube.getDown().getNorth(), names[0]));
    inv.setItem(29, getCubeItemStack(cube.getDown().getNorthEast(), names[0]));
    inv.setItem(36, getCubeItemStack(cube.getDown().getWest(), names[0]));
    inv.setItem(37, getCubeItemStack(cube.getDown().getMain(), names[0]));
    inv.setItem(38, getCubeItemStack(cube.getDown().getEast(), names[0]));
    inv.setItem(45, getCubeItemStack(cube.getDown().getSouthWest(), names[0]));
    inv.setItem(46, getCubeItemStack(cube.getDown().getSouth(), names[0]));
    inv.setItem(47, getCubeItemStack(cube.getDown().getSouthEast(), names[0]));
    
    inv.setItem(33, getCubeItemStack(cube.getUp().getNorthWest(), names[2]));
    inv.setItem(34, getCubeItemStack(cube.getUp().getNorth(), names[2]));
    inv.setItem(35, getCubeItemStack(cube.getUp().getNorthEast(), names[2]));
    inv.setItem(42, getCubeItemStack(cube.getUp().getWest(), names[2]));
    inv.setItem(43, getCubeItemStack(cube.getUp().getMain(), names[2]));
    inv.setItem(44, getCubeItemStack(cube.getUp().getEast(), names[2]));
    inv.setItem(51, getCubeItemStack(cube.getUp().getSouthWest(), names[2]));
    inv.setItem(53, getCubeItemStack(cube.getUp().getSouth(), names[2]));
    inv.setItem(53, getCubeItemStack(cube.getUp().getSouthEast(), names[2]));
    
    List<String> lores = new ArrayList<>();
    for (String line : I18N.tr(
        "machine.recipe-album.cube.tip", cube.isCheckCompass()).split("\n")) {
      lores.add(line);
    }
    ItemStack item = GuiItemCreater.getItem("WRITTEN_BOOK", "WRITTEN_BOOK", (short) 0, "");
    ItemMeta meta = item.getItemMeta();
    meta.setLore(lores);
    item.setItemMeta(meta);
    inv.setItem(10, item);
    p.openInventory(inv);
  }
  
  /**
   * 获取多方块结构中的物品.

   * @param cubeBlock 多方块结构中记录的方块.
   * @param name 需要设定的物品名.
   * @return 符合条件的物品.
   */
  public static ItemStack getCubeItemStack(String cubeBlock, String name) {
    if (cubeBlock.equals("AIR")) {
      return null;
    }
    if (cubeBlock.contains(":")) {
      return GuiItemCreater.getItem(
          cubeBlock.split(":")[0], 
          cubeBlock.split(":")[0], 
          Short.parseShort(cubeBlock.split(":")[1]), name);
    } else {
      return GuiItemCreater.getItem(
          cubeBlock, 
          cubeBlock, 
          (short) 0, name);
    }
  }
}
