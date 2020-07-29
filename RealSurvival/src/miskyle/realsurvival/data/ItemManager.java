package miskyle.realsurvival.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import com.github.miskyle.mcpt.MCPT;
import com.github.miskyle.mcpt.nms.nbtitem.NBTItem;

import miskyle.realsurvival.RealSurvival;
import miskyle.realsurvival.data.effect.EffectData;
import miskyle.realsurvival.data.item.DrugData;
import miskyle.realsurvival.data.item.RSItemData;
import miskyle.realsurvival.util.RSEntry;
import miskyle.realsurvival.util.Utils;

public class ItemManager {
  private static ItemManager im;
  private NBTItem nbtItem;
  
  private String split;
  private HashMap<String, String> labels;
  
  private HashMap<String, RSItemData> nbtItemData;
  private HashMap<String, RSItemData> minecraftItemData;
  
  public ItemManager() {
    im = this;
    nbtItem = NBTItem.getNBTItem(RealSurvival.getVersion());
    split = MCPT.plugin.getConfig().getString("label.split",":");
    labels = new HashMap<String, String>();
    for(String line : MCPT.plugin.getConfig().getStringList("label.labels")) {
      String[] temp = line.split(":");
      labels.put(temp[0], temp[1]);
    }
    
    loadNBTItem();
    loadMinecraftItem();
  }
  
