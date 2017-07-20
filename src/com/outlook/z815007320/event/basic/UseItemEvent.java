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
	
	@EventHandler
	public void playerEatFood(PlayerItemConsumeEvent e){
		if(rs.getPlayerData(e.getPlayer())==null)return;
		ItemStack item=e.getItem();
		ItemMeta itemM=item.getItemMeta();
		if(itemM!=null&&itemM.getLore()!=null)
			getLoresData(e.getPlayer(), itemM.getLore());
		
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
		if(im.getDisplayName().contains(""))
		
		//ƒ⁄÷√∏–√∞“©≈–∂œ
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
			if(pd.getSickKind()==null || !(pd.getSickKindList().contains("π«’€")||pd.getSickKindList().contains("—œ÷ÿπ«’€"))){
				ateWrongMedicine(e.getPlayer(),pd);
				return;
			}
			if(pd.getSickKindList().contains("π«’€")){
				pd.setDuration(120,"π«’€");
				pd.setMedication(true,"π«’€");
				pd.settEffect(0.2,"π«’€");
			}else{
				pd.setDuration(120,"—œ÷ÿπ«’€");
				pd.setMedication(true,"—œ÷ÿπ«’€");
				pd.settEffect(0.2,"—œ÷ÿπ«’€");
			}

			subItemInHand(e);
			return;
		}
		if(getLoresData(e.getPlayer(), im.getLore())){
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
	
	private boolean getLoresData(Player p,List<String> lore){
		double sleep=Utils.getLore(rs.getLoreTabel("Sleep"), lore);
		double thirst=Utils.getLore(rs.getLoreTabel("Thirst"), lore);
		double medicine=Utils.getLore(rs.getLoreTabel("Medicine"),  lore);
		int medicineDuration=(int)Utils.getLore(rs.getLoreTabel("MedicineDuration"),  lore);
		String sickKind=Utils.getLoreString(rs.getLoreTabel("SickKind"),  lore);
		String sick=Utils.getLoreString(rs.getLoreTabel("Sick"),  lore);
		double sickness=Utils.getLore(rs.getLoreTabel("Sickness"),  lore);
		double tem=Utils.getLore(rs.getLoreTabel("Tem"),  lore);
		double ps=Utils.getLore("PhysicalStrength", lore);
		
		PlayerData pd=rs.getPlayerData(p);
		boolean isUse=false;
		//…Ë∂®≤°÷÷
		if(rs.getConfig().getBoolean("Switch.Sick")&&sickness!=-1.1111111){
			if(Math.random()*100<sickness){
				pd.setSick(true);
				if(sickKind!=null)
					pd.addSickKind(sickKind);
				else
					pd.addSickKind(rs.getDefSick());
			}
			isUse=true;
		}
		//ÀØæı
		if(rs.getConfig().getBoolean("Switch.Sleep")&&sleep!=-1.1111111){
			isUse=true;
			pd.changeSleep(sleep/100*rs.getSleepMax());
		}
		//ø⁄ø 
		if(rs.getConfig().getBoolean("Switch.Thirst")&&thirst!=-1.1111111){
			isUse=true;
			pd.changeThirst(thirst/100*rs.getThirstMax());
		}
		//…˙≤°
		if(rs.getConfig().getBoolean("Switch.Sick")&&medicine!=-1.1111111){
			isUse=true;
			if(pd.isSick()&&sick==null){
				pd.setAllTEffect(medicine);
				pd.setAllMedication(true);
				if(medicineDuration!=-1.1111111)
					pd.setAllDuration(medicineDuration);
				else
					pd.setAllDuration(1);
			}else{
				boolean isSet=false;
				List<String> list=Arrays.asList(sick.replaceAll(" ", "").split(","));
				for(String temp:pd.getSickKind())
					if(list.contains(temp)){
						isSet=true;
						pd.settEffect(medicine,temp);
						pd.setMedication(true,temp);
						if(medicineDuration!=-1.1111111)
							pd.setDuration(medicineDuration,temp);
						else
							pd.setDuration(1,temp);
					}
				if(!pd.isSick() || !isSet)
					ateWrongMedicine(p,pd);
			}
		}
		//Œ¬∂»
		if(rs.getConfig().getBoolean("Switch.Temperature")&&tem!=-1.1111111){
			isUse=true;
			pd.changeTemperature(tem);
		}
		//ÃÂ¡¶
		if(rs.getConfig().getBoolean("Switch.PhysicalStrength")&&tem!=-1.1111111){
			isUse=true;
			pd.changePS(ps);
		}
		return isUse;
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
