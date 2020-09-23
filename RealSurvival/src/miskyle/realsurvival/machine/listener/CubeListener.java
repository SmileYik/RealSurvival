package miskyle.realsurvival.machine.listener;

import com.github.miskyle.mcpt.MCPT;
import com.github.miskyle.mcpt.i18n.I18N;
import java.util.ArrayList;
import java.util.LinkedList;
import miskyle.realsurvival.Msg;
import miskyle.realsurvival.blockarray.BlockArrayCreator;
import miskyle.realsurvival.data.PlayerManager;
import miskyle.realsurvival.data.blockarray.BlockArrayData;
import miskyle.realsurvival.data.blockarray.CubeData;
import miskyle.realsurvival.data.recipe.RecipeType;
import miskyle.realsurvival.machine.MachineAccess;
import miskyle.realsurvival.machine.MachineManager;
import miskyle.realsurvival.machine.crafttable.CraftTable;
import miskyle.realsurvival.machine.furnace.Furnace;
import miskyle.realsurvival.util.RSEntry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class CubeListener implements Listener {

  private LinkedList<String> playerNames = new LinkedList<String>();

  /**
   * 处理玩家点击多方块结构.
   * @param event 玩家交互事件
   */
  @EventHandler
  public void onPlayerUse(final PlayerInteractEvent event) {
    if (!PlayerManager.isActive(event.getPlayer().getName()) 
        || !(event.hasBlock() && event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
      return;
    } else if (playerNames.contains(event.getPlayer().getName())) {
      event.getPlayer().sendMessage(
          Msg.getPrefix() + I18N.tr("machine.open-too-fast"));
      return;
    }
    

    Block block = event.getClickedBlock();
    ArrayList<CubeData> cubes = MachineManager.getCubesByMainBlock(block);

    for (CubeData cube : cubes) {
      event.setCancelled(true);
      // Simple Check
      if (!simpleCheck(block, 
          cube.getUp().getMain(),
          cube.getMid().getMain(),
          cube.getDown().getMain())) {
        continue;
      }

      // Check Mid-Floor
      if (!checkBlocks(block, cube.getMid(), cube.isCheckCompass())) {
        continue;
      }

      // Check Down-Floor
      if (!checkBlocks(
          block.getRelative(BlockFace.DOWN), cube.getDown(), cube.isCheckCompass())) {
        continue;
      }

      // Check Up-Floor
      if (!checkBlocks(block.getRelative(BlockFace.UP), cube.getUp(), cube.isCheckCompass())) {
        continue;
      }
      
      //Success
      event.setCancelled(true);
      playerNames.add(event.getPlayer().getName());
      Bukkit.getScheduler().runTaskLaterAsynchronously(MCPT.plugin, () -> {
        playerNames.remove(event.getPlayer().getName());
      }, 60L); // 3s 冷却
      
      //MachineAccess
      MachineAccess access = MachineManager.getMachineAccess(block.getLocation());
      if (access == null) {
        access = new MachineAccess(event.getPlayer().getName());
        MachineManager.setMachineAccess(block.getLocation(), access);
      }
      if (event.getPlayer().isSneaking()) {
        ItemStack item = event.getItem();
        //1.9以下无双持.
        if (item != null 
            && item.getItemMeta() instanceof BookMeta
            && item.getType().name().contains("WRITTEN_BOOK")) {
          //设定名单
          BookMeta meta = (BookMeta) item.getItemMeta();
          if (!meta.hasAuthor()) {
            //no author
            event.getPlayer().sendMessage(
                Msg.getPrefix() + I18N.tr("machine.access.no-author"));
            return;
          } else if (!meta.getAuthor().equalsIgnoreCase(access.getOwner())) {
            //author not owner
            event.getPlayer().sendMessage(
                Msg.getPrefix() + I18N.tr("machine.access.not-yours"));
            return;
          } else if (meta.getPageCount() < 1) {
            //页数少于1
            event.getPlayer().sendMessage(
                Msg.getPrefix() + I18N.tr("machine.access.no-page"));
            return;
          }
          String text = meta.getPage(1).replace("§0", "").split("\n")[0];
          if (text == null || text.isEmpty() 
              || !(text.equalsIgnoreCase("[R.S.M.A.W.L.]") 
                  || text.equalsIgnoreCase("[R.S.M.A.B.L.]"))) {
            //第一页第一行不是[R.S.M.A.W.L.]或[R.S.M.A.B.L.]
            event.getPlayer().sendMessage(
                Msg.getPrefix() + I18N.tr("machine.access.error-format"));
            return;
          }
          final MachineAccess modifyAccess = access;
          meta.getPages().forEach(page -> {
            for (String line : page.replace("§0", "").split("\n")) {
              if (line.equalsIgnoreCase("[R.S.M.A.W.L.]")) {
                modifyAccess.setMode(MachineAccess.WHITELIST_MODE);
                continue;
              } else if (line.equalsIgnoreCase("[R.S.M.A.B.L.]")) {
                modifyAccess.setMode(MachineAccess.BLACKLIST_MODE);
                continue;
              } else {
                modifyAccess.addPlayer(line);
              }
            }
          });
          MachineManager.setMachineAccess(block.getLocation(), modifyAccess);
          event.getPlayer().sendMessage(
              Msg.getPrefix() + I18N.tr("machine.access.success"));
          return;
        } else {
          //查看名单
          if (!access.isAccessable(event.getPlayer().getName())) {
            //禁止无权限人查看权限名单
            event.getPlayer().sendMessage(
                Msg.getPrefix() + I18N.tr("machine.access.no-permission", access.getOwner()));
            return;
          }
          ItemStack book = new ItemStack(Material.valueOf("WRITTEN_BOOK"));
          BookMeta meta = (BookMeta) book.getItemMeta();
          String page = I18N.tr("machine.access.owner", access.getOwner()) + "\n" 
              + I18N.tr("machine.access.mode." + access.getMode()) + "\n";
          
          int line = 2;
          int pageCount = 1;
          for (String name : access.getPlayers()) {
            page += name + "\n";
            if (++line >= 10) {
              meta.addPage(page);
              line = 0;
              pageCount++;
            }
          }
          if (pageCount == 1 
              || (pageCount > 1 && line > 0)) {
            meta.addPage(page);
          }
          meta.setAuthor(access.getOwner());
          meta.setTitle(access.getOwner());
          book.setItemMeta(meta);
          event.getPlayer().openBook(book);
          return;
        }
      }
      //Check Access
      if (!access.isAccessable(event.getPlayer().getName())) {
        event.getPlayer().sendMessage(
            Msg.getPrefix() + I18N.tr("machine.access.no-permission", access.getOwner()));
        return;
      }

      String machineName = MachineManager.getMachineNameByCubeName(cube.getName());
      RSEntry<RecipeType, String> machineData = MachineManager.getMachineData(machineName);
      if (machineData.getLeft() == RecipeType.CRAFT_TABLE) {
        CraftTable.openDefaultGUI(
            event.getPlayer(), block.getLocation(), machineName, machineData.getRight());
      } else if (machineData.getLeft() == RecipeType.FURNACE) {
        Furnace.openDefaultGUI(
            event.getPlayer(), block.getLocation(), machineName, machineData.getRight());
      } else {
        //TODO 
      }
      return;
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

  private boolean compareBlock(Block block, String cubeBlockKey) {
    if (cubeBlockKey.equals("AIR")) {
      return true;
    } else {
      return cubeBlockKey.equals(BlockArrayCreator.getBlockKey(block));
    }
  }
}
