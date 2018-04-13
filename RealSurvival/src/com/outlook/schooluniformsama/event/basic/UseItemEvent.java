package com.outlook.schooluniformsama.event.basic;

import java.util.Arrays;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.effect.Food;
import com.outlook.schooluniformsama.data.item.ItemLoreData;
import com.outlook.schooluniformsama.data.item.NBTItemData;
import com.outlook.schooluniformsama.data.player.PlayerData;

/*
 * SPECKLED_MELON FERMENTED_SPIDER_EYE RABBIT RABBIT_FOOT POISONOUS_POTATO BAKED_POTATO POTATO_ITEM CARROT_ITEM ROTTEN_FLESH SPIDER_EYE GOLDEN_APPLE GOLDEN_APPLE RAW_FISH RAW_FISH GRILLED_PORK PORK BREAD MUSHROOM_SOUP APPLE RAW_FISH RAW_FISH COOKED_FISH COOKED_FISH MELON COOKIE COOKED_BEEF RAW_CHICKEN RAW_BEEF PUMPKIN_PIE COOKED_RABBIT RABBIT_STEW MUTTON BEETROOT BEETROOT_SOUP MILK_BUCKET POTION COOKED_CHICKEN
 */

public class UseItemEvent implements Listener {
	
	private String foods="SPECKLED_MELON FERMENTED_SPIDER_EYE RABBIT RABBIT_FOOT POISONOUS_POTATO BAKED_POTATO POTATO_ITEM CARROT_ITEM ROTTEN_FLESH SPIDER_EYE GOLDEN_APPLE GOLDEN_APPLE RAW_FISH RAW_FISH GRILLED_PORK PORK BREAD MUSHROOM_SOUP APPLE RAW_FISH RAW_FISH COOKED_FISH COOKED_FISH MELON COOKIE COOKED_BEEF RAW_CHICKEN RAW_BEEF PUMPKIN_PIE COOKED_RABBIT RABBIT_STEW MUTTON BEETROOT BEETROOT_SOUP MILK_BUCKET POTION COOKED_CHICKEN";
	
	@EventHandler
	public void playerEatFood(PlayerItemConsumeEvent e){
		PlayerData pd = Data.playerData.get(e.getPlayer().getUniqueId());
		if(pd==null)return;
		
		if(NBTItemData.isNBTItem(e.getItem())){
			if(useItem(NBTItemData.load(e.getItem()),pd)){
				
			}else{
				if(Data.foodEffect.containsKey(e.getItem().getType().name())){
					eatFood(pd,Data.foodEffect.get(e.getItem().getType().name()));
					return;
				}
			}
		}
		
		
		ItemLoreData id=ItemLoreData.getItemLoreData(e.getItem());
		if(id==null){
			if(!Data.foodEffect.containsKey(e.getItem().getType().name()))
				return;
			else{
				//e.setCancelled(true);
				eatFood(pd,Data.foodEffect.get(e.getItem().getType().name()));
				e.getItem().setAmount(e.getItem().getAmount()-1);
				return;
			}
		}else{
			if(!useItem(id, pd))
				if(Data.foodEffect.containsKey(e.getItem().getType().name())){
					eatFood(pd,Data.foodEffect.get(e.getItem().getType().name()));
					//e.getItem().setAmount(e.getItem().getAmount()-1);
					return;
				}
			return;
		}
	}
	
	@EventHandler
	public void useItem(PlayerInteractEvent e){
		PlayerData pd = Data.playerData.get(e.getPlayer().getUniqueId());
		if(pd==null)return;
		if(!(e.getAction()==Action.RIGHT_CLICK_BLOCK||e.getAction()==Action.RIGHT_CLICK_AIR))return;
		if(e.getItem()==null||e.getItem().getType()==Material.AIR||foods.contains(e.getItem().getType().name()))return;
		if(NBTItemData.isNBTItem(e.getItem()) && useItem(NBTItemData.load(e.getItem()),pd)){
			e.setCancelled(true);
			e.getPlayer().getInventory().setItemInMainHand(sub(e.getItem()));
			return;
		}else{
			ItemLoreData id=ItemLoreData.getItemLoreData(e.getItem());
			if(id!=null && useItem(id, pd)){
				e.setCancelled(true);
				e.getPlayer().getInventory().setItemInMainHand(sub(e.getItem()));
				return;
			}
		}
	}
	
	
	private void eatFood(PlayerData pd,Food f){
		pd.getSleep().change(f.getSleep());
		pd.getThirst().change(f.getThirst());
		pd.getEnergy().change(f.getEnergy());
		if(f.isHasIllness())
			for(Map.Entry<String,Double> entity:f.getIllness().entrySet())
					pd.getIllness().addIllness(entity.getKey(),entity.getValue(),null);
	}
	
