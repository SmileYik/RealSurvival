package miskyle.realsurvival.status.task;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.miskyle.mcpt.MCPT;

import miskyle.realsurvival.data.PlayerManager;

public class WeightTask implements Runnable{

	@Override
	public void run() {
		PlayerManager.getActivePlayers().forEachValue(
				PlayerManager.getActivePlayers().mappingCount(), pd->{
					Player p = MCPT.plugin.getServer().getPlayer(pd.getPlayerName());
					for(ItemStack item:p.getInventory().getContents()) {
						
					}
				});
		
	}

}
