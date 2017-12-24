package com.outlook.schooluniformsama.event;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.item.Items;

public class ThirstEvent implements Listener{

	@EventHandler
	public void getWater(PlayerInteractEvent e){
		if(!Data.playerData.containsKey(e.getPlayer().getUniqueId()))return;
		if(!(e.getAction()==Action.RIGHT_CLICK_BLOCK||e.getAction()==Action.RIGHT_CLICK_AIR)
				||e.getMaterial()!=Material.GLASS_BOTTLE||!e.getHand().equals(EquipmentSlot.HAND))return;
		Block block=e.getPlayer().getWorld().getBlockAt(e.getPlayer().getTargetBlock((Set<Material>)null, 6).getLocation()); 
		if(!(block.getType()==Material.WATER||block.getType()==Material.STATIONARY_WATER))return;
		e.setCancelled(true);
		
		switch(block.getBiome()){
			case FROZEN_OCEAN:
				givePlayerWater("IceSeawater", e.getPlayer(), e.getItem());
				break;
			case FROZEN_RIVER:
				givePlayerWater("IceLakeWater", e.getPlayer(), e.getItem());
				break;
			case ICE_MOUNTAINS:
				givePlayerWater("IceLakeWater", e.getPlayer(), e.getItem());
				break;
			case COLD_BEACH:
				givePlayerWater("IceSeawater", e.getPlayer(), e.getItem());
				break;
			case TAIGA_COLD:
				givePlayerWater("IceLakeWater", e.getPlayer(), e.getItem());
				break;
			case TAIGA_COLD_HILLS:
				givePlayerWater("IceLakeWater", e.getPlayer(), e.getItem());
				break;
			case ICE_FLATS:
				givePlayerWater("IceLakeWater", e.getPlayer(), e.getItem());
				break;
			case MUTATED_TAIGA_COLD:
				givePlayerWater("IceLakeWater", e.getPlayer(), e.getItem());
				break;
			case MUTATED_ICE_FLATS:
				givePlayerWater("IceLakeWater", e.getPlayer(), e.getItem());
				break;
			case OCEAN:
				givePlayerWater("Seawater", e.getPlayer(), e.getItem());
				break;
			case DEEP_OCEAN:
				givePlayerWater("Seawater", e.getPlayer(), e.getItem());
				break;
			case BEACHES:
				givePlayerWater("Seawater", e.getPlayer(), e.getItem());
				break;
			case DESERT:
				givePlayerWater("HotLakeWater", e.getPlayer(), e.getItem());
				break;
			case SWAMPLAND:
				givePlayerWater("SwampWater", e.getPlayer(), e.getItem());
				break;
			case HELL:
				givePlayerWater("HotLakeWater", e.getPlayer(), e.getItem());
				break;
			case DESERT_HILLS:
				givePlayerWater("HotLakeWater", e.getPlayer(), e.getItem());
				break;
			case STONE_BEACH:
				givePlayerWater("Seawater", e.getPlayer(), e.getItem());
				break;
			case MUTATED_SWAMPLAND:
				givePlayerWater("SwampWater", e.getPlayer(), e.getItem());
				break;
			case MUTATED_DESERT:
				givePlayerWater("HotLakeWater", e.getPlayer(), e.getItem());
				break;
			default:
				givePlayerWater("LakeWater", e.getPlayer(), e.getItem());
				break;
		}
		return;
	}
	
/*	private void subItemInHand(PlayerInteractEvent e){
		ItemStack is=e.getPlayer().getInventory().getItemInMainHand();
		if(is.getAmount()>1){
			is.setAmount(is.getAmount()-1);
			e.getPlayer().getInventory().setItemInMainHand(is);
		}else e.getPlayer().getInventory().setItemInMainHand(null);
	}*/
	
	private void givePlayerWater(String name,Player p,ItemStack glass){
		glass.setAmount(glass.getAmount()-1);
		p.getInventory().setItemInMainHand(glass);
		givePlayerItem(p, Items.getWater(name));
	}
	
	public static void givePlayerItem(Player p,ItemStack item,int amount){
		item.setAmount(amount);
		int index=0;
		for(ItemStack is:p.getInventory().getContents())
			if(is==null && index++<p.getInventory().getContents().length-6){
				p.getInventory().addItem(item);
				return;
			}
		p.getWorld().dropItem(p.getLocation(), item);
	}
	
	public static void givePlayerItem(Player p,ItemStack item){
		int index=0;
		for(ItemStack is:p.getInventory().getContents())//.getStorageContents())
			if(is==null && index++<p.getInventory().getContents().length-6){
				p.getInventory().addItem(item);
				return;
			}
		p.getWorld().dropItem(p.getLocation(), item);
	}
}