  private void loadMinecraftItem() {
    minecraftItemData = new HashMap<String, RSItemData>();
    File file = new File(MCPT.plugin.getDataFolder(), "minecraft-item.yml");
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
      MCPT.plugin.getLogger().info("");
      return;
    }
    YamlConfiguration c = YamlConfiguration.loadConfiguration(file);
    c.getKeys(false).forEach(key -> {
      RSItemData item = new RSItemData();
      DrugData drug = new DrugData();
      if (c.contains(key + ".sleep.value")) {
        item.setSleep(c.getString(key + ".sleep.value").split("/"));
        item.setMaxSleep(c.getBoolean(key + ".sleep.max", true));
      }
      if (c.contains(key + ".thirst.value")) {
        item.setThirst(c.getString(key + ".thirst.value").split("/"));
        item.setMaxThirst(c.getBoolean(key + ".thirst.max", true));
      }
      if (c.contains(key + ".energy.value")) {
        item.setEnergy(c.getString(key + ".energy.value").split("/"));
        item.setMaxEnergy(c.getBoolean(key + ".energy.max", true));
      }
      if (c.contains(key + ".hunger.value")) {
        item.setHunger(c.getString(key + ".hunger.value").split("/"));
        item.setMaxHunger(c.getBoolean(key + ".hunger.max", true));
      }
      if (c.contains(key + ".health.value")) {
        item.setHealth(c.getString(key + ".health.value").split("/"));
        item.setMaxHealth(c.getBoolean(key + ".health.max", true));
      }
      if (c.contains(key + ".temperature")) {
        item.setTemperature(c.getString(key + ".temperature").split("/"));
      }
      if (c.contains(key + ".weight")) {
        item.setWeight(c.getDouble(key + ".weight"));
      }
      if (c.contains(key + ".disease.get-disease")) {
        HashMap<String, Double> getDisease = new HashMap<String, Double>();
        c.getStringList(key + ".disease.get-disease").forEach(line -> {
          String[] temp = line.split(":");
          getDisease.put(temp[0], Double.parseDouble(temp[1]));
        });
        drug.setGetDisease(getDisease);
      }
      if (c.contains(key + ".disease.enable-disease")) {
        HashMap<String, RSEntry<Double, Double>> drugd = new HashMap<String, RSEntry<Double, Double>>();
        HashMap<String, RSEntry<Integer, Integer>> duration = new HashMap<String, RSEntry<Integer, Integer>>();
        List<String> wrongDisease = null;
        ArrayList<EffectData> eatWrongDrug = new ArrayList<EffectData>();
        ArrayList<EffectData> noNeedDrug = new ArrayList<EffectData>();
        c.getStringList(key + ".disease.enable-disease").forEach(line -> {
          String[] temp = line.split(",");
          drugd.put(temp[0], getRSEntryDouble(temp[1]));
          duration.put(temp[0], getRSEntryInt(temp[2]));
        });

        if (c.contains(key + ".disease.eat-wrong-drug") && c.contains(key + ".disease.wrong-disease")) {
          wrongDisease = c.getStringList(key + ".disease.wrong-disease");
          c.getStringList(key + ".disease.eat-wrong-drug").forEach(line -> {
            eatWrongDrug.add(EffectData.loadFromString(line));
          });
        }
        if (c.contains(key + "disease.no-need-drug")) {
          c.getStringList(key + "disease.no-need-drug").forEach(line -> {
            noNeedDrug.add(EffectData.loadFromString(line));
          });
        }
        drug.setDrug(drugd);
        drug.setDuration(duration);
        drug.setWrongDisease(wrongDisease);
        drug.setEatWrongDrug(eatWrongDrug);
        drug.setNoNeedDrug(noNeedDrug);
      }
      item.setDrugData(drug);
      minecraftItemData.put(key, item);
    });
  }
  
  private void loadNBTItem() {
    nbtItemData = new HashMap<String, RSItemData>();
      searchNBTItem(new File(MCPT.plugin.getDataFolder()+"/nbtitem/"),
              new File(MCPT.plugin.getDataFolder()+"/nbtitem/").getAbsolutePath());
  }
  
  private void searchNBTItem(File path,String root) {
    for(File f : path.listFiles()) {
      if(f.isDirectory()) {
        searchNBTItem(f,root);        
      }else {
        String name = f.getAbsolutePath().replace(root, "").toLowerCase().replace(".yml", "").replace("\\", "/");
        while(name.charAt(0) == '/')
          name = name.substring(1);
        
        nbtItemData.put(name, loadItemDataFromFIle(f));
      }
    }
  }
  
  /**
   * 从文件中获取物品信息
   * @param fileName 文件名 大小写不敏感
   * @return
   */
  public static RSItemData loadItemData(String fileName) {
    return loadItemDataFromFIle(
        new File(MCPT.plugin.getDataFolder()+"/nbtitem/"+fileName.toLowerCase()+".yml"));
  }
  /**
     * 从文件中获取物品信息
     * @param fileName 文件名 大小写不敏感
     * @return
     */
  public static RSItemData loadItemDataFromFIle(File file) {
    if(!file.exists()) {
      return null;
    }
    YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
    RSItemData item = new RSItemData();
    if(data.contains("sleep")) {
      item.setSleep(data.getString("sleep").split("/"));
      item.setMaxSleep(data.getBoolean("sleep-max",false));
    }
    if(data.contains("thirst")) {
      item.setThirst(data.getString("thirst").split("/"));
      item.setMaxThirst(data.getBoolean("thirst-max",false));
    }
    if(data.contains("energy")) {
      item.setEnergy(data.getString("energy").split("/"));
      item.setMaxEnergy(data.getBoolean("energy-max",false));
    }
    if(data.contains("hunger")) {
      item.setHunger(data.getString("hunger").split("/"));
      item.setMaxHunger(data.getBoolean("hunger-max",false));
    }
    if(data.contains("health")) {
      item.setHealth(data.getString("health").split("/"));
      item.setMaxHealth(data.getBoolean("health-max"));
    }
    if(data.contains("weight")) {
      item.setWeight(data.getDouble("weight"));      
    }
    if(data.contains("temperature")) {
      item.setTemperature(data.getString("temperature").split("/"));
    }
    
    item.setDrugData(DrugData.getDrugData(data));
    return item;
  }
  
  public static RSItemData loadItemData(ItemStack item) {
    if(item==null)return null;
    String name = im.nbtItem.getString(item, "RSNBT");
    if(name!=null) {
      name = name.toLowerCase();
      if(im.nbtItemData.containsKey(name))
        return im.nbtItemData.get(name);
    }
    if(!item.hasItemMeta() || !item.getItemMeta().hasLore()) {
      if(im.minecraftItemData.containsKey(item.getType().name())) {
        return im.minecraftItemData.get(item.getType().name());
      }
      return null;
    }
    
    RSItemData rsItem = new RSItemData();
    item.getItemMeta().getLore().forEach(s->{
      String ss = Utils.removeColor(s);
      im.labels.forEach((k,v)->{
        if(ss.contains(v) && ss.contains(im.split)) {
          if(k.equalsIgnoreCase("drug")) {
            String drugName = ss.split(":")[1].replace(" ", "");
            RSItemData drug = im.nbtItemData.get(drugName);
            if(drug!=null) {
              rsItem.setDrugData(drug.getDrugData());
            }
            return;
          }
          
          String temp = ss.replaceAll("[^0-9+-/%]", "");
          if(temp.contains("%")) {
            switch (k.toUpperCase()) {
              case "SLEEP":
                rsItem.setMaxSleep(true);
                break;
              case "THIRST":
                rsItem.setMaxThirst(true);
                break;
              case "ENERGY":
                rsItem.setMaxEnergy(true);
                break;
              case "HEALTH":
                rsItem.setMaxHealth(true);
                break;
              case "HUNGER":
                rsItem.setMaxHunger(true);
                break;
            }
          }
          if(temp.contains("/")) {
            //范围类型
            String[] temp2 = temp.split("/");
            rsItem.addStatusValue(
                k, Double.parseDouble(temp2[0]), Double.parseDouble(temp2[1]));
          }else {
            //单个数值类型
            double a = Double.parseDouble(temp);
            rsItem.addStatusValue(k, a, a);
          }
        }
      });
    });
    return rsItem;
  }
  
  public static double getStatusValue(String status,ItemStack item) {
    if(item==null)return 0;
    String name = im.nbtItem.getString(item, "RSNBT");
    if(name!=null) {
      name = name.toLowerCase();
      if(im.nbtItemData.containsKey(name))
        return im.nbtItemData.get(name).getValue(status);
    }
    double value = 0;
    String key = im.labels.get(status);
    if(!item.hasItemMeta() || !item.getItemMeta().hasLore()) {
      if(im.minecraftItemData.containsKey(item.getType().name())) {
        return im.minecraftItemData.get(item.getType().name()).getValue(status);
      }
      return 0;
    }
    for(String s : item.getItemMeta().getLore()) {
      String ss = Utils.removeColor(s);
      if(ss.contains(key) && ss.contains(im.split)) {
        String temp = ss.replaceAll("[^0-9+-/]", "");
        if(temp.contains("/")) {
          //范围类型
          String[] temp2 = temp.split("/");
          value+=im.random(Double.parseDouble(temp2[0]), Double.parseDouble(temp2[1]));
        }else {
          //单个数值类型
          value+=Double.parseDouble(temp);
        }
      }
    }
    return value;
  }
  
  public static double getStatusValueOnly(String status,ItemStack item) {
    if(item==null)return 0;
    String name = im.nbtItem.getString(item, "RSNBT");
    if(name!=null) {
      name = name.toLowerCase();
      if(im.nbtItemData.containsKey(name))
        return im.nbtItemData.get(name).getValue(status);
    }
    String key = im.labels.get(status);
    if(!item.hasItemMeta() || !item.getItemMeta().hasLore()) {
      if(status.equals("weight")) {
        if(ConfigManager.getWeightConfig().getItemWeight().containsKey(item.getType().name())) {
          return ConfigManager.getWeightConfig().getItemWeight().get(item.getType().name());
        }
      }else {
        if(im.minecraftItemData.containsKey(item.getType().name())) {
          return im.minecraftItemData.get(item.getType().name()).getValue(status);
        }
      }
      return 0;
    }
    for(String s : item.getItemMeta().getLore()) {
      String ss = Utils.removeColor(s);
      if(ss.contains(key) && ss.contains(im.split)) {
        String temp = ss.replaceAll("[^0-9+-/]", "");
        if(temp.contains("/")) {
          //范围类型
          String[] temp2 = temp.split("/");
          return im.random(Double.parseDouble(temp2[0]), Double.parseDouble(temp2[1]));
        }else {
          //单个数值类型
          return Double.parseDouble(temp);
        }
      }
    }
    return 0;
  }
  
  public static boolean isValidNBTItem(String name) {
    return im.nbtItemData.containsKey(name.toLowerCase());
  }
  
  private double random(double a,double b) {
    return Math.abs(a-b)*Math.random()+Math.min(a, b);
  }

  public static HashMap<String, String> getLabels() {
    return im.labels;
  }
  
  public static NBTItem getNBT() {
    return im.nbtItem;
  }

  private static RSEntry<Double, Double> getRSEntryDouble(String str){
    String[] temp = str.split("/");
      return new RSEntry<Double, Double>(
          Double.parseDouble(temp[0]),Double.parseDouble(temp[1]));
    }
    private static RSEntry<Integer, Integer> getRSEntryInt(String str){
      String[] temp = str.split("/");
      return new RSEntry<Integer, Integer>(
          Integer.parseInt(temp[0]),Integer.parseInt(temp[1]));
    }
}