	private boolean useItem(ItemLoreData id,PlayerData pd){
		boolean isUsed = false;
		if(id.getSleep()!=ItemLoreData.badCode()){
			pd.getSleep().change(id.getSleep());
			isUsed=true;
			System.out.println("1");
		}
		if(id.getThirst()!=ItemLoreData.badCode()){
			pd.getThirst().change(id.getThirst());
			isUsed=true;
			System.out.println("11");
		}
		if(id.getDrugEffect()!=ItemLoreData.badCode() && id.getMedicineDuration()!=ItemLoreData.badCode()){
			pd.getIllness().eatMedicine(id.getTreatable(), id.getDrugEffect(), (long)id.getMedicineDuration());
			isUsed=true;
			System.out.println("111");
		}
		if(id.getIllnessNames()!=null && id.getIllnessProbability()!=ItemLoreData.badCode()){
			for(String str:id.getIllnessNames())
					pd.getIllness().addIllness(str,id.getIllnessProbability(),null);
			isUsed=true;
			System.out.println("1111");
		}
		if(id.getEnergy()!=ItemLoreData.badCode()){
			pd.getEnergy().change(id.getEnergy());
			isUsed=true;
			System.out.println("11111");
		}
		if(id.getHungery()!=ItemLoreData.badCode()){
			pd.getPlayer().setFoodLevel(pd.getPlayer().getFoodLevel()+(int)id.getHungery());
			isUsed=true;
			System.out.println("111111");
		}
		return isUsed;
	}
	
	private boolean useItem(NBTItemData id,PlayerData pd){
		boolean isUsed = false;
		if(id.getSleep()!=ItemLoreData.badCode()){
			pd.getSleep().change(id.getSleep());
			isUsed=true;
		}
		if(id.getThirst()!=ItemLoreData.badCode()){
			pd.getThirst().change(id.getThirst());
			isUsed=true;
		}
		if(id.getDrugEffect()!=ItemLoreData.badCode() && id.getMedicineDuration()!=ItemLoreData.badCode()){
			pd.getIllness().eatMedicine(id.getTreatable(), id.getDrugEffect(), (long)id.getMedicineDuration());
			
			isUsed=true;
		}
		if(id.getIllness()!=null){
			for(Map.Entry<String, Double> entity:id.getIllness().entrySet())
				pd.getIllness().addIllness(entity.getKey(),entity.getValue(),null);
			isUsed=true;
		}
		if(id.getEnergy()!=ItemLoreData.badCode()){
			pd.getEnergy().change(id.getEnergy());
			isUsed=true;
		}
		if(id.getHungery()!=ItemLoreData.badCode()){
			pd.getPlayer().setFoodLevel(pd.getPlayer().getFoodLevel()+(int)id.getHungery());
			isUsed=true;
		}
		return isUsed;
	}
	
