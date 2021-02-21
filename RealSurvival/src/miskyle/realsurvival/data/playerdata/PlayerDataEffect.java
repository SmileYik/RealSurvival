package miskyle.realsurvival.data.playerdata;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import miskyle.realsurvival.api.status.StatusType;
import miskyle.realsurvival.util.RsEntry;

public class PlayerDataEffect {
  // 储存的都是比例值
  // 在修改属性大小时,将会把修改值乘上此比例值

  private HashMap<StatusType, ConcurrentHashMap<String, RsEntry<Double, Integer>>> effect;
  private ConcurrentHashMap<String, RsEntry<RsEntry<Double, Double>, Integer>> temperatureEffect;

  /**
   * 初始化.
   */
  public PlayerDataEffect() {
    temperatureEffect = new ConcurrentHashMap<>();
    effect = new HashMap<StatusType, ConcurrentHashMap<String, RsEntry<Double, Integer>>>();
    effect.put(StatusType.SLEEP, new ConcurrentHashMap<String, RsEntry<Double, Integer>>());
    effect.put(StatusType.THIRST, new ConcurrentHashMap<String, RsEntry<Double, Integer>>());
    effect.put(StatusType.ENERGY, new ConcurrentHashMap<String, RsEntry<Double, Integer>>());
    effect.put(StatusType.WEIGHT, new ConcurrentHashMap<String, RsEntry<Double, Integer>>());
  }

  /**
   * 增加普通属性附加效果(不包括体温).

   * @param status      属性类型
   * @param effectName  效果名
   * @param effectValue 效果值
   * @param duration    持续时间
   */
  public void addEffect(StatusType status, String effectName, double effectValue, int duration) {
    ConcurrentHashMap<String, RsEntry<Double, Integer>> map = effect.get(status);
    if (map.contains(effectName)) {
      map.replace(effectName, new RsEntry<Double, Integer>(effectValue, duration));
    } else {
      map.put(effectName, new RsEntry<Double, Integer>(effectValue, duration));
    }
  }

  /**
   * 增加体温效果.

   * @param effectName 效果值
   * @param up         增加最高温耐受值
   * @param down       增加体温最低温耐受值
   * @param duration 持续时间
   */
  public void addEffect(String effectName, double up, double down, int duration) {
    if (temperatureEffect.contains(effectName)) {
      temperatureEffect.replace(effectName,
          new RsEntry<RsEntry<Double, Double>, Integer>(
              new RsEntry<Double, Double>(down, up), duration));
    } else {
      temperatureEffect.put(effectName,
          new RsEntry<RsEntry<Double, Double>, Integer>(
              new RsEntry<Double, Double>(down, up), duration));
    }
  }

  /**
   * 获取某一属性最终倍率值, 此值不可直接与修改值相乘(未加上 1).

   * @param status 属性类型.
   * @return
   */
  public double getValue(StatusType status) {
    double value = 0;
    for (RsEntry<Double, Integer> v : effect.get(status).values()) {
      value += v.getLeft();      
    }
    return value;
  }

  /**
   * 获取温度的耐受值增加值 此值将直接与玩家的耐受值简单相加.

   * @return
   */
  public RsEntry<Double, Double> getValue() {
    RsEntry<Double, Double> v = new RsEntry<Double, Double>(0D, 0D);
    temperatureEffect.values().forEach(vv -> {
      v.setLeft(v.getLeft() + vv.getLeft().getLeft());
      v.setRight(v.getRight() + vv.getLeft().getRight());
    });
    return v;
  }

  public void removeEffect(StatusType status, String effectName) {
    effect.get(status).remove(effectName);
  }

  public HashMap<StatusType, ConcurrentHashMap<String, RsEntry<Double, Integer>>> getEffect() {
    return effect;
  }

  public void setEffect(
      HashMap<StatusType, ConcurrentHashMap<String, RsEntry<Double, Integer>>> effect) {
    this.effect = effect;
  }

  /**
   * 从保存的带有信息的String中取读信息.

   * @param str 带有信息的字符串
   */
  public void setEffect(String str) {
    if (str == null) {
      return;      
    }
    for (String status : str.split(";")) {
      if (!str.contains(":")) {
        continue;
      }
      String[] temp1 = status.split(":");
      for (String effects : temp1[1].split("/")) {
        String[] temp2 = effects.split(",");
        addEffect(StatusType.valueOf(status), temp2[0], 
            Double.parseDouble(temp2[1]), Integer.parseInt(temp2[2]));
      }
    }
  }

  /**
   * 获取温度效果的保存数据的字符串.

   * @return
   */
  public String getTemperatureSaveString() {
    StringBuilder sb = new StringBuilder();
    if (temperatureEffect.isEmpty()) {
      return ";";      
    }
    temperatureEffect.forEach((k, v) -> {
      sb.append(k + "," + v.getLeft().getLeft())
          .append("," + v.getLeft().getRight() + "," + v.getRight() + ";");
    });
    return sb.deleteCharAt(sb.length() - 1).toString();
  }

  public ConcurrentHashMap<String, 
      RsEntry<RsEntry<Double, Double>, Integer>> getTemperatureEffect() {
    return temperatureEffect;
  }

  public void setTemperatureEffect(
      ConcurrentHashMap<String, RsEntry<RsEntry<Double, Double>, Integer>> temperatureEffect) {
    this.temperatureEffect = temperatureEffect;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    effect.forEach((k, v) -> {
      sb.append(k.name());
      sb.append(":");
      v.forEach((name, entry) -> {
        sb.append(name + ",");
        sb.append(entry.getLeft() + ",");
        sb.append(entry.getRight() + "/");
      });
      sb.setCharAt(sb.length() - 1, ';');
    });
    return sb.deleteCharAt(sb.length() - 1).toString();
  }
}
