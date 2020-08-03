package miskyle.realsurvival.machine.furnace;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import com.github.miskyle.mcpt.MCPT;
import com.github.miskyle.mcpt.i18n.I18N;
import com.sun.istack.internal.NotNull;

import miskyle.realsurvival.data.recipe.FurnaceRecipe;
import miskyle.realsurvival.machine.util.GuiItemCreater;

public class FurnaceOpenEvent implements Runnable {
  private static HashMap<String, Integer> taskId = new HashMap<>();
  private final FurnaceHolder holder;
  private final FurnaceRecipe recipe;
  
  private FurnaceOpenEvent(@NotNull FurnaceHolder holder) {
    this.holder = holder;
    this.recipe = (FurnaceRecipe) holder.getTimer().getRecipe();
  }
  
  @Override
  public void run() {
    double temperature = holder.getTimer().getTemperature();
    
    if (recipe == null) {
      for (int slot : Furnace.TEMPERATURE_SLOTS) {
        holder.getInventory().setItem(slot, GuiItemCreater.getItem(
            Material.WHITE_STAINED_GLASS_PANE, "STAINED_GLASS_PANE", (short) 0, 
            I18N.tr("machine.furnace.temperature",_2f(temperature))));
      }
      return;
    }
    
    //温度
    if (recipe.getMinTemperature() >= 0) {
      //需求温度大于0
      if (temperature < 0) {
        for (int slot : Furnace.TEMPERATURE_SLOTS) {
          holder.getInventory().setItem(slot, GuiItemCreater.getItem(
              Material.LIGHT_BLUE_STAINED_GLASS_PANE, "STAINED_GLASS_PANE", (short) 3, 
              I18N.tr("machine.furnace.temperature",_2f(temperature))));
        }
      } else if (temperature >= recipe.getMaxTemperature()) {
        for (int slot : Furnace.TEMPERATURE_SLOTS) {
          holder.getInventory().setItem(slot, GuiItemCreater.getItem(
              Material.BROWN_STAINED_GLASS_PANE, "STAINED_GLASS_PANE", (short) 12, 
              I18N.tr("machine.furnace.temperature",_2f(temperature))));
        }
      } else {
        double temPass = temperature/recipe.getMinTemperature();
        if (temPass >= 0.25) {
          holder.getInventory().setItem(Furnace.TEMPERATURE_SLOTS.get(3), GuiItemCreater.getItem(
              Material.CYAN_STAINED_GLASS_PANE, "STAINED_GLASS_PANE", (short) 9, 
              I18N.tr("machine.furnace.temperature",_2f(temperature))));
        }
        if (temPass >= 0.5) {
          holder.getInventory().setItem(Furnace.TEMPERATURE_SLOTS.get(2), GuiItemCreater.getItem(
              Material.LIME_STAINED_GLASS_PANE, "STAINED_GLASS_PANE", (short) 5, 
              I18N.tr("machine.furnace.temperature",_2f(temperature))));
        }
        if(temPass >= 0.75) {
          holder.getInventory().setItem(Furnace.TEMPERATURE_SLOTS.get(1), GuiItemCreater.getItem(
              Material.YELLOW_STAINED_GLASS_PANE, "STAINED_GLASS_PANE", (short) 4, 
              I18N.tr("machine.furnace.temperature",_2f(temperature))));
        }
        if(temPass >= 1) {
          holder.getInventory().setItem(Furnace.TEMPERATURE_SLOTS.get(0), GuiItemCreater.getItem(
              Material.ORANGE_STAINED_GLASS_PANE, "STAINED_GLASS_PANE", (short) 1, 
              I18N.tr("machine.furnace.temperature",_2f(temperature))));
        }
      }
    } else {
      //需求温度小于0
      if (temperature >= 0) {
        for (int slot : Furnace.TEMPERATURE_SLOTS) {
          holder.getInventory().setItem(slot, GuiItemCreater.getItem(
              Material.ORANGE_STAINED_GLASS_PANE, "STAINED_GLASS_PANE", (short) 1, 
              I18N.tr("machine.furnace.temperature",_2f(temperature))));
        }
      } else if (temperature <= recipe.getMaxTemperature()) {
        for (int slot : Furnace.TEMPERATURE_SLOTS) {
          holder.getInventory().setItem(slot, GuiItemCreater.getItem(
              Material.BLUE_STAINED_GLASS_PANE, "STAINED_GLASS_PANE", (short) 11, 
              I18N.tr("machine.furnace.temperature",_2f(temperature))));
        }
      } else {
        double temPass = temperature/recipe.getMinTemperature();
        if (temPass >= 0.25) {
          holder.getInventory().setItem(Furnace.TEMPERATURE_SLOTS.get(3), GuiItemCreater.getItem(
              Material.ORANGE_STAINED_GLASS_PANE, "STAINED_GLASS_PANE", (short) 1, 
              I18N.tr("machine.furnace.temperature",_2f(temperature))));
        }
        if (temPass >= 0.5) {
          holder.getInventory().setItem(Furnace.TEMPERATURE_SLOTS.get(2), GuiItemCreater.getItem(
              Material.YELLOW_STAINED_GLASS_PANE, "STAINED_GLASS_PANE", (short) 4, 
              I18N.tr("machine.furnace.temperature",_2f(temperature))));
        if(temPass >= 0.75) {
          holder.getInventory().setItem(Furnace.TEMPERATURE_SLOTS.get(1), GuiItemCreater.getItem(
              Material.LIME_STAINED_GLASS_PANE, "STAINED_GLASS_PANE", (short) 5, 
              I18N.tr("machine.furnace.temperature",_2f(temperature))));
          }
        }
        if(temPass >= 1) {
          holder.getInventory().setItem(Furnace.TEMPERATURE_SLOTS.get(0), GuiItemCreater.getItem(
              Material.CYAN_STAINED_GLASS_PANE, "STAINED_GLASS_PANE", (short) 9, 
              I18N.tr("machine.furnace.temperature",_2f(temperature))));
        }
      }
    }
    
    //PASS
    double pass = (double)holder.getTimer().getTime()/recipe.getForgeTime();
    int amount = (int)pass;
    pass%=1;
    if(pass == 0 && amount > 0) {
      pass = 1;
    }
    if (pass >= 0.25) {
      holder.getInventory().setItem(Furnace.PROGRESS_SLOTS.get(3), GuiItemCreater.getItem(
          Material.CYAN_STAINED_GLASS_PANE, "STAINED_GLASS_PANE", (short) 9, 
          I18N.tr("machine.furnace.progress",_2f(pass*100))));
    }
    if (pass >= 0.5) {
      holder.getInventory().setItem(Furnace.PROGRESS_SLOTS.get(2), GuiItemCreater.getItem(
          Material.LIME_STAINED_GLASS_PANE, "STAINED_GLASS_PANE", (short) 5, 
          I18N.tr("machine.furnace.progress",_2f(pass*100))));
    }
    if(pass >= 0.75) {
      holder.getInventory().setItem(Furnace.PROGRESS_SLOTS.get(1), GuiItemCreater.getItem(
          Material.YELLOW_STAINED_GLASS_PANE, "STAINED_GLASS_PANE", (short) 4, 
          I18N.tr("machine.furnace.progress",_2f(pass*100))));
    }
    if(pass >= 1) {
      holder.getInventory().setItem(Furnace.PROGRESS_SLOTS.get(0), GuiItemCreater.getItem(
          Material.ORANGE_STAINED_GLASS_PANE, "STAINED_GLASS_PANE", (short) 1, 
          I18N.tr("machine.furnace.progress",_2f(pass*100))));
    }
    if(amount >= 1) {
      holder.getInventory().setItem(49, GuiItemCreater.getItem(
          Material.LIME_STAINED_GLASS_PANE, "STAINED_GLASS_PANE", (short) 5, 
          I18N.tr("machine.furnace.ok-slot",amount)));
    }
  }
  
  private String _2f(double d){
    return String.format("%.2f", d);
  }
  
  public static void openEvent(@NotNull FurnaceHolder holder,String playerName) {
    FurnaceOpenEvent foe = new FurnaceOpenEvent(holder);
    int taskId = Bukkit.getScheduler().runTaskTimerAsynchronously(MCPT.plugin, foe, 0, 20L).getTaskId();
    FurnaceOpenEvent.taskId.put(playerName, taskId);
  }
  
  public static void cancelEvent(String playerName) {
    if(taskId.containsKey(playerName)) {
      Bukkit.getScheduler().cancelTask(taskId.get(playerName));
    }
  }
}
