package miskyle.realsurvival.papi;

import com.github.miskyle.mcpt.i18n.I18N;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import miskyle.realsurvival.data.PlayerManager;
import miskyle.realsurvival.data.playerdata.Disease;
import miskyle.realsurvival.data.playerdata.PlayerData;
import miskyle.realsurvival.randomday.RandomDayManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlaceholderModeIi extends PlaceholderExpansion {
  private Plugin rs;
  private char c1;
  private String backString;
  private int length;
  
  /**
   * 初始化.

   * @param plugin 插件实例
   * @param c1 c1
   * @param c2 c2
   * @param length 长度
   */
  public PlaceholderModeIi(Plugin plugin, char c1, char c2, int length) {
    rs = plugin;
    this.c1 = c1;
    this.length = length;
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < length; ++i) {
      sb.append(c2);
    }
    backString = sb.toString();
    sb = null;
  }
  

  @Override
  public String onPlaceholderRequest(Player p, String arg) {
    if (p == null) {
      return "";
    }
    if (!PlayerManager.isActive(p)) {
      return I18N.tr("status.freezing");      
    }
    PlayerData pd = PlayerManager.getPlayerData(p.getName());
    Disease disease = pd.getDisease().getShowDisease();
    if (arg.equalsIgnoreCase("sleep")) {
      return getRateString(pd.getSleep().getValue() / pd.getSleep().getMaxValue());
    } else if (arg.equalsIgnoreCase("thirst")) {
      return getRateString(pd.getThirst().getValue() / pd.getThirst().getMaxValue());
    } else if (arg.equalsIgnoreCase("weight")) {
      return getRateString(pd.getWeight().getValue() / pd.getWeight().getMaxValue());
    } else if (arg.equalsIgnoreCase("energy")) {
      return getRateString(pd.getEnergy().getValue() / pd.getEnergy().getMaxValue());
    } else if (arg.equalsIgnoreCase("temperature")) {
      return I18N.tr("status.temperature." + pd.getTemperature().getValue().name().toLowerCase());
    } else if (arg.equalsIgnoreCase("disease")) {
      if (disease == null) {
        return I18N.tr("status.fine");
      } else {
        return disease.getDiseaseName();
      }
    } else if (arg.equalsIgnoreCase("drug-duration")) {
      if (disease == null) {
        return I18N.tr("status.fine");
      }
      return I18N.tr("status.disease.drug-duration", disease.getDuration());
    } else if (arg.equalsIgnoreCase("recovery")) {
      if (disease == null) {
        return I18N.tr("status.fine");
      }
      return getRateString(disease.getRecover());
    } else if (arg.equalsIgnoreCase("season")) {
      return I18N.tr("season." + RandomDayManager.getWorldSeason(p.getWorld().getName()));
    }

    return "";
  }
  
  private String getRateString(double rate) {
    int c1Len = (int) (rate * length);
    StringBuilder sb = new StringBuilder(backString);
    for (int i = 0; i < c1Len; ++i) {
      sb.setCharAt(i, c1);
    }
    return sb.toString();
  }

  @Override
  public String getIdentifier() {
    return "rs";
  }

  @Override
  public String getAuthor() {
    return "miSkYle";
  }

  @Override
  public String getVersion() {
    return rs.getDescription().getVersion();
  }
}