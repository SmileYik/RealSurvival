package miskyle.realsurvival.papi;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.miskyle.mcpt.i18n.I18N;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import miskyle.realsurvival.data.PlayerManager;
import miskyle.realsurvival.data.playerdata.Disease;
import miskyle.realsurvival.data.playerdata.PlayerData;
import miskyle.realsurvival.randomday.RandomDayManager;

public class Papi extends PlaceholderExpansion  {
	private  Plugin rs;

	public Papi(Plugin plugin) {
		rs = plugin;
	}

	@Override
	public String onPlaceholderRequest(Player p, String arg) {
		if (p == null) {return "";}
		if(!PlayerManager.isActive(p))
			return I18N.tr("status.freezing");
		PlayerData pd = PlayerManager.getPlayerData(p.getName());
		Disease disease = pd.getDisease().getShowDisease();
		if(arg.equalsIgnoreCase("sleep")) {
		  return _2f(pd.getSleep().getValue()/pd.getSleep().getMaxValue()*100)+"%";		  
		}else  if(arg.equalsIgnoreCase("thirst")) {
		  return _2f(pd.getThirst().getValue()/pd.getThirst().getMaxValue()*100)+"%";		  
		}else if(arg.equalsIgnoreCase("weight")) {
		  return _2f(pd.getWeight().getValue()/pd.getWeight().getMaxValue()*100)+"%";		  
		}else if(arg.equalsIgnoreCase("energy")) {
		  return _2f(pd.getEnergy().getValue()/pd.getEnergy().getMaxValue()*100)+"%";		  
		}else if(arg.equalsIgnoreCase("temperature")) {
		  return I18N.tr("status.temperature."+pd.getTemperature().getValue().name().toLowerCase());
		}else if(arg.equalsIgnoreCase("disease")) {
		  if(disease == null) {
		    return I18N.tr("status.fine");
		  }else {
		    return disease.getDiseaseName();
		  }
		}else if(arg.equalsIgnoreCase("drug-duration")) {
		  if(disease == null) {
		    return I18N.tr("status.fine");
		  }
		  return I18N.tr("status.disease.drug-duration",disease.getDuration());
		}else if(arg.equalsIgnoreCase("recovery")) {
          if(disease == null) {
            return I18N.tr("status.fine");
          }
		  return I18N.tr("status.disease.recovery",_2f(disease.getRecover()));
        }else if(arg.equalsIgnoreCase("season")) {
          return I18N.tr("season."+RandomDayManager.getWorldSeason(p.getWorld().getName()));
        }
		
		return "";
	}
	
	private String _2f(double d){
		return String.format("%.2f", d);
	}

	@Override
	public String getIdentifier() {
		return "rs";
	}

	@Override
	public String getAuthor() {
		return "miSkYle";
	}

	@Override
	public String getVersion() {
		return rs.getDescription().getVersion();
	}
}