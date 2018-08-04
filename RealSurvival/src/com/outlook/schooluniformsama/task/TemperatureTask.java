package com.outlook.schooluniformsama.task;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.outlook.schooluniformsama.RealSurvival;
import com.outlook.schooluniformsama.api.data.EffectType;
import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.item.ItemLoreData;
import com.outlook.schooluniformsama.data.player.PlayerData;
import com.outlook.schooluniformsama.randomday.RandomDayManager;
import com.outlook.schooluniformsama.util.Util;

public class TemperatureTask implements Runnable{
	@Override
	public void run() {
		for(PlayerData pd:Data.playerData.values()){
			Player p = RealSurvival.getPlayer(pd.getUuid());
			
			if(p==null || pd==null || p.isDead())return;
			pd.change(EffectType.TEMPERATURE, (Data.versionData[0] >= 9)?getTemFix(p.getLocation(), p.getInventory()):getTemFixLow(p.getLocation(), p.getInventory()));
		}
	}
	
	public static double getBaseTemperature(Location loc,boolean isFix){
		double baseTemperature;
		baseTemperature = RandomDayManager.getBiomeTemperature(loc.getWorld().getBiome(loc.getBlockX(), loc.getBlockZ()));
		baseTemperature+=RandomDayManager.getTodayData(loc.getWorld().getName())[4] +getBlocks(loc,isFix);
		baseTemperature+=loc.getWorld().hasStorm()?RandomDayManager.getTemperaturefix()[2]:0;
		long time = loc.getWorld().getTime();
		
		if(time>22550)baseTemperature+=RandomDayManager.getTemperaturefix()[1]*(24000-time);
		else if(time<6000) baseTemperature+=RandomDayManager.getTemperaturefix()[1]*(time-4550);
		else baseTemperature+=RandomDayManager.getTemperaturefix()[0]*(time-6000);
		
		if(loc.getBlockY()>64) baseTemperature+=RandomDayManager.getTemperaturefix()[3]*(loc.getBlockY()-64);
		return baseTemperature;
	}
	
	private double getTemFix(Location loc,PlayerInventory inv){
		int amount = 1;
		double fixMax = 100,fixMin = 100;
		for(ItemStack is : inv.getArmorContents()){
			if(is==null || !is.hasItemMeta() || !is.getItemMeta().hasLore()) continue;
			String temp = ItemLoreData.getLoreString("Temperature", is.clone().getItemMeta().getLore());
			if(temp == null) continue;
			if(temp.contains("--")){
				double tem1 = Double.parseDouble(temp.split("--")[0].replaceAll("[^0-9.+-]", "")),
				              tem2 = Double.parseDouble(temp.split("--")[1].replaceAll("[^0-9.+-]", ""));
				fixMax+=tem1>tem2?tem1:tem2;
				fixMin+=tem1<tem2?tem1:tem2;
			}else{
				double tem = Double.parseDouble(temp.replaceAll("[^0-9.+-]", ""));
				fixMax+=tem;
				fixMin+=tem;
			}
			amount++;
		}
		
			ItemStack is = inv.getItemInOffHand();
			if(!(is==null || !is.hasItemMeta() || !is.getItemMeta().hasLore())){
				String temp = ItemLoreData.getLoreString("Temperature", is.clone().getItemMeta().getLore());
				if(temp != null){
					if(temp.contains("--")){
						double tem1 = Double.parseDouble(temp.split("--")[0].replaceAll("[^0-9.+-]", "")),
						              tem2 = Double.parseDouble(temp.split("--")[1].replaceAll("[^0-9.+-]", ""));
						fixMax+=tem1>tem2?tem1:tem2;
						fixMin+=tem1<tem2?tem1:tem2;
					}else{
						double tem = Double.parseDouble(temp.replaceAll("[^0-9.+-]", ""));
						fixMax+=tem;
						fixMin+=tem;
					}
					amount++;
				}
			}
			
			fixMax/=amount*100;
			fixMin/=amount*100;
			
			if(fixMax==fixMin) return getApparentTemperature(loc, fixMax==0?1:fixMax);

			double tem1,tem2;
			
			tem1 = getApparentTemperature(loc, fixMax);
			if(tem1>=Data.temperature[5] && tem1<=Data.temperature[6])return tem1;
			tem2 = getApparentTemperature(loc, fixMin);
			if(tem2>=Data.temperature[5] && tem2<=Data.temperature[6])return tem2;
			else if(tem2>Data.temperature[6]||tem1<Data.temperature[5])return Util.randomNum(tem2, tem1);
			else return Util.randomNum(tem2, tem1);
	}
	
