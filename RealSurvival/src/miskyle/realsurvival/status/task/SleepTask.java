package miskyle.realsurvival.status.task;

import org.bukkit.entity.Player;

import com.github.miskyle.mcpt.MCPT;

import miskyle.realsurvival.Msg;
import miskyle.realsurvival.api.status.StatusType;
import miskyle.realsurvival.data.ConfigManager;
import miskyle.realsurvival.data.EffectManager;
import miskyle.realsurvival.data.PlayerManager;
import miskyle.realsurvival.status.sleepinday.SleepInDay;
import miskyle.realsurvival.util.RSEntry;

public class SleepTask implements Runnable{

	@Override
	public void run() {
		PlayerManager.getActivePlayers().forEachValue(
				PlayerManager.getActivePlayers().mappingCount(), pd->{
					RSEntry<Double, Double> values;
					if(pd.getSleep().isSleep()) {
						values = pd.getSleep().modify(ConfigManager.getSleepConfig().getIncreaseValue(),pd.getEffect().getValue(StatusType.SLEEP));
					}else {
						values = pd.getSleep().modify(-ConfigManager.getSleepConfig().getDecreaseValue(),pd.getEffect().getValue(StatusType.SLEEP));
					}
					double max = pd.getSleep().getMaxValue();
					values.setRight(values.getRight()/max*100);
					values.setLeft(values.getLeft()/max*100);
					Player p = MCPT.plugin.getServer().getPlayer(pd.getPlayerName());
					sendMessage(p, values);
					attachEffect(p, pd.getSleep().getValue()*100/max);
					if(pd.getSleep().getValue()<=0) {
						MCPT.plugin.getServer().getScheduler().runTask(MCPT.plugin, ()->{
							SleepInDay.sleep(p, p.getLocation());							
						});
					}
		});
		
	}
	
	public static void sendMessage(Player p,RSEntry<Double, Double> values) {
		for(RSEntry<Double, Double> entry: ConfigManager.getSleepConfig().getEffectData().keySet()) {
			if(entry.getRight()>=entry.getLeft()
					&&values.getLeft()<=entry.getLeft()
					&& values.getRight()>entry.getLeft()
					&& values.getRight()<=entry.getRight()) {
				PlayerManager.bar.sendActionBar(
						p, Msg.tr("messages.sleep."+entry.getLeft()+"-"+entry.getRight()));
				break;
			}else if(values.getLeft()>entry.getLeft()
					&& values.getRight()<=entry.getLeft()
					&& values.getRight()>=entry.getRight()) {
				PlayerManager.bar.sendActionBar(
						p, Msg.tr("messages.sleep."+entry.getLeft()+"-"+entry.getRight()));
				break;
			}
		}
	}
	
	public static void attachEffect(Player p,double value) {
		for(RSEntry<Double, Double> entry: ConfigManager.getSleepConfig().getEffectData().keySet()) {
			if(value<Math.max(entry.getLeft(), entry.getRight())
					&& value>=Math.min(entry.getLeft(), entry.getRight())) {
				ConfigManager.getSleepConfig().getEffectData().get(entry).forEach(e->{
					EffectManager.effectPlayer(p, e);
				});
				return;
			}
		}
	}
	
}
