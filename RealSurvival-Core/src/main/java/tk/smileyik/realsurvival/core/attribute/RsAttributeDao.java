package tk.smileyik.realsurvival.core.attribute;

/**
 * 对游戏属性所带来的存档数据进行操作.
 * @param <T> 游戏属性的存档数据.
 */
public interface RsAttributeDao <T extends RsAttributeData> {

  /**
   * 加载玩家属性值.
   * @return 玩家该属性的属性值.
   */
  T load();

  /**
   * 将玩家的属性值数据保存(缓存).
   * @param data 玩家的属性值数据.
   */
  void save(T data);

  /**
   * 使用Java对象直接修改玩家数据.
   * @param data
   */
  void modify(T data);
}
