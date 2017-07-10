package com.outlook.z815007320.event;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.outlook.z815007320.data.Items;
import com.outlook.z815007320.data.PluginRS;

public class ThirstEvent extends PluginRS implements Listener{

	@EventHandler
	public void getWater(PlayerInteractEvent e){
		if(rs.getPlayerData(e.getPlayer())==null)return;
		//如果手上拿的不是玻璃瓶,不是右击,不是手则返回
		if(!(e.getAction()==Action.RIGHT_CLICK_BLOCK||e.getAction()==Action.RIGHT_CLICK_AIR)
				||e.getMaterial()!=Material.GLASS_BOTTLE||!e.getHand().equals(EquipmentSlot.HAND))return;
		//获取远处方块
		Block block=e.getPlayer().getWorld().getBlockAt(e.getPlayer().getTargetBlock((Set<Material>)null, 6).getLocation()); 
		if(!(block.getType()==Material.WATER||block.getType()==Material.STATIONARY_WATER))return;
		e.setCancelled(true);
		
		Biome b=block.getBiome();//e.getClickedBlock().getBiome();
		if(b.equals(Biome.OCEAN)||b.equals(Biome.DEEP_OCEAN)||b.equals(Biome.COLD_BEACH)||b.equals(Biome.STONE_BEACH)){
			subItemInHand(e);
			e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), Items.getSeaWater());
			return;
		}else if(b.equals(Biome.SWAMPLAND)||b.equals(Biome.MUTATED_SWAMPLAND)){
			subItemInHand(e);
			e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), Items.getSwamplandWater());
			return;
		}else{
			subItemInHand(e);
			e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), Items.getLakeWater());
			return;
		}
		
		
	}
	
	private void subItemInHand(PlayerInteractEvent e){
		ItemStack is=e.getPlayer().getInventory().getItemInMainHand();
		if(is.getAmount()>1){
			is.setAmount(is.getAmount()-1);
			e.getPlayer().getInventory().setItemInMainHand(is);
		}else
			e.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
	}
}
