package com.outlook.z815007320.event.basic;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.outlook.z815007320.data.Items;
import com.outlook.z815007320.data.PlayerData;
import com.outlook.z815007320.data.PluginRS;
import com.outlook.z815007320.utils.Utils;

public class UseItemEvent extends PluginRS implements Listener {
	private double sleep;
	private double thirst;
	private double medicine;
	private int medicineDuration;
	private String sickKind;
	private String sick;
	private double sickness;
	private double tem;
	
	@EventHandler
	public void playerEatFood(PlayerItemConsumeEvent e){
		if(rs.getPlayerData(e.getPlayer())==null)return;
		ItemStack item=e.getItem();
		ItemMeta itemM=item.getItemMeta();
		if(itemM!=null&&itemM.getLore()!=null){
			//ÒûË®ÌõÄ¿
			/*
			if(rs.getConfig().getBoolean("Switch.Thirst")){
				if(itemM.getLore().equals(Items.getFreshWater().getItemMeta().getLore())){
					rs.getPlayerData(e.getPlayer()).changeThirst(Math.random()*0.25*rs.getThirstMax());
					Utils.sendMsgToPlayer(rs.getPlayerData(e.getPlayer()),"DrinkWater");
					return;
				}else if(itemM.getLore().equals(Items.getHotWater().getItemMeta().getLore())){
					rs.getPlayerData(e.getPlayer()).changeThirst(Math.random()*0.25*rs.getThirstMax());
					rs.getPlayerData(e.getPlayer()).changeTemperature(Math.random()*0.1);
					if(rs.getPlayerData(e.getPlayer()).isSick())
						rs.getPlayerData(e.getPlayer()).changeAllRecovery(Math.random()*5);
					Utils.sendMsgToPlayer(rs.getPlayerData(e.getPlayer()),"DrinkWater");
					return;
				}else if(itemM.getLore().equals(Items.getIceWater().getItemMeta().getLore())){
					rs.getPlayerData(e.getPlayer()).changeThirst(Math.random()*0.25*rs.getThirstMax());
					rs.getPlayerData(e.getPlayer()).changeTemperature(Math.random()*(-0.1));
					Utils.sendMsgToPlayer(rs.getPlayerData(e.getPlayer()),"DrinkWater");
					return;
				}else if(itemM.getLore().equals(Items.getLakeWater().getItemMeta().getLore())){
					rs.getPlayerData(e.getPlayer()).changeThirst(Math.random()*0.25*rs.getThirstMax());
					Utils.sendMsgToPlayer(rs.getPlayerData(e.getPlayer()),"DrinkWater");
					if(Math.random()*100<20){
						rs.getPlayerData(e.getPlayer()).addSickKind(rs.getDefSick());
						if(!rs.getPlayerData(e.getPlayer()).isSick())
							Utils.sendMsgToPlayer(rs.getPlayerData(e.getPlayer()),"DrinkWaterSick");
						rs.getPlayerData(e.getPlayer()).setSick(true);
					}
					return;
				}else if(itemM.getLore().equals(Items.getSeaWater().getItemMeta().getLore())){
					rs.getPlayerData(e.getPlayer()).changeThirst(-Math.random()*0.1*rs.getThirstMax());
					Utils.sendMsgToPlayer(rs.getPlayerData(e.getPlayer()),"DrinkWaterSub");
					if(Math.random()*100<40){
						rs.getPlayerData(e.getPlayer()).addSickKind(rs.getDefSick());
						if(!rs.getPlayerData(e.getPlayer()).isSick())
							Utils.sendMsgToPlayer(rs.getPlayerData(e.getPlayer()),"DrinkWaterSick");
						rs.getPlayerData(e.getPlayer()).setSick(true);
					}
					return;
				}else if(itemM.getLore().equals(Items.getSwamplandWater().getItemMeta().getLore())){
					rs.getPlayerData(e.getPlayer()).changeThirst(Math.random()*0.1*rs.getThirstMax());
					Utils.sendMsgToPlayer(rs.getPlayerData(e.getPlayer()),"DrinkWaterSub");
					if(Math.random()*100<60){
						rs.getPlayerData(e.getPlayer()).addSickKind(rs.getDefSick());
						if(!rs.getPlayerData(e.getPlayer()).isSick())
							Utils.sendMsgToPlayer(rs.getPlayerData(e.getPlayer()),"DrinkWaterSick");
						rs.getPlayerData(e.getPlayer()).setSick(true);
					}
					if(rs.getPlayerData(e.getPlayer()).isSick())
						rs.getMessage("DrinkWaterSick",rs.getPlayerData(e.getPlayer()));
					else 
						rs.getMessage("DrinkWater",rs.getPlayerData(e.getPlayer()));
					return;
				}else if(itemM.getLore().equals(Items.getRainwater().getItemMeta().getLore())){
					rs.getPlayerData(e.getPlayer()).changeThirst(Math.random()*0.25*rs.getThirstMax());
					Utils.sendMsgToPlayer(rs.getPlayerData(e.getPlayer()),"DrinkWater");
					if(Math.random()*100<10){
						rs.getPlayerData(e.getPlayer()).addSickKind(rs.getDefSick());
						if(!rs.getPlayerData(e.getPlayer()).isSick())
							Utils.sendMsgToPlayer(rs.getPlayerData(e.getPlayer()),"DrinkWaterSick");
						rs.getPlayerData(e.getPlayer()).setSick(true);
					}
					return;
				}
			}*/
			getLoresData(e.getPlayer(), itemM.getLore());
			useItem(rs.getPlayerData(e.getPlayer()),e.getPlayer());
		}
		if(rs.sickFoodContains(item.getType().name())){
			for(Object[] obj:rs.getFoodSick(item.getType().name())){
				PlayerData pd=rs.getPlayerData(e.getPlayer());
				if(obj==null||pd.isSick())return;
				if(Math.random()*100<=(Double)obj[1]){
					pd.setSick(true);
					pd.addSickKind((String)obj[0]);
					Utils.sendMsgToPlayer(pd, "EatSickFood");
				}
			}
		}
		if(rs.containsFoods(item.getType().name())){
			Double[] d=rs.getFoods(item.getType().name());
			PlayerData pd=rs.getPlayerData(e.getPlayer());
			pd.changeSleep(d[0]);
			pd.changeThirst(d[1]);
			pd.changeTemperature(d[2]);
		}
	}
	
