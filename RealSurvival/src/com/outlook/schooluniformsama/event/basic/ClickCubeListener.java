package com.outlook.schooluniformsama.event.basic;

import java.util.ArrayList;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.outlook.schooluniformsama.data.cube.BlockArrayData;
import com.outlook.schooluniformsama.data.cube.BlockCubeData;
import com.outlook.schooluniformsama.data.cube.CubeManager;


public class ClickCubeListener implements Listener{
	private Plugin plugin;

	public ClickCubeListener(Plugin plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerUse(final PlayerInteractEvent event) {
		if(!(event.hasBlock()
				&&event.getAction()==Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		Block block = event.getClickedBlock();
		ArrayList<BlockCubeData> cubes = CubeManager.getCubeByBlock(CubeManager.getBlock(block));
		final ItemStack item;
		if(event.hasItem() && event.getHand() == EquipmentSlot.HAND) {
			item = event.getItem().clone();
			item.setAmount(1);
		}else{
			item = null;
		}

		for(BlockCubeData cube : cubes) {
			if(!cube.isCheckItem() || (cube.getItem().equals(item))) {
				//Item Pass
				event.setCancelled(true);
				//Simple Check
				if(!simpleCheck(block, cube.getUp().getMain(),
						cube.getMid().getMain(), cube.getDown().getMain())) {
					continue;
				}

				//Check Mid-Floor
				if(!checkBlocks(block, cube.getMid(), cube.isCheckCompass())) {
					continue;
				}

				//Check Down-Floor
				if(!checkBlocks(block.getRelative(BlockFace.DOWN),
						cube.getDown(), cube.isCheckCompass())) {
					continue;
				}

				//Check Up-Floor
				if(!checkBlocks(block.getRelative(BlockFace.UP),
						cube.getUp(), cube.isCheckCompass())) {
					continue;
				}

				//Success
				event.setCancelled(true);
				//TODO open forge gui
				
				return;
			}
		}
		event.setCancelled( false );
	}
	
	private boolean simpleCheck(Block mainBlock,String upMain,String main,String downMain) {
		return compareBlock(mainBlock, main)
				&& compareBlock(mainBlock.getRelative(BlockFace.UP), upMain)
				&& compareBlock(mainBlock.getRelative(BlockFace.DOWN), downMain);
	}
	
	private boolean checkBlocks(Block mainBlock, BlockArrayData blocks, boolean checkCompass) {
		if(checkCompass) {
			return compareBlock(mainBlock.getRelative(BlockFace.NORTH), blocks.getNorth())
					&& compareBlock(mainBlock.getRelative(BlockFace.NORTH_EAST), blocks.getNorthEast())
					&& compareBlock(mainBlock.getRelative(BlockFace.EAST), blocks.getEast())
					&& compareBlock(mainBlock.getRelative(BlockFace.SOUTH_EAST), blocks.getSouthEast())
					&& compareBlock(mainBlock.getRelative(BlockFace.SOUTH), blocks.getSouth())
					&& compareBlock(mainBlock.getRelative(BlockFace.SOUTH_WEST), blocks.getSouthWest())
					&& compareBlock(mainBlock.getRelative(BlockFace.WEST), blocks.getWest())
					&& compareBlock(mainBlock.getRelative(BlockFace.NORTH_WEST), blocks.getNorthWest());
		}else {
			if(compareBlock(mainBlock.getRelative(BlockFace.EAST), blocks.getNorth()) //2
					&&compareBlock(mainBlock.getRelative(BlockFace.SOUTH_EAST), blocks.getNorthEast()) 
					&&compareBlock(mainBlock.getRelative(BlockFace.SOUTH), blocks.getEast()) 
					&&compareBlock(mainBlock.getRelative(BlockFace.SOUTH_WEST), blocks.getSouthEast()) 
					&&compareBlock(mainBlock.getRelative(BlockFace.WEST), blocks.getSouth()) 
					&&compareBlock(mainBlock.getRelative(BlockFace.NORTH_WEST), blocks.getSouthWest()) 
					&&compareBlock(mainBlock.getRelative(BlockFace.NORTH), blocks.getWest()) 
					&&compareBlock(mainBlock.getRelative(BlockFace.NORTH_EAST), blocks.getNorthWest())) {
				return true;
			}else if(compareBlock(mainBlock.getRelative(BlockFace.NORTH), blocks.getNorth()) //1
					&&compareBlock(mainBlock.getRelative(BlockFace.NORTH_EAST), blocks.getNorthEast()) 
					&&compareBlock(mainBlock.getRelative(BlockFace.EAST), blocks.getEast()) 
					&&compareBlock(mainBlock.getRelative(BlockFace.SOUTH_EAST), blocks.getSouthEast()) 
					&&compareBlock(mainBlock.getRelative(BlockFace.SOUTH), blocks.getSouth()) 
					&&compareBlock(mainBlock.getRelative(BlockFace.SOUTH_WEST), blocks.getSouthWest()) 
					&&compareBlock(mainBlock.getRelative(BlockFace.WEST), blocks.getWest()) 
					&&compareBlock(mainBlock.getRelative(BlockFace.NORTH_WEST), blocks.getNorthWest())) {
				return true;
			}else if(compareBlock(mainBlock.getRelative(BlockFace.SOUTH), blocks.getNorth()) //3
					&&compareBlock(mainBlock.getRelative(BlockFace.SOUTH_WEST), blocks.getNorthEast()) 
					&&compareBlock(mainBlock.getRelative(BlockFace.WEST), blocks.getEast()) 
					&&compareBlock(mainBlock.getRelative(BlockFace.NORTH_WEST), blocks.getSouthEast()) 
					&&compareBlock(mainBlock.getRelative(BlockFace.NORTH), blocks.getSouth()) 
					&&compareBlock(mainBlock.getRelative(BlockFace.NORTH_EAST), blocks.getSouthWest()) 
					&&compareBlock(mainBlock.getRelative(BlockFace.EAST), blocks.getWest()) 
					&&compareBlock(mainBlock.getRelative(BlockFace.SOUTH_EAST), blocks.getNorthWest())) {
				return true;
			}else if(compareBlock(mainBlock.getRelative(BlockFace.WEST), blocks.getNorth()) //4
					&&compareBlock(mainBlock.getRelative(BlockFace.NORTH_WEST), blocks.getNorthEast())
					&&compareBlock(mainBlock.getRelative(BlockFace.NORTH), blocks.getEast())
					&&compareBlock(mainBlock.getRelative(BlockFace.NORTH_EAST), blocks.getSouthEast())
					&&compareBlock(mainBlock.getRelative(BlockFace.EAST), blocks.getSouth())
					&&compareBlock(mainBlock.getRelative(BlockFace.SOUTH_EAST), blocks.getSouthWest())
					&&compareBlock(mainBlock.getRelative(BlockFace.SOUTH), blocks.getWest())
					&&compareBlock(mainBlock.getRelative(BlockFace.SOUTH_WEST), blocks.getNorthWest())) {
				return true;
			}else {
				return false;
			}
		}
	}
	
	private boolean compareBlock(Block block,String block2) {
		if(block2.equalsIgnoreCase("AIR:0")) return true;
		return block2.equals(CubeManager.getBlock(block));
	}

/*			1
			Main:        BEDROCK
			NORTH:       STONE
			NORTH_EAST:  LOG
			EAST:        GOLD_ORE
			SOUTH_EAST:  LAPIS_BLOCK
			SOUTH:       IRON_ORE
			SOUTH_WEST:  LAPIS_ORE
			WEST:        COAL_ORE
			NORTH_WEST:  GLASS

			2
			Main:        BEDROCK
			NORTH:       COAL_ORE
			NORTH_EAST:  GLASS
			EAST:        STONE
			SOUTH_EAST:  LOG
			SOUTH:       GOLD_ORE
			SOUTH_WEST:  LAPIS_BLOCK
			WEST:        IRON_ORE
			NORTH_WEST:  LAPIS_ORE

			3
			Main:        BEDROCK
			NORTH:       IRON_ORE
			NORTH_EAST:  LAPIS_ORE
			EAST:        COAL_ORE
			SOUTH_EAST:  GLASS
			SOUTH:       STONE
			SOUTH_WEST:  LOG
			WEST:        GOLD_ORE
			NORTH_WEST:  LAPIS_BLOCK

			4
			Main:        BEDROCK
			NORTH:       GOLD_ORE
			NORTH_EAST:  LAPIS_BLOCK
			EAST:        IRON_ORE
			SOUTH_EAST:  LAPIS_ORE
			SOUTH:       COAL_ORE
			SOUTH_WEST:  GLASS
			WEST:        STONE
			NORTH_WEST:  LOG*/
}
