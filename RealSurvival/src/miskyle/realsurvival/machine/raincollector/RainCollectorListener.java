package miskyle.realsurvival.machine.raincollector;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.github.miskyle.mcpt.i18n.I18N;

import miskyle.realsurvival.Msg;
import miskyle.realsurvival.data.PlayerManager;
import miskyle.realsurvival.data.item.RSItem;
import miskyle.realsurvival.machine.MachineManager;

public class RainCollectorListener implements Listener{
    @EventHandler(priority = EventPriority.HIGH)
    public void clickRainCollector(final PlayerInteractEvent e) {
      if(!PlayerManager.isActive(e.getPlayer())
          ||e.getAction() != Action.RIGHT_CLICK_BLOCK) {
        return;
      }
      Block block = e.getClickedBlock();
      
      if(block.getType() == Material.CAULDRON 
          &&block.getRelative(BlockFace.UP).getType() == Material.HOPPER) {
        e.setCancelled(true);
        if(MachineManager.isActiveTimer(block.getLocation())) {
          //该位置已经存在一个机器
          RainCollectorTimer collector = (RainCollectorTimer)MachineManager.getTImer(block.getLocation());
          //判断是要取水还是要打开界面
          if(e.hasItem() && e.getItem().getType() == Material.GLASS_BOTTLE) {
            if(collector.hasWater()) {
              collector.getWater();
              e.getItem().setAmount(e.getItem().getAmount()-1);
              e.getPlayer().getInventory().addItem(RSItem.load("/water/rainwater").getItem()).values().forEach(item->{
                    block.getWorld().dropItem(e.getPlayer().getLocation(), item);
                  });
            }else {
              e.getPlayer().sendMessage(Msg.getPrefix()+I18N.tr("machine.rain-collector.no-water"));
            }
            return;
          }else {
            //打开储水界面
            RainCollector.open(e.getPlayer(), block.getLocation());
            return;
          }
        }else {
          //注册个机器并打开储水界面
          MachineManager.addTimer(
              new RainCollectorTimer(e.getPlayer().getName(), block.getLocation()));
          RainCollector.open(e.getPlayer(), block.getLocation());
          return;
        }
      }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void clickRainCollectorGUI(final InventoryClickEvent e) {
      if(e.isCancelled()) {
        return;
      }
      if(!(e.getWhoClicked() instanceof Player)
          || !(e.getInventory().getHolder() instanceof RainCollectorHolder)
          || e.getRawSlot()<0) {
        return;
      }
      
      Player p = (Player)e.getWhoClicked();
      if(e.getRawSlot()<27) {
        e.setCancelled(true);
        p.updateInventory();
      }
    }
}