	@EventHandler
	public void useItem(PlayerInteractEvent e){
		if(rs.getPlayerData(e.getPlayer())==null)return;
		if(!(e.getAction()==Action.RIGHT_CLICK_BLOCK||e.getAction()==Action.RIGHT_CLICK_AIR)
				||!e.getHand().equals(EquipmentSlot.HAND))return;
		
		ItemStack is=e.getItem();
		
		if(is==null||is.getType()==Material.AIR)return;
		
		for(String a:new String[]{"APPLE","MUSHROOM_STEW","BREAD","PORKCHOP","COOKED_PORKCHOP"
				,"GOLDEN_APPLE","FISH","COOKED_FISH","CAKE","COOKIE","MELON","BEEF","COOKED_BEEF","CHICKEN",
				"COOKED_CHICKEN","ROTTEN_FLESH","SPIDER_EYE","CARROT","POTATO","BAKED_POTATO"
				,"POISONOUS_POTATO","PUMPKIN_PIE","RABBIT","COOKED_RABBIT","RABBIT_STEW","MUTTON",
				"COOKED_MUTTON","BEETROOT","BEETROOT_SOUP","FERMENTED_SPIDER_EYE","GOLDEN_CARROT"
				,"SPECKLED_MELON","POTION","MILK_BUCKET"})
			if(a.equalsIgnoreCase(is.getType().name()))return;
		
		ItemMeta im=is.getItemMeta();
		
		if(im==null||im.getLore()==null){
			if(e.getClickedBlock()==null)return;
			if(!rs.getHeatSource().containsKey(e.getItem().getType().name()))return;
			if(!rs.getFCT().containsKey(Utils.toWKey(e.getClickedBlock())))return;
			String[] str=rs.getFCT(Utils.toWKey(e.getClickedBlock()));
			str[3]=Double.parseDouble(str[3])+rs.getHeatSource().get(e.getItem().getType().name())*rs.getHeatSourceFix()+"";
			rs.resetFCT(Utils.toWKey(e.getClickedBlock()), str[0]+","+str[1]+","+str[2]+","+str[3]+","+str[4]+","+str[5]+","+str[6]+","+str[7]+","+str[8]+","+str[9]);
			subItemInHand(e);
			return;
		}
		//ÄÚÖÃ¸ÐÃ°Ò©ÅÐ¶Ï
		if(im.getLore().equals(Items.getMedicine01().getItemMeta().getLore())){
			PlayerData pd=rs.getPlayerData(e.getPlayer());
			if(!pd.isSick()){
				subItemInHand(e);
				ateWrongMedicine(e.getPlayer(),pd);
				return;
			}
			pd.setAllTEffect(0.2);
			pd.setAllDuration(120);
			pd.setAllMedication(true);
			subItemInHand(e);
			return;
		}
		
		if(im.getLore().equals(Items.getMedicine02().getItemMeta().getLore())){
			PlayerData pd=rs.getPlayerData(e.getPlayer());
			if(!pd.isSick()){
				subItemInHand(e);
				ateWrongMedicine(e.getPlayer(),pd);
				return;
			}
			if(pd.getSickKind()==null || !(pd.getSickKindList().contains("¹ÇÕÛ")||pd.getSickKindList().contains("ÑÏÖØ¹ÇÕÛ"))){
				ateWrongMedicine(e.getPlayer(),pd);
				return;
			}
			if(pd.getSickKindList().contains("¹ÇÕÛ")){
				pd.setDuration(120,"¹ÇÕÛ");
				pd.setMedication(true,"¹ÇÕÛ");
				pd.settEffect(0.2,"¹ÇÕÛ");
			}else{
				pd.setDuration(120,"ÑÏÖØ¹ÇÕÛ");
				pd.setMedication(true,"ÑÏÖØ¹ÇÕÛ");
				pd.settEffect(0.2,"ÑÏÖØ¹ÇÕÛ");
			}

			subItemInHand(e);
			return;
		}
		getLoresData(e.getPlayer(), im.getLore());
		if(useItem(rs.getPlayerData(e.getPlayer()),e.getPlayer())){
			subItemInHand(e);
			e.setCancelled(true);
			return;
		}
		
	}
	
