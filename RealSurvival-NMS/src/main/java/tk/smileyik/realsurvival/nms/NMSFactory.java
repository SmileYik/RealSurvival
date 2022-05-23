package tk.smileyik.realsurvival.nms;

import org.bukkit.Bukkit;

public class NMSFactory {
  private static final String version;

  static {
    version = Bukkit.getServer().getClass()
                    .getPackage().getName().split("\\.")[3];
  }

  public static String getVersion() {
    return version;
  }
}
