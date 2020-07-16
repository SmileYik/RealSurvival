package miskyle.realsurvival;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.miskyle.mcpt.MCPT;

import miskyle.realsurvival.api.RealSurvivalAPI;
import miskyle.realsurvival.api.player.PlayerData;
import miskyle.realsurvival.command.CommandItem;
import miskyle.realsurvival.data.ConfigManager;
import miskyle.realsurvival.data.EffectManager;
import miskyle.realsurvival.data.ItemManager;
import miskyle.realsurvival.data.PlayerManager;

public class RealSurvival extends JavaPlugin implements RealSurvivalAPI{
	@Override
	public void onEnable(){
		MCPT.plugin = this;
		prepare();
		PlayerManager.init();
		new ItemManager();
		new ConfigManager(this);
		new Msg(this);
		new EffectManager();
		getLogger().info("RealSurvival is Ready");
		
		CommandItem cmdItem = new CommandItem();
		cmdItem.initialization();
		this.getCommand("rsi").setExecutor(cmdItem);
		this.getCommand("rsi").setTabCompleter(cmdItem);
	}
	
	private void prepare() {
		if(!getDataFolder().exists())
			getDataFolder().mkdir();
		if(!new File(getDataFolder()+"/effect/").exists())
			new File(getDataFolder()+"/effect/").mkdir();
		if(!new File(getDataFolder()+"/nbtitem/").exists())
			new File(getDataFolder()+"/nbtitem/").mkdir();
		if(!new File(getDataFolder()+"/item/water").exists())
			new File(getDataFolder()+"/item/water").mkdirs();
		if(!new File(getDataFolder()+"/playerdata/").exists())
			new File(getDataFolder()+"/playerdata/").mkdir();
		if(!new File(getDataFolder()+"/config.yml").exists()) 
			saveDefaultConfig();
		try{reloadConfig();}catch (Exception e){}
	}
	
	public static String getVersion(){
		String version;
        try {
            version = Bukkit.getServer().getClass()
            		.getPackage().getName().replace(".",  ",").split(",")[3];
        } catch (ArrayIndexOutOfBoundsException whatVersionAreYouUsingException) {
            return null;
        }
        return version;
	}

	@Override
	public PlayerData getPlayerData(String playerName) {
		return PlayerManager.getPlayerData(playerName);
	}

	@Override
	public boolean isPlayerActive(String playerName) {
		return PlayerManager.isActive(playerName);
	}
}
