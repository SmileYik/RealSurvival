package miskyle.realsurvival.data.config.status;

import java.util.ArrayList;

import miskyle.realsurvival.data.effect.EffectData;

public class WeightConfig {
  private boolean enable;
  private double maxValue;
  private ArrayList<EffectData> effects;

  public WeightConfig() {

  }

  public boolean isEnable() {
    return enable;
  }

  public void setEnable(boolean enable) {
    this.enable = enable;
  }

  public double getMaxValue() {
    return maxValue;
  }

  public void setMaxValue(double maxValue) {
    this.maxValue = maxValue;
  }

  public ArrayList<EffectData> getEffects() {
    return effects;
  }

  public void setEffects(String effectString) {
    effects = new ArrayList<EffectData>();
    if (effectString.equalsIgnoreCase("null"))
      return;
    for (String s : effectString.split(";"))
      effects.add(EffectData.loadFromString(s));
  }

}