	private ItemStack sub(ItemStack is){
		if(is.getAmount()>1)
			is.setAmount(is.getAmount()-1);
		else
			is.setType(Material.AIR);
		return is;
	}
	
	
	/*	
	@EventHandler
	public void playerEatFood(PlayerItemConsumeEvent e){
		if(rs.getPlayerData(e.getPlayer())==null)return;
		ItemStack item=e.getItem();
		ItemMeta itemM=item.getItemMeta();
		if(itemM!=null&&itemM.getLore()!=null)
			getLoresData(e.getPlayer(), itemM.getLore());
		
		if(rs.sickFoodContains(item.getType().name())){
			for(Object[] obj:rs.getFoodSick(item.getType().name())){
				OldPlayerData pd=rs.getPlayerData(e.getPlayer());
				if(obj==null||pd.isSick())return;
				if(Math.random()*100<=(Double)obj[1]){
					pd.setSick(true);
					pd.addSickKind((String)obj[0]);
					OldUtils.sendMsgToPlayer(pd, "EatSickFood");
				}
			}
		}
		if(rs.containsFoods(item.getType().name())){
			Double[] d=rs.getFoods(item.getType().name());
			OldPlayerData pd=rs.getPlayerData(e.getPlayer());
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
			if(!rs.getFCT().containsKey(OldUtils.toWKey(e.getClickedBlock())))return;
			String[] str=rs.getFCT(OldUtils.toWKey(e.getClickedBlock()));
			str[3]=Double.parseDouble(str[3])+rs.getHeatSource().get(e.getItem().getType().name())*rs.getHeatSourceFix()+"";
			rs.resetFCT(OldUtils.toWKey(e.getClickedBlock()), str[0]+","+str[1]+","+str[2]+","+str[3]+","+str[4]+","+str[5]+","+str[6]+","+str[7]+","+str[8]+","+str[9]);
			subItemInHand(e);
			return;
		}
		if(im.getDisplayName().contains(""))
		
		//内置感冒药判断
		if(im.getLore().equals(Items.getMedicine01().getItemMeta().getLore())){
			OldPlayerData pd=rs.getPlayerData(e.getPlayer());
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
			OldPlayerData pd=rs.getPlayerData(e.getPlayer());
			if(!pd.isSick()){
				subItemInHand(e);
				ateWrongMedicine(e.getPlayer(),pd);
				return;
			}
			if(pd.getSickKind()==null || !(pd.getSickKindList().contains("骨折")||pd.getSickKindList().contains("严重骨折"))){
				ateWrongMedicine(e.getPlayer(),pd);
				return;
			}
			if(pd.getSickKindList().contains("骨折")){
				pd.setDuration(120,"骨折");
				pd.setMedication(true,"骨折");
				pd.settEffect(0.2,"骨折");
			}else{
				pd.setDuration(120,"严重骨折");
				pd.setMedication(true,"严重骨折");
				pd.settEffect(0.2,"严重骨折");
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
		double sleep=OldUtils.getLore(rs.getLoreTabel("Sleep"), lore);
		double thirst=OldUtils.getLore(rs.getLoreTabel("Thirst"), lore);
		double medicine=OldUtils.getLore(rs.getLoreTabel("Medicine"),  lore);
		int medicineDuration=(int)OldUtils.getLore(rs.getLoreTabel("MedicineDuration"),  lore);
		String sickKind=OldUtils.getLoreString(rs.getLoreTabel("SickKind"),  lore);
		String sick=OldUtils.getLoreString(rs.getLoreTabel("Sick"),  lore);
		double sickness=OldUtils.getLore(rs.getLoreTabel("Sickness"),  lore);
		double tem=OldUtils.getLore(rs.getLoreTabel("Tem"),  lore);
		double ps=OldUtils.getLore("PhysicalStrength", lore);
		
		OldPlayerData pd=rs.getPlayerData(p);
		boolean isUse=false;
		//设定病种
		if(rs.getConfig().getBoolean("Switch.Sick")&&sickness!=ItemLoreData.badCode()){
			if(Math.random()*100<sickness){
				pd.setSick(true);
				if(sickKind!=null)
					pd.addSickKind(sickKind);
				else
					pd.addSickKind(rs.getDefSick());
			}
			isUse=true;
		}
		//睡觉
		if(rs.getConfig().getBoolean("Switch.Sleep")&&sleep!=ItemLoreData.badCode()){
			isUse=true;
			pd.changeSleep(sleep/100*rs.getSleepMax());
		}
		//口渴
		if(rs.getConfig().getBoolean("Switch.Thirst")&&thirst!=ItemLoreData.badCode()){
			isUse=true;
			pd.changeThirst(thirst/100*rs.getThirstMax());
		}
		//生病
		if(rs.getConfig().getBoolean("Switch.Sick")&&medicine!=ItemLoreData.badCode()){
			isUse=true;
			if(pd.isSick()&&sick==null){
				pd.setAllTEffect(medicine);
				pd.setAllMedication(true);
				if(medicineDuration!=ItemLoreData.badCode())
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
						if(medicineDuration!=ItemLoreData.badCode())
							pd.setDuration(medicineDuration,temp);
						else
							pd.setDuration(1,temp);
					}
				if(!pd.isSick() || !isSet)
					ateWrongMedicine(p,pd);
			}
		}
		//温度
		if(rs.getConfig().getBoolean("Switch.Temperature")&&tem!=ItemLoreData.badCode()){
			isUse=true;
			pd.changeTemperature(tem);
		}
		//体力
		if(rs.getConfig().getBoolean("Switch.PhysicalStrength")&&tem!=ItemLoreData.badCode()){
			isUse=true;
			pd.changePS(ps);
		}
		return isUse;
	}
	
	private void ateWrongMedicine(Player p,OldPlayerData pd){
		OldUtils.sendMsgToPlayer(pd, "WrongMedicine");
		Bukkit.getServer().getScheduler().runTaskLater(rs, new Runnable() {
			@Override
			public void run() {
				OldUtils.addPotionEffect(p, rs.getEffects("AfterAteWrongMedicine"));
				OldUtils.sendMsgToPlayer(pd, "AfterAteWrongMedicine");
			}
		}, 1200L);
	}*/
}
