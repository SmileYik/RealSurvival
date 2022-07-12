package tk.smileyik.realsurvival.core.item.lore;

public interface LoreParser<T> {
  /**
   * 获取Lore标签名.
   * @return Lore标签名.
   */
  String getTag();

  /**
   * 获取Lore标签与实际数值所分隔开的分隔字符串.
   * <p>
   *   如有以下一行Lore:
   *   <span>Damage: 10</span>
   *   那么<span>Damage</span>为标签名,
   *   而<span>:</span>为分隔字符串,
   *   <span>10</span>为属性值.
   * </p>
   *
   * @return 将Lore的标签与其值分隔开来的字符串.
   */
  String getSeparator();

  /**
   * 获取值所匹配的正则表达式.
   * <p>
   *   该标签词条对应的值都得符合该正则表达式.
   * </p>
   * @return 正则表达式字符串.
   */
  String getValueFormat();

  /**
   * 获取Nbt Config的键的文本.
   * @return 键文本.
   */
  String getNbtConfigTag();

  /**
   * 判断给定的Lore是否满足当前属性的格式要求.
   * @param lore 要判断的lore.
   * @return 如果满足格式要求则返回true, 不满足则返回false.
   */
  boolean isMatch(String lore);

  /**
   * 读取给定的Lore中的属性值.
   * @param lore 给定的Lore.
   * @return 如果能将lore转换为对应的值则返回读取到的值,
   *         如果不能则返回null.
   */
  T readLore(String lore);

  /**
   * 读取给定的Lore中的属性值.
   * @param lore 给定的Lore.
   * @return 如果能将lore转换为对应的值则返回读取到的值,
   *         如果不能则返回给定的默认值.
   */
  T readLore(String lore, T defaultValue);
}
