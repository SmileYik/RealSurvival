package miskyle.realsurvival.listener;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import miskyle.realsurvival.api.status.StatusType;
import miskyle.realsurvival.data.ConfigManager;
import miskyle.realsurvival.data.EffectManager;
import miskyle.realsurvival.data.ItemManager;
import miskyle.realsurvival.data.PlayerManager;
import miskyle.realsurvival.data.item.DrugData;
import miskyle.realsurvival.data.item.RSItemData;
import miskyle.realsurvival.data.playerdata.PlayerData;
import miskyle.realsurvival.util.RSEntry;

public class UseItemListener implements Listener{
	// 以下是1.13右键有动画的物品列表
	//POTION,MILK_BUCKET,APPLE,MUSHROOM_STEW,BREAD,PORKCHOP,COOKED_PORKCHOP,GOLDEN_APPLE,ENCHANTED_GOLDEN_APPLE,COD,SALMON,BEEF,DRIED_KELP,MELON_SLICE,COOKIE,POISONOUS_POTATO,COOKED_SALMON,COOKED_COD,PUFFERFISH,TROPICAL_FISH,BAKED_POTATO,POTATO,CARROT,SPIDER_EYE,ROTTEN_FLESH,COOKED_CHICKEN,CHICKEN,COOKED_BEEF,PUMPKIN_PIE,COOKED_RABBIT,RABBIT_STEW,MUTTON,COOKED_MUTTON,BEETROOT,BEETROOT_SOUP,GOLDEN_CARROT,RABBIT
	private final String ITEM_LIST;
	public UseItemListener() {
		if(ConfigManager.getBukkitVersion()>=13) {
			ITEM_LIST =
					"POTION,MILK_BUCKET,APPLE,MUSHROOM_STEW,"
					+ "BREAD,PORKCHOP,COOKED_PORKCHOP,GOLDEN_APPLE,"
					+ "ENCHANTED_GOLDEN_APPLE,COD,SALMON,BEEF,DRIED_KELP,"
					+ "MELON_SLICE,COOKIE,POISONOUS_POTATO,COOKED_SALMON,"
					+ "COOKED_COD,PUFFERFISH,TROPICAL_FISH,BAKED_POTATO,"
					+ "POTATO,CARROT,SPIDER_EYE,ROTTEN_FLESH,COOKED_CHICKEN,"
					+ "CHICKEN,COOKED_BEEF,PUMPKIN_PIE,COOKED_RABBIT,RABBIT_STEW,"
					+ "MUTTON,COOKED_MUTTON,BEETROOT,BEETROOT_SOUP,"
					+ "GOLDEN_CARROT,RABBIT";
		}else {
			ITEM_LIST = 
					"SPECKLED_MELON FERMENTED_SPIDER_EYE RABBIT RABBIT_FOOT "
					+ "POISONOUS_POTATO BAKED_POTATO POTATO_ITEM CARROT_ITEM "
					+ "ROTTEN_FLESH SPIDER_EYE GOLDEN_APPLE GOLDEN_APPLE RAW_FISH "
					+ "RAW_FISH GRILLED_PORK PORK BREAD MUSHROOM_SOUP APPLE "
					+ "RAW_FISH RAW_FISH COOKED_FISH COOKED_FISH MELON COOKIE "
					+ "COOKED_BEEF RAW_CHICKEN RAW_BEEF PUMPKIN_PIE COOKED_RABBIT "
					+ "RABBIT_STEW MUTTON BEETROOT BEETROOT_SOUP MILK_BUCKET "
					+ "POTION COOKED_CHICKEN";
		}
	}
	
