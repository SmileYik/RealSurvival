package miskyle.realsurvival.status.task;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.miskyle.mcpt.MCPT;

import miskyle.realsurvival.Msg;
import miskyle.realsurvival.data.ItemManager;
import miskyle.realsurvival.data.PlayerManager;
import miskyle.realsurvival.util.RSEntry;

public class WeightTask implements Runnable{

	@Override
	public void run() {
		PlayerManager.getActivePlayers().forEachValue(
				PlayerManager.getActivePlayers().mappingCount(), pd->{
					Player p = MCPT.plugin.getServer().getPlayer(pd.getPlayerName());
					double weight = 0;
					for(ItemStack item:p.getInventory().getContents()) {
						weight += ItemManager.getStatusValueOnly("WEIGHT", item);
					}
					RSEntry<Double, Double> v = pd.getWeight().setValue(weight);
					if(v.getLeft()<=pd.getWeight().getMaxValue()
							&&v.getRight()>pd.getWeight().getMaxValue()) {
						//TODO 给效果
						PlayerManager.bar.sendActionBar(p, Msg.tr("messages.weight-over"));
					}
				});
		
	}

}
