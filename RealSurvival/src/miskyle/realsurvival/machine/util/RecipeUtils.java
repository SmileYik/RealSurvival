package miskyle.realsurvival.machine.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.miskyle.mcpt.i18n.I18N;

import miskyle.realsurvival.Msg;
import miskyle.realsurvival.data.recipe.CraftTableRecipe;
import miskyle.realsurvival.data.recipe.Recipe;
import miskyle.realsurvival.data.recipe.RecipeType;
import miskyle.realsurvival.machine.MachineManager;
import miskyle.realsurvival.machine.MachineTimer;
import miskyle.realsurvival.machine.MachineType;
import miskyle.realsurvival.machine.crafttable.CraftTable;
import miskyle.realsurvival.machine.crafttable.CraftTableTimer;
import miskyle.realsurvival.util.RSEntry;

public class RecipeUtils {
  public static final char[] SHAPE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
  
  /**
   * 比较仓库是否符合配方中摆放标准
   * @param machineName 机器名
   * @param type 机器类型
   * @param inv  需要比较的仓库
   * @return 返回配方名及最多可做的数量
   */
  public static RSEntry<String, Integer> cheekRecipe(String machineName,MachineType type,Inventory inv) {
    final List<Integer> materialSlot;
    ArrayList<Recipe> recipes = MachineManager.getRecipes(type, machineName);
    
    if(recipes == null || recipes.isEmpty()) {
      return null;
    }
    if(type == MachineType.CRAFT_TABLE) {
      materialSlot = CraftTable.materials;
    }else {
      materialSlot = null;
    }
    for(Recipe recipe : recipes) {
      int index = 0;
      int amount = 64;
      boolean find = true;
      for(char c : recipe.getMaterialShape().toCharArray()) {
        ItemStack invItem = inv.getItem(materialSlot.get(index++));
        if(c == ' ' && invItem != null) {
          find = false;
          break;
        }else if(c != ' ' && invItem == null) {
          find = false;
          break;
        }else if(c == ' ' && invItem == null) {
          continue;
        }else if(invItem.isSimilar(recipe.getMaterials().get(c))) {
          invItem = invItem.clone();
          ItemStack materialItem = recipe.getMaterials().get(c);
          if(materialItem.getAmount()>invItem.getAmount()) {
            find = false;
            break;
          }else {
            //配对成功, 并记录可以制作的最大数目
            int temp = (int)(invItem.getAmount()/materialItem.getAmount());
            if(temp < amount) {
              amount = temp;
            }
          }
        }else {
          find = false;
          break;
        }
      }
      if(find) {
        return new RSEntry<String, Integer>(recipe.getRecipeName(), amount);
      }
    }
    return null;
  }
  
  public static void startForgeRecipe(String machineName,String recipeName,
      MachineType type,int amount,Inventory inv,Player p,MachineTimer timer) {
    final List<Integer> materialSlot;
    if(type == MachineType.CRAFT_TABLE) {
      materialSlot = CraftTable.materials;
    }else {
      materialSlot = null;
    }
    final Recipe recipe = MachineManager.getRecipe(type, recipeName);
    //循环配料槽,将多余物品返还给玩家并将配料槽清空
    int index = 0;
    for(char c : recipe.getMaterialShape().toCharArray()) {
      if(c!=' ') {
        int slotIndex = materialSlot.get(index);
        ItemStack materialItem = recipe.getMaterials().get(c);
        ItemStack item = inv.getItem(slotIndex);
        if(item == null) {
          continue;
        }else {
          item = item.clone();
        }
        inv.setItem(slotIndex, null);
        if(item.getAmount()>amount*materialItem.getAmount()) {
          ItemStack returnItem = materialItem.clone();
          returnItem.setAmount(item.getAmount()-returnItem.getAmount()*amount);
          giveItemToPlayer(p, returnItem);
        }
        index++;
      }
    }
    //注册工作台Timer
    if(timer.getType() == MachineType.CRAFT_TABLE) {
      CraftTableTimer ctTimer = (CraftTableTimer) timer;
      ctTimer.setTimes(amount);
      ctTimer.setRecipe((CraftTableRecipe) recipe);
      timer = (MachineTimer) ctTimer;
    }else {
      
    }
    MachineManager.addTimer(timer);
  }
  
  public static boolean createRecipe(Recipe recipe, Player p, Inventory inv) {
    final List<Integer> materialSlot;
    final List<Integer> productSlot;
    
    if(recipe.getType() == RecipeType.CRAFT_TABLE) {
      materialSlot = CraftTable.materials;
      productSlot = CraftTable.products;
    }else {
      materialSlot = CraftTable.materials;
      productSlot = CraftTable.products;
    }
    
    int index = 0;
    StringBuilder shape = new StringBuilder();
    HashMap<ItemStack,Character> materialTemp = new HashMap<ItemStack, Character>();
    for(int i : materialSlot) {
      ItemStack invItem = inv.getItem(i);
      if(invItem == null) {
        shape.append(" ");
      }else if(materialTemp.containsKey(invItem)) {
        shape.append(materialTemp.get(invItem));
      }else {
        char c = SHAPE_CHARS[index++];
        shape.append(c);
        materialTemp.put(invItem.clone(), c);
      }
    }
    if(materialTemp.isEmpty()) {
      p.sendMessage(Msg.getPrefix()+I18N.tr("machine.create-recipe.no-material"));
      return false;
    }
    HashMap<Character,ItemStack> materials = new HashMap<>();
    materialTemp.forEach((k,v)->{
      materials.put(v, k);
    });
    
    ArrayList<ItemStack> products = new ArrayList<ItemStack>();
    for(int i : productSlot) {
      ItemStack invItem = inv.getItem(i);
      if(invItem!=null) {
        boolean flag = false;
        for(ItemStack temp : products) {
          if(temp.isSimilar(invItem)) {
            temp.setAmount(temp.getAmount()+invItem.getAmount());
            flag = true;
            break;
          }
        }
        if(!flag) {
          products.add(invItem.clone());
        }
      }
    }
    if(products.isEmpty()) {
      p.sendMessage(Msg.getPrefix()+I18N.tr("machine.create-recipe.no-product"));
      return false;
    }
    
    recipe.setMaterials(materials);
    recipe.setProducts(products);
    recipe.setMaterialShape(shape.toString());
    if(recipe.getType() == RecipeType.CRAFT_TABLE) {
      ((CraftTableRecipe)recipe).save(recipe.getMachineName()+"/"+recipe.getRecipeName());
    }
    
    p.sendMessage(Msg.getPrefix()+I18N.tr("machine.craft-table.create-recipe-success",
        "/"+recipe.getMachineName()+"/"+recipe.getRecipeName()+".yml"));
    return true;
  }
  
  private static void giveItemToPlayer(Player p,ItemStack item) {
    p.getInventory().addItem(item).values().forEach(i->{
      p.getWorld().dropItem(p.getLocation(), i);
    });
  }
}