	private void subItemInHand(PlayerInteractEvent e){
		ItemStack is=e.getItem();
		is.setAmount(is.getAmount()-1);
		e.getPlayer().getInventory().setItemInMainHand(is);
	}
	
	private void getLoresData(Player p,List<String> lore){
		sleep=Utils.getLore(rs.getLoreTabel("Sleep"), lore);
		thirst=Utils.getLore(rs.getLoreTabel("Thirst"), lore);
		medicine=Utils.getLore(rs.getLoreTabel("Medicine"),  lore);
		medicineDuration=(int)Utils.getLore(rs.getLoreTabel("MedicineDuration"),  lore);
		sickKind=Utils.getLoreString(rs.getLoreTabel("SickKind"),  lore);
		sick=Utils.getLoreString(rs.getLoreTabel("Sick"),  lore);
		sickness=Utils.getLore(rs.getLoreTabel("Sickness"),  lore);
		tem=Utils.getLore(rs.getLoreTabel("tem"),  lore);
	}
	
	private boolean useItem(PlayerData pd,Player p){
		boolean isUse=false;
		//Éè¶¨²¡ÖÖ
		if(rs.getConfig().getBoolean("Switch.Sick")&&sickness!=-1){
			if(Math.random()*100<sickness){
				pd.setSick(true);
				if(sickKind!=null)
					pd.addSickKind(sickKind);
				else
					pd.addSickKind(rs.getDefSick());
			}
			isUse=true;
		}
		//Ë¯¾õ
		if(rs.getConfig().getBoolean("Switch.Sleep")&&sleep!=-1){
			isUse=true;
			pd.changeSleep(sleep/100*rs.getSleepMax());
		}
		//¿Ú¿Ê
		if(rs.getConfig().getBoolean("Switch.Thirst")&&thirst!=-1){
			isUse=true;
			pd.changeThirst(thirst/100*rs.getThirstMax());
		}
		//Éú²¡
		if(rs.getConfig().getBoolean("Switch.Sick")&&medicine!=-1){
			isUse=true;
			if(pd.isSick()&&sick==null){
				pd.setAllTEffect(medicine);
				pd.setAllMedication(true);
				if(medicineDuration!=-1)
					pd.setAllDuration(medicineDuration);
				else
					pd.setAllDuration(1);
			}else if(!pd.isSick() || !recoveryDifferentSick(pd))
				ateWrongMedicine(p,pd);
		}
		//ÎÂ¶È
		if(rs.getConfig().getBoolean("Switch.Temperature")&&tem!=-1){
			isUse=true;
			pd.changeTemperature(tem);
		}
		return isUse;
	}
	
	private boolean recoveryDifferentSick(PlayerData pd){
		boolean isSet=false;
		List<String> list=Arrays.asList(sick.replaceAll(" ", "").split(","));
		for(String temp:pd.getSickKind())
			if(list.contains(temp)){
				isSet=true;
				pd.settEffect(medicine,temp);
				pd.setMedication(true,temp);
				if(medicineDuration!=-1)
					pd.setDuration(medicineDuration,temp);
				else
					pd.setDuration(1,temp);
			}
		return isSet;
	}
	
	private void ateWrongMedicine(Player p,PlayerData pd){
		Utils.sendMsgToPlayer(pd, "WrongMedicine");
		Bukkit.getServer().getScheduler().runTaskLater(rs, new Runnable() {
			@Override
			public void run() {
				Utils.addPotionEffect(p, rs.getEffects("AfterAteWrongMedicine"));
				Utils.sendMsgToPlayer(pd, "AfterAteWrongMedicine");
			}
		}, 1200L);
	}
}
