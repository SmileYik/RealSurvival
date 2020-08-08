package miskyle.realsurvival.status.task;

import miskyle.realsurvival.data.PlayerManager;

public class EffectTask implements Runnable {

  @Override
  public void run() {
    PlayerManager.getActivePlayers().forEachValue(PlayerManager.getActivePlayers().mappingCount(), pd -> {
      pd.getEffect().getEffect().forEach((k, v) -> {
        v.forEach((name, entry) -> {
          entry.setRight(entry.getRight() - 1);
          if (entry.getRight() <= 0)
            pd.getEffect().removeEffect(k, name);
        });
      });
      // 对温度操作
      pd.getEffect().getTemperatureEffect().forEach((k, v) -> {
        v.setRight(v.getRight() - 1);
        if (v.getRight() <= 0) {
          pd.getEffect().getTemperatureEffect().remove(k);
        }
      });
    });
  }

}
