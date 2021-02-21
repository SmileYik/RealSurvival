package miskyle.realsurvival.data.config.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import miskyle.realsurvival.data.effect.EffectData;

public class TemperatureConfig {
  private double max;
  private double min;
  private boolean enable;

  private ArrayList<EffectData> maxEffect;
  private ArrayList<EffectData> minEffect;
  private HashMap<String, Double> block;

  public TemperatureConfig() {
    block = new HashMap<String, Double>();
  }

  /**
   * 设置方块温度.

   * @param list 方块温度列表.
   */
  public void setBlock(List<String> list) {
    list.forEach(line -> {
      String[] temp = line.split(":");
      block.put(temp[0], Double.parseDouble(temp[1]));
    });
  }
  
  public void setBlock(HashMap<String, Double> block) {
    this.block = block;
  }

  public double getMax() {
    return max;
  }

  public void setMax(double max) {
    this.max = max;
  }

  public double getMin() {
    return min;
  }

  public void setMin(double min) {
    this.min = min;
  }

  public HashMap<String, Double> getBlock() {
    return block;
  }

  public boolean isEnable() {
    return enable;
  }

  public void setEnable(boolean enable) {
    this.enable = enable;
  }

  /**
   * 获取方块温度.

   * @param type 方块类型.
   * @return
   */
  public double getBlockTemperature(String type) {
    if (block.containsKey(type)) {
      return block.get(type);      
    }
    return 0;
  }

  public ArrayList<EffectData> getMaxEffect() {
    return maxEffect;
  }

  public void setMaxEffect(ArrayList<EffectData> maxEffect) {
    this.maxEffect = maxEffect;
  }

  /**
   * 设置最大效果.

   * @param effectString 保存数据的字符串.
   */
  public void setMaxEffect(String effectString) {
    maxEffect = new ArrayList<EffectData>();
    if (effectString.equalsIgnoreCase("null")) {
      return;      
    }
    for (String s : effectString.split(";")) {
      maxEffect.add(EffectData.loadFromString(s));      
    }
  }

  public ArrayList<EffectData> getMinEffect() {
    return minEffect;
  }

  public void setMinEffect(ArrayList<EffectData> minEffect) {
    this.minEffect = minEffect;
  }

  /**
   * 设置最小效果.

   * @param effectString 保存数据的字符串.
   */
  public void setMinEffect(String effectString) {
    minEffect = new ArrayList<EffectData>();
    if (effectString.equalsIgnoreCase("null")) {
      return;      
    }
    for (String s : effectString.split(";")) {
      minEffect.add(EffectData.loadFromString(s));      
    }
  }

}
