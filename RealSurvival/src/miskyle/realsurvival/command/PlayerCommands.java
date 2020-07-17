package miskyle.realsurvival.command;

import org.bukkit.entity.Player;

import com.github.miskyle.mcpt.i18n.I18N;

import miskyle.realsurvival.Msg;
import miskyle.realsurvival.data.PlayerManager;

public class PlayerCommands {
	@Cmd(subCmd = {"status"},
			args = {""},
			des = "cmd.des.player.status")
	public void getStatus(Player p,String[] args) {
		if(PlayerManager.isActive(p.getName())){
			p.sendMessage(
					PlayerManager.getPlayerData(p.getName()).getStatusMessage(p.isOp()));
		}else {
			p.sendMessage(
					Msg.getPrefix()+I18N.tr("cmd.info.player.status.freezing"));
		}
	}
}
