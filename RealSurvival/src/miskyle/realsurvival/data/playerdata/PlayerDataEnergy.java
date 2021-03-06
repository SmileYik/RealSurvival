package miskyle.realsurvival.data.playerdata;

import miskyle.realsurvival.data.ConfigManager;
import miskyle.realsurvival.util.RsEntry;

public class PlayerDataEnergy extends PlayerDataStatus {

  /**
   * 修改属性值,若为加则数值为正/若为减则数值为负.(带效果.

   * @param value 值
   * @param effect 效果
   * @return 属性的原始值(左)及新值(右)
   */
  @Override
  public RsEntry<Double, Double> modify(double value, double effect) {
    if (value < 0) {
      value = value * (1 - effect);
    } else {
      value = value * (1 + effect);
    }
    double oldValue = this.value;
    this.value += value;
    double max = getMaxValue();
    if (super.value < 0) {
      super.value = 0;      
    } else if (super.value > max) {
      super.value = max;      
    }
    return new RsEntry<Double, Double>(oldValue, this.value);
  }

  /**
   * 修改属性值,若为加则数值为正/若为减则数值为负.

   * @param value 值
   * @return 属性的原始值(左)及新值(右)
   */
  @Override
  public RsEntry<Double, Double> modify(double value) {
    double oldValue = this.value;
    this.value += value;
    double max = getMaxValue();
    if (super.value < 0) {
      super.value = 0;      
    } else if (super.value > max) {
      super.value = max;      
    }
    return new RsEntry<Double, Double>(oldValue, this.value);
  }

  public double getProportionValue() {
    return getValue() / getMaxValue();
  }

  public double getMaxValue() {
    return super.getExtraValueSum() + ConfigManager.getEnergyConfig().getMaxValue();
  }

  /**
   * 取最大属性值的比例,p应属于0~100.

   * @param p 比例值
   * @return
   */
  public double getMaxValue(double p) {
    return getMaxValue() * p / 100;
  }
}
