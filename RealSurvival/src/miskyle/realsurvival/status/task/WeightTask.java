package miskyle.realsurvival.status.task;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import com.github.miskyle.mcpt.MCPT;
import miskyle.realsurvival.Msg;
import miskyle.realsurvival.api.status.StatusType;
import miskyle.realsurvival.data.ConfigManager;
import miskyle.realsurvival.data.EffectManager;
import miskyle.realsurvival.data.ItemManager;
import miskyle.realsurvival.data.PlayerManager;
import miskyle.realsurvival.util.RsEntry;

public class WeightTask implements Runnable {

  @Override
  public void run() {
    PlayerManager.getActivePlayers().forEachValue(
        PlayerManager.getActivePlayers().mappingCount(), pd -> {
          Player p = MCPT.plugin.getServer().getPlayer(pd.getPlayerName());
          if (!p.isOnline()) {
            PlayerManager.removePlayer(p.getName());
            return;
          }
          double weight = 0;
          for (ItemStack item : p.getInventory().getContents()) {
            weight += ItemManager.getStatusValueOnly("weight", item) 
                * ((item == null) ? 1 : item.getAmount());
          }
          RsEntry<Double, Double> v 
                = pd.getWeight().setValue(weight, pd.getEffect().getValue(StatusType.WEIGHT));
          double max = pd.getWeight().getMaxValue();
          v.set(v.getLeft() * 100 / max, v.getRight() * 100 / max);
          if (v.getLeft() <= pd.getWeight().getMaxValue() 
              && v.getRight() > pd.getWeight().getMaxValue()) {
            ConfigManager.getWeightConfig().getEffects().forEach(effect -> {
              EffectManager.effectPlayer(p, effect, StatusType.WEIGHT);
            });
            PlayerManager.bar.sendActionBar(p, Msg.tr("messages.weight.weight-over"));
          }
      });
  }
}