	@EventHandler
	public void playerEatItem(PlayerInteractEvent e) {
		if(!PlayerManager.isActive(e.getPlayer()))return;
		if(!e.hasItem()) return;
		if(!(e.getAction() == Action.RIGHT_CLICK_AIR 
				|| e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
		if(ITEM_LIST.contains(e.getItem().getType().name()))return;
		if(eatItem(e.getPlayer(), 
				PlayerManager.getPlayerData(e.getPlayer().getName()), 
				ItemManager.loadItemData(e.getItem()))) {
			e.setCancelled(true);
			e.getItem().setAmount(e.getItem().getAmount()-1);
		}
	}
	
	@EventHandler
	public void playerEatItem(PlayerItemConsumeEvent e) {
		if(!PlayerManager.isActive(e.getPlayer()))return;
		if(e.getItem()==null) return;
		eatItem(e.getPlayer(), 
				PlayerManager.getPlayerData(e.getPlayer().getName()), 
				ItemManager.loadItemData(e.getItem()));
	}
	
	private boolean eatItem(Player p,PlayerData pd,RSItemData itemData) {
	  if(itemData == null) return false;
		if(itemData.isTool())return false;
		boolean flag = false;
		if(itemData.isValidEnergy()) {
			flag = true;
			if(itemData.isMaxEnergy()) {
				pd.modifyWithEffect(StatusType.ENERGY, 
						+itemData.getEnergyValue()/100D*pd.getEnergy().getMaxValue());
			}else {
				pd.modifyWithEffect(StatusType.ENERGY, 
						+itemData.getEnergyValue());
			}
		}
		if(itemData.isValidSleep()) {
			flag = true;
			if(itemData.isMaxSleep()) {
				pd.modify(StatusType.SLEEP, 
						+itemData.getSleepValue()/100D*pd.getSleep().getMaxValue());
			}else {
				pd.modify(StatusType.SLEEP, 
						+itemData.getSleepValue());
			}
		}
		if(itemData.isValidThirst()) {
			flag = true;
			if(itemData.isMaxThirst()) {
				pd.modifyWithEffect(StatusType.THIRST, 
						+itemData.getThirstValue()/100D*pd.getThirst().getMaxValue());
			}else {
				pd.modifyWithEffect(StatusType.THIRST, 
						+itemData.getThirstValue());
			}
		}
		if(itemData.isValidHealth()) {
			flag = true;
			double v;
			double max = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
			if(itemData.isMaxHealth()) {
				v =p.getHealth()+max*itemData.getHealthValue()/100D;
			}else {
				v = p.getHealth()+itemData.getHealthValue();
			}
			if(v>max) {
				v = max;
			}else if(v<0) {
				p.damage(max, p);
				return true;
			}
			p.setHealth(v);
		}
		if(itemData.isValidHunger()) {
			flag = true;
			double v;
			if(itemData.isMaxHunger()) {
				v = p.getFoodLevel()+0.2D*itemData.getHungerValue();
			}else {
				v = p.getFoodLevel()+itemData.getHungerValue();
			}
			if(v>20) {
				v = 20;
			}else if(v<0) {
				v = 0;
			}
			p.setFoodLevel((int)v);
		}
		DrugData drug = itemData.getDrugData();
		if(drug != null) {
		  if(drug.isMakeDisease()) {
		    flag = true;
		    drug.getGetDisease().forEach((k,v)->{
		      if(Math.random()*100<v) {
		        pd.getDisease().addDisease(k);
		      }
		    });
		  }
		  if(drug.isValidDrug()) {
		    flag = true;
		    if(pd.getDisease().getDiseases().isEmpty()) {
		      drug.getNoNeedDrug().forEach(effect->{
		        EffectManager.effectPlayer(p, effect);		        
		      });
		    }else {
		      pd.getDisease().getDiseases().forEachKey(
		          pd.getDisease().getDiseases().mappingCount(), k->{
		        if(drug.isValidAtDisease(k)) {
		          RSEntry<Double,Integer> entry = drug.getMedicien(k);
		          pd.getDisease().eatDurg(k, entry.getLeft(),entry.getRight());
		        }else if(drug.getWrongDisease().contains(k)) {
		          drug.getEatWrongDrug().forEach(effect->{
	                EffectManager.effectPlayer(p, effect);              
	              });
		        }
		      });
		    }
		  }
		}
		return flag;
	}
}
