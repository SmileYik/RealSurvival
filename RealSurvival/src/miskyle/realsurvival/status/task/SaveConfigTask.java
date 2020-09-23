package miskyle.realsurvival.status.task;

import org.bukkit.entity.Player;

import com.github.miskyle.mcpt.MCPT;

import miskyle.realsurvival.data.PlayerManager;
import miskyle.realsurvival.machine.MachineManager;
import miskyle.realsurvival.randomday.RandomDayManager;

public class SaveConfigTask implements Runnable {

  @Override
  public void run() {
    for (Player p : MCPT.plugin.getServer().getOnlinePlayers()) {
      if (PlayerManager.isActive(p)) {
        PlayerManager.getPlayerData(p.getName()).save();
      }
    }
    RandomDayManager.save();
    MachineManager.saveTimers();
    MachineManager.saveMachineAccesses();
  }

}
