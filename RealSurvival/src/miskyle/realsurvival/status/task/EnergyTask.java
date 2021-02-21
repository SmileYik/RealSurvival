package miskyle.realsurvival.status.task;

import org.bukkit.entity.Player;
import com.github.miskyle.mcpt.MCPT;
import miskyle.realsurvival.Msg;
import miskyle.realsurvival.api.status.StatusType;
import miskyle.realsurvival.data.ConfigManager;
import miskyle.realsurvival.data.EffectManager;
import miskyle.realsurvival.data.PlayerManager;
import miskyle.realsurvival.util.RsEntry;

public class EnergyTask implements Runnable {

  @Override
  public void run() {
    PlayerManager.getActivePlayers().forEachValue(
        PlayerManager.getActivePlayers().mappingCount(), pd -> {
          Player p = MCPT.plugin.getServer().getPlayer(pd.getPlayerName());
          if (!p.isOnline()) {
            PlayerManager.removePlayer(p.getName());
            return;
          }
          RsEntry<Double, Double> values;
          if (ConfigManager.getEnergyConfig().getDecreaseSneaking() != 0 && p.isSneaking()) {
            values = pd.getEnergy().modify(-ConfigManager.getEnergyConfig().getDecreaseSneaking(),
                pd.getEffect().getValue(StatusType.ENERGY));
          } else if (ConfigManager.getEnergyConfig().getDecreaseSprinting() != 0 
              && p.isSprinting()) {
            values = pd.getEnergy().modify(-ConfigManager.getEnergyConfig().getDecreaseSprinting(),
                pd.getEffect().getValue(StatusType.ENERGY));
          } else {
            values = pd.getEnergy().modify(+ConfigManager.getEnergyConfig().getIncreaseValue(),
                pd.getEffect().getValue(StatusType.ENERGY));
          }
          double max = pd.getEnergy().getMaxValue();
          values.set(values.getLeft() * 100 / max, values.getRight() * 100 / max);
          attachEffect(p, pd.getEnergy().getValue() * 100 / max);
        });

  }

  /**
   * 给玩家发送一条消息.

   * @param p 玩家
   * @param values 改属性值与修改前的值.
   */
  public static void sendMessage(Player p, RsEntry<Double, Double> values) {
    for (RsEntry<Double, Double> entry : ConfigManager.getEnergyConfig().getEffectData().keySet()) {
      if (entry.getRight() >= entry.getLeft() && values.getLeft() <= entry.getLeft()
          && values.getRight() > entry.getLeft() && values.getRight() <= entry.getRight()) {
        PlayerManager.bar.sendActionBar(p, 
            Msg.tr("messages.energy." + entry.getLeft() + "-" + entry.getRight()));
        break;
      } else if (values.getLeft() > entry.getLeft() && values.getRight() <= entry.getLeft()
          && values.getRight() >= entry.getRight()) {
        PlayerManager.bar.sendActionBar(p, 
            Msg.tr("messages.energy." + entry.getLeft() + "-" + entry.getRight()));
        break;
      }
    }
  }

  /**
   * 判断玩家是否要增加相应的buff.

   * @param p 玩家
   * @param value 属性值.
   */
  public static void attachEffect(Player p, double value) {
    for (RsEntry<Double, Double> entry : ConfigManager.getEnergyConfig().getEffectData().keySet()) {
      if (value < Math.max(entry.getLeft(), entry.getRight()) 
          && value >= Math.min(entry.getLeft(), entry.getRight())) {
        ConfigManager.getEnergyConfig().getEffectData().get(entry).forEach(e -> {
          EffectManager.effectPlayer(p, e, StatusType.ENERGY);
        });
        return;
      }
    }
  }

}
