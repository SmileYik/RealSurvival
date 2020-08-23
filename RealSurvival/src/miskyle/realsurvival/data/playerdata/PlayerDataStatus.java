package miskyle.realsurvival.data.playerdata;

import java.util.HashMap;

import miskyle.realsurvival.util.RSEntry;

public class PlayerDataStatus {
  protected double value;

  protected HashMap<String, Double> extraMaxValue = new HashMap<String, Double>();

  public PlayerDataStatus() {

  }

  public PlayerDataStatus(double value) {
    this.value = value;
  }

  /**
   * 修改属性值,若为加则数值为正/若为减则数值为负
   * 
   * @param value
   * @param effect
   * @return 属性的原始值(左)及新值(右)
   */
  public RSEntry<Double, Double> modify(double value, double effect) {
    double oldValue = this.value;
    this.value += value;
    if (value < 0)
      value = 0;
    return new RSEntry<Double, Double>(oldValue, this.value);
  }

  /**
   * 修改属性值,若为加则数值为正/若为减则数值为负
   * 
   * @param value
   * @return 属性的原始值(左)及新值(右)
   */
  public RSEntry<Double, Double> modify(double value) {
    double oldValue = this.value;
    this.value += value;
    if (value < 0)
      value = 0;
    return new RSEntry<Double, Double>(oldValue, this.value);
  }

  /**
   * 设置属性值为指定值
   * 
   * @param value
   * @return 属性的原始值(左)及新值(右)
   */
  public RSEntry<Double, Double> setValue(double value) {
    double oldValue = this.value;
    this.value = value;
    return new RSEntry<Double, Double>(oldValue, this.value);
  }

  /**
   * 获取其他插件所增加的额外属性值总和
   * 
   * @return
   */
  public double getExtraValueSum() {
    double sum = 0;
    for (double a : extraMaxValue.values()) {
      sum += a;
    }
    return sum;
  }

  /**
   * 设定某个插件所增加的额外属性最大值
   * 
   * @param pluginName 插件名称
   * @param value      属性数值
   */
  public void setExtraMaxValue(String pluginName, double value) {
    if (extraMaxValue.containsKey(pluginName)) {
      extraMaxValue.replace(pluginName, value);
    } else {
      extraMaxValue.put(pluginName, value);
    }
  }

  /**
   * 获取当前属性数值
   * 
   * @return
   */
  public double getValue() {
    return value;
  }

  /**
   * 获取插件所给予的额外属性值列表
   * 
   * @return
   */
  public HashMap<String, Double> getExtraMaxValue() {
    return extraMaxValue;
  }

}
