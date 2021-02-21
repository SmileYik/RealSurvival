package miskyle.realsurvival.api.effect;

import java.io.File;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import com.github.miskyle.mcpt.MCPT;
import miskyle.realsurvival.api.status.StatusType;

/**
 * \u5982\u4f55\u4f7f\u7528?.
 * \u521b\u5efa\u4e00\u4e2a\u65b0\u7c7b\u5e76\u7ee7\u627f\u6b64
 * \u7c7b,\u5e76\u4e14\u8fd9\u4e2a\u65b0\u7c7b\u4e0d\u5728\u4efb
 * \u4f55\u5305\u5185 RSEffect
 * \u5c06\u5728\u5f02\u6b65\u7ebf\u7a0b\u4e2d\u8fdb\u884c.

 * @author MiSkYle
 * @version 1.0.0
 */
public abstract class RsEffect {
  private final String pluginName;

  /**
   * \u521d\u59cb\u5316RSEffect,pluginName\u5c06\u4f1a\u81ea\u52a8\u5206\u914d.

   * @param pluginName \u63d2\u5165RSEffect\u7684\u540d\u5b57
   */
  public RsEffect(String pluginName) {
    this.pluginName = pluginName;
  }
  
  /**
   * \u82e5\u4e00\u76f4\u7b26\u5408\u6267\u884c\u8981\u6c42.
   * \u5c06\u4f1a\u6bcf\u79d2\u6267\u884c\u4e00\u6b21

   * @param player    \u73a9\u5bb6
   * @param type \u89E6\u53D1\u7684\u5C5E\u6027
   * @param amplifier \u5f3a\u5ea6
   * @param duration  \u6301\u7eed\u65f6\u95f4
   */
  public abstract void effect(Player player, StatusType type, int amplifier, int duration);
  
  /**
   * \u83b7\u53d6\u88ab\u63d2\u5165\u63d2\u4ef6\u7684\u6548\u679c\u7684\u540d\u5b57.

   * @return
   */
  public String getPluginName() {
    return pluginName;
  }

  /**
   * \u5199\u65e5\u5fd7,\u7ea7\u522b: \u4fe1\u606f.

   * @param message \u4FE1\u606F
   */
  public void info(String message) {
    MCPT.plugin.getLogger().info("RSEffect[" + pluginName + "] " + message);
  }

  /**
   * \u5199\u65e5\u5fd7, \u7ea7\u522b: \u8b66\u544a.

   * @param message \u4FE1\u606F
   */
  public void warning(String message) {
    MCPT.plugin.getLogger().warning("RSEffect[" + pluginName + "] " + message);
  }

  /**
   * \u83b7\u53d6\u914d\u7f6e\u6587\u4ef6\u8def\u5f84.

   * @return
   */
  public String getConfigPath() {
    return MCPT.plugin.getDataFolder() 
        + File.separator + "effect" + File.separator + pluginName + ".yml";
  }

  /**
   * \u83b7\u53d6\u914d\u7f6e\u6587\u4ef6.

   * @return
   */
  public YamlConfiguration getConfig() {
    return YamlConfiguration.loadConfiguration(new File(getConfigPath()));
  }
}
