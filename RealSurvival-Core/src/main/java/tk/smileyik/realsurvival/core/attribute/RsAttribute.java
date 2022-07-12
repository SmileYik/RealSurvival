package tk.smileyik.realsurvival.core.attribute;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

/**
 * 代表一个RealSurvival属性, 例如口渴等.
 * @param <T> 该属性读取物品格式化后的内容.
 */
public interface RsAttribute <T> extends Runnable {
  String PROPERTY_MAX_VALUE = "max-value";

  /**
   * 获取注册的属性id.
   * @return 属性id.
   */
  String getAttributeId();

  /**
   * 获取属性显示的名称.
   * @return 属性对外显示的名称.
   */
  String getDisplayName();

  /**
   * 获取当前属性的属性值的类型.
   * @return 当前属性的属性值的类型.
   */
  RsAttributeType getAttributeType();

  /**
   * 读取多个lore, 从lore中获取对当前游戏属性有帮助的值.
   * <p>
   *   lore列表中的值可以删除, 如果该值已经被匹配到.
   * </p>
   * @param lore 要读取的lore列表.
   * @return 从lore中读取到的对当前游戏属性有帮助的值的集合.
   */
  T read(List<String> lore);

  /**
   * 从Yaml配置文件片段中获取对当前游戏属性有帮助的值.
   * @param config 要读取的lore片段.
   * @return 从配置文件片段中获取到对当前游戏属性有帮助的值.
   */
  T read(ConfigurationSection config);

  /**
   * 获取所有配置属性.
   * @return 所有的配置属性.
   */
  Map<String, Object> getProperties();

  /**
   * 获取指定配置属性的值.
   * @param key 配置属性.
   * @return 指定配置属性的值.
   */
  Object getProperty(String key);

  /**
   * 设置指定配置属性的值.
   * @param key   配置属性.
   * @param value 配置属性的值.
   */
  void putProperty(String key, Object value);

  /**
   * 注册当前游戏属性.
   */
  void register();

  /**
   * 卸载当前游戏属性.
   */
  void unregister();

  /**
   * 当玩家进入服务器时.
   * @param player 进入服务器的玩家.
   */
  void onPlayerJoin(Player player);

  /**
   * 当玩家离开服务器时候.
   * @param player 离开服务器的玩家.
   */
  void onPlayerQuit(Player player);

}
