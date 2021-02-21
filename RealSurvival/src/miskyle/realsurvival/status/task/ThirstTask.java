package miskyle.realsurvival.status.task;

import org.bukkit.entity.Player;
import com.github.miskyle.mcpt.MCPT;
import miskyle.realsurvival.Msg;
import miskyle.realsurvival.api.status.StatusType;
import miskyle.realsurvival.data.ConfigManager;
import miskyle.realsurvival.data.EffectManager;
import miskyle.realsurvival.data.PlayerManager;
import miskyle.realsurvival.util.RsEntry;

public class ThirstTask implements Runnable {

  @Override
  public void run() {
    PlayerManager.getActivePlayers().forEachValue(
        PlayerManager.getActivePlayers().mappingCount(), pd -> {
          Player p = MCPT.plugin.getServer().getPlayer(pd.getPlayerName());
          if (!p.isOnline()) {
            PlayerManager.removePlayer(p.getName());
            return;
          }
          RsEntry<Double, Double> values = 
              pd.getThirst().modify(-ConfigManager.getThirstConfig().getDecreaseValue(),
              pd.getEffect().getValue(StatusType.THIRST));
          double max = pd.getThirst().getMaxValue();
          values.set(values.getLeft() * 100 / max, values.getRight() * 100 / max);
          for (RsEntry<Double, Double> entry : 
              ConfigManager.getThirstConfig().getEffectData().keySet()) {
            
            // 代表属性值由高到低减少
            if (entry.getLeft() > entry.getRight() && values.getLeft() > entry.getLeft()
                && values.getRight() <= entry.getLeft() && values.getRight() > entry.getRight()) {
              PlayerManager.bar.sendActionBar(
                  p, Msg.tr("messages.thirst." + entry.getLeft() + "-" + entry.getRight()));
            }

            if (values.getRight() >= Math.min(entry.getLeft(), entry.getRight())
                && values.getRight() < Math.max(entry.getLeft(), entry.getRight())) {
              ConfigManager.getThirstConfig().getEffectData().get(entry).forEach(effect -> {
                EffectManager.effectPlayer(p, effect, StatusType.THIRST);
              });
            }
          }
        });

  }

}
