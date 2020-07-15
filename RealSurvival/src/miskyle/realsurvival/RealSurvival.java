package miskyle.realsurvival;

import java.io.File;

import org.bukkit.Bukkit;
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
		prepare();
		PlayerManager.init();
		new ConfigManager(this);
		new Msg(this);
		new EffectManager();
		new ItemManager();
		getLogger().info("RealSurvival is Ready");
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
}