	private double getTemFixLow(Location loc, PlayerInventory inv){
		int amount = 1;
		double fixMax = 100,fixMin = 100;
		for(ItemStack is : inv.getArmorContents()){
			if(is==null || !is.hasItemMeta() || !is.getItemMeta().hasLore()) continue;
			String temp = ItemLoreData.getLoreString("Temperature", is.clone().getItemMeta().getLore());
			if(temp == null) continue;
			if(temp.contains("--")){
				double tem1 = Double.parseDouble(temp.split("--")[0].replaceAll("[^0-9.+-]", "")),
				              tem2 = Double.parseDouble(temp.split("--")[1].replaceAll("[^0-9.+-]", ""));
				fixMax+=tem1>tem2?tem1:tem2;
				fixMin+=tem1<tem2?tem1:tem2;
			}else{
				double tem = Double.parseDouble(temp.replaceAll("[^0-9.+-]", ""));
				fixMax+=tem;
				fixMin+=tem;
			}
			amount++;
		}
		
		fixMax/=amount*100;
		fixMin/=amount*100;
		
		if(fixMax==fixMin) return getApparentTemperature(loc, fixMax==0?1:fixMax);
		
		double tem1,tem2;
		
		tem1 = getApparentTemperature(loc, fixMax);
		if(tem1>=Data.temperature[5] && tem1<=Data.temperature[6])return tem1;
		tem2 = getApparentTemperature(loc, fixMin);
		if(tem2>=Data.temperature[5] && tem2<=Data.temperature[6])return tem2;
		else if(tem2>Data.temperature[6]||tem1<Data.temperature[5])return Util.randomNum(tem2, tem1);
		else return Util.randomNum(tem2, tem1);
	}
	
	
	/*
	 * The computation of the heat index is a refinement of a result obtained by multiple regression analysis carried out by Lans P. Rothfusz and described in a 1990 National Weather Service (NWS) Technical Attachment (SR 90-23).  The regression equation of Rothfusz is
		HI = -42.379 + 2.04901523*T + 10.14333127*RH - .22475541*T*RH - .00683783*T*T - .05481717*RH*RH + .00122874*T*T*RH + .00085282*T*RH*RH - .00000199*T*T*RH*RH
		where T is temperature in degrees F and RH is relative humidity in percent.  HI is the heat index expressed as an apparent temperature in degrees F.  If the RH is less than 13% and the temperature is between 80 and 112 degrees F, then the following adjustment is subtracted from HI:
		ADJUSTMENT = [(13-RH)/4]*SQRT{[17-ABS(T-95.)]/17}
		where ABS and SQRT are the absolute value and square root functions, respectively.  On the other hand, if the RH is greater than 85% and the temperature is between 80 and 87 degrees F, then the following adjustment is added to HI:
		ADJUSTMENT = [(RH-85)/10] * [(87-T)/5]
		The Rothfusz regression is not appropriate when conditions of temperature and humidity warrant a heat index value below about 80 degrees F. In those cases, a simpler formula is applied to calculate values consistent with Steadman's results:
		HI = 0.5 * {T + 61.0 + [(T-68.0)*1.2] + (RH*0.094)}
		In practice, the simple formula is computed first and the result averaged with the temperature. If this heat index value is 80 degrees F or higher, the full regression equation along with any adjustment as described above is applied.
		The Rothfusz regression is not valid for extreme temperature and relative humidity conditions beyond the range of data considered by Steadman.
		
		Copy from http://www.wpc.ncep.noaa.gov/html/heatindex_equation.shtml
	 */
	private double getApparentTemperature(Location loc,double fix){
		double T = getBaseTemperature(loc,false)*fix*9/5+32;
		double RH = RandomDayManager.getTodayData(loc.getWorld().getName())[1];
		double HI;
		if(T<80) HI = 0.5 * (T + 61.0 + ((T-68.0)*1.2) + (RH*0.094));
		else{
			HI = -42.379 + 2.04901523*T + 10.14333127*RH - .22475541*T*RH - .00683783*T*T - .05481717*RH*RH + .00122874*T*T*RH + .00085282*T*RH*RH - .00000199*T*T*RH*RH;
			if(RH<13&&T>=80&&T<112) HI-=((13-RH)/4)*Math.sqrt((17-Math.abs(T-95.))/17);
			else if(RH>=85&&T>=80&&T<87) HI+= ((RH-85)/10) * ((87-T)/5);
		}
		return (HI-32)*5/9;
	}
	
	
	private static double getBlocks(Location l,boolean isFix){
		//5x5x5
		double _tem=0;
		int x=l.getBlockX()+(int)((Data.temperature[0]-1)*0.5);
		int y=l.getBlockY()+(int)((Data.temperature[2]-1)*0.5);
		int z=l.getBlockZ()+(int)((Data.temperature[1]-1)*0.5);
		
		for(int sx=x-((int)Data.temperature[0]-1);sx<x;sx++)
			for(int sy=y-((int)Data.temperature[2]-1);sy<y;sy++)
				for(int sz=z-((int)Data.temperature[1]-1);sz<z;sz++){
					Block temp=l.getWorld().getBlockAt(sx, sy, sz);
					if(Data.itemData.containsKey(temp.getType().name()))
						_tem+=Data.itemData.get(temp.getType().name()).getHeat()*Math.pow(
								1-Data.temperature[4], Math.sqrt(Math.pow(sx-x, 2)+Math.pow(
										sy-y, 2)+Math.pow(sz-z, 2)));
				}
		return isFix?_tem*Data.temperature[3]:_tem;
	}
	
/*	private double getInv(Player p){
		double aTem=0;
		for(ItemStack is:p.getInventory().getContents())
			if(is!=null&&Data.itemData.containsKey(is.getType().name()))
				aTem+=Data.itemData.get(is.getType().name()).getHeat();
	      return aTem;
	}*/
}
