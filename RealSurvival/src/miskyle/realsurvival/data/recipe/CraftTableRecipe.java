package miskyle.realsurvival.data.recipe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import com.github.miskyle.mcpt.MCPT;
import miskyle.realsurvival.data.item.RsItem;

public class CraftTableRecipe extends Recipe {
  public CraftTableRecipe(String recipeName, String machineName,
      String materialShape, int forgeTime,
      HashMap<Character, ItemStack> materials, ArrayList<ItemStack> products) {
    super(recipeName, machineName, materialShape, 
        RecipeType.CRAFT_TABLE, forgeTime, materials, products);
  }

  /**
   * 从文件加载配方.

   * @param file 目标文件
   * @return 若此文件为有效配方文件则返回配方, 否则返回null
   */
  public static CraftTableRecipe loadRecipe(File file) {
    if (!file.exists()) {
      return null;
    }
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
    String materialShape = config.getString("material-shape", null);
    if (materialShape == null) {
      return null;
    }
    int forgeTime = config.getInt("forge-time");
    HashMap<Character, ItemStack> materials = new HashMap<>();
    for (char c : materialShape.toCharArray()) {
      if (config.contains("materials." + c)) {
        materials.put(c, new RsItem(config.getItemStack("materials." + c)).getItem());
      }
    }
    ArrayList<ItemStack> products = new ArrayList<>();
    int index = 0;
    while (config.contains("products.item-" + index)) {
      products.add(new RsItem(config.getItemStack("products.item-" + index++)).getItem());
    }
    String recipeName = config.getString("recipe-name");
    String machineName = config.getString("machine-name");
    return new CraftTableRecipe(
        recipeName, machineName, materialShape, forgeTime, materials, products);
  }

  public void save(String fileName) {
    save(new File(MCPT.plugin.getDataFolder() + "/recipe/crafttable/" + fileName + ".yml"));
  }

  /**
   * 保存配方到目的文件中.

   * @param file 目的文件
   */
  public void save(File file) {
    file.getParentFile().mkdirs();
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
    config.set("recipe-name", getRecipeName());
    config.set("machine-name", getMachineName());
    config.set("material-shape", getMaterialShape());
    config.set("forge-time", getForgeTime());
    getMaterials().forEach((k, v) -> {
      config.set("materials." + k, new RsItem(v).getSaveItem());
    });
    int index = 0;
    for (ItemStack item : getProducts()) {
      config.set("products.item-" + index++, new RsItem(item).getSaveItem());
    }
    try {
      config.save(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
