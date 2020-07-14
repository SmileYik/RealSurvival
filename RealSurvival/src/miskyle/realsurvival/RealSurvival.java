package miskyle.realsurvival;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.miskyle.mcpt.MCPT;

import miskyle.realsurvival.data.ConfigManager;
import miskyle.realsurvival.data.EffectManager;
import miskyle.realsurvival.data.ItemManager;
import miskyle.realsurvival.data.PlayerManager;

public class RealSurvival extends JavaPlugin{
	@Override
	public void onEnable() {
		MCPT.plugin = this;
		new ConfigManager(this);
		new Msg(this);
		PlayerManager.init();
		new EffectManager();
		new ItemManager();
	}
}
