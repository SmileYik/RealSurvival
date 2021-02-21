package miskyle.realsurvival.data.config.status;

import java.util.ArrayList;
import java.util.HashMap;
import miskyle.realsurvival.data.effect.EffectData;
import miskyle.realsurvival.util.RsEntry;

public class SleepConfig {
  private boolean enable;
  private boolean sleepInDay;
  private double maxValue;
  private double decreaseValue;
  private double increaseValue;
  private boolean sleepZero;

  private HashMap<RsEntry<Double, Double>, ArrayList<EffectData>> effectData;

  public SleepConfig() {

  }

  public boolean isEnable() {
    return enable;
  }

  public void setEnable(boolean enable) {
    this.enable = enable;
  }

  public boolean isSleepInDay() {
    return sleepInDay;
  }

  public void setSleepInDay(boolean sleepInDay) {
    this.sleepInDay = sleepInDay;
  }

  public double getMaxValue() {
    return maxValue;
  }

  public void setMaxValue(double maxValue) {
    this.maxValue = maxValue;
  }

  public double getDecreaseValue() {
    return decreaseValue;
  }

  public void setDecreaseValue(double decreaseValue) {
    this.decreaseValue = decreaseValue;
  }

  public double getIncreaseValue() {
    return increaseValue;
  }

  public void setIncreaseValue(double increaseValue) {
    this.increaseValue = increaseValue;
  }

  public HashMap<RsEntry<Double, Double>, ArrayList<EffectData>> getEffectData() {
    return effectData;
  }

  /**
   * 设定sleep属性所遭受的效果(buff).

   * @param effectData 储存数据的字符串.
   */
  public void setEffectData(HashMap<String, String> effectData) {
    this.effectData = new HashMap<RsEntry<Double, Double>, ArrayList<EffectData>>();
    effectData.forEach((s1, s2) -> {
      String[] temp = s1.split("-");
      if (s2.equalsIgnoreCase("null")) {
        this.effectData.put(
            new RsEntry<Double, Double>(Double.parseDouble(temp[0]), Double.parseDouble(temp[1])),
            new ArrayList<EffectData>());
      } else {
        String[] temp2 = s2.split(";");
        ArrayList<EffectData> list = new ArrayList<>();
        for (String s : temp2) {
          list.add(EffectData.loadFromString(s));          
        }
        this.effectData.put(
            new RsEntry<Double, Double>(
                Double.parseDouble(temp[0]), Double.parseDouble(temp[1])), list);
      }
    });
  }

  public boolean getSleepZero() {
    return sleepZero;
  }

  public void setSleepZero(boolean sleepZero) {
    this.sleepZero = sleepZero;
  }

}
