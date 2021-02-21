package miskyle.realsurvival.data.effect.sub;

import miskyle.realsurvival.api.effect.RsEffect;
import miskyle.realsurvival.api.player.PlayerData;
import miskyle.realsurvival.api.status.StatusType;
import miskyle.realsurvival.data.PlayerManager;
import org.bukkit.entity.Player;

public class EffectTemperatureUp extends RsEffect {

  public EffectTemperatureUp(String pluginName) {
    super(pluginName);
  }

  @Override
  public void effect(Player player, StatusType type, int amplifier, int duration) {
    PlayerData pd = PlayerManager.getPlayerData(player.getName());
    if (pd == null) {
      return;
    }
    pd.addTemperatureEffect(getEffectName(type), amplifier / 100D, 0, duration);
  }
  
  private String getEffectName(StatusType type) {
    if (type == null) {
      return getPluginName();
    }
    return getPluginName() + "." + type.name();
  }

}
