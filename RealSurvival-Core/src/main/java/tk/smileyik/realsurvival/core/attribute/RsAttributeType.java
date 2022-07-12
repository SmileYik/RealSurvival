package tk.smileyik.realsurvival.core.attribute;

public enum RsAttributeType {
  /**
   * 它是一对依据某个限定条件而变化的两个固定值,
   * 其不受上一个值的变化而变化.
   */
  TwoFixed,
  /**
   * 它是由一个依据某个限定条件所生成的固定值,
   * 它不依赖与上一次的值的影响.
   */
  OneFixed,
  /**
   * 它是一个依据时间或者其他条件持续变化的值,
   * 类似于进度条.
   */
  Progress;
}
