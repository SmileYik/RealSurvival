package miskyle.realsurvival.status.task;

import org.bukkit.entity.Player;
import com.github.miskyle.mcpt.MCPT;
import miskyle.realsurvival.Msg;
import miskyle.realsurvival.api.status.StatusType;
import miskyle.realsurvival.data.ConfigManager;
import miskyle.realsurvival.data.EffectManager;
import miskyle.realsurvival.data.PlayerManager;
import miskyle.realsurvival.status.sleepinday.SleepInDay;
import miskyle.realsurvival.util.RsEntry;

public class SleepTask implements Runnable {

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
          if (pd.getSleep().isSleep()) {
            values = pd.getSleep().modify(ConfigManager.getSleepConfig().getIncreaseValue(),
                pd.getEffect().getValue(StatusType.SLEEP));
          } else {
            values = pd.getSleep().modify(-ConfigManager.getSleepConfig().getDecreaseValue(),
                pd.getEffect().getValue(StatusType.SLEEP));
          }
          double max = pd.getSleep().getMaxValue();
          values.setRight(values.getRight() / max * 100);
          values.setLeft(values.getLeft() / max * 100);
          sendMessage(p, values);
          attachEffect(p, pd.getSleep().getValue() * 100 / max);
          if (pd.getSleep().getValue() <= 0 && ConfigManager.getSleepConfig().getSleepZero()) {
            MCPT.plugin.getServer().getScheduler().runTask(MCPT.plugin, () -> {
              SleepInDay.sleep(p, p.getLocation());
            });
          }
        });

  }

  /**
   * 向玩家发送消息.

   * @param p 玩家名.
   * @param values 值.
   */
  public static void sendMessage(Player p, RsEntry<Double, Double> values) {
    for (RsEntry<Double, Double> entry : ConfigManager.getSleepConfig().getEffectData().keySet()) {
      if (entry.getRight() >= entry.getLeft() && values.getLeft() <= entry.getLeft()
          && values.getRight() > entry.getLeft() && values.getRight() <= entry.getRight()) {
        PlayerManager.bar.sendActionBar(
            p, Msg.tr("messages.sleep." + entry.getLeft() + "-" + entry.getRight()));
        break;
      } else if (values.getLeft() > entry.getLeft() && values.getRight() <= entry.getLeft()
          && values.getRight() >= entry.getRight()) {
        PlayerManager.bar.sendActionBar(
            p, Msg.tr("messages.sleep." + entry.getLeft() + "-" + entry.getRight()));
        break;
      }
    }
  }

  /**
   * 附加效果(buff.

   * @param p 玩家.
   * @param value sleep属性值.
   */
  public static void attachEffect(Player p, double value) {
    for (RsEntry<Double, Double> entry : ConfigManager.getSleepConfig().getEffectData().keySet()) {
      if (value < Math.max(entry.getLeft(), entry.getRight()) 
          && value >= Math.min(entry.getLeft(), entry.getRight())) {
        ConfigManager.getSleepConfig().getEffectData().get(entry).forEach(e -> {
          EffectManager.effectPlayer(p, e, StatusType.SLEEP);
        });
        return;
      }
    }
  }

}
