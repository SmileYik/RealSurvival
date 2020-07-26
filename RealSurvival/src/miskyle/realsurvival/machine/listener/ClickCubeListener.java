package miskyle.realsurvival.machine.listener;

import java.util.ArrayList;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.github.miskyle.mcpt.MCPT;

import miskyle.realsurvival.blockarray.BlockArrayCreator;
import miskyle.realsurvival.data.PlayerManager;
import miskyle.realsurvival.data.blockarray.BlockArrayData;
import miskyle.realsurvival.data.blockarray.CubeData;
import miskyle.realsurvival.data.recipe.RecipeType;
import miskyle.realsurvival.machine.MachineManager;
import miskyle.realsurvival.machine.crafttable.CraftTable;
import miskyle.realsurvival.util.RSEntry;

public class ClickCubeListener implements Listener {

  private LinkedList<String> playerNames = new LinkedList<String>();

  @EventHandler
  public void onPlayerUse(final PlayerInteractEvent event) {
    if (!PlayerManager.isActive(event.getPlayer().getName()) || playerNames.contains(event.getPlayer().getName())
        || !(event.hasBlock() && event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
      return;
    }

    Block block = event.getClickedBlock();
    ArrayList<CubeData> cubes = MachineManager.getCubesByMainBlock(block);

    ItemStack item = null;
    int originItemAmount = 0;
    if (event.hasItem() && event.getHand() == EquipmentSlot.HAND) {
      item = event.getItem().clone();
      originItemAmount = item.getAmount();
      item.setAmount(1);
    }
    for (CubeData cube : cubes) {
      if ((cube.isCheckItem() && item != null && cube.getItem().equals(item)) || !cube.isCheckItem()) {
        // Item Pass
        event.setCancelled(true);
        // Simple Check
        if (!simpleCheck(block, cube.getUp().getMain(), cube.getMid().getMain(), cube.getDown().getMain())) {
          continue;
        }

        // Check Mid-Floor
        if (!checkBlocks(block, cube.getMid(), cube.isCheckCompass())) {
          continue;
        }

        // Check Down-Floor
        if (!checkBlocks(block.getRelative(BlockFace.DOWN), cube.getDown(), cube.isCheckCompass())) {
          continue;
        }

        // Check Up-Floor
        if (!checkBlocks(block.getRelative(BlockFace.UP), cube.getUp(), cube.isCheckCompass())) {
          continue;
        }

        // Success
        final int size = --originItemAmount;
        playerNames.add(event.getPlayer().getName());
        event.setCancelled(true);
        Bukkit.getScheduler().runTaskLaterAsynchronously(MCPT.plugin, () -> {
          if (cube.isCheckItem() && cube.isReduceItem()) {
            event.getItem().setAmount(size);
          }
          playerNames.remove(event.getPlayer().getName());
        }, 20L);
        String machineName = MachineManager.getMachineNameByCubeName(cube.getName());
        RSEntry<RecipeType, String> machineData = MachineManager.getMachineData(machineName);
        if(machineData.getLeft() == RecipeType.CRAFT_TABLE) {
          CraftTable.openDefaultGUI(event.getPlayer(), block.getLocation(),
              machineName, machineData.getRight());
        }else {
          
        }
        return;
      }
    }
    event.setCancelled(false);
  }

  private boolean simpleCheck(Block mainBlock, String upMain, String main, String downMain) {
    if (compareBlock(mainBlock, main) && compareBlock(mainBlock.getRelative(BlockFace.UP), upMain)
        && compareBlock(mainBlock.getRelative(BlockFace.DOWN), downMain)) {
      return true;
    } else {
      return false;
    }
  }

  private boolean checkBlocks(Block mainBlock, BlockArrayData blocks, boolean checkCompass) {
    if (checkCompass) {
      if (!compareBlock(mainBlock.getRelative(BlockFace.NORTH), blocks.getNorth())
          || !compareBlock(mainBlock.getRelative(BlockFace.NORTH_EAST), blocks.getNorthEast())
          || !compareBlock(mainBlock.getRelative(BlockFace.EAST), blocks.getEast())
          || !compareBlock(mainBlock.getRelative(BlockFace.SOUTH_EAST), blocks.getSouthEast())
          || !compareBlock(mainBlock.getRelative(BlockFace.SOUTH), blocks.getSouth())
          || !compareBlock(mainBlock.getRelative(BlockFace.SOUTH_WEST), blocks.getSouthWest())
          || !compareBlock(mainBlock.getRelative(BlockFace.WEST), blocks.getWest())
          || !compareBlock(mainBlock.getRelative(BlockFace.NORTH_WEST), blocks.getNorthWest())) {
        return false;
      }
      return true;
    } else {
      if (compareBlock(mainBlock.getRelative(BlockFace.EAST), blocks.getNorth()) // 2
          && compareBlock(mainBlock.getRelative(BlockFace.SOUTH_EAST), blocks.getNorthEast())
          && compareBlock(mainBlock.getRelative(BlockFace.SOUTH), blocks.getEast())
          && compareBlock(mainBlock.getRelative(BlockFace.SOUTH_WEST), blocks.getSouthEast())
          && compareBlock(mainBlock.getRelative(BlockFace.WEST), blocks.getSouth())
          && compareBlock(mainBlock.getRelative(BlockFace.NORTH_WEST), blocks.getSouthWest())
          && compareBlock(mainBlock.getRelative(BlockFace.NORTH), blocks.getWest())
          && compareBlock(mainBlock.getRelative(BlockFace.NORTH_EAST), blocks.getNorthWest())) {
        return true;
      } else if (compareBlock(mainBlock.getRelative(BlockFace.NORTH), blocks.getNorth()) // 1
          && compareBlock(mainBlock.getRelative(BlockFace.NORTH_EAST), blocks.getNorthEast())
          && compareBlock(mainBlock.getRelative(BlockFace.EAST), blocks.getEast())
          && compareBlock(mainBlock.getRelative(BlockFace.SOUTH_EAST), blocks.getSouthEast())
          && compareBlock(mainBlock.getRelative(BlockFace.SOUTH), blocks.getSouth())
          && compareBlock(mainBlock.getRelative(BlockFace.SOUTH_WEST), blocks.getSouthWest())
          && compareBlock(mainBlock.getRelative(BlockFace.WEST), blocks.getWest())
          && compareBlock(mainBlock.getRelative(BlockFace.NORTH_WEST), blocks.getNorthWest())) {
        return true;
      } else if (compareBlock(mainBlock.getRelative(BlockFace.SOUTH), blocks.getNorth()) // 3
          && compareBlock(mainBlock.getRelative(BlockFace.SOUTH_WEST), blocks.getNorthEast())
          && compareBlock(mainBlock.getRelative(BlockFace.WEST), blocks.getEast())
          && compareBlock(mainBlock.getRelative(BlockFace.NORTH_WEST), blocks.getSouthEast())
          && compareBlock(mainBlock.getRelative(BlockFace.NORTH), blocks.getSouth())
          && compareBlock(mainBlock.getRelative(BlockFace.NORTH_EAST), blocks.getSouthWest())
          && compareBlock(mainBlock.getRelative(BlockFace.EAST), blocks.getWest())
          && compareBlock(mainBlock.getRelative(BlockFace.SOUTH_EAST), blocks.getNorthWest())) {
        return true;
      } else if (compareBlock(mainBlock.getRelative(BlockFace.WEST), blocks.getNorth()) // 4
          && compareBlock(mainBlock.getRelative(BlockFace.NORTH_WEST), blocks.getNorthEast())
          && compareBlock(mainBlock.getRelative(BlockFace.NORTH), blocks.getEast())
          && compareBlock(mainBlock.getRelative(BlockFace.NORTH_EAST), blocks.getSouthEast())
          && compareBlock(mainBlock.getRelative(BlockFace.EAST), blocks.getSouth())
          && compareBlock(mainBlock.getRelative(BlockFace.SOUTH_EAST), blocks.getSouthWest())
          && compareBlock(mainBlock.getRelative(BlockFace.SOUTH), blocks.getWest())
          && compareBlock(mainBlock.getRelative(BlockFace.SOUTH_WEST), blocks.getNorthWest())) {
        return true;
      } else {
        return false;
      }
    }
  }

  private boolean compareBlock(Block block, String block2) {
    return block2.equals(BlockArrayCreator.getBlockKey(block));
  }
}
